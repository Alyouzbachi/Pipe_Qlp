/* Generated By:JJTree: Do not edit this line. ASTEstatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=BaseNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class ASTEstatement extends SimpleNode {
  public ASTEstatement(int id) {
    super(id);
  }

  public ASTEstatement(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=1dda1382f073c2cb94ac6626f5d0b9d0 (do not edit this line) */