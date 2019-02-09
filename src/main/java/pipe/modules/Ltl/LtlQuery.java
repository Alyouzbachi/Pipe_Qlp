/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipe.modules.Ltl;

import LTLqueries.MyGraph;
import LTLqueries.Path;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacc.ASTStart;
import javacc.Interpreter;
import javacc.ParseException;
import javacc.Parser;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.sourceforge.jpowergraph.defaults.DefaultGraph;
import net.sourceforge.jpowergraph.defaults.DefaultNode;
import net.sourceforge.jpowergraph.defaults.TextEdge;
import pipe.calculations.StateSpaceGenerator;
import pipe.calculations.myTree;
import pipe.exceptions.MarkingNotIntegerException;
import pipe.exceptions.TreeTooBigException;
import pipe.extensions.jpowergraph.PIPELoopWithTextEdge;
import pipe.extensions.jpowergraph.PIPEState;
import pipe.extensions.jpowergraph.PIPETangibleState;
import pipe.extensions.jpowergraph.PIPEVanishingState;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.EnterOptionsPane;
import pipe.gui.widgets.EscapableDialog;
import pipe.gui.widgets.PetriNetChooserPanel;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.io.ImmediateAbortException;
import pipe.io.IncorrectFileFormatException;
import pipe.io.ReachabilityGraphFileHeader;
import pipe.io.StateRecord;
import pipe.io.TransitionRecord;
import pipe.modules.interfaces.IModule;
import static pipe.modules.reachability.ReachabilityGraphGenerator.DataLayer;
import pipe.utilities.Expander;
import pipe.utilities.writers.PNMLWriter;
import pipe.views.MarkingView;
import pipe.views.PetriNetView;
import pipe.views.PlaceView;
import static pipe.modules.reachability.ReachabilityGraphGenerator.Graph;
import static pipe.modules.reachability.ReachabilityGraphGenerator.reachabilityGraphFile;

/**
 *
 * @author Yazan
 */
public class LtlQuery implements IModule {

    private static final String MODULE_NAME = "Ltl Query";
    private final EscapableDialog guiDialog
            = new EscapableDialog(ApplicationSettings.getApplicationView(), MODULE_NAME, true);
    private JTextField queryInput = new JTextField();
    private JButton submit;

    private static MyGraph mygraph;

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    ButtonBar LivenessBtn, DeadlockBtn;
    JRadioButton jRadioButton1 = new JRadioButton("DeadLock");
    JRadioButton jRadioButton2 = new JRadioButton("Liveness");
    JButton jButton6 = new JButton("Check");

    List<String> legend = new ArrayList<String>();
    List<int[]> listMarkings = new ArrayList<int[]>();
    static List<String> parsingStates = new ArrayList<String>();

    private Map<String, Set<String>> nextStates = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> previousStates = new HashMap<String, Set<String>>();

    static LtlStructure ltlStct = new LtlStructure();
    private static EnterQueryPane queryPane;
    private static String dataLayerName;
    private static ResultsHTMLPane results;

    public String getName() {
        return MODULE_NAME;
    }

    public void start() {
        {
            PetriNetView pnmlData = ApplicationSettings.getApplicationView().getCurrentPetriNetView();
            // Check if this net is a CGSPN. If it is, then this
            // module won't work with it and we must convert it.
            if (pnmlData.getEnabledTokenClassNumber() > 1) {
//		if(pnmlData.getTokenViews().size() > 1)
                Expander expander = new Expander(pnmlData);
                pnmlData = expander.unfold();
                JOptionPane.showMessageDialog(null, "This is CGSPN. The analysis will only apply to default color (black)",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }

            Container contentPane = guiDialog.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
            //  JLabel label = new JLabel("Query:");
            //   contentPane.add(label);
            //   contentPane.add(queryInput);
            ButtonBar SubmitQuery = new ButtonBar("Submit Query", analyisQuery,
                    guiDialog.getRootPane());
            queryPane = new EnterQueryPane(SubmitQuery);
            contentPane.add(queryPane);
            ButtonBar DeadAndLive
                    = new ButtonBar(new String[]{"Deadlock", "Liveness"},
                    new ActionListener[]{Deadlock, Liveness});
            // copyAndSaveButtons.setButtonsEnabled(false);
            contentPane.add(DeadAndLive, BorderLayout.SOUTH);

            results = new ResultsHTMLPane(pnmlData.getPNMLName());
            contentPane.add(results);
            contentPane.add(new ButtonBar("Help", Help, guiDialog.getRootPane()));
            //   contentPane.add(new ButtonBar("Submit Query", analyisQuery,
            //          guiDialog.getRootPane()));

            //***
            // 5 Make window fit contents' preferred size
            guiDialog.pack();
            guiDialog.setLocationRelativeTo(null);

            guiDialog.setModal(false);
            guiDialog.setVisible(false);
            guiDialog.setVisible(true);
        }

    }

    private final ActionListener analyisQuery = new ActionListener() {

        public void actionPerformed(ActionEvent arg0) {
            long start = new Date().getTime();
            long gfinished;
            long allfinished;
            double graphtime;
            double constructiontime;
            double totaltime;
            legend = new ArrayList<String>();
            listMarkings = new ArrayList<int[]>();
            parsingStates = new ArrayList<String>();
            ltlStct = new LtlStructure();
            //data layer corrected, so that we could have the correct calculation
            PetriNetView sourcePetriNetView = ApplicationSettings.getApplicationView().getCurrentPetriNetView();//sourceFilePanel.getDataLayer();
            dataLayerName = sourcePetriNetView.getPNMLName();

            // This will be used to store the reachability graph data
            File reachabilityGraph = new File("results.rg");

            // This will be used to store the steady state distribution
            String s = "Query Analayis";
            boolean finalResult = false;

            if (sourcePetriNetView == null) {
                JOptionPane.showMessageDialog(null, "Please, choose a source net",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!sourcePetriNetView.hasPlaceTransitionObjects()) {
                s += "No Petri net objects defined!";
            } else {
                try {

                    PNMLWriter.saveTemporaryFile(sourcePetriNetView,
                            this.getClass().getName());

                    String graph = "Reachability graph";

                    boolean generateCoverability = false;
                    try {
                        StateSpaceGenerator.generate(sourcePetriNetView, reachabilityGraph);
                    } catch (OutOfMemoryError e) {
                        // net seems to be unbounded, let's try to generate the
                        // coverability graph
                        generateCoverability = true;
                    }

                    LinkedList<MarkingView>[] markings = sourcePetriNetView.getInitialMarkingVector();
                    int[] currentMarking = new int[markings.length];
                    for (int i = 0; i < markings.length; i++) {
                        currentMarking[i] = markings[i].getFirst().getCurrentMarking();
                    }
                    //       TODO: reachability graph and coverability graph are the same
                    //       when the net is bounded so we could just generate the
                    //        coverability graph
                    if (generateCoverability) {
                        myTree tree = new myTree(sourcePetriNetView,
                                currentMarking,
                                reachabilityGraph);
                        graph = "Coverability graph";
                    }
                    DefaultGraph rgraph = createGraph(reachabilityGraph, sourcePetriNetView,
                            generateCoverability);
                    // the query analys code after generating the graph   
                    //   String temp = queryInput.getText();
                    String temp = queryPane.getQueryInput();
                    createMarkingsArray(reachabilityGraph, sourcePetriNetView, generateCoverability);
                    List<String> up = new ArrayList<String>();
                    // making all places names Upper case
                    for (String legend1 : legend) {
                        legend1 = legend1.toUpperCase();
                        up.add(legend1);
                    }
                    legend = up;
                    ltlStct.setMarkings(listMarkings);
                    ltlStct.setPlaces(legend);
                    ltlStct.setParsingStates(parsingStates);
                    //edit
                    ltlStct.setDeadlockGraph(rgraph);
                    ltlStct.setGraph(rgraph);
                    ltlStct.setGenerateCoverability(generateCoverability);
                    ltlStct.setSourcePetriNetView(sourcePetriNetView);
                    nextStates = ltlStct.getNextStates();
                    previousStates = ltlStct.getPreviousStates();
                    //passing query to compiler
                    InputStream stream = new ByteArrayInputStream(temp.getBytes());
                    Parser parser = new Parser(stream);
                    ASTStart expr;

                    Interpreter intpret = new Interpreter();
                    try {
                        expr = parser.Start();
                        expr.dump("-");
                        finalResult = intpret.Start(expr, ltlStct);
                    } catch (ParseException ex) {
                        System.out.println(ex);
                    }
                    // check Next (X) ltl word
                    /*int Uindex = temp.indexOf("X");
                     String place1, place2;
                     List<String> before = new ArrayList<String>();
                     List<String> after = new ArrayList<String>();
                     boolean m;
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }

                     //                        if (temp.length() - Uindex + 1 == 1) {
                     //                            m = NextState(place1, place2);
                     //                        } else {
                     //                            List<String> a = new ArrayList<String>();
                     //                            for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     //                                a.add(temp.substring(i, i + 2));
                     //                            }
                     //                            m = NextState(place1, a);
                     //                        }
                     m = ltlStct.NextState(before, after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }
                     // List<Node> nodes = new ArrayList<Node>();
                     //     PlaceView p1 = sourcePetriNetView.getPlaceByName(place1);
                     //        PlaceView p2 = sourcePetriNetView.getPlaceByName(place2);
                     //      sourcePetriNetView.getPlaceByName(place2);
                     //    nodes = rgraph.getAllNodes();
                     // check global (G) ltl word

                     Uindex = temp.indexOf("G");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.GlobalState(before, after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
                     //  m = GlobalState(place1);
                     }
                     //check future (F) ltl word
                     Uindex = temp.indexOf("F");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.FutureState(before, after);
                     //    m = FutureState(place1, place2);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
                     }

                     //check Until (U) ltl word
                     Uindex = temp.indexOf("U");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.UntilState(before, after);
                     //  m = ltlStct.checkUntilState(place1, place2);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }

                     if (temp.equalsIgnoreCase("Deadlock")) {
                     DefaultGraph graph1 = createGraph(reachabilityGraph, sourcePetriNetView, generateCoverability);
                     ltlStct.findDeadlocks(graph1);
                     }
                     // test allNext 
                     Uindex = temp.indexOf("S");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.AllNext(before, after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }

                     //test allFuture
                     Uindex = temp.indexOf("Q");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.AllFuture(before, after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }

                     //test all until
                     Uindex = temp.indexOf("W");
                     if (Uindex >= 0) {
                     place1 = temp.substring(0, Uindex);
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     for (int i = 0; i < Uindex; i += 2) {
                     before.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.AllUntil(before, after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }
                     //test Exist
                     Uindex = temp.indexOf("E");
                     if (Uindex >= 0) {
                     place2 = temp.substring(Uindex + 1, temp.length());
                     for (int i = Uindex + 1; i < temp.length() - 1; i += 2) {
                     after.add(temp.substring(i, i + 2));
                     }
                     m = ltlStct.Exist(after);
                     JOptionPane.showMessageDialog(null, m, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

                     }*/
                    gfinished = new Date().getTime();
                    System.gc();
                    allfinished = new Date().getTime();
                    graphtime = (gfinished - start) / 1000.0;
                    constructiontime = (allfinished - gfinished) / 1000.0;
                    totaltime = (allfinished - start) / 1000.0;
                    DecimalFormat f = new DecimalFormat();
                    f.setMaximumFractionDigits(5);
                    s += "<br>Generating " + graph + " took "
                            + f.format(graphtime) + "s";
                    s += "<br>Constructing it took "
                            + f.format(constructiontime) + "s";
                    s += "<br>Total time was " + f.format(totaltime) + "s";
                    results.setEnabled(true);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    results.setText("");
                    s = "Memory error: " + e.getMessage();

                    s += "<br>Not enough memory. Please use a larger heap size."
                            + "<br>"
                            + "<br>Note:"
                            + "<br>The Java heap size can be specified with the -Xmx option."
                            + "<br>E.g., to use 512MB as heap size, the command line looks like this:"
                            + "<br>java -Xmx512m -classpath ...\n";
                    results.setText(s);
                    return;
                } catch (StackOverflowError e) {
                    s += "StackOverflow Error";
                    results.setText(s);
                    return;
                } catch (ImmediateAbortException e) {
                    s += "<br>Error: " + e.getMessage();
                    results.setText(s);
                    return;
                } catch (IOException e) {
                    s += "<br>" + e.getMessage();
                    results.setText(s);
                    return;
                } catch (TreeTooBigException e) {
                    s += "<br>" + e.getMessage();
                    results.setText(s);
                    return;
                } catch (MarkingNotIntegerException e) {
                    JOptionPane.showMessageDialog(null,
                            "Weighting cannot be less than 0. Please re-enter");
                    sourcePetriNetView.restorePlaceViewsMarking();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    s += "<br>Error" + e.getMessage();
                    results.setText(s);
                    return;
                } finally {
                    if (reachabilityGraph.exists()) {
                        reachabilityGraph.delete();
                    }
                }
            }
            s += "<br><br>##    Query result is : " + finalResult + " ##";
            results.setText(s);
            JOptionPane.showMessageDialog(null, finalResult, "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

        }
    };

    public boolean FutureState(String place1, String place2) {
        int index1, index2;
        index1 = legend.indexOf(place1);
        index2 = legend.indexOf(place2);
        if (index1 == -1) {
            place1 = place1.toLowerCase();
            index1 = legend.indexOf(place1);
        }
        if (index1 == -1) {
            place1 = place1.toUpperCase();
            index1 = legend.indexOf(place1);
        }
        if (index2 == -1) {
            place2 = place2.toLowerCase();
            index2 = legend.indexOf(place2);
        }
        if (index2 == -1) {
            place2 = place2.toUpperCase();
            index2 = legend.indexOf(place2);
        }
        Set<String> futureStates = null;
        futureStates = ltlStct.getFutureStates(index1);
        for (String a : futureStates) {
            int i = parsingStates.indexOf(a);
            int[] mark = listMarkings.get(i);
            if (mark[index2] > 0 || mark[index2] == -1) {
                return true;
            }
        }
        return false;
    }

    public boolean UntilState(String place1, String place2) {
        //      ltlStct.findStatePaths(place1);
        boolean m = ltlStct.checkUntilState(place1, place2);
        return m;
    }

    private static DefaultGraph createGraph(File rgFile, PetriNetView dataLayer,
            boolean coverabilityGraph) throws IOException {
        DefaultGraph graph = new DefaultGraph();
        Graph = graph;

        ReachabilityGraphFileHeader header = new ReachabilityGraphFileHeader();
        RandomAccessFile reachabilityFile;
        try {
            reachabilityFile = new RandomAccessFile(rgFile, "r");
            header.read(reachabilityFile);
            reachabilityGraphFile = rgFile;
            DataLayer = dataLayer;
        } catch (IncorrectFileFormatException e1) {
            System.err.println("createGraph: "
                    + "incorrect file format on state space file");
            Graph = graph;
            return graph;
        } catch (IOException e1) {
            System.err.println("createGraph: unable to read header file");
            Graph = graph;
            return graph;
        }

        if ((header.getNumStates() + header.getNumTransitions()) > 400) {
            throw new IOException("There are " + header.getNumStates() + " states with "
                    + header.getNumTransitions() + " arcs. The graph is too big to be displayed properly.");
        }

        ArrayList nodes = new ArrayList();
        ArrayList edges = new ArrayList();
        ArrayList loopEdges = new ArrayList();
        ArrayList loopEdgesTransitions = new ArrayList();
        String label;
        String marking;

        int stateArraySize = header.getStateArraySize();
        StateRecord record = new StateRecord();
        record.read1(stateArraySize, reachabilityFile);
        label = "S0";
        parsingStates.add(label);
        marking = record.getMarkingString();
        if (record.getTangible()) {
            nodes.add(coverabilityGraph
                    ? new PIPEState(label, marking)
                    : new PIPETangibleState(label, marking));

        } else {
            nodes.add(coverabilityGraph
                    ? new PIPEState(label, marking)
                    : new PIPEVanishingState(label, marking));

        }

        for (int count = 1; count < header.getNumStates(); count++) {
            record.read1(stateArraySize, reachabilityFile);
            label = "S" + count;
            parsingStates.add(label);
            marking = record.getMarkingString();
            if (record.getTangible()) {
                nodes.add(coverabilityGraph
                        ? new PIPEState(label, marking)
                        : new PIPETangibleState(label, marking));
            } else {
                nodes.add(coverabilityGraph
                        ? new PIPEState(label, marking)
                        : new PIPEVanishingState(label, marking));
            }
        }

        reachabilityFile.seek(header.getOffsetToTransitions());
        int numberTransitions = header.getNumTransitions();
        for (int transitionCounter = 0; transitionCounter < numberTransitions;
                transitionCounter++) {
            TransitionRecord transitions = new TransitionRecord();
            transitions.read1(reachabilityFile);

            int from = transitions.getFromState();
            int to = transitions.getToState();
            String temp = parsingStates.get(from);
            String temp1 = parsingStates.get(to);
            ltlStct.addNextState(temp, temp1);
            ltlStct.addPrevState(temp1, temp);
            if (from != to) {
                edges.add(new TextEdge(
                        (DefaultNode) (nodes.get(from)),
                        (DefaultNode) (nodes.get(to)),
                        dataLayer.getTransitionName(transitions.getTransitionNo())));
            } else {
                if (loopEdges.contains(nodes.get(from))) {
                    int i = loopEdges.indexOf(nodes.get(from));

                    loopEdgesTransitions.set(i,
                            loopEdgesTransitions.get(i) + ", "
                            + dataLayer.getTransitionName(transitions.getTransitionNo()));
                } else {
                    loopEdges.add(nodes.get(from));
                    loopEdgesTransitions.add(
                            dataLayer.getTransitionName(transitions.getTransitionNo()));
                }
            }
        }

        for (int i = 0; i < loopEdges.size(); i++) {
            edges.add(new PIPELoopWithTextEdge((DefaultNode) (loopEdges.get(i)),
                    (String) (loopEdgesTransitions.get(i))));
        }
        graph.addElements(nodes, edges);
        LTLqueries.ReadRG.ReadStates();
        LTLqueries.ReadRG.ReadTransitions();
        LTLqueries.ReadRG.ReadRGFileDataLayer(rgFile, dataLayer);
        LTLqueries.ReadRG.ReadNamesofPlaces();
        Graph = graph;
        
        return graph;
    }

    public DefaultGraph createMarkingsArray(File rgFile, PetriNetView dataLayer,
            boolean coverabilityGraph)
            throws Exception {
        PlaceView[] placeView = dataLayer.places();
        //String legend = "";
        if (placeView.length > 0) {
            legend.add(placeView[0].getName());
        }
        for (int i = 1; i < placeView.length; i++) {
            legend.add(placeView[i].getName());
        }
        //legend += "}";

        DefaultGraph graph = new DefaultGraph();

        ReachabilityGraphFileHeader header = new ReachabilityGraphFileHeader();
        RandomAccessFile reachabilityFile;
        try {
            reachabilityFile = new RandomAccessFile(rgFile, "r");
            header.read(reachabilityFile);
        } catch (IncorrectFileFormatException e1) {
            System.err.println("createGraph: "
                    + "incorrect file format on state space file");
            return graph;
        } catch (IOException e1) {
            System.err.println("createGraph: unable to read header file");
            return graph;
        }

        if ((header.getNumStates() + header.getNumTransitions()) > 400) {
            throw new IOException("There are " + header.getNumStates() + " states with "
                    + header.getNumTransitions() + " arcs. The graph is too big to be displayed properly.");
        }

        int stateArraySize = header.getStateArraySize();
        StateRecord record = new StateRecord();
        record.read1(stateArraySize, reachabilityFile);
        listMarkings.add(record.getState());
        for (int count = 1; count < header.getNumStates(); count++) {
            record.read1(stateArraySize, reachabilityFile);
            listMarkings.add(record.getState());

        }
        return null;
    }

//    private final ActionListener Help = new ActionListener() {
//
//        public void actionPerformed(ActionEvent arg0) {
//            JOptionPane.showMessageDialog(null, "List(X)List to check Next States "
//                    + "\n List(G)List to check Global States"
//                    + "\n List(F)List to check Future States"
//                    + "\n List(U)List to check Until States"
//                    + "\n List(AllX)List to check All Next States for the previous List"
//                    + "\n List(AllF)List to check All Future States for the previous List"
//                    + "\n List(AllU)List to check All Until States for the previuos List"
//                    + "\n (E)List to check Places if true at the same time  "
//                    + "\n (Deadlock) to find deadlock in the active petri "
//                    + "\n (Liveness) to check Liveness in the active petri ", " Help Syntax Menu ", JOptionPane.INFORMATION_MESSAGE);
//
//        }
//    ;
//
//    };
    private final ActionListener Help = new ActionListener() {

        public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(null, "List(X)List to check Next States "
                    + "\n List(G)List to check Global States"
                    + "\n List(F)List to check Future States"
                    + "\n List(U)List to check Until States"
                    + "\n List(AllX)List to check All Next States for the previous List"
                    + "\n List(AllF)List to check All Future States for the previous List"
                    + "\n List(AllU)List to check All Until States for the previuos List"
                    + "\n (E)List to check Places if true at the same time  "
                    + "\n (Deadlock) to find deadlock in the active petri "
                    + "\n (Liveness) to check Liveness in the active petri ", " Help Syntax Menu ", JOptionPane.INFORMATION_MESSAGE);

        }
    ;
    };
        
    public final ActionListener Deadlock = new ActionListener() {

        public void actionPerformed(ActionEvent arg0) {
            int indexOfLastGraph = LTLqueries.ReadRG.allGraphs.size() - 1;
            mygraph = LTLqueries.ReadRG.allGraphs.get(indexOfLastGraph);

            ArrayList<Path> deadlockpaths = new ArrayList<Path>();
            deadlockpaths.addAll(mygraph.getDeadlocks());

            if (deadlockpaths.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There is no deadlock in this Petri net",
                        "Result of Deadlock", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                // get names of all deadlock states and it's transitions that drive petri net to it
                StringBuilder namesOfdeadlockStatesAndTrans = new StringBuilder();
                mygraph.showPaths(deadlockpaths);
                for (Path p : deadlockpaths) {
                    namesOfdeadlockStatesAndTrans.append("\nS" + p.peek().getID() + "\n");
                    namesOfdeadlockStatesAndTrans.append("transitions: ");
                    ArrayList<TransitionRecord> transOfPath = new ArrayList<TransitionRecord>();
                    transOfPath.addAll(mygraph.getTransitionsOfPath(p));
                    for (TransitionRecord tran : transOfPath) {
                        namesOfdeadlockStatesAndTrans.append("T" + Integer.toString(tran.getTransitionNo() - 1) + ", ");
                    }
                    namesOfdeadlockStatesAndTrans.delete(namesOfdeadlockStatesAndTrans.length() - 2, namesOfdeadlockStatesAndTrans.length());
                    namesOfdeadlockStatesAndTrans.append(".\n");
                    namesOfdeadlockStatesAndTrans.append("S" + p.peek().getID() + "=" + p.peek().getMarkingString() + ".\n");

                }
                JOptionPane.showMessageDialog(null, "this Petri net has deadlock in: \n" + namesOfdeadlockStatesAndTrans,
                        "Result of Deadlock", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    };

    public final ActionListener Liveness = new ActionListener() {

        public void actionPerformed(ActionEvent arg0) {
            File reachabilityGraph = new File("results.rg");
            PetriNetView sourcePetriNetView = ApplicationSettings.getApplicationView().getCurrentPetriNetView();//sourceFilePanel.getDataLayer();
            boolean generateCoverability = false;
            try {
                StateSpaceGenerator.generate(sourcePetriNetView, reachabilityGraph);
            } catch (OutOfMemoryError e) {
                // net seems to be unbounded, let's try to generate the
                // coverability graph
                generateCoverability = true;
            } catch (ImmediateAbortException ex) {
                Logger.getLogger(LtlQuery.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LtlQuery.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MarkingNotIntegerException ex) {
                Logger.getLogger(LtlQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                DefaultGraph rgraph = createGraph(reachabilityGraph, sourcePetriNetView,
                        generateCoverability);
            } catch (IOException ex) {
                Logger.getLogger(LtlQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            int indexOfLastGraph = LTLqueries.ReadRG.allGraphs.size() - 1;
            mygraph = LTLqueries.ReadRG.allGraphs.get(indexOfLastGraph);

            // here show linvness if existed
            ArrayList<Path> livenessPaths = new ArrayList<Path>();
            livenessPaths.addAll(mygraph.getLiveness());
            if (livenessPaths.isEmpty()) {
                JOptionPane.showMessageDialog(null, "This Petri net is not aLive.",
                        "Result of Liveness", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                // get names of all states of all livenesspaths 
                StringBuilder namesofPathsStatesAndTrans = new StringBuilder();
                namesofPathsStatesAndTrans.append("this Petri net is alive in these paths:\n");
                mygraph.showPaths(livenessPaths);
                for (Path p : livenessPaths) {
                    namesofPathsStatesAndTrans.append("\npath: ");
                    for (StateRecord s : p.getStates()) {
                        namesofPathsStatesAndTrans.append("S" + s.getID() + ", ");
                    }
                    namesofPathsStatesAndTrans.delete(namesofPathsStatesAndTrans.length() - 2, namesofPathsStatesAndTrans.length());
                    namesofPathsStatesAndTrans.append(".\n");

                    // getting transitions of this path (p)
                    namesofPathsStatesAndTrans.append("transitions: ");
                    ArrayList<TransitionRecord> transOfPath = new ArrayList<TransitionRecord>();
                    transOfPath.addAll(mygraph.getTransitionsOfPath(p));
                    for (TransitionRecord tran : transOfPath) {
                        namesofPathsStatesAndTrans.append("T" + Integer.toString(tran.getTransitionNo() - 1) + ", ");
                    }
                    namesofPathsStatesAndTrans.delete(namesofPathsStatesAndTrans.length() - 2, namesofPathsStatesAndTrans.length());
                    namesofPathsStatesAndTrans.append(".\n");

                    for (StateRecord s : p.getStates()) {
                        namesofPathsStatesAndTrans.append("\t S" + s.getID() + "=" + s.getMarkingString() + "\n");
                    }
                }

                // showing
                // declation of new frame 
                final JFrame frame;
                frame = new JFrame("Liveness Result");
                frame.setLocationRelativeTo(null);
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                // declarating of text area and insert nemes of states of paths into it
                JTextArea textArea = new JTextArea();
                textArea.setText(namesofPathsStatesAndTrans.toString());
                textArea.setFont(textArea.getFont().deriveFont(12f));

                // declaration of scroll panel and insert it in frame
                final JScrollPane panel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                frame.add(panel);

                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        }
    ;

};
}
