// Generated from KotlinParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KotlinParser}.
 */
public interface KotlinParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void enterKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void exitKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#packageHeader}.
	 * @param ctx the parse tree
	 */
	void enterPackageHeader(KotlinParser.PackageHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#packageHeader}.
	 * @param ctx the parse tree
	 */
	void exitPackageHeader(KotlinParser.PackageHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#importList}.
	 * @param ctx the parse tree
	 */
	void enterImportList(KotlinParser.ImportListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#importList}.
	 * @param ctx the parse tree
	 */
	void exitImportList(KotlinParser.ImportListContext ctx);
}