grammar SillyKotlin;

PACKAGE: 'package';
IMPORT: 'import';

NL: [\n]+;
WS: [ \t\r]+;
ID: [a-zA-Z0-9]+;

file:
    packages
    imports
    declarations
    ;

packages: PACKAGE WS ID ('.' ID)* NL;

imports: (IMPORT WS ID ('.' ID)* NL)*;

declarations: WS;
