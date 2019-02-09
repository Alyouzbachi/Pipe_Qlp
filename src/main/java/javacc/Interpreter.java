/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacc;

/**
 *
 * @author abdua
 */
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pipe.modules.Ltl.LtlStructure;
import pipe.modules.Ltl.LtlQuery;

public class Interpreter implements ParserVisitor{
 
 private LinkedList stack=new LinkedList();
 private HashMap symbolTable=new HashMap();    
 public List<String> before = new ArrayList<String>() ;
 public List<String> after = new ArrayList<String>();
 public LtlStructure ltl =new LtlStructure();
 
 public boolean Start(SimpleNode node , LtlStructure l){
     Object ob = null;
     ltl = l;
     return (Boolean)visit(node,ob);
 }
 
 public Object visit(SimpleNode node, Object data){
     System.out.println("javacc.Interpreter.visit(simple)");
     return node.jjtGetChild(0).jjtAccept(this, data);
 }
 
 public Object visit(ASTStart node, Object data){ 
    node.jjtGetChild(0).jjtAccept(this, data);
     return symbolTable;
 }
 
 public Object visit(ASTStatement node, Object data){ 
    
      boolean result = (Boolean) node.jjtGetChild(0).jjtAccept(this, data);
      return result;
     
 }
 
 public Object visit(ASTMultiStatement node, Object data){
     if (node.jjtGetNumChildren() >1){
         for (int j =1; j<node.jjtGetNumChildren(); j=j+2){
             if ((Boolean)node.jjtGetChild(j).jjtAccept(this, data)==false)
                 return false;
         }
         System.out.println("test.Interpreter.visit()trueee");
         return true;
     }
     else
        return node.jjtGetChild(0).jjtAccept(this, data);
 }

 
 public Object visit(ASTLstatement node, Object data){ 
     //check if the petri net is alive.
     return null;
 }
 
 public Object visit(ASTDstatement node, Object data){ 
     //check if there is a deadlock in the net.
     
     return null;
 }
 
 public Object visit(ASTAllStatement node, Object data){
     return node.jjtGetChild(0).jjtAccept(this, data);
 }
 
 public Object visit(ASTEstatement node, Object data){ 
     if (node.jjtGetChild(0).getId()>13){
         node.childrenAccept(this, data);
         before.clear();
         before = getVars(node,0);
         boolean result = false;
         try {
             result = ltl.Exist(before);
         } catch (IOException ex) {
             Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
         }
         return result;
     }
     return node.jjtGetChild(0).jjtAccept(this, data);
 }
 
 public Object visit(ASTAnd node, Object data){
     int id = getID(node);
     if ((Boolean)node.parent.jjtGetChild(id-1).jjtAccept(this, data)==true)
         if ((Boolean)node.parent.jjtGetChild(id+1).jjtAccept(this, data)==true)
            return true;
     
     return false;
 }
 
 public Object visit(ASTOr node, Object data){ 
     int id = getID(node);
     if ((Boolean)node.parent.jjtGetChild(id-1).jjtAccept(this, data)==true)
         return true;
     else if ((Boolean)node.parent.jjtGetChild(id+1).jjtAccept(this, data)==true)
        return true;
     else
        return false;
 }
 
 public Object visit(ASTNot node, Object data){
     int id = getID(node);
     if ((Boolean)node.parent.jjtGetChild(id+1).jjtAccept(this, data)==true)
        return false;
     else
        return true;     
 }
 
 public Object visit(ASTNext node, Object data){
     node.childrenAccept(this, data);
     before.clear();
     after.clear();
     if (node.jjtGetNumChildren()==1){
         after=getVars(node,0);
     }
     else{
         before=getVars(node,0);
         after=getVars(node,1);
     }
     boolean result = false;
     if (node.parent.getId()==5)
         try {
             result = ltl.AllNext(before, after);
     } catch (IOException ex) {
         Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
     }
     else{
         try{
        result = ltl.NextState(before, after);
         }catch(Exception e){
            System.out.println("Error");
                    }
         }
     if (result)
         System.out.println("true");
     else
         System.out.println("false");
     return result;
 }
 
 public Object visit(ASTGlobally node, Object data){
      node.childrenAccept(this, data);
     before.clear();
     after.clear();
     if (node.jjtGetNumChildren()==1){
         after=getVars(node,0);
     }
     else{
         before=getVars(node,0);
         after=getVars(node,1);
     }
     boolean result = false;
     try {
         result = ltl.GlobalState(before, after);
     } catch (IOException ex) {
         Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
     }
     if (result)
         System.out.println("true");
     else
         System.out.println("false");
     return result;
 }
 
 public Object visit(ASTFinaly node, Object data){
      node.childrenAccept(this, data);
     before.clear();
     after.clear();
     if (node.jjtGetNumChildren()==1){
         after=getVars(node,0);
     }
     else{
         before=getVars(node,0);
         after=getVars(node,1);
     }
     boolean result = false;
     if (node.parent.getId()==5)
         try {
             result= ltl.AllFuture(before, after);
      } catch (IOException ex) {
          Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
      }
     else
         try {
             result = ltl.FutureState(before, after);
      } catch (IOException ex) {
          Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
      }
     if (result)
         System.out.println("true");
     else
         System.out.println("false");
     return result;
 }
 
 public Object visit(ASTUntil node, Object data){
     node.childrenAccept(this, data);
     before=getVars(node,0);
     after=getVars(node,1);
     boolean result = false;
     if (node.parent.getId()==5)
         try {
             result = ltl.AllUntil(before, after);
     } catch (IOException ex) {
         Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
     }
     else
         try {
             result = ltl.UntilState(before, after);
     } catch (IOException ex) {
         Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
     }
     if (result)
         System.out.println("true");
     else
         System.out.println("false");
     return result;
 }
 
 public Object visit(ASTplacename node, Object data){
     node.childrenAccept(this, data);
     String var=(String)node.data.get("place");
     stack.addFirst(var);     
     return null;
 }
 
 public Object visit(ASTPlaces node, Object data){
     node.childrenAccept(this, data);     
     return null;
 }
 
 public List<String> getVars(Node n,int i){
     List<String> vars = new ArrayList<String>();
     Node child = n.jjtGetChild(i);
     int count = (int)child.jjtGetNumChildren();
        while (count>0){
        String var = (String)stack.removeLast();
        vars.add(var);
        count--;
        }
        return vars;
 }
 
 public int getID (SimpleNode n){
     Node parent = n.jjtGetParent();
     int i=0;
     for (i=0;i<= parent.jjtGetNumChildren();i++){
         if (parent.jjtGetChild(i).equals(n)){
             return i;
         }
     }
     return -1;
 }
}
