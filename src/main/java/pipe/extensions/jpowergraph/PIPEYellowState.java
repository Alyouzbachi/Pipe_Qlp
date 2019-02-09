/*
 * MyNode.java
 *
 * Created on September 28, 2007, 10:37 AM
 */
package pipe.extensions.jpowergraph;

import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;

/**
 * This class defines a node used in the reachability graph to represent a
 * Tangible state
 *
 * @author Pere Bonet
 */
public class PIPEYellowState
        extends PIPENode {

    static final JPowerGraphColor bgColor = new JPowerGraphColor(255, 255, 0);

    private static final ShapeNodePainter shapeNodePainter = new ShapeNodePainter(
            ShapeNodePainter.ELLIPSE, bgColor, JPowerGraphColor.YELLOW,
            bgColor);

    /**
     * Creates a new instance of PIPETangibleState
     *
     * @param _label the node id.
     * @param _marking the marking
     * @param label
     * @param marking
     */
    public PIPEYellowState(String label, String marking) {
        super(label, marking);
    }

    public static ShapeNodePainter getShapeNodePainter() {
        return shapeNodePainter;
    }

    public String getNodeType() {
        return "Accepted State";
    }

}
