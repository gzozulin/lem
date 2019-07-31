grammar CommGrammar;

WS
    : [\u0020\u0009\u000C]
    ;

NL:
    '\u000A' | '\u000D' '\u000A'
    ;

commentLines:
    WS* (delimitedComment | lineComment)
    ;

delimitedComment:
    '/*' NL
    '*' text NL
    '*/' NL
    ;

lineComment:
    '//' text
    ;

text: '[a-zA-Z0-9]';