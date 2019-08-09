// Generated from Statements.g4 by ANTLR 4.7.2

package com.blaster.data.managers.parsing;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StatementsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StatementsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#singleLineComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleLineComment(StatementsParser.SingleLineCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#multiLineComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiLineComment(StatementsParser.MultiLineCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(StatementsParser.CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#meat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeat(StatementsParser.MeatContext ctx);
}