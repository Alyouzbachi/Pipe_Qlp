/* Generated By:JJTree: Do not edit this line. ASTLstatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=BaseNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class ASTLstatement extends SimpleNode {
  public ASTLstatement(int id) {
    super(id);
  }

  public ASTLstatement(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c83ca3f2bc8e7123ea8bc8e75ef6b7bf (do not edit this line) */