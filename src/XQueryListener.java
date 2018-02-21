// Generated from XQuery.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQueryParser}.
 */
public interface XQueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void enterAp(XQueryParser.ApContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void exitAp(XQueryParser.ApContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_attName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_attName(XQueryParser.Rp_attNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_attName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_attName(XQueryParser.Rp_attNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_tagNmae}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_tagNmae(XQueryParser.Rp_tagNmaeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_tagNmae}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_tagNmae(XQueryParser.Rp_tagNmaeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_parent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_parent(XQueryParser.Rp_parentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_parent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_parent(XQueryParser.Rp_parentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_rp}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_rp(XQueryParser.Rp_rpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_rp}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_rp(XQueryParser.Rp_rpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_nextLevel}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_nextLevel(XQueryParser.Rp_nextLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_nextLevel}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_nextLevel(XQueryParser.Rp_nextLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_nextLevelRecursive}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_nextLevelRecursive(XQueryParser.Rp_nextLevelRecursiveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_nextLevelRecursive}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_nextLevelRecursive(XQueryParser.Rp_nextLevelRecursiveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_children}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_children(XQueryParser.Rp_childrenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_children}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_children(XQueryParser.Rp_childrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_current}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_current(XQueryParser.Rp_currentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_current}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_current(XQueryParser.Rp_currentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_text}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_text(XQueryParser.Rp_textContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_text}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_text(XQueryParser.Rp_textContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_concat}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_concat(XQueryParser.Rp_concatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_concat}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_concat(XQueryParser.Rp_concatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rp_filter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp_filter(XQueryParser.Rp_filterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rp_filter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp_filter(XQueryParser.Rp_filterContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#f}.
	 * @param ctx the parse tree
	 */
	void enterF(XQueryParser.FContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#f}.
	 * @param ctx the parse tree
	 */
	void exitF(XQueryParser.FContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#fileName}.
	 * @param ctx the parse tree
	 */
	void enterFileName(XQueryParser.FileNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#fileName}.
	 * @param ctx the parse tree
	 */
	void exitFileName(XQueryParser.FileNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#tagName}.
	 * @param ctx the parse tree
	 */
	void enterTagName(XQueryParser.TagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#tagName}.
	 * @param ctx the parse tree
	 */
	void exitTagName(XQueryParser.TagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#attName}.
	 * @param ctx the parse tree
	 */
	void enterAttName(XQueryParser.AttNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#attName}.
	 * @param ctx the parse tree
	 */
	void exitAttName(XQueryParser.AttNameContext ctx);
}