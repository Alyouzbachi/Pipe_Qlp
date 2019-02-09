/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipe.extensions.jpowergraph;
import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;

/**
 *
 * @author Yazan
 */
public class PIPEGreenState extends PIPENode{
    
      static final JPowerGraphColor bgColor = new JPowerGraphColor(128, 0, 128);
   
   private static final ShapeNodePainter shapeNodePainter = new ShapeNodePainter(
           ShapeNodePainter.ELLIPSE, bgColor, JPowerGraphColor.LIGHT_GRAY,
           fgColor);
   
     public PIPEGreenState(String label, String marking){
      super(label, marking);
   }
     
     public static ShapeNodePainter getShapeNodePainter(){
      return shapeNodePainter;
   }
   
   
   public String getNodeType(){
      return "Rejected State";
   }
   
    
}
