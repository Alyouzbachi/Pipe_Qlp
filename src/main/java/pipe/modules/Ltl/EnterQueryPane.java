/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipe.modules.Ltl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeCellEditor;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.ButtonBar;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.views.PetriNetView;

/**
 *
 * @author Yazan
 */
public class EnterQueryPane extends JPanel {

    //Components in panel
    private final JTextField QueryInput;
    private final JTextField DefineS0;
    private final JCheckBox DrawPath;
    private static ResultsHTMLPane results;

    /**
     * Constructs the dialog box and displays it on screen
     *
     * @param defaultStatus
     */
    public EnterQueryPane(ButtonBar submit) {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel startStLbl = new JLabel("Input Query: ");
        c.gridx = 0;
        c.gridy = 0;
        this.add(startStLbl, c);
        QueryInput = new JTextField(20);
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridwidth = 5;
        this.add(QueryInput, c);

        DrawPath = new JCheckBox("Draw Path ");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        this.add(DrawPath, c);

        JLabel bufferLbl = new JLabel("Define S0: ");
        c.weightx = 0;
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 2;
        this.add(bufferLbl, c);

        DefineS0 = new JTextField();
        c.weightx = 0.5;
        c.gridx = 5;
        c.gridy = 3;
        c.gridwidth = 1;
        this.add(DefineS0, c);

        this.add(submit);

        this.setPreferredSize(new Dimension(400, 100));
        this.setBorder(new TitledBorder(new EtchedBorder(), "Input Fields"));
    }

    /**
     * Returns contents of StartStatea text field
     *
     * @return
     */
    public String getQueryInput() {
        return QueryInput.getText();
    }

    /**
     * Returns contents of TargetStates text fiedl
     *
     * @return
     */
    /**
     * Returns contents of StepSize text field as a double
     *
     * @return
     * @throws NumberFormatException
     */
    /**
     * Return contents of TStart text field as a double
     *
     * @return
     * @throws NumberFormatException
     */
    /**
     * Returns contents fo TStop text field as a double
     *
     * @return
     * @throws NumberFormatException
     */
    /**
     * Returns true if user has selected to analyse as a MapReduce job
     *
     * @return
     */
    public boolean isDrawPath() {
        return DrawPath.isSelected();
    }

    /**
     * Returns the number of maps entered by the user as an integer
     *
     * @return
     */
    public int getDefineS0() {
        return Integer.parseInt(DefineS0.getText());
    }

    public void setDrawPath(boolean exp) {
        DrawPath.setSelected(exp);
    }

    public void setStartStates(String exp) {
        QueryInput.setText(exp);
    }

    public void setBufferSize(String exp) {
        DefineS0.setText(exp);
    }

    /**
     * Sets the contents of the Error Message text area
     *
     * @param msg
     */
    /**
     * Associated panel that contains input options related to selecting whether
     * the analysis should be run locally or as a MapReduce job
     *
     * @author Oliver Haggarty - August 2007
     *
     */
}
