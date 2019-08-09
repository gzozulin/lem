// Generated from Statements.g4 by ANTLR 4.7.2

    package com.blaster.data.managers.parsing;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StatementsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SL_START=1, ML_START=2, ML_END=3, ANY=4;
	public static final int
		RULE_statements = 0, RULE_singleLineComment = 1, RULE_multiLineComment = 2, 
		RULE_code = 3, RULE_meat = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"statements", "singleLineComment", "multiLineComment", "code", "meat"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'//'", "'/*'", "'*/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SL_START", "ML_START", "ML_END", "ANY"
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
	public String getGrammarFileName() { return "Statements.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StatementsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StatementsContext extends ParserRuleContext {
		public List<CodeContext> code() {
			return getRuleContexts(CodeContext.class);
		}
		public CodeContext code(int i) {
			return getRuleContext(CodeContext.class,i);
		}
		public List<SingleLineCommentContext> singleLineComment() {
			return getRuleContexts(SingleLineCommentContext.class);
		}
		public SingleLineCommentContext singleLineComment(int i) {
			return getRuleContext(SingleLineCommentContext.class,i);
		}
		public List<MultiLineCommentContext> multiLineComment() {
			return getRuleContexts(MultiLineCommentContext.class);
		}
		public MultiLineCommentContext multiLineComment(int i) {
			return getRuleContext(MultiLineCommentContext.class,i);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitStatements(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(15);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SL_START) | (1L << ML_START) | (1L << ANY))) != 0)) {
				{
				setState(13);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ANY:
					{
					setState(10);
					code();
					}
					break;
				case SL_START:
					{
					setState(11);
					singleLineComment();
					}
					break;
				case ML_START:
					{
					setState(12);
					multiLineComment();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(17);
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

	public static class SingleLineCommentContext extends ParserRuleContext {
		public TerminalNode SL_START() { return getToken(StatementsParser.SL_START, 0); }
		public MeatContext meat() {
			return getRuleContext(MeatContext.class,0);
		}
		public SingleLineCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleLineComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterSingleLineComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitSingleLineComment(this);
		}
	}

	public final SingleLineCommentContext singleLineComment() throws RecognitionException {
		SingleLineCommentContext _localctx = new SingleLineCommentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_singleLineComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(SL_START);
			setState(19);
			meat();
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

	public static class MultiLineCommentContext extends ParserRuleContext {
		public TerminalNode ML_START() { return getToken(StatementsParser.ML_START, 0); }
		public MeatContext meat() {
			return getRuleContext(MeatContext.class,0);
		}
		public TerminalNode ML_END() { return getToken(StatementsParser.ML_END, 0); }
		public MultiLineCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiLineComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterMultiLineComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitMultiLineComment(this);
		}
	}

	public final MultiLineCommentContext multiLineComment() throws RecognitionException {
		MultiLineCommentContext _localctx = new MultiLineCommentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_multiLineComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			match(ML_START);
			setState(22);
			meat();
			setState(23);
			match(ML_END);
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

	public static class CodeContext extends ParserRuleContext {
		public MeatContext meat() {
			return getRuleContext(MeatContext.class,0);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitCode(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			meat();
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

	public static class MeatContext extends ParserRuleContext {
		public List<TerminalNode> ANY() { return getTokens(StatementsParser.ANY); }
		public TerminalNode ANY(int i) {
			return getToken(StatementsParser.ANY, i);
		}
		public MeatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_meat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterMeat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitMeat(this);
		}
	}

	public final MeatContext meat() throws RecognitionException {
		MeatContext _localctx = new MeatContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_meat);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(28); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(27);
					match(ANY);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(30); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\6#\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\7\2\20\n\2\f\2\16\2\23\13\2\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\6\6\6\37\n\6\r\6\16\6 \3\6\2\2\7\2"+
		"\4\6\b\n\2\2\2!\2\21\3\2\2\2\4\24\3\2\2\2\6\27\3\2\2\2\b\33\3\2\2\2\n"+
		"\36\3\2\2\2\f\20\5\b\5\2\r\20\5\4\3\2\16\20\5\6\4\2\17\f\3\2\2\2\17\r"+
		"\3\2\2\2\17\16\3\2\2\2\20\23\3\2\2\2\21\17\3\2\2\2\21\22\3\2\2\2\22\3"+
		"\3\2\2\2\23\21\3\2\2\2\24\25\7\3\2\2\25\26\5\n\6\2\26\5\3\2\2\2\27\30"+
		"\7\4\2\2\30\31\5\n\6\2\31\32\7\5\2\2\32\7\3\2\2\2\33\34\5\n\6\2\34\t\3"+
		"\2\2\2\35\37\7\6\2\2\36\35\3\2\2\2\37 \3\2\2\2 \36\3\2\2\2 !\3\2\2\2!"+
		"\13\3\2\2\2\5\17\21 ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}