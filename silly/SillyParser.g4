parser grammar SillyParser;

options { tokenVocab = SillyLexer; }

file:
    packages
    imports
    declarations
    ;

packages:
    PACKAGE WS PACKAGE_ID NL;

imports:
    (IMPORT WS PACKAGE_ID NL)*
    ;

declarations:
    (function | clazz)+
    ;

function:
    functionDecl WS LCURL Block_NL functionBody RCURL
    ;

functionDecl:
    FUN WS ID LBRACK RBRACK
    ;

functionBody:
    // comment or code only, we do not care about specifics
    LOC*
    ;

clazz:
    WS;