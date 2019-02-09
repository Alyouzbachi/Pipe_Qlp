/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LTLqueries;

import java.awt.Checkbox;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sourceforge.jpowergraph.defaults.DefaultGraph;
import net.sourceforge.jpowergraph.defaults.DefaultNode;
import net.sourceforge.jpowergraph.defaults.TextEdge;
import pipe.calculations.StateSpaceGenerator;
import pipe.calculations.myTree;
import pipe.controllers.PetriNetController;
import pipe.exceptions.MarkingNotIntegerException;
import pipe.exceptions.TimelessTrapException;
import pipe.exceptions.TreeTooBigException;
import pipe.extensions.jpowergraph.PIPEInitialState;
import pipe.extensions.jpowergraph.PIPEInitialTangibleState;
import pipe.extensions.jpowergraph.PIPEInitialVanishingState;
import pipe.extensions.jpowergraph.PIPELoopWithTextEdge;
import pipe.extensions.jpowergraph.PIPEState;
import pipe.extensions.jpowergraph.PIPETangibleState;
import pipe.extensions.jpowergraph.PIPEVanishingState;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.EscapableDialog;
import pipe.gui.widgets.GraphFrame;
import pipe.gui.widgets.PetriNetChooserPanel;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.io.ImmediateAbortException;
import pipe.io.IncorrectFileFormatException;
import pipe.io.NewReachabilityGraphFileHeader;
import pipe.io.ReachabilityGraphFileHeader;
import pipe.io.StateRecord;
import pipe.io.TransitionRecord;
import pipe.models.PetriNet;
import pipe.modules.interfaces.IModule;
import pipe.modules.reachability.ReachabilityGraphGenerator;
import pipe.utilities.Expander;
import pipe.utilities.writers.PNMLWriter;
import pipe.views.ArcView;
import pipe.views.InhibitorArcView;
import pipe.views.MarkingView;
import pipe.views.PetriNetView;
import pipe.views.PlaceView;
import pipe.views.TokenSetController;
import pipe.views.TransitionView;
import pipe.views.viewComponents.AnnotationNote;
import pipe.views.viewComponents.RateParameter;

/**
 *
 * @author TOSHIBA
 */
public class ReadRG {

    // to store all Reachability graph
    public static List<MyGraph> allGraphs = new ArrayList<MyGraph>();
    
    
    public static List<StateRecord> currStates = new ArrayList<StateRecord>();
    public static List<TransitionRecord> currTransitions = new ArrayList<TransitionRecord>();
    public static List<String> currNamesofPlaces = new ArrayList<String>();
    public static  File currRGFile ;
    public static PetriNetView currDataLayer ;
    
    public static List<StateRecord> ReadStates() {

        File rgFile;
        rgFile = ReachabilityGraphGenerator.reachabilityGraphFile;
        RandomAccessFile reachabilityFile2;
        List<StateRecord> States = new ArrayList<StateRecord>();
        try {
            reachabilityFile2 = new RandomAccessFile(rgFile, "r");

            ReachabilityGraphFileHeader header2 = new ReachabilityGraphFileHeader();

            header2.read(reachabilityFile2);

            int stateSize = header2.getStateArraySize();
            int NumStates = header2.getNumStates();

            StateRecord staterecord = new StateRecord();
            int[] currentState = new int[stateSize];
            int stateid;

            for (int i = 0; i < NumStates; i++) {
                stateid = staterecord.ReadandGetID(stateSize, reachabilityFile2);
                currentState = staterecord.ReadandGetVector(stateSize, reachabilityFile2);
                System.out.println("state[" + stateid + "]= " + staterecord.getMarkingString());
                States.add(new StateRecord(staterecord));
            }
            currStates = States;
            return States;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadRG.class.getName()).log(Level.SEVERE, null, ex);
            return States;

        } catch (IOException ex) {
            Logger.getLogger(ReadRG.class.getName()).log(Level.SEVERE, null, ex);
            return States;
        }
    }

    public static List<TransitionRecord> ReadTransitions() {

        List<TransitionRecord> transitions = new ArrayList<TransitionRecord>();
        File transource = ReachabilityGraphGenerator.reachabilityGraphFile;
        ReachabilityGraphFileHeader header = new ReachabilityGraphFileHeader();

        try {

            RandomAccessFile reachabilityFile = new RandomAccessFile(transource, "r");
            header.read(reachabilityFile);
            reachabilityFile.seek(header.getOffsetToTransitions());

            int num_transitions = header.getNumTransitions();
            TransitionRecord currenttranRecord = new TransitionRecord();
            int tranID = 0;
            int from = 0;
            int to = 0;
            double rate = 0.0;

            for (int count = 0; count < num_transitions; count++) {
                currenttranRecord.ReadandGetTransistion(reachabilityFile);
                tranID = currenttranRecord.getTransitionNo() + 1;
                rate = currenttranRecord.getRate();
                to = currenttranRecord.getToState();
                from = currenttranRecord.getFromState();

                System.out.println("transition[" + tranID + "]:"
                        + " rate= " + rate
                        + " from state= " + from
                        + " to state= " + to);
                transitions.add(new TransitionRecord(from, to, rate, tranID));
            }
            currTransitions = transitions;
            return transitions;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadRG.class.getName()).log(Level.SEVERE, null, ex);
            return transitions;
        } catch (IOException ex) {
            Logger.getLogger(ReadRG.class.getName()).log(Level.SEVERE, null, ex);
            return transitions;
        }

    }

    public static void ReadNamesofPlaces() {
        
        PlaceView[] placeView = currDataLayer.places();
        List<String> names = new ArrayList<String>();
        
        //String legend = "";
        if (placeView.length > 0) {
            names.add(placeView[0].getName());
        }
        for (int i = 1; i < placeView.length; i++) {
            names.add(placeView[i].getName());
        }
        MyGraph graph = new MyGraph(currStates, currTransitions);
        graph.setDataLayer(currDataLayer);
        graph.setRgFile(currRGFile);
        graph.setNamesofPlaces(names);
        allGraphs.add(graph);
          
    }
    
        public static void ReadRGFileDataLayer( File rgFile, PetriNetView dataLayer) {
        
            currRGFile = rgFile;
            currDataLayer = new PetriNetView(dataLayer.getPlacesArrayList(),dataLayer.getTransitionsArrayList(),
            dataLayer.getArcsArrayList(),dataLayer.getInhibitorsArrayList(),
            dataLayer.getLabels(),dataLayer.getRateParameters(),
            dataLayer.getFunctionRelatedPlaces(),dataLayer.getSelectedTokenTypeNumber(),
            dataLayer.getInitialMarkingVector(),dataLayer.getCurrentMarkingVector(),
            dataLayer.getCapacityMatrix(),dataLayer.getPriorityMatrix(),dataLayer.getTimedMatrix(),
            dataLayer.getMarkingVectorAnimationStorage(),dataLayer.getArcsMap(),
            dataLayer.getInhibitorsMap(),dataLayer.getGroups(),
            dataLayer.getPetriNetController(),dataLayer.getModel(),
            dataLayer.getTokenSetController());
          
    }

}
