// Generated from SillyLexer.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SillyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, WS=2, LBRACK=3, RBRACK=4, LCURL=5, PACKAGE=6, IMPORT=7, FUN=8, CLASS=9, 
		ID=10, PACKAGE_ID=11, Block_NL=12, RCURL=13, LOC=14, ErrorCharacter=15;
	public static final int
		Block=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "Block"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NL", "WS", "LBRACK", "RBRACK", "LCURL", "PACKAGE", "IMPORT", "FUN", 
			"CLASS", "ID", "PACKAGE_ID", "Block_NL", "RCURL", "LOC", "ErrorCharacter"
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


	public SillyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SillyLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21r\b\1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\5\2$\n\2\3\2\6"+
		"\2\'\n\2\r\2\16\2(\3\3\6\3,\n\3\r\3\16\3-\3\4\3\4\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\6\13R\n\13\r\13\16\13S\3\f\3\f"+
		"\3\f\7\fY\n\f\f\f\16\f\\\13\f\3\r\5\r_\n\r\3\r\6\rb\n\r\r\r\16\rc\3\16"+
		"\3\16\3\16\3\16\3\17\6\17k\n\17\r\17\16\17l\3\17\3\17\3\20\3\20\2\2\21"+
		"\4\3\6\4\b\5\n\6\f\7\16\b\20\t\22\n\24\13\26\f\30\r\32\16\34\17\36\20"+
		" \21\4\2\3\7\3\2\17\17\3\2\f\f\4\2\13\13\"\"\5\2\62;C\\c|\f\2\13\13\""+
		"\"$$)+-.\60;??C\\aac|\2x\2\4\3\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2\2\n\3\2\2"+
		"\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2\2\2\2\22\3\2\2\2\2\24\3\2\2\2\2\26"+
		"\3\2\2\2\2\30\3\2\2\2\2 \3\2\2\2\3\32\3\2\2\2\3\34\3\2\2\2\3\36\3\2\2"+
		"\2\4&\3\2\2\2\6+\3\2\2\2\b/\3\2\2\2\n\61\3\2\2\2\f\63\3\2\2\2\16\67\3"+
		"\2\2\2\20?\3\2\2\2\22F\3\2\2\2\24J\3\2\2\2\26Q\3\2\2\2\30U\3\2\2\2\32"+
		"a\3\2\2\2\34e\3\2\2\2\36j\3\2\2\2 p\3\2\2\2\"$\t\2\2\2#\"\3\2\2\2#$\3"+
		"\2\2\2$%\3\2\2\2%\'\t\3\2\2&#\3\2\2\2\'(\3\2\2\2(&\3\2\2\2()\3\2\2\2)"+
		"\5\3\2\2\2*,\t\4\2\2+*\3\2\2\2,-\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\7\3\2\2"+
		"\2/\60\7*\2\2\60\t\3\2\2\2\61\62\7+\2\2\62\13\3\2\2\2\63\64\7}\2\2\64"+
		"\65\3\2\2\2\65\66\b\6\2\2\66\r\3\2\2\2\678\7r\2\289\7c\2\29:\7e\2\2:;"+
		"\7m\2\2;<\7c\2\2<=\7i\2\2=>\7g\2\2>\17\3\2\2\2?@\7k\2\2@A\7o\2\2AB\7r"+
		"\2\2BC\7q\2\2CD\7t\2\2DE\7v\2\2E\21\3\2\2\2FG\7h\2\2GH\7w\2\2HI\7p\2\2"+
		"I\23\3\2\2\2JK\7e\2\2KL\7n\2\2LM\7c\2\2MN\7u\2\2NO\7u\2\2O\25\3\2\2\2"+
		"PR\t\5\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\27\3\2\2\2UZ\5\26"+
		"\13\2VW\7\60\2\2WY\5\26\13\2XV\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2"+
		"[\31\3\2\2\2\\Z\3\2\2\2]_\t\2\2\2^]\3\2\2\2^_\3\2\2\2_`\3\2\2\2`b\t\3"+
		"\2\2a^\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2d\33\3\2\2\2ef\7\177\2\2f"+
		"g\3\2\2\2gh\b\16\3\2h\35\3\2\2\2ik\t\6\2\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2"+
		"\2lm\3\2\2\2mn\3\2\2\2no\5\32\r\2o\37\3\2\2\2pq\13\2\2\2q!\3\2\2\2\r\2"+
		"\3#(-SZ^cjl\4\7\3\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}