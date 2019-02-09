/* Parser.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. Parser.java */
package javacc;
public class Parser/*@bgen(jjtree)*/implements ParserTreeConstants, ParserConstants {/*@bgen(jjtree)*/
  protected JJTParserState jjtree = new JJTParserState();public static void main(String args[]) throws Exception {

  }

  final public ASTStart Start() throws ParseException {/*@bgen(jjtree) Start */
  ASTStart jjtn000 = new ASTStart(JJTSTART);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_1(17)) {
        MultiStatement();
      } else if (jj_2_2(17)) {
        Statement();
      } else if (jj_2_3(17)) {
ASTLstatement jjtn001 = new ASTLstatement(JJTLSTATEMENT);
                                  boolean jjtc001 = true;
                                  jjtree.openNodeScope(jjtn001);
        try {
          jj_consume_token(LIVE);
        } finally {
if (jjtc001) {
                                    jjtree.closeNodeScope(jjtn001, true);
                                  }
        }
      } else if (jj_2_4(17)) {
ASTDstatement jjtn002 = new ASTDstatement(JJTDSTATEMENT);
                                  boolean jjtc002 = true;
                                  jjtree.openNodeScope(jjtn002);
        try {
          jj_consume_token(DEADLOCK);
        } finally {
if (jjtc002) {
                                    jjtree.closeNodeScope(jjtn002, true);
                                  }
        }
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
jjtree.closeNodeScope(jjtn000, true);
  jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
                                jjtree.clearNodeScope(jjtn000);
                                jjtc000 = false;
                              } else {
                                jjtree.popNode();
                              }
                              if (jjte000 instanceof RuntimeException) {
                                {if (true) throw (RuntimeException)jjte000;}
                              }
                              if (jjte000 instanceof ParseException) {
                                {if (true) throw (ParseException)jjte000;}
                              }
                              {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                jjtree.closeNodeScope(jjtn000, true);
                              }
    }
    throw new Error("Missing return statement in function");
  }

  final public void Statement() throws ParseException {/*@bgen(jjtree) Statement */
  ASTStatement jjtn000 = new ASTStatement(JJTSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_7(17)) {
        if (jj_2_5(17)) {
          Not();
        } else {
          ;
        }
        All();
      } else if (jj_2_8(17)) {
        if (jj_2_6(17)) {
          Not();
        } else {
          ;
        }
        Exist();
      } else if (jj_2_9(17)) {
        State();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                    jjtree.clearNodeScope(jjtn000);
                                    jjtc000 = false;
                                  } else {
                                    jjtree.popNode();
                                  }
                                  if (jjte000 instanceof RuntimeException) {
                                    {if (true) throw (RuntimeException)jjte000;}
                                  }
                                  if (jjte000 instanceof ParseException) {
                                    {if (true) throw (ParseException)jjte000;}
                                  }
                                  {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                    jjtree.closeNodeScope(jjtn000, true);
                                  }
    }
  }

  final public void MultiStatement() throws ParseException {/*@bgen(jjtree) MultiStatement */
  ASTMultiStatement jjtn000 = new ASTMultiStatement(JJTMULTISTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_12(17)) {
        Statement();
        label_1:
        while (true) {
          And();
          Statement();
          if (jj_2_10(17)) {
            ;
          } else {
            break label_1;
          }
        }
      } else if (jj_2_13(17)) {
        Statement();
        label_2:
        while (true) {
          Or();
          Statement();
          if (jj_2_11(17)) {
            ;
          } else {
            break label_2;
          }
        }
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                                 jjtree.clearNodeScope(jjtn000);
                                                 jjtc000 = false;
                                               } else {
                                                 jjtree.popNode();
                                               }
                                               if (jjte000 instanceof RuntimeException) {
                                                 {if (true) throw (RuntimeException)jjte000;}
                                               }
                                               if (jjte000 instanceof ParseException) {
                                                 {if (true) throw (ParseException)jjte000;}
                                               }
                                               {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                                 jjtree.closeNodeScope(jjtn000, true);
                                               }
    }
  }

  final public void All() throws ParseException {/*@bgen(jjtree) AllStatement */
  ASTAllStatement jjtn000 = new ASTAllStatement(JJTALLSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(ALL);
      jj_consume_token(20);
      State();
      jj_consume_token(21);
    } catch (Throwable jjte000) {
if (jjtc000) {
                                  jjtree.clearNodeScope(jjtn000);
                                  jjtc000 = false;
                                } else {
                                  jjtree.popNode();
                                }
                                if (jjte000 instanceof RuntimeException) {
                                  {if (true) throw (RuntimeException)jjte000;}
                                }
                                if (jjte000 instanceof ParseException) {
                                  {if (true) throw (ParseException)jjte000;}
                                }
                                {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                  jjtree.closeNodeScope(jjtn000, true);
                                }
    }
  }

  final public void Exist() throws ParseException {/*@bgen(jjtree) Estatement */
  ASTEstatement jjtn000 = new ASTEstatement(JJTESTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_14(17)) {
        jj_consume_token(EXIST);
        jj_consume_token(20);
        State();
        jj_consume_token(21);
      } else if (jj_2_15(17)) {
        jj_consume_token(EXIST);
        Places();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                     jjtree.clearNodeScope(jjtn000);
                                     jjtc000 = false;
                                   } else {
                                     jjtree.popNode();
                                   }
                                   if (jjte000 instanceof RuntimeException) {
                                     {if (true) throw (RuntimeException)jjte000;}
                                   }
                                   if (jjte000 instanceof ParseException) {
                                     {if (true) throw (ParseException)jjte000;}
                                   }
                                   {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                     jjtree.closeNodeScope(jjtn000, true);
                                   }
    }
  }

  final public void And() throws ParseException {/*@bgen(jjtree) And */
  ASTAnd jjtn000 = new ASTAnd(JJTAND);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(AND);
    } finally {
if (jjtc000) {
                         jjtree.closeNodeScope(jjtn000, true);
                       }
    }
  }

  final public void Or() throws ParseException {/*@bgen(jjtree) Or */
  ASTOr jjtn000 = new ASTOr(JJTOR);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(OR);
    } finally {
if (jjtc000) {
                       jjtree.closeNodeScope(jjtn000, true);
                     }
    }
  }

  final public void Not() throws ParseException {/*@bgen(jjtree) Not */
  ASTNot jjtn000 = new ASTNot(JJTNOT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(NOT);
    } finally {
if (jjtc000) {
                         jjtree.closeNodeScope(jjtn000, true);
                       }
    }
  }

  final public void State() throws ParseException {
    if (jj_2_16(17)) {
      Next();
    } else if (jj_2_17(17)) {
      Globally();
    } else if (jj_2_18(17)) {
      Finaly();
    } else if (jj_2_19(17)) {
      Until();
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Next() throws ParseException {/*@bgen(jjtree) Next */
  ASTNext jjtn000 = new ASTNext(JJTNEXT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_20(17)) {
        Places();
        jj_consume_token(NEXT);
        Places();
      } else if (jj_2_21(17)) {
        jj_consume_token(NEXT);
        Places();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                             jjtree.clearNodeScope(jjtn000);
                             jjtc000 = false;
                           } else {
                             jjtree.popNode();
                           }
                           if (jjte000 instanceof RuntimeException) {
                             {if (true) throw (RuntimeException)jjte000;}
                           }
                           if (jjte000 instanceof ParseException) {
                             {if (true) throw (ParseException)jjte000;}
                           }
                           {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                             jjtree.closeNodeScope(jjtn000, true);
                           }
    }
  }

  final public void Globally() throws ParseException {/*@bgen(jjtree) Globally */
  ASTGlobally jjtn000 = new ASTGlobally(JJTGLOBALLY);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_22(17)) {
        Places();
        jj_consume_token(GLOBAL);
        Places();
      } else if (jj_2_23(17)) {
        jj_consume_token(GLOBAL);
        Places();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                     jjtree.clearNodeScope(jjtn000);
                                     jjtc000 = false;
                                   } else {
                                     jjtree.popNode();
                                   }
                                   if (jjte000 instanceof RuntimeException) {
                                     {if (true) throw (RuntimeException)jjte000;}
                                   }
                                   if (jjte000 instanceof ParseException) {
                                     {if (true) throw (ParseException)jjte000;}
                                   }
                                   {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                     jjtree.closeNodeScope(jjtn000, true);
                                   }
    }
  }

  final public void Finaly() throws ParseException {/*@bgen(jjtree) Finaly */
  ASTFinaly jjtn000 = new ASTFinaly(JJTFINALY);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_24(17)) {
        Places();
        jj_consume_token(FUTURE);
        Places();
      } else if (jj_2_25(17)) {
        jj_consume_token(FUTURE);
        Places();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                 jjtree.clearNodeScope(jjtn000);
                                 jjtc000 = false;
                               } else {
                                 jjtree.popNode();
                               }
                               if (jjte000 instanceof RuntimeException) {
                                 {if (true) throw (RuntimeException)jjte000;}
                               }
                               if (jjte000 instanceof ParseException) {
                                 {if (true) throw (ParseException)jjte000;}
                               }
                               {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                 jjtree.closeNodeScope(jjtn000, true);
                               }
    }
  }

  final public void Until() throws ParseException {/*@bgen(jjtree) Until */
  ASTUntil jjtn000 = new ASTUntil(JJTUNTIL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      Places();
      jj_consume_token(UNTIL);
      Places();
    } catch (Throwable jjte000) {
if (jjtc000) {
                               jjtree.clearNodeScope(jjtn000);
                               jjtc000 = false;
                             } else {
                               jjtree.popNode();
                             }
                             if (jjte000 instanceof RuntimeException) {
                               {if (true) throw (RuntimeException)jjte000;}
                             }
                             if (jjte000 instanceof ParseException) {
                               {if (true) throw (ParseException)jjte000;}
                             }
                             {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                               jjtree.closeNodeScope(jjtn000, true);
                             }
    }
  }

  final public void Places() throws ParseException {/*@bgen(jjtree) Places */
  ASTPlaces jjtn000 = new ASTPlaces(JJTPLACES);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_27(17)) {
        jj_consume_token(22);
        Place();
        label_3:
        while (true) {
          if (jj_2_26(17)) {
            ;
          } else {
            break label_3;
          }
          jj_consume_token(AND);
          Place();
        }
        jj_consume_token(23);
      } else if (jj_2_28(17)) {
        Place();
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
                                 jjtree.clearNodeScope(jjtn000);
                                 jjtc000 = false;
                               } else {
                                 jjtree.popNode();
                               }
                               if (jjte000 instanceof RuntimeException) {
                                 {if (true) throw (RuntimeException)jjte000;}
                               }
                               if (jjte000 instanceof ParseException) {
                                 {if (true) throw (ParseException)jjte000;}
                               }
                               {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
                                 jjtree.closeNodeScope(jjtn000, true);
                               }
    }
  }

  final public void Place() throws ParseException {
    jj_consume_token(PLACE);
ASTplacename jjtn001 = new ASTplacename(JJTPLACENAME);
                                boolean jjtc001 = true;
                                jjtree.openNodeScope(jjtn001);
    try {
jjtree.closeNodeScope(jjtn001, true);
                                jjtc001 = false;
jjtn001.data.put("place",token.image);
    } finally {
if (jjtc001) {
                                  jjtree.closeNodeScope(jjtn001, true);
                                }
    }
  }

  private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_2_10(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  private boolean jj_2_11(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  private boolean jj_2_12(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(11, xla); }
  }

  private boolean jj_2_13(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(12, xla); }
  }

  private boolean jj_2_14(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(13, xla); }
  }

  private boolean jj_2_15(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(14, xla); }
  }

  private boolean jj_2_16(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(15, xla); }
  }

  private boolean jj_2_17(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(16, xla); }
  }

  private boolean jj_2_18(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(17, xla); }
  }

  private boolean jj_2_19(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(18, xla); }
  }

  private boolean jj_2_20(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(19, xla); }
  }

  private boolean jj_2_21(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(20, xla); }
  }

  private boolean jj_2_22(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(21, xla); }
  }

  private boolean jj_2_23(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(22, xla); }
  }

  private boolean jj_2_24(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(23, xla); }
  }

  private boolean jj_2_25(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(24, xla); }
  }

  private boolean jj_2_26(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(25, xla); }
  }

  private boolean jj_2_27(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(26, xla); }
  }

  private boolean jj_2_28(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(27, xla); }
  }

  private boolean jj_3_21()
 {
    if (jj_scan_token(NEXT)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_19()
 {
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3_26()
 {
    if (jj_scan_token(AND)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_11()
 {
    if (jj_3R_11()) return true;
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3_10()
 {
    if (jj_3R_10()) return true;
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3_18()
 {
    if (jj_3R_15()) return true;
    return false;
  }

  private boolean jj_3R_12()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_27()) {
    jj_scanpos = xsp;
    if (jj_3_28()) return true;
    }
    return false;
  }

  private boolean jj_3_27()
 {
    if (jj_scan_token(22)) return true;
    if (jj_3R_17()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_26()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(23)) return true;
    return false;
  }

  private boolean jj_3R_14()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3_23()) return true;
    }
    return false;
  }

  private boolean jj_3_22()
 {
    if (jj_3R_12()) return true;
    if (jj_scan_token(GLOBAL)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_16()
 {
    if (jj_3R_12()) return true;
    if (jj_scan_token(UNTIL)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_15()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_24()) {
    jj_scanpos = xsp;
    if (jj_3_25()) return true;
    }
    return false;
  }

  private boolean jj_3_24()
 {
    if (jj_3R_12()) return true;
    if (jj_scan_token(FUTURE)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_17()
 {
    if (jj_scan_token(PLACE)) return true;
    return false;
  }

  private boolean jj_3_13()
 {
    if (jj_3R_5()) return true;
    Token xsp;
    if (jj_3_11()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_11()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_4()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_12()) {
    jj_scanpos = xsp;
    if (jj_3_13()) return true;
    }
    return false;
  }

  private boolean jj_3_12()
 {
    if (jj_3R_5()) return true;
    Token xsp;
    if (jj_3_10()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_10()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_17()
 {
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_13()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_20()) {
    jj_scanpos = xsp;
    if (jj_3_21()) return true;
    }
    return false;
  }

  private boolean jj_3_20()
 {
    if (jj_3R_12()) return true;
    if (jj_scan_token(NEXT)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_8()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_14()) {
    jj_scanpos = xsp;
    if (jj_3_15()) return true;
    }
    return false;
  }

  private boolean jj_3_15()
 {
    if (jj_scan_token(EXIST)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_14()
 {
    if (jj_scan_token(EXIST)) return true;
    if (jj_scan_token(20)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(21)) return true;
    return false;
  }

  private boolean jj_3R_6()
 {
    if (jj_scan_token(NOT)) return true;
    return false;
  }

  private boolean jj_3_16()
 {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_7()
 {
    if (jj_scan_token(ALL)) return true;
    if (jj_scan_token(20)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(21)) return true;
    return false;
  }

  private boolean jj_3R_9()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_16()) {
    jj_scanpos = xsp;
    if (jj_3_17()) {
    jj_scanpos = xsp;
    if (jj_3_18()) {
    jj_scanpos = xsp;
    if (jj_3_19()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_11()
 {
    if (jj_scan_token(OR)) return true;
    return false;
  }

  private boolean jj_3R_10()
 {
    if (jj_scan_token(AND)) return true;
    return false;
  }

  private boolean jj_3_5()
 {
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3R_5()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_7()) {
    jj_scanpos = xsp;
    if (jj_3_8()) {
    jj_scanpos = xsp;
    if (jj_3_9()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_7()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_5()) jj_scanpos = xsp;
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3_4()
 {
    if (jj_scan_token(DEADLOCK)) return true;
    return false;
  }

  private boolean jj_3_3()
 {
    if (jj_scan_token(LIVE)) return true;
    return false;
  }

  private boolean jj_3_9()
 {
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3_1()
 {
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3_2()
 {
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3_6()
 {
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3_8()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) jj_scanpos = xsp;
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3_28()
 {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_23()
 {
    if (jj_scan_token(GLOBAL)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_25()
 {
    if (jj_scan_token(FUTURE)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[0];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[28];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[24];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 0; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 24; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 28; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
            case 11: jj_3_12(); break;
            case 12: jj_3_13(); break;
            case 13: jj_3_14(); break;
            case 14: jj_3_15(); break;
            case 15: jj_3_16(); break;
            case 16: jj_3_17(); break;
            case 17: jj_3_18(); break;
            case 18: jj_3_19(); break;
            case 19: jj_3_20(); break;
            case 20: jj_3_21(); break;
            case 21: jj_3_22(); break;
            case 22: jj_3_23(); break;
            case 23: jj_3_24(); break;
            case 24: jj_3_25(); break;
            case 25: jj_3_26(); break;
            case 26: jj_3_27(); break;
            case 27: jj_3_28(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}