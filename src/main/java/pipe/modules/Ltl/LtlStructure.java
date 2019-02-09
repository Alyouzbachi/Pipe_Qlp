/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipe.modules.Ltl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JOptionPane;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.defaults.DefaultEdge;
import net.sourceforge.jpowergraph.defaults.DefaultGraph;
import net.sourceforge.jpowergraph.defaults.DefaultNode;
import net.sourceforge.jpowergraph.defaults.TextEdge;
import pipe.calculations.myNode;
import pipe.calculations.myTree;
import pipe.exceptions.TreeTooBigException;
import pipe.extensions.jpowergraph.PIPEGreenState;
import pipe.extensions.jpowergraph.PIPEInitialState;
import pipe.extensions.jpowergraph.PIPEInitialTangibleState;
import pipe.extensions.jpowergraph.PIPEInitialVanishingState;
import pipe.extensions.jpowergraph.PIPELoopWithTextEdge;
import pipe.extensions.jpowergraph.PIPEState;
import pipe.extensions.jpowergraph.PIPETangibleState;
import pipe.extensions.jpowergraph.PIPEVanishingState;
import pipe.extensions.jpowergraph.PIPEYellowState;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.GraphFrame;
import pipe.io.ImmediateAbortException;
import pipe.io.IncorrectFileFormatException;
import pipe.io.ReachabilityGraphFileHeader;
import pipe.io.StateRecord;
import pipe.io.TransitionRecord;
import static pipe.modules.Ltl.LtlQuery.ltlStct;
import pipe.views.MarkingView;
import pipe.views.PetriNetView;

/**
 *
 * @author Yazan
 */
public class LtlStructure {

    private List<String> places = new ArrayList<String>();
    private List<String> parsingStates = new ArrayList<String>();
    private List<int[]> markings = new ArrayList<int[]>();
    private Map<String, Set<String>> nextStates = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> previousStates = new HashMap<String, Set<String>>();
    private List<ArrayList<String>> untilPath = new ArrayList<ArrayList<String>>();
    private Stack<String> path = new Stack<String>();
    private ArrayList<String> onPath = new ArrayList<String>();
    private Stack<String> currPath = new Stack<String>();
    //edit with setters and getters
    private DefaultGraph DeadlockGraph;
    private static DefaultGraph graph;
    Set<Edge> DrawEdgesTemp = new HashSet<Edge>();
    ArrayList DrawEdges = new ArrayList();
    ArrayList DrawNodes = new ArrayList();
    static File rgFile = new File("results.rg");
    private static PetriNetView dataLayer;
    private static boolean coverabilityGraph;
    GraphFrame frame = new GraphFrame();
    static List<String> AcceptedNodes = new ArrayList<String>();
    static List<String> RejectedNodes = new ArrayList<String>();
//edit

    public PetriNetView getSourcePetriNetView() {
        return dataLayer;
    }

    public void setSourcePetriNetView(PetriNetView dataLayer) {
        this.dataLayer = dataLayer;
    }

    public boolean isGenerateCoverability() {
        return coverabilityGraph;
    }

    public void setGenerateCoverability(boolean coverabilityGraph) {
        this.coverabilityGraph = coverabilityGraph;
    }

    public DefaultGraph getGraph() {
        return graph;
    }

    public void setGraph(DefaultGraph graph) {
        this.graph = graph;
    }

    public DefaultGraph getDeadlockGraph() {
        return DeadlockGraph;
    }

    public void setDeadlockGraph(DefaultGraph DeadlockGraph) {
        this.DeadlockGraph = DeadlockGraph;
    }

    public LtlStructure() {
    }

    //Setters and getters
    public List<ArrayList<String>> getUntilPath() {
        return untilPath;
    }

    public void setUntilPath(List<ArrayList<String>> untilPath) {
        this.untilPath = untilPath;
    }

    public Map<String, Set<String>> getPreviousStates() {
        return previousStates;
    }

    public void setPreviousStates(Map<String, Set<String>> previousStates) {
        this.previousStates = previousStates;
    }

    public List<String> getPlaces() {
        return places;
    }

    public Map<String, Set<String>> getNextStates() {
        return nextStates;
    }

    public void setNextStates(Map<String, Set<String>> nextStates) {
        this.nextStates = nextStates;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<String> getParsingStates() {
        return parsingStates;
    }

    public void setParsingStates(List<String> parsingStates) {
        this.parsingStates = parsingStates;
    }

    public List<int[]> getMarkings() {
        return markings;
    }

    public void setMarkings(List<int[]> markings) {
        this.markings = markings;
    }

    public void addNextState(String a, String nxt) {
        if (nextStates.containsKey(a)) {
            Set<String> temp = nextStates.get(a);
            temp.add(nxt);
        } else {
            Set<String> temp = new HashSet<String>();
            temp.add(nxt);
            nextStates.put(a, temp);
        }
    }

    public void addPrevState(String a, String nxt) {
        if (previousStates.containsKey(a)) {
            Set<String> temp = previousStates.get(a);
            temp.add(nxt);
        } else {
            Set<String> temp = new HashSet<String>();
            temp.add(nxt);
            previousStates.put(a, temp);
        }
    }

    // getting only states that are true depending on a place name
    public Set<String> getStatesForPlace(int id) {
        int counter = 0;
        Set<String> temp = new HashSet<String>();
        for (int[] mark : markings) {
            if (mark[id] > 0 || mark[id] == -1) {
                temp.add(parsingStates.get(counter));
            }
            counter++;
        }
        return temp;
    }

    public boolean NextState(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && !after.isEmpty()) {
            Set<String> a = nextStates.get("S0");
            Set<Integer> placesId = new HashSet<Integer>();
            for (String q : after) {
                q = q.toUpperCase();
                int id = places.indexOf(q);
                placesId.add(id);
            }
            for (String a1 : a) {
                int id = parsingStates.indexOf(a1);
                int[] mark = markings.get(id);
                for (int n : placesId) {
                    if (mark[n] > 0 || mark[n] == -1) {
                        placesId.remove(n);
                        AcceptedNodes.add(a1);
                    }
                }
                if (!AcceptedNodes.contains(a1)) {
                    RejectedNodes.add(a1);
                }
            }
            if (placesId.isEmpty()) {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return true;
            } else {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return false;
            }
        } else if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after X to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place After X to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                boolean s = checkNextForPlace(b, after);
                if (s == false) {
                    createGraph();
                    frame.constructGraphFrame(graph, "Analayis Query");
                    return false;
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        }
        return false;
    }

    public boolean checkNextForPlace(String p, List<String> list) {
        int idP = places.indexOf(p);
        Set<String> states = ltlStct.getStatesForPlace(idP);
        Set<Integer> placesId = new HashSet<Integer>();
        for (String list1 : list) {
            list1 = list1.toUpperCase();
            placesId.add(places.indexOf(list1));
        }
        for (String u : states) {
            Set<String> next = nextStates.get(u);
            if (!next.isEmpty()) {
                for (String a : next) {
                    int id = parsingStates.indexOf(a);
                    int[] mark = markings.get(id);
                    for (Iterator<Integer> i = placesId.iterator(); i.hasNext();) {
                        Integer element = i.next();
                        if (mark[element] > 0 || mark[element] == -1) {
                            AcceptedNodes.add(a);
                            i.remove();
                        }
                        if (!AcceptedNodes.contains(a)) {
                            RejectedNodes.add(a);
                        }
                    }
                }
                if (placesId.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean FutureState(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && !after.isEmpty()) {
            Set<String> futureState = getFutureStatesForS0();
            Set<Integer> placesId = new HashSet<Integer>();
            for (String q : after) {
                q = q.toUpperCase();
                int id = places.indexOf(q);
                placesId.add(id);
            }
            for (String a1 : futureState) {
                int id = parsingStates.indexOf(a1);
                int[] mark = markings.get(id);
                for (Iterator<Integer> i = placesId.iterator(); i.hasNext();) {
                    Integer element = i.next();
                    if (mark[element] > 0 || mark[element] == -1) {
                        AcceptedNodes.add(a1);
                        i.remove();
                    }
                    if (!AcceptedNodes.contains(a1)) {
                        RejectedNodes.add(a1);
                    }
                }
            }
            if (placesId.isEmpty()) {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return true;
            } else {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return false;
            }
        } else if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after F to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place After F to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                boolean s = checkFuturetForPlace(b, after);
                if (s == false) {
                    createGraph();
                    frame.constructGraphFrame(graph, "Analayis Query");
                    return false;

                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        }
        return false;
    }

    public boolean checkFuturetForPlace(String p, List<String> list) {

        int id = places.indexOf(p);
        Set<String> futureStates = getFutureStates(id);
        Set<Integer> placesId = new HashSet<Integer>();
        for (String list1 : list) {
            list1 = list1.toUpperCase();
            int idp = places.indexOf(list1);
            placesId.add(idp);
        }

        for (String state : futureStates) {
            int ids = parsingStates.indexOf(state);
            int[] mark = markings.get(ids);
            for (Iterator<Integer> i = placesId.iterator(); i.hasNext();) {
                Integer element = i.next();
                if (mark[element] > 0 || mark[element] == -1) {
                    AcceptedNodes.add(state);
                    i.remove();
                }
                if (!AcceptedNodes.contains(state)) {
                    RejectedNodes.add(state);
                }
            }

        }
        if (placesId.isEmpty()) {
            return true;
        }
        return false;
    }

    public Set<String> getFutureStatesForAstate(String state) {
        Set<String> startPlaces = new HashSet<String>();
        startPlaces.add(state);
        Set<String> alreadyChecked = new HashSet<String>();
        Set<String> next = new HashSet<String>();
        Stack a = new Stack();
        for (String startPlace : startPlaces) {
            a.add(startPlace);
        }
        while (!a.isEmpty()) {
            String temp = (String) a.pop();
            next = nextStates.get(temp);
            if (next != null) {
                for (String next1 : next) {
                    if (!alreadyChecked.contains(next1)) {
                        alreadyChecked.add(next1);
                        if (!a.contains(next1)) {
                            a.add(next1);
                        }
                    }
                }
            }
        }
        return alreadyChecked;
    }

    public Set<String> getFutureStatesForS0() {
        Set<String> startPlaces = new HashSet<String>();
        startPlaces.add("S0");
        Set<String> alreadyChecked = new HashSet<String>();
        Set<String> next = new HashSet<String>();
        Stack a = new Stack();
        for (String startPlace : startPlaces) {
            a.add(startPlace);
        }
        while (!a.isEmpty()) {
            String temp = (String) a.pop();
            next = nextStates.get(temp);
            if (next != null) {
                for (String next1 : next) {
                    if (!alreadyChecked.contains(next1)) {
                        alreadyChecked.add(next1);
                        if (!a.contains(next1)) {
                            a.add(next1);
                        }
                    }
                }
            }
        }
        return alreadyChecked;
    }

    // Iterative Dfs for finding all future states for a determined state
    public Set<String> getFutureStates(int id) {
        Set<String> startPlaces = getStatesForPlace(id);
        Set<String> alreadyChecked = new HashSet<String>();
        Set<String> next = new HashSet<String>();
        Stack a = new Stack();
        for (String startPlace : startPlaces) {
            a.add(startPlace);
        }
        while (!a.isEmpty()) {
            String temp = (String) a.pop();
            next = nextStates.get(temp);
            if (next != null) {
                for (String next1 : next) {
                    if (!alreadyChecked.contains(next1)) {
                        alreadyChecked.add(next1);
                        if (!a.contains(next1)) {
                            a.add(next1);
                        }
                    }
                }
            }
        }
        return alreadyChecked;
    }

    public boolean UntilState(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after U to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty() || before.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before Or After U to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                for (String a : after) {
                    a = a.toUpperCase();
                    boolean s = checkUntilState(b, a);
                    if (s == false) {
                        createGraph();
                        frame.constructGraphFrame(graph, "Anayalis Query");
                        return false;

                    }
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Anayalis Query");
            return true;
        }
        return false;
    }

    // fill all pathes for a determined state
    public boolean checkUntilState(String place1, String place2) {
        int id = places.indexOf(place1);
        int id2 = places.indexOf(place2);
        Set<String> startPlaces = getStatesForPlace(id);
        //FindAllPaths(startPlaces, id);
        for (String a : startPlaces) {
            Set<String> nextPlaces = nextStates.get(a);
            if (nextPlaces != null) {
                for (String b : nextPlaces) {
                    int k = parsingStates.indexOf(b);
                    int[] mark = markings.get(k);
                    if (mark[id2] == -1 || mark[id2] > 0) {
                        AcceptedNodes.add(b);
                        return true;
                    }
                    if (!AcceptedNodes.contains(b)) {
                        RejectedNodes.add(b);
                    }
                }
            }
        }
        return false;
    }

    public boolean CheckGlobalState(String place1) {

        place1 = place1.toUpperCase();
        int i = places.indexOf(place1);
        for (int[] temp : markings) {
            int k = markings.indexOf(temp);
            String a = parsingStates.get(k);
            if (temp[i] == 0) {
                RejectedNodes.add(a);
                return false;
            }
            AcceptedNodes.add(a);
        }
        return true;
    }

    public boolean GlobalState(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && !after.isEmpty()) {
            for (String after1 : after) {
                boolean m = CheckGlobalState(after1);
                if (m == false) {
                    createGraph();
                    frame.constructGraphFrame(graph, "Anayalis Query");
                    return false;
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Anayalis Query");
            return true;
        } else if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after G to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place After G to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                int id = places.indexOf(b);
                Set<String> states = getStatesForPlace(id);
                for (String state : states) {
                    Set<String> future = getFutureStatesForAstate(state);
                    for (String future1 : future) {
                        int ids = parsingStates.indexOf(future1);
                        int[] mark = markings.get(ids);
                        for (String after1 : after) {
                            after1 = after1.toUpperCase();
                            int idp = places.indexOf(after1);
                            if (mark[idp] == 0) {
                                RejectedNodes.add(future1);
                                createGraph();
                                frame.constructGraphFrame(graph, "Anayalis Query");
                                return false;
                            }
                            if (!RejectedNodes.contains(future1)) {
                                AcceptedNodes.add(future1);
                            }
                        }
                    }
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Anayalis Query");
            return true;
        }
        return false;
    }

    public boolean Exist(List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        Set<Integer> placesId = new HashSet<Integer>();
        Set<String> statesExist = new HashSet<String>();
        boolean check = true;
        if (!after.isEmpty()) {
            for (String after1 : after) {
                after1 = after1.toUpperCase();
                int id = places.indexOf(after1);
                placesId.add(id);
            }
            for (int[] marking : markings) {
                check = true;
                int id = markings.indexOf(marking);
                String state = parsingStates.get(id);
                for (int pId : placesId) {
                    if (marking[pId] == 0) {
                        RejectedNodes.add(state);
                        check = false;
                    }
                }
                if (check == true) {
                    AcceptedNodes.add(state);
                    statesExist.add(state);
                }
            }
            if (!statesExist.isEmpty()) {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return true;
            } else {
                createGraph();
                frame.constructGraphFrame(graph, "Analayis Query");
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean AllNext(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && !after.isEmpty()) {
            Set<String> a = nextStates.get("S0");
            Set<Integer> placesId = new HashSet<Integer>();
            for (String q : after) {
                q = q.toUpperCase();
                int id = places.indexOf(q);
                placesId.add(id);
            }
            for (String a1 : a) {
                int id = parsingStates.indexOf(a1);
                int[] mark = markings.get(id);
                for (int n : placesId) {
                    if (mark[n] == 0) {
                        RejectedNodes.add(a1);
                        createGraph();
                        frame.constructGraphFrame(graph, "Analayis Query");
                        return false;
                    }
                    if (!RejectedNodes.contains(a1)) {
                        AcceptedNodes.add(a1);
                    }
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        } else if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after AllX to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place After AllX to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                int id = places.indexOf(b);
                Set<String> states = getStatesForPlace(id);
                for (String state : states) {
                    Set<String> nx = nextStates.get(state);
                    for (String nx1 : nx) {
                        for (String after1 : after) {
                            after1 = after1.toUpperCase();
                            int ida = places.indexOf(after1);
                            int stateid = parsingStates.indexOf(nx1);
                            int[] mark = markings.get(stateid);
                            if (mark[ida] == 0) {
                                RejectedNodes.add(nx1);
                                createGraph();
                                frame.constructGraphFrame(graph, "Analayis Query");
                                return false;
                            }
                            if (!RejectedNodes.contains(nx1)) {
                                AcceptedNodes.add(nx1);
                            }
                        }
                    }
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        }
        return false;
    }

//    public void ColorNode(String name, boolean m) {
//        List<Node> nodes = new ArrayList<Node>();
//        List<Edge> edges = new ArrayList<Edge>();
//        List<Edge> from = new ArrayList<Edge>();
//        List<Edge> to = new ArrayList<Edge>();
//        String label = null, nodeType, marking = null;
//        PIPEYellowState state = new PIPEYellowState(null, null);
//        int[] marks;
//        double x, y, repulsion;
//        Node temp = null;
//        nodes = graph.getAllNodes();
//        edges = graph.getAllEdges();
//        for (Node node : nodes) {
//            if (node.getLabel().equalsIgnoreCase(name)) {
//                from = node.getEdgesFrom();
//                to = node.getEdgesTo();
//                label = node.getLabel();
//                int id = parsingStates.indexOf(label);
//                marks = markings.get(id);
//                marking = Arrays.toString(marks);
//                nodeType = node.getNodeType();
//                x = node.getX();
//                y = node.getY();
//                repulsion = node.getRepulsion();
//                temp = node;
//            }
//        }
//        if (temp != null) {
//            state = new PIPEYellowState(label, marking);
//            //    state.setEdges(from, to);
//        }
//        for (Edge ed : edges) {
//            Node fromNode = ed.getFrom();
//            Node toNode = ed.getTo();
//            TextEdge edTemp = new TextEdge(null, null, null);
//            if ((fromNode != temp) && (toNode != temp)) {
//                DrawEdgesTemp.add(ed);
//            }
//            if (fromNode == temp && toNode == temp) {
//                edTemp = new TextEdge(state, state, null);
//                DrawEdgesTemp.add(edTemp);
//            }
//            if (fromNode == temp) {
//                edTemp = new TextEdge(state, toNode, null);
//                DrawEdgesTemp.add(edTemp);
//            }
//            if (toNode == temp) {
//                edTemp = new TextEdge(fromNode, state, null);
//                DrawEdgesTemp.add(edTemp);
//            }
//
//        }
//        nodes.remove(temp);
//        nodes.add(state);
//        DrawNodes = (ArrayList) nodes;
//        for (Edge no : DrawEdgesTemp) {
//            DrawEdges.add(no);
//        }
//        graph.addElements(DrawNodes, DrawEdges);
//
//    }
    public boolean AllFuture(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && !after.isEmpty()) {
            Set<String> futureState = getFutureStatesForS0();
            for (String a1 : futureState) {
                int id = parsingStates.indexOf(a1);
                int[] mark = markings.get(id);
                for (String after1 : after) {
                    after1 = after1.toUpperCase();
                    int ida = places.indexOf(after1);
                    if (mark[ida] == 0) {
                        RejectedNodes.add(a1);
                        createGraph();
                        frame.constructGraphFrame(graph, "Analayis Query");
                        return false;
                    }
                }
            }
            return true;
        } else if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after AllF to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place After AllF to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                int id = places.indexOf(b);
                Set<String> futureState = getFutureStates(id);
                if (!futureState.isEmpty()) {
                    for (String fs : futureState) {
                        int ids = parsingStates.indexOf(fs);
                        int[] mark = markings.get(ids);
                        for (String after1 : after) {
                            after1 = after1.toUpperCase();
                            int idp = places.indexOf(after1);
                            if (mark[idp] == 0) {
                                RejectedNodes.add(fs);
                                createGraph();
                                frame.constructGraphFrame(graph, "Analayis Query");
                                return false;
                            }
                            if (!RejectedNodes.contains(fs)) {
                                AcceptedNodes.add(fs);
                            }
                        }
                    }
                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        }
        return false;
    }

    public boolean AllUntil(List<String> before, List<String> after) throws IOException {
        AcceptedNodes = new ArrayList<String>();
        RejectedNodes = new ArrayList<String>();
        if (before.isEmpty() && after.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before and after AllUntil to check !!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else if (after.isEmpty() || before.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error no place before or After AllUntil to check!!!", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String b : before) {
                b = b.toUpperCase();
                int id = places.indexOf(b);
                Set<String> states = getStatesForPlace(id);
                if (!states.isEmpty()) {
                    for (String st : states) {
                        Set<String> next = nextStates.get(st);
                        for (String next1 : next) {
                            int idnext = parsingStates.indexOf(next1);
                            int[] mark = markings.get(idnext);
                            for (String after1 : after) {
                                after1 = after1.toUpperCase();
                                int idp = places.indexOf(after1);
                                if (mark[idp] == 0) {
                                    RejectedNodes.add(next1);
                                    createGraph();
                                    frame.constructGraphFrame(graph, "Analayis Query");
                                    return false;
                                }
                                AcceptedNodes.add(next1);
                            }
                        }
                    }

                }
            }
            createGraph();
            frame.constructGraphFrame(graph, "Analayis Query");
            return true;
        }
        return false;
    }

    private void FindAllPaths(Set<String> place1, int id) {
        for (String place : place1) {
            FindAllPaths(place, id);
        }
    }
    //   private ArrayList<Pp> paths = new ArrayList<Pp>();

    private void FindAllPaths(String placeName, int id) {
        currPath.push(placeName);
        //onPath.add(placeName);
        Set<String> NextStates = nextStates.get(placeName);
        if (!NextStates.isEmpty()) {
            for (String state : NextStates) {
                if ((nextStates.get(state).isEmpty()) || (currPath.contains(state))) {
                    currPath.push(state);
                    ArrayList<String> list = new ArrayList<String>();
                    list.addAll(currPath);
                    untilPath.add(list);
                } else {
                    int k = parsingStates.indexOf(state);
                    int[] marking = markings.get(k);
                    if (marking[id] == -1 || marking[id] > 0) {
                        FindAllPaths(state, id);
                    }
                }
                try {
                    currPath.pop();
                } catch (EmptyStackException e) {
                    continue;
                }

            }

        }
    }

    //edit
    public void findDeadlocks() throws IOException, TreeTooBigException, ImmediateAbortException {
        GraphFrame frame = new GraphFrame();
        List<Node> nodes = new ArrayList<Node>();
        List<Node> deadlocks = new ArrayList<Node>();
        //  Node dead = null;
        nodes = DeadlockGraph.getAllNodes();
        for (Node node : nodes) {
            if (node.getEdgesFrom().isEmpty()) {
                deadlocks.add(node);
            }
        }
        DeadlockGraph.clear();
        //  nodes.clear();
        //    nodes.add(dead);
        DeadlockGraph.addElements(deadlocks, null);
        if (deadlocks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No deadlock found", "Query Result: ", JOptionPane.INFORMATION_MESSAGE);

        } else {
//            PetriNetView sourcePetriNetView = ApplicationSettings.getApplicationView().getCurrentPetriNetView();//sourceFilePanel.getDataLayer();
//            LinkedList<MarkingView>[] markings = sourcePetriNetView.getInitialMarkingVector();
//            int[] currentMarking = new int[markings.length];
//            for (int i = 0; i < markings.length; i++) {
//                currentMarking[i] = markings[i].getFirst().getCurrentMarking();
//            }
//            File reachabilityGraph = new File("results.rg");
//
//            myTree tree = new myTree(sourcePetriNetView,
//                    currentMarking,
//                    reachabilityGraph);
//            tree.root.RecordDeadlockPath();
//            int [] dead = tree.pathToDeadlock;
            frame.constructGraphFrame(DeadlockGraph, "Deadlock");
        }
    }

    private static DefaultGraph createGraph() throws IOException {
        DefaultGraph graph1 = new DefaultGraph();

        ReachabilityGraphFileHeader header = new ReachabilityGraphFileHeader();
        RandomAccessFile reachabilityFile;
        try {
            reachabilityFile = new RandomAccessFile(rgFile, "r");
            header.read(reachabilityFile);
        } catch (IncorrectFileFormatException e1) {
            System.err.println("createGraph: "
                    + "incorrect file format on state space file");
            return graph1;
        } catch (IOException e1) {
            System.err.println("createGraph: unable to read header file");
            return graph1;
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
        marking = record.getMarkingString();
        if (AcceptedNodes.contains(label)) {
            nodes.add(new PIPEInitialState(label, marking));
        } else if (RejectedNodes.contains(label)) {
            nodes.add(new PIPEGreenState(label, marking));
        } else {
            nodes.add(coverabilityGraph
                    ? new PIPEState(label, marking)
                    : new PIPEVanishingState(label, marking));

        }

        for (int count = 1; count < header.getNumStates(); count++) {
            record.read1(stateArraySize, reachabilityFile);
            label = "S" + count;
            marking = record.getMarkingString();
            if (AcceptedNodes.contains(label)) {
                nodes.add(new PIPEInitialState(label, marking));
            } else if (RejectedNodes.contains(label)) {
                nodes.add(new PIPEGreenState(label, marking));
            } else {
                nodes.add(coverabilityGraph
                        ? new PIPEState(label, marking)
                        : new PIPEVanishingState(label, marking));
            }
        }

        reachabilityFile.seek(header.getOffsetToTransitions());
        int numberTransitions = header.getNumTransitions();
        for (int transitionCounter = 0;
                transitionCounter < numberTransitions;
                transitionCounter++) {
            TransitionRecord transitions = new TransitionRecord();
            transitions.read1(reachabilityFile);

            int from = transitions.getFromState();
            int to = transitions.getToState();
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

        for (int i = 0;
                i < loopEdges.size();
                i++) {
            edges.add(new PIPELoopWithTextEdge((DefaultNode) (loopEdges.get(i)),
                    (String) (loopEdgesTransitions.get(i))));
        }

        graph1.addElements(nodes, edges);
        graph = graph1;
        return graph1;
    }

}
   // public void findStatePaths(String place1) {

//int id = places.indexOf(place1);
//        Set<String> startPlaces = getStatesForPlace(id);
//        List<String> path = new ArrayList<String>();
//        Set<String> next = new HashSet<String>();
//        Stack a = new Stack();
//        Stack b = new Stack();
//        boolean check = false;
//
//        for (String startPlace : startPlaces) {
//            a.add(startPlace);
//        }
//        
//        while (!a.isEmpty()) {
//            String temp = (String) a.pop();
//            path.add("|");
//            path.add(temp);
//            next = nextStates.get(temp);
//            if (next != null) {
//                for (String next1 : next) {
//                        check = false;
//                        int i = parsingStates.indexOf(next1);
//                        int[] mark = markings.get(i);
//                        if (mark[id] == -1 || mark[id] > 0) {
//                            path.add(next1);
//                            check = true;
//                        }
//                        if (!a.contains(next1) && check == true) {
//                            a.add(next1);
//                        }
//                    
//                }
//            }
//        }
//        System.err.println("");
