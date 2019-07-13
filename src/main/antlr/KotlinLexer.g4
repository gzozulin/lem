lexer grammar KotlinLexer;

// ---------------------- Common -----------------------------

COMMSECT: '/*' ( COMMSECT | . )*? '*/';

COMMLINE
    : '//' ~[\r\n]*
      -> channel(HIDDEN)
    ;

WS: [\u0020\u0009\u000C];

NL: '\n';

CURL: '{';
CURR: '}';
BRACL: '(';
BRACR: ')';
SEMI: ';';
DOT: '.';

WORD: [a-zA-Z0-9.=,/"]+;
LINE: WS* (WORD WS*)+ NL+;
TEXT: LINE+;

// ----------------------- Lang -------------------------------

PACKAGE: 'package';
IMPORT: 'import';

FUN: 'fun';
CLASS: 'class';


