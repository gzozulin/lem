// Generated from SillyParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SillyParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, WS=2, LBRACK=3, RBRACK=4, LCURL=5, PACKAGE=6, IMPORT=7, FUN=8, CLASS=9, 
		ID=10, PACKAGE_ID=11, Block_NL=12, RCURL=13, LOC=14, ErrorCharacter=15;
	public static final int
		RULE_file = 0, RULE_packages = 1, RULE_imports = 2, RULE_declarations = 3, 
		RULE_function = 4, RULE_functionDecl = 5, RULE_functionBody = 6, RULE_clazz = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "packages", "imports", "declarations", "function", "functionDecl", 
			"functionBody", "clazz"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'('", "')'", "'{'", "'package'", "'import'", "'fun'", 
			"'class'", null, null, null, "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NL", "WS", "LBRACK", "RBRACK", "LCURL", "PACKAGE", "IMPORT", "FUN", 
			"CLASS", "ID", "PACKAGE_ID", "Block_NL", "RCURL", "LOC", "ErrorCharacter"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SillyParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SillyParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FileContext extends ParserRuleContext {
		public PackagesContext packages() {
			return getRuleContext(PackagesContext.class,0);
		}
		public ImportsContext imports() {
			return getRuleContext(ImportsContext.class,0);
		}
		public DeclarationsContext declarations() {
			return getRuleContext(DeclarationsContext.class,0);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			packages();
			setState(17);
			imports();
			setState(18);
			declarations();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackagesContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(SillyParser.PACKAGE, 0); }
		public TerminalNode WS() { return getToken(SillyParser.WS, 0); }
		public TerminalNode PACKAGE_ID() { return getToken(SillyParser.PACKAGE_ID, 0); }
		public TerminalNode NL() { return getToken(SillyParser.NL, 0); }
		public PackagesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packages; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterPackages(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitPackages(this);
		}
	}

	public final PackagesContext packages() throws RecognitionException {
		PackagesContext _localctx = new PackagesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_packages);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			match(PACKAGE);
			setState(21);
			match(WS);
			setState(22);
			match(PACKAGE_ID);
			setState(23);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportsContext extends ParserRuleContext {
		public List<TerminalNode> IMPORT() { return getTokens(SillyParser.IMPORT); }
		public TerminalNode IMPORT(int i) {
			return getToken(SillyParser.IMPORT, i);
		}
		public List<TerminalNode> WS() { return getTokens(SillyParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(SillyParser.WS, i);
		}
		public List<TerminalNode> PACKAGE_ID() { return getTokens(SillyParser.PACKAGE_ID); }
		public TerminalNode PACKAGE_ID(int i) {
			return getToken(SillyParser.PACKAGE_ID, i);
		}
		public List<TerminalNode> NL() { return getTokens(SillyParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(SillyParser.NL, i);
		}
		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imports; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterImports(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitImports(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_imports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(25);
				match(IMPORT);
				setState(26);
				match(WS);
				setState(27);
				match(PACKAGE_ID);
				setState(28);
				match(NL);
				}
				}
				setState(33);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationsContext extends ParserRuleContext {
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public List<ClazzContext> clazz() {
			return getRuleContexts(ClazzContext.class);
		}
		public ClazzContext clazz(int i) {
			return getRuleContext(ClazzContext.class,i);
		}
		public DeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitDeclarations(this);
		}
	}

	public final DeclarationsContext declarations() throws RecognitionException {
		DeclarationsContext _localctx = new DeclarationsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(36);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case FUN:
					{
					setState(34);
					function();
					}
					break;
				case WS:
					{
					setState(35);
					clazz();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(38); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS || _la==FUN );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public TerminalNode WS() { return getToken(SillyParser.WS, 0); }
		public TerminalNode LCURL() { return getToken(SillyParser.LCURL, 0); }
		public TerminalNode Block_NL() { return getToken(SillyParser.Block_NL, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public TerminalNode RCURL() { return getToken(SillyParser.RCURL, 0); }
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			functionDecl();
			setState(41);
			match(WS);
			setState(42);
			match(LCURL);
			setState(43);
			match(Block_NL);
			setState(44);
			functionBody();
			setState(45);
			match(RCURL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDeclContext extends ParserRuleContext {
		public TerminalNode FUN() { return getToken(SillyParser.FUN, 0); }
		public TerminalNode WS() { return getToken(SillyParser.WS, 0); }
		public TerminalNode ID() { return getToken(SillyParser.ID, 0); }
		public TerminalNode LBRACK() { return getToken(SillyParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(SillyParser.RBRACK, 0); }
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterFunctionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitFunctionDecl(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_functionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(FUN);
			setState(48);
			match(WS);
			setState(49);
			match(ID);
			setState(50);
			match(LBRACK);
			setState(51);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionBodyContext extends ParserRuleContext {
		public List<TerminalNode> LOC() { return getTokens(SillyParser.LOC); }
		public TerminalNode LOC(int i) {
			return getToken(SillyParser.LOC, i);
		}
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitFunctionBody(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_functionBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LOC) {
				{
				{
				setState(53);
				match(LOC);
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClazzContext extends ParserRuleContext {
		public TerminalNode WS() { return getToken(SillyParser.WS, 0); }
		public ClazzContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clazz; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).enterClazz(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyParserListener ) ((SillyParserListener)listener).exitClazz(this);
		}
	}

	public final ClazzContext clazz() throws RecognitionException {
		ClazzContext _localctx = new ClazzContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_clazz);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(WS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\21@\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7\4 \n\4\f\4\16\4#\13\4\3\5\3\5\6\5\'"+
		"\n\5\r\5\16\5(\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b"+
		"\7\b9\n\b\f\b\16\b<\13\b\3\t\3\t\3\t\2\2\n\2\4\6\b\n\f\16\20\2\2\2;\2"+
		"\22\3\2\2\2\4\26\3\2\2\2\6!\3\2\2\2\b&\3\2\2\2\n*\3\2\2\2\f\61\3\2\2\2"+
		"\16:\3\2\2\2\20=\3\2\2\2\22\23\5\4\3\2\23\24\5\6\4\2\24\25\5\b\5\2\25"+
		"\3\3\2\2\2\26\27\7\b\2\2\27\30\7\4\2\2\30\31\7\r\2\2\31\32\7\3\2\2\32"+
		"\5\3\2\2\2\33\34\7\t\2\2\34\35\7\4\2\2\35\36\7\r\2\2\36 \7\3\2\2\37\33"+
		"\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"\7\3\2\2\2#!\3\2\2\2$\'\5\n"+
		"\6\2%\'\5\20\t\2&$\3\2\2\2&%\3\2\2\2\'(\3\2\2\2(&\3\2\2\2()\3\2\2\2)\t"+
		"\3\2\2\2*+\5\f\7\2+,\7\4\2\2,-\7\7\2\2-.\7\16\2\2./\5\16\b\2/\60\7\17"+
		"\2\2\60\13\3\2\2\2\61\62\7\n\2\2\62\63\7\4\2\2\63\64\7\f\2\2\64\65\7\5"+
		"\2\2\65\66\7\6\2\2\66\r\3\2\2\2\679\7\20\2\28\67\3\2\2\29<\3\2\2\2:8\3"+
		"\2\2\2:;\3\2\2\2;\17\3\2\2\2<:\3\2\2\2=>\7\4\2\2>\21\3\2\2\2\6!&(:";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}