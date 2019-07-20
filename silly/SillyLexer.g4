lexer grammar SillyLexer;

NL: ([\r]? [\n])+;
WS: [ \t]+;

LBRACK: '(';
RBRACK: ')';

LCURL: '{' -> pushMode(Block);

PACKAGE: 'package';
IMPORT: 'import';
FUN: 'fun';
CLASS: 'class';

ID: [a-zA-Z0-9]+;
PACKAGE_ID: ID ('.' ID)*;

mode Block;

    Block_NL: ([\r]? [\n])+;

    RCURL: '}' -> popMode;

    LOC:
        ([ \t]
        | [a-zA-Z0-9]
        | [=+-,_/()"']
        | '.')+
        Block_NL;

mode DEFAULT_MODE;

ErrorCharacter: .;


