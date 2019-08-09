// Generated from Statements.g4 by ANTLR 4.7.2

package com.blaster.data.managers.parsing;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StatementsParser}.
 */
public interface StatementsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(StatementsParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#singleLineComment}.
	 * @param ctx the parse tree
	 */
	void enterSingleLineComment(StatementsParser.SingleLineCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#singleLineComment}.
	 * @param ctx the parse tree
	 */
	void exitSingleLineComment(StatementsParser.SingleLineCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#multiLineComment}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineComment(StatementsParser.MultiLineCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#multiLineComment}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineComment(StatementsParser.MultiLineCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(StatementsParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(StatementsParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#meat}.
	 * @param ctx the parse tree
	 */
	void enterMeat(StatementsParser.MeatContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#meat}.
	 * @param ctx the parse tree
	 */
	void exitMeat(StatementsParser.MeatContext ctx);
}