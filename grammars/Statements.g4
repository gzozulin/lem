grammar Statements;

@header {
    package com.blaster.data.managers.parsing;
}

SL_START: '//';
ML_START: '/*';
ML_END:   '*/';

ANY: .;

statements: (code | singleLineComment | multiLineComment)*;

singleLineComment: SL_START meat;

multiLineComment: ML_START meat ML_END;

code: meat;

meat: ANY+;