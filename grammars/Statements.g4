grammar Statements;

@header {
package com.blaster.data.managers.kotlin;
}

DelimitedComment:   '/*' ( DelimitedComment | . )*? '*/';

LineComment:        '//' ~[\u000A\u000D]*;

Any:                .;

statements:         (delimitedComment | lineComment | code)*;

delimitedComment:   DelimitedComment;

lineComment:        LineComment;

code:               Any+;