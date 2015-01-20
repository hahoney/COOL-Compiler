/*
 *  The scanner definition for COOL.
 */

/* Some issues in the Java version
(1) The skeleton program does not run correctly. Different from the C++ version
the scanner has to handle the line feed character EOL. It is not seen in vi
unless ":set list" is set. EOL could be written as [\r\n]+
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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
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
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

INTEGER=[0-9]+
LETTER=[a-zA-Z]
UNDERSCORE='_'
STRING={LETTER}+

TYPEID={A-Z}{INTEGER|LETTER|UNDERSCORE}*
OBJID={a-z}{INTEGER|LETTER|UNDERSCORE}*
NONEWLINE_WS=[ \f\r\t\v]+
EOL=(\r|\n|\r\n)

A=[Aa]
B=[Bb]
C=[Cc]
D=[Dd]
E=[Ee]
F=[Ff]
G=[Gg]
H=[Hh]
I=[Ii]
J=[Jj]
K=[Kk]
L=[Ll]
M=[Mm]
N=[Nn]
O=[Oo]
P=[Pp]
Q=[Qq]
R=[Rr]
S=[Ss]
T=[Tt]
U=[Uu]
V=[Vv]
W=[Ww]
X=[Xx]
Y=[Yy]
Z=[Zz]
%%

<YYINITIAL>{C}{L}{A}{S}{S}	 	{ return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>{E}{L}{S}{E}		 	{ return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>{F}{I} 			{ return new Symbol(TokenConstants.FI); }
<YYINITIAL>{I}{F} 			{ return new Symbol(TokenConstants.IF); }
<YYINITIAL>{I}{N} 			{ return new Symbol(TokenConstants.IN); }
<YYINITIAL>{I}{N}{H}{E}{R}{I}{T}{S} 	{ return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>{L}{E}{T} 			{ return new Symbol(TokenConstants.LET); }
<YYINITIAL>{L}{O}{O}{P}			{ return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>{P}{O}{O}{L} 	        { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>{T}{H}{E}{N} 		{ return new Symbol(TokenConstants.THEN); }
<YYINITIAL>{W}{H}{I}{L}{E}       	{ return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>{C}{A}{S}{E}			{ return new Symbol(TokenConstants.CASE); }
<YYINITIAL>{E}{S}{A}{C}			{ return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>{O}{F}			{ return new Symbol(TokenConstants.OF); }
<YYINITIAL>{N}{E}{W}			{ return new Symbol(TokenConstants.NEW); }
<YYINITIAL>{N}{O}{T}			{ return new Symbol(TokenConstants.NOT); }
<YYINITIAL>{I}{S}{V}{O}{I}{D}		{ return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>(t){R}{U}{E}			{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
<YYINITIAL>(f){A}{L}{S}{E}		{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }

<YYINITIAL>TYPEID			{ 
              AbstractSymbol typeid = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.TYPEID, typeid); }
<YYINITIAL>OBJID                        { 
              AbstractSymbol objid = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.OBJECTID, objid); }
<YYINITIAL>INTEGER			{ 
              AbstractSymbol integer = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.INT_CONST, integer); }

<YYINITIAL>"=>"			{ return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"+"			{ return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"/"			{ return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"-"			{ return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"			{ return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"="			{ return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"			{ return new Symbol(TokenConstants.LT); } 
<YYINITIAL>"."                  { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~"                  { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>","			{ return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>";"			{ return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"                  { return new Symbol(TokenConstants.COLON); } 
<YYINITIAL>"("                  { return new Symbol(TokenConstants.LPAREN); } 
<YYINITIAL>")"                  { return new Symbol(TokenConstants.RPAREN); } 
<YYINITIAL>"@"                  { return new Symbol(TokenConstants.AT); }
<YYINITIAL>"{"                  { return new Symbol(TokenConstants.LBRACE); } 
<YYINITIAL>"}"                  { return new Symbol(TokenConstants.RBRACE); } 

<YYINITIAL>{NONEWLINE_WS}       {  }
<YYINITIAL>{EOL}		{ if (yytext().contains("\n")) curr_lineno++; }

.                               {
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
