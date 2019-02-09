/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipe.extensions.jpowergraph;
import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import static pipe.extensions.jpowergraph.PIPETangibleState.bgColor;

/**
 *
 * @author Yazan
 */
public class PIPEInitialYellowState extends PIPEYellowState{
  private static final ShapeNodePainter shapeNodePainter = new ShapeNodePainter(
           ShapeNodePainter.RECTANGLE, bgColor, bgColor, bgColor);   
   
   
   public PIPEInitialYellowState(String label, String marking){
      super(label, marking);
   }

   
   public static ShapeNodePainter getShapeNodePainter(){
      return shapeNodePainter;
   }
   
   
   public String getNodeType(){
      return "Yellow State (Initial State)";
   }   

}
