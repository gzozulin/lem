grammar Statements;

@header {
package com.blaster.data.managers.parsing;
}

SL_START: '//';
SL_END:   '[\r]?[\n]';
ML_START: '/*';
ML_END:   '*/';

ANY: .;

statements: (singleLineComment | multiLineComment | code)*;

singleLineComment: SL_START meat SL_END;

multiLineComment: ML_START meat ML_END;

code: meat;

meat: ANY+;