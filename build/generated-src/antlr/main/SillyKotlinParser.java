// Generated from SillyKotlin.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SillyKotlinParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, PACKAGE=2, IMPORT=3, NL=4, WS=5, ID=6;
	public static final int
		RULE_file = 0, RULE_packages = 1, RULE_imports = 2, RULE_declarations = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "packages", "imports", "declarations"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'package'", "'import'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "PACKAGE", "IMPORT", "NL", "WS", "ID"
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
	public String getGrammarFileName() { return "SillyKotlin.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SillyKotlinParser(TokenStream input) {
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
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).exitFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SillyKotlinVisitor ) return ((SillyKotlinVisitor<? extends T>)visitor).visitFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			packages();
			setState(9);
			imports();
			setState(10);
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
		public TerminalNode PACKAGE() { return getToken(SillyKotlinParser.PACKAGE, 0); }
		public TerminalNode WS() { return getToken(SillyKotlinParser.WS, 0); }
		public List<TerminalNode> ID() { return getTokens(SillyKotlinParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SillyKotlinParser.ID, i);
		}
		public TerminalNode NL() { return getToken(SillyKotlinParser.NL, 0); }
		public PackagesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packages; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).enterPackages(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).exitPackages(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SillyKotlinVisitor ) return ((SillyKotlinVisitor<? extends T>)visitor).visitPackages(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackagesContext packages() throws RecognitionException {
		PackagesContext _localctx = new PackagesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_packages);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			match(PACKAGE);
			setState(13);
			match(WS);
			setState(14);
			match(ID);
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(15);
				match(T__0);
				setState(16);
				match(ID);
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
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
		public List<TerminalNode> IMPORT() { return getTokens(SillyKotlinParser.IMPORT); }
		public TerminalNode IMPORT(int i) {
			return getToken(SillyKotlinParser.IMPORT, i);
		}
		public List<TerminalNode> WS() { return getTokens(SillyKotlinParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(SillyKotlinParser.WS, i);
		}
		public List<TerminalNode> ID() { return getTokens(SillyKotlinParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SillyKotlinParser.ID, i);
		}
		public List<TerminalNode> NL() { return getTokens(SillyKotlinParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(SillyKotlinParser.NL, i);
		}
		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imports; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).enterImports(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).exitImports(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SillyKotlinVisitor ) return ((SillyKotlinVisitor<? extends T>)visitor).visitImports(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_imports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(24);
				match(IMPORT);
				setState(25);
				match(WS);
				setState(26);
				match(ID);
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0) {
					{
					{
					setState(27);
					match(T__0);
					setState(28);
					match(ID);
					}
					}
					setState(33);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(34);
				match(NL);
				}
				}
				setState(39);
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
		public TerminalNode WS() { return getToken(SillyKotlinParser.WS, 0); }
		public DeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).enterDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SillyKotlinListener ) ((SillyKotlinListener)listener).exitDeclarations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SillyKotlinVisitor ) return ((SillyKotlinVisitor<? extends T>)visitor).visitDeclarations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationsContext declarations() throws RecognitionException {
		DeclarationsContext _localctx = new DeclarationsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declarations);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b-\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\7\3\24\n\3\f\3"+
		"\16\3\27\13\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\7\4 \n\4\f\4\16\4#\13\4\3\4"+
		"\7\4&\n\4\f\4\16\4)\13\4\3\5\3\5\3\5\2\2\6\2\4\6\b\2\2\2+\2\n\3\2\2\2"+
		"\4\16\3\2\2\2\6\'\3\2\2\2\b*\3\2\2\2\n\13\5\4\3\2\13\f\5\6\4\2\f\r\5\b"+
		"\5\2\r\3\3\2\2\2\16\17\7\4\2\2\17\20\7\7\2\2\20\25\7\b\2\2\21\22\7\3\2"+
		"\2\22\24\7\b\2\2\23\21\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2"+
		"\2\26\30\3\2\2\2\27\25\3\2\2\2\30\31\7\6\2\2\31\5\3\2\2\2\32\33\7\5\2"+
		"\2\33\34\7\7\2\2\34!\7\b\2\2\35\36\7\3\2\2\36 \7\b\2\2\37\35\3\2\2\2 "+
		"#\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"$\3\2\2\2#!\3\2\2\2$&\7\6\2\2%\32\3"+
		"\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\7\3\2\2\2)\'\3\2\2\2*+\7\7\2\2"+
		"+\t\3\2\2\2\5\25!\'";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}