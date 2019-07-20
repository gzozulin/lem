// Generated from SillyParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SillyParser}.
 */
public interface SillyParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SillyParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(SillyParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(SillyParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#packages}.
	 * @param ctx the parse tree
	 */
	void enterPackages(SillyParser.PackagesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#packages}.
	 * @param ctx the parse tree
	 */
	void exitPackages(SillyParser.PackagesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#imports}.
	 * @param ctx the parse tree
	 */
	void enterImports(SillyParser.ImportsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#imports}.
	 * @param ctx the parse tree
	 */
	void exitImports(SillyParser.ImportsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(SillyParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(SillyParser.DeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(SillyParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(SillyParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(SillyParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(SillyParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(SillyParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(SillyParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyParser#clazz}.
	 * @param ctx the parse tree
	 */
	void enterClazz(SillyParser.ClazzContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyParser#clazz}.
	 * @param ctx the parse tree
	 */
	void exitClazz(SillyParser.ClazzContext ctx);
}