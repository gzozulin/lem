// Generated from SillyKotlin.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SillyKotlinParser}.
 */
public interface SillyKotlinListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SillyKotlinParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(SillyKotlinParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyKotlinParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(SillyKotlinParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyKotlinParser#packages}.
	 * @param ctx the parse tree
	 */
	void enterPackages(SillyKotlinParser.PackagesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyKotlinParser#packages}.
	 * @param ctx the parse tree
	 */
	void exitPackages(SillyKotlinParser.PackagesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyKotlinParser#imports}.
	 * @param ctx the parse tree
	 */
	void enterImports(SillyKotlinParser.ImportsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyKotlinParser#imports}.
	 * @param ctx the parse tree
	 */
	void exitImports(SillyKotlinParser.ImportsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SillyKotlinParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(SillyKotlinParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SillyKotlinParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(SillyKotlinParser.DeclarationsContext ctx);
}