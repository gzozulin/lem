parser grammar KotlinParser;

options { tokenVocab = KotlinLexer; }

kotlinFile:
    WS* NL*
    packageHeader SEMI
    importList
    //function?
    ;

packageHeader:
    PACKAGE WS* WORD WS* NL*
    ;

importList:
    (WS* IMPORT WS* WORD WS* NL*)?
    ;

/*function:
    functionDeclaration WS* NL* CURL functionDefinition CURR
    ;

functionDeclaration:
    WS* FUN WS* WORD WS* BRACL WS* BRACR WS*;

functionDefinition:
    paragraph*
    ;

paragraph:
    COMMSECT? TEXT
    ;*/
