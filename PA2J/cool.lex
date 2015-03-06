/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

int string_buf_ptr;
boolean is_closed;
boolean is_escaped;
int nest;

%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case STRING:
        yybegin(YYINITIAL); 
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    case COMMENTa:
        yybegin(YYINITIAL); 
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%state STRING
%state COMMENTa
%state COMMENTb
%cup

WS=[\040\f\r\t\v]

%%

<YYINITIAL> \"            {  string_buf.delete(0, string_buf.length()); 
                             is_closed = false; is_escaped = false;
                             string_buf_ptr = 0; 
                             yybegin(STRING); }

<YYINITIAL>"(*"            { yybegin(COMMENTa); nest = 1; }
<YYINITIAL>"--"            { yybegin(COMMENTb); }

<COMMENTa>"*)"             { nest--; if (nest==0) { yybegin(YYINITIAL); } }
<COMMENTa>"(*"             { nest++; }
<COMMENTa>[^\*\(\)\n]*     { /* eat comment in chunks */ }
<COMMENTa>[\(\)\*]         { }
<COMMENTa>\n               { curr_lineno++; }


<COMMENTb>\n               { curr_lineno++; yybegin(YYINITIAL); }
<COMMENTb>.*               { }


<STRING>\"                {  yybegin(YYINITIAL); 
                             AbstractSymbol stringid = AbstractTable.stringtable.addString(string_buf.toString());
                             return new Symbol(TokenConstants.STR_CONST, stringid); 
                          }

<STRING>[^\"\n]*[\"\n]    {
    if (!is_closed) {
        int len = 0;
        while (yytext().charAt(len) != '\n' && yytext().charAt(len) != '\"') { len++; }

        int yy_idx = 0;
        for (yy_idx = 0; yy_idx < len+1; yy_idx++) {
            if (yytext().charAt(yy_idx) == '\"' && !is_escaped) { 
                is_closed = true; 
                break; 
            }
            if (yytext().charAt(yy_idx) == '\0') { 
                yybegin(YYINITIAL); 
                return new Symbol(TokenConstants.ERROR, "String contains null character"); 
            }
            if (yytext().charAt(yy_idx) == '\\' && !is_escaped) {
                string_buf.append(yytext().charAt(yy_idx));
                is_escaped = true;
                continue; 
            }
            if (is_escaped) {
                switch (yytext().charAt(yy_idx)) {
                    case 'b': string_buf.setCharAt(string_buf_ptr++, '\b'); break;
                    case 'f': string_buf.setCharAt(string_buf_ptr++, '\f'); break;
                    case 'n': string_buf.setCharAt(string_buf_ptr++, '\n'); break;
                    case 't': string_buf.setCharAt(string_buf_ptr++, '\t'); break;
                    case '\"': string_buf.setCharAt(string_buf_ptr++, '\"'); break;
                    default: string_buf.setCharAt(string_buf_ptr++, yytext().charAt(yy_idx)); break; 
                }
                is_escaped = false;
            } else {
                if(yytext().charAt(yy_idx) == '\n') { 
                    curr_lineno++;
                    //string_buf_ptr = 0;
                    yybegin(YYINITIAL);
                    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
                }
                string_buf.append(yytext().charAt(yy_idx)); 
                string_buf_ptr++;
            }
        } 
    } 
    if (is_closed) {
        yybegin(YYINITIAL);
        //string_buf_ptr = 0;
        int len = string_buf.length() + 1;
        if (len > MAX_STR_CONST) { return new Symbol(TokenConstants.ERROR, "String constant too long"); }
        AbstractSymbol strid = AbstractTable.stringtable.addString(string_buf.toString());
        return new Symbol(TokenConstants.STR_CONST, strid);
    }
}

<STRING>[^\"\n\0]*                                 { /* Eat the line when the it is followed by EOF */ }


<YYINITIAL>{WS}                                    {}
<YYINITIAL>"+"                                     { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"/"                                     { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"-"                                     { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"                                     { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"="                                     { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"                                     { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"."                                     { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~"                                     { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>","                                     { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>";"                                     { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"                                     { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"("                                     { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"                                     { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>"@"                                     { return new Symbol(TokenConstants.AT); }
<YYINITIAL>"{"                                     { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}"                                     { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"<="                                    { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"<-"                                    { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"=>"                                    { return new Symbol(TokenConstants.DARROW); }

<YYINITIAL>[Cc][Ll][Aa][Ss][Ss]                    { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[Ee][Ll][Ss][Ee]                        { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>[Ff][Ii]                                { return new Symbol(TokenConstants.FI); }
<YYINITIAL>[Ii][Ff]                                { return new Symbol(TokenConstants.IF); }
<YYINITIAL>[Ii][Nn]                                { return new Symbol(TokenConstants.IN); }
<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]        { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[Ll][Ee][Tt]                            { return new Symbol(TokenConstants.LET); }
<YYINITIAL>[Ll][Oo][Oo][Pp]                        { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[Pp][Oo][Oo][Ll]                        { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[Tt][Hh][Ee][Nn]                        { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>[Ww][Hh][Ii][Ll][Ee]                    { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>[Cc][Aa][Ss][Ee]                        { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[Ee][Ss][Aa][Cc]                        { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>[Oo][Ff]                                { return new Symbol(TokenConstants.OF); }
<YYINITIAL>[Nn][Ee][Ww]                            { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[Nn][Oo][Tt]                            { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd]                { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>"*)"                                    { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }

<YYINITIAL>[0-9]+                                  { AbstractSymbol integer = AbstractTable.inttable.addString(yytext());
                                                     return new Symbol(TokenConstants.INT_CONST, integer); }
<YYINITIAL>t[Rr][Uu][Ee]                           { return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
<YYINITIAL>f[Aa][Ll][Ss][Ee]                       { return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }

<YYINITIAL>[A-Z][A-Za-z0-9_]*                      { AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
<YYINITIAL>[a-z][A-Za-z0-9_]*                      { AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }

<YYINITIAL>[\r\n(\r\n)]                            { curr_lineno++; }
.                                                  { return new Symbol(TokenConstants.ERROR, yytext()); }
