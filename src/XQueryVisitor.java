// Generated from XQuery.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XQueryVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAp(XQueryParser.ApContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_attName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_attName(XQueryParser.Rp_attNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_tagNmae}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_tagNmae(XQueryParser.Rp_tagNmaeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_parent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_parent(XQueryParser.Rp_parentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_rp}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_rp(XQueryParser.Rp_rpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_nextLevel}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_nextLevel(XQueryParser.Rp_nextLevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_nextLevelRecursive}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_nextLevelRecursive(XQueryParser.Rp_nextLevelRecursiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_children}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_children(XQueryParser.Rp_childrenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_current}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_current(XQueryParser.Rp_currentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_text}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_text(XQueryParser.Rp_textContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_concat}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_concat(XQueryParser.Rp_concatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rp_filter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRp_filter(XQueryParser.Rp_filterContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#f}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitF(XQueryParser.FContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#fileName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileName(XQueryParser.FileNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#tagName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagName(XQueryParser.TagNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#attName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttName(XQueryParser.AttNameContext ctx);
}