/* Generated By:JJTree: Do not edit this line. ASTPlaces.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=BaseNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class ASTPlaces extends SimpleNode {
  public ASTPlaces(int id) {
    super(id);
  }

  public ASTPlaces(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=4314ed2af95b4a9dc304994f8cfc1da0 (do not edit this line) */
