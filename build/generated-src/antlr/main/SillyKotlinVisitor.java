// Generated from SillyKotlin.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SillyKotlinParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SillyKotlinVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SillyKotlinParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(SillyKotlinParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link SillyKotlinParser#packages}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackages(SillyKotlinParser.PackagesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SillyKotlinParser#imports}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImports(SillyKotlinParser.ImportsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SillyKotlinParser#declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarations(SillyKotlinParser.DeclarationsContext ctx);
}