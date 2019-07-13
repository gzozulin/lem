// Generated from KotlinParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KotlinParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KotlinParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link KotlinParser#packageHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageHeader(KotlinParser.PackageHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link KotlinParser#importList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportList(KotlinParser.ImportListContext ctx);
}