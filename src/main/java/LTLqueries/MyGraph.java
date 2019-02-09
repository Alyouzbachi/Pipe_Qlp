/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LTLqueries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.defaults.DefaultGraph;
import net.sourceforge.jpowergraph.defaults.DefaultNode;
import net.sourceforge.jpowergraph.defaults.TextEdge;
import org.w3c.dom.Node;
import pipe.extensions.jpowergraph.PIPEState;
import pipe.gui.widgets.GraphFrame;
import pipe.io.StateRecord;
import pipe.io.TransitionRecord;
import pipe.modules.reachability.ReachabilityGraphGenerator;
import static pipe.modules.reachability.ReachabilityGraphGenerator.createGraph;
import pipe.views.PetriNetView;

/**
 *
 * @author TOSHIBA
 */
public class MyGraph {

    private List<StateRecord> States = new ArrayList<StateRecord>();
    private List<TransitionRecord> Transitions = new ArrayList<TransitionRecord>();

    private File rgFile;
    private PetriNetView dataLayer;
    private StateRecord LastCurrentState;
    private StateRecord LastCurrentStateofGeneratepaths;
    private List<String> NamesofPlaces;

    public MyGraph() {
    }

    public void setNamesofPlaces(List<String> NamesofPlaces) {
        this.NamesofPlaces = NamesofPlaces;
    }

    public File getRgFile() {
        return rgFile;
    }

    public PetriNetView getDataLayer() {
        return dataLayer;
    }

    public void setRgFile(File rgFile) {
        this.rgFile = rgFile;
    }

    public void setDataLayer(PetriNetView dataLayer) {
        this.dataLayer = dataLayer;
    }

    public List<String> getNamesofPlaces() {
        return NamesofPlaces;
    }

    public StateRecord getLastCurrentState() {
        return LastCurrentState;
    }

    public void setLastCurrentState(StateRecord LastCurrentState) {
        this.LastCurrentState = LastCurrentState;
    }

    public MyGraph(List<StateRecord> S, List<TransitionRecord> T) {
        States = S;
        Transitions = T;
        LastCurrentState = S.get(0);
    }

    public List<StateRecord> getStates() {
        return States;
    }

    public List<TransitionRecord> getTransitions() {
        return Transitions;
    }

    // retrun the transition that is located between two states
    public TransitionRecord getTransition(StateRecord lastState, StateRecord nextState) {

        for (TransitionRecord tran : Transitions) {
            if (tran.getFromState() == lastState.getID() && tran.getToState() == nextState.getID()) {
                return tran;
            }
        }
        return null;
    }

    // return the transitions Of path that is existed in the graph 
    public ArrayList<TransitionRecord> getTransitionsOfPath(Path path) {
        ArrayList<TransitionRecord> Transitions = new ArrayList<TransitionRecord>();
        for (int i = 0; i < path.getStates().size() - 1; i++) {
            Transitions.add(getTransition(path.getStates().elementAt(i), path.getStates().elementAt(i + 1)));
        }
        return Transitions;
    }

    public ArrayList<TransitionRecord> getOutTransFromState(StateRecord state) {
        int stateID = state.getID();
        ArrayList<TransitionRecord> OutTransFromState = new ArrayList<TransitionRecord>();

        for (TransitionRecord tran : Transitions) {

            if (tran.getFromState() == stateID) {
                OutTransFromState.add(tran);
            }
        }
        return OutTransFromState;
    }

    public ArrayList<TransitionRecord> getInTransToState(StateRecord state) {
        int stateID = state.getID();
        ArrayList<TransitionRecord> InTransToState = new ArrayList<TransitionRecord>();

        for (TransitionRecord tran : Transitions) {

            if (tran.getToState() == stateID) {
                InTransToState.add(tran);
            }
        }
        return InTransToState;
    }

    public StateRecord getState(int ID) {

        for (StateRecord state : States) {

            if (state.getID() == ID) {
                return state;
            }
        }
        return null;
    }
    
    public ArrayList<StateRecord> getInvestigatedStates(StateRecord spesificState){
        ArrayList<StateRecord> result = new ArrayList<StateRecord>();
        for(StateRecord state : this.States){
            if(spesificState.IsInvestigate(state)){
                result.add(state);
            }
        }
        
        return result;
    }
    

    // return the state according it's markings
    public StateRecord getStateRecord(int[] markings) {

        if (markings == null) {
            return null;
        }
        if (!FitVector(markings.length)) {
            return null;
        }
        if (hasAnyValue(markings)) {
            StateRecord state = new StateRecord(-1, false, markings);
            return state;
        }
        for (StateRecord state : States) {
            for (int i = 0; i < markings.length; i++) {
                if (state.getState()[i] != markings[i]) {
                    break;
                } else if (i == markings.length - 1) {
                    return state;
                }
            }

        }
        return null;
    }

    // to check the vactor if has any value (not important value) for one place or more
    private boolean hasAnyValue(int[] markings) {

        for (int i = 0; i < markings.length; i++) {
            if (markings[i] == -2) {
                return true;
            }
        }
        return false;
    }

    // to check if the vector of numbers has length = the number of places
    public boolean FitVector(int len) {

        if (len == States.get(0).getState().length) {
            return true;
        }
        return false;
    }

    public ArrayList<StateRecord> getNextStatesFromState(StateRecord state) {

        ArrayList<TransitionRecord> OutTransFromState = getOutTransFromState(state);
        ArrayList<StateRecord> NextStatesFromState = new ArrayList<StateRecord>();
        int nextStateID = 0;
        for (TransitionRecord tran : OutTransFromState) {
            nextStateID = tran.getToState();
            NextStatesFromState.add(this.getState(nextStateID));
        }
        return NextStatesFromState;
    }

    // return paths that end with deadlock
    public ArrayList<Path> getDeadlocks() {

        ArrayList<Path> DeadlockPaths = new ArrayList<Path>();
        // if there is a path ends with state not repeated in it so this state is a deadlock
        
        ArrayList<Path> allpaths = new ArrayList<Path>();
        allpaths.addAll(getAllPaths(States.get(0)));
        for (Path p : allpaths) {
           if (p.getStates().indexOf(p.peek()) == p.getStates().lastIndexOf(p.peek())) {
                DeadlockPaths.add(p);
            }
        }
        return DeadlockPaths;
    }


    public ArrayList<Path> getLiveness() {

        ArrayList<Path> allpaths = new ArrayList<Path>();
        ArrayList<Path> Loops = new ArrayList<Path>();

        // if doesn't have any path has loop so it is not live
        allpaths.addAll(getAllPaths(States.get(0)));
        for (Path p : allpaths) {
            if (!p.getRepeatedStates().isEmpty()) {
                Loops.add(p);
            }
        }
        return Loops;
    }

    public ArrayList<StateRecord> getLastStatesToState(StateRecord state) {

        ArrayList<TransitionRecord> InTransToState = getInTransToState(state);
        ArrayList<StateRecord> LastStatesToState = new ArrayList<StateRecord>();
        int lastStateID = 0;
        for (TransitionRecord tran : InTransToState) {
            lastStateID = tran.getFromState();
            LastStatesToState.add(this.getState(lastStateID));
        }
        return LastStatesToState;
    }

    private ArrayList<Path> paths = new ArrayList<Path>();
    private Path currPath = new Path();

    private void FindAllPaths(StateRecord CurrState) {
        currPath.push(CurrState);
        ArrayList<StateRecord> NextStates = new ArrayList<StateRecord>();
        NextStates = this.getNextStatesFromState(CurrState);

        for (StateRecord state : NextStates) {
            if ((getNextStatesFromState(state).isEmpty()) || (currPath.contains(state))) {
                currPath.push(state);
                Path path = new Path(currPath);
                paths.add(path);
            } else {
                FindAllPaths(state);
            }
            currPath.pop();
        }

    }

    public ArrayList<Path> getAllPaths(StateRecord CurrState) {
        // if not generate path before then generate else
        // if current state is not equal last one that use to generate paths then generate
        // if last current state is not equal last one that use to generate paths then generate
        if (paths.isEmpty() || CurrState != LastCurrentStateofGeneratepaths || LastCurrentStateofGeneratepaths != LastCurrentState) {
            LastCurrentStateofGeneratepaths = CurrState;
            LastCurrentState = CurrState;
            paths.clear();
            currPath.getStates().clear();
            FindAllPaths(CurrState);
        }
        return paths;
    }
    
    
    public ArrayList<Path> deleteRepeatedPaths (ArrayList<Path> paths){
        
        ArrayList<Path> Result = new ArrayList<Path>();
        ArrayList<Path> copypaths = new ArrayList<Path>();
        Path copyPath = new Path();
        if(paths != null){
            // copying
            for(Path p : paths){
                copyPath = new Path(p);
                copypaths.add(copyPath);
            }
            for(int i =0 ; i<paths.size() ; i++){
                for(int j = i+1 ; j<paths.size() ; j++ ){
                        if(paths.get(i).equals(paths.get(j)) && i!=j){
                            copypaths.get(j).getStates().removeAllElements();
                    }
                }
            }
            for(Path p : copypaths){
                if(!p.getStates().isEmpty()){
                    Result.add(p);
                }
            }
            return Result;
            // dumy code
        }
        return null;
    }

    public void showPaths(ArrayList<Path> paths) {
        if (paths != null) {
            if (!paths.isEmpty()) {
                for (Path p : paths) {
                    try {
                        this.showPath(p);
                    } catch (IOException ex) {
                        Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }
    }

    public void showPath(Path path) throws IOException {
        DefaultGraph graph = ConvertPathToDefaultGraph(path);
        GraphFrame frame = new GraphFrame();
        frame.constructGraphFrame(graph, "Path");
        frame.setVisible(true);
    }

    public DefaultGraph ConvertPathToDefaultGraph(Path path) throws IOException {

        DefaultGraph graph1 = ReachabilityGraphGenerator.createGraph(rgFile, dataLayer, true);
        DefaultGraph graph2 = ReachabilityGraphGenerator.createGraph(rgFile, dataLayer, true);

        List<net.sourceforge.jpowergraph.Node> nodesofPath = getNodesofPath(path, graph2);
        List<net.sourceforge.jpowergraph.Edge> edgesofPath = getEdgesofPath(path, graph2);

        graph1.getAllEdges().clear();
        graph1.getAllNodes().clear();

        graph1.getAllNodes().addAll(nodesofPath);
        graph1.getAllEdges().addAll(edgesofPath);

        return graph1;
    }

    private List<net.sourceforge.jpowergraph.Node> getNodesofPath(Path path, DefaultGraph Defgraph) throws IOException {

        List<net.sourceforge.jpowergraph.Node> nodes = Defgraph.getAllNodes();
        List<net.sourceforge.jpowergraph.Node> pathnodes = new ArrayList();

        boolean found = false;
        for (net.sourceforge.jpowergraph.Node node : nodes) {
            int StateID = Integer.valueOf(node.getLabel().substring(1));
            for (StateRecord state : path.getStates()) {
                if (state.getID() == StateID) {
                    found = true;
                    break;
                }
            }

            if (found) {
                pathnodes.add(node);
                found = false;
            }
        }

        return pathnodes;
    }

    private List<net.sourceforge.jpowergraph.Edge> getEdgesofPath(Path path, DefaultGraph Defgraph) throws IOException {

        List<net.sourceforge.jpowergraph.Edge> edges = Defgraph.getAllEdges();
        List<net.sourceforge.jpowergraph.Edge> edgesofPath = createAllEdgesofPath(path);
        List<net.sourceforge.jpowergraph.Edge> realedgesofpath = new ArrayList();

        for (net.sourceforge.jpowergraph.Edge edge : edges) {
            for (net.sourceforge.jpowergraph.Edge removededge : edgesofPath) {
                if (edge.getFrom().getLabel().equals(removededge.getFrom().getLabel())
                        && edge.getTo().getLabel().equals(removededge.getTo().getLabel()
                        )) {
                    realedgesofpath.add(edge);
                    break;
                }
            }
        }
        return realedgesofpath;

    }

    // بافتراض عدم وجود أكثر من انتقال من حالة إلى أخرى ضمن شجرة التغطية
    private List<net.sourceforge.jpowergraph.Edge> createAllEdgesofPath(Path path) {

        StateRecord state1;
        String label1;
        String MarkingString1;
        PIPEState pipeState1;

        StateRecord state2;
        String label2;
        String MarkingString2;
        PIPEState pipeState2;

        List<net.sourceforge.jpowergraph.Edge> edges = new ArrayList<net.sourceforge.jpowergraph.Edge>();
        net.sourceforge.jpowergraph.Edge tempedge;

        for (int i = 0; i < path.getStates().size() - 1; i++) {
            state1 = path.getStates().get(i);
            label1 = "S" + Integer.toString(state1.getID());
            MarkingString1 = getMarkingString(state1);
            pipeState1 = new PIPEState(label1, MarkingString1);

            state2 = path.getStates().get(i + 1);
            label2 = "S" + Integer.toString(state2.getID());
            MarkingString2 = getMarkingString(state2);
            pipeState2 = new PIPEState(label2, MarkingString2);

            edges.add(new TextEdge((DefaultNode) pipeState1, (DefaultNode) pipeState2, Integer.toString(1)));

        }
        return edges;
    }

    private String getMarkingString(StateRecord state) {
        String s = "{";

        for (int i = 0; i < state.getState().length - 1; i++) {
            if (state.getState()[i] == -1) {
                s += "\u03C9, "; //\u03C9: Unicode Character 'GREEK SMALL LETTER OMEGA'
            } else {
                s += Integer.toString(state.getState()[i]) + ", ";
            }
        }
        if (state.getState()[state.getState().length - 1] == -1) {
            s += "\u03C9";
        } else {
            s += Integer.toString(state.getState()[state.getState().length - 1]);
        }
        s += "}";

        return s;
    }

    private List<Edge> ArrayList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
