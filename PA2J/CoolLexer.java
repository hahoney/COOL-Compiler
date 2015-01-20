/*
 *  The scanner definition for COOL.
 */
/* Some issues in the Java version
(1) The skeleton program does not run correctly. Different from the C++ version
the scanner has to handle the line feed character EOL. It is not seen in vi
unless ":set list" is set. EOL could be written as [\r\n]+
*/
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NOT_ACCEPT,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"52:9,48,51,52,48,50,52:18,48,52:7,43,44,36,33,40,35,38,34,52:10,42,41,37,31" +
",32,52,45,3,26,1,24,22,6,29,9,23,27,52,2,52,28,25,21,52,30,4,11,18,15,14,52" +
",20,52:7,3,52,1,16,5,19,52,9,7,52:2,2,52,8,12,13,52,10,4,17,18,49,14,52:3,4" +
"6,52,47,39,52,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,106,
"0,1,2,3,1:15,4,1:3,5,1:20,6,7,8,9,10,1,11,12,13,14,15,16,17,18,19,20,21,22," +
"23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47," +
"48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66")[0];

	private int yy_nxt[][] = unpackFromString(67,53,
"1,2,45,49:2,51,53,55,98,49:2,57,59,61,63,49:2,65,49,67,49,61,51,69,49,71,49" +
":2,98,49:2,3,49,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19:2,46,20,49,-1:55," +
"44,48,-1:81,25,-1:68,19:3,-1:11,101,-1:46,105,-1:54,50,-1:6,52,-1:9,50,-1:2" +
",52,-1:75,19:3,20,-1:10,101,-1,103,-1:45,72,-1:59,26,-1:5,26,-1:37,99,-1,97" +
",-1:60,73,-1:12,73,-1:34,21,-1:15,21,-1:44,76,-1:33,76,-1:7,54,-1,22,-1,23," +
"-1:10,22,-1:8,23,-1:38,27,-1:47,60,-1:10,62,-1:43,28,-1:5,28,-1:41,24,-1:12" +
",24,-1:38,77,-1:16,77,-1:42,100,-1:12,100,-1:48,78,-1:40,64,-1:50,102,-1:15" +
",102,-1:38,60,66,-1:19,66,-1:40,80,-1:37,68,-1:3,21,-1:15,21,-1:31,81,-1:54" +
",54,-1,22,-1,23,-1:10,22,-1:8,47,-1:51,82,-1:31,24,-1:12,24,-1:6,70,-1:31,2" +
"9,-1:16,29,-1:43,30,-1:7,30,-1:36,31,-1:16,31,-1:31,32,-1:63,84,-1:12,84,-1" +
":35,33,-1:19,33,-1:46,104,-1:32,34,-1:55,35,-1:16,35,-1:34,87,-1:71,89,-1:3" +
"3,36,-1:55,90,-1:15,90,-1:39,91,-1:19,91,-1:27,37,-1:16,37,-1:35,38,-1:16,3" +
"8,-1:59,93,-1:47,39,-1:44,40,-1:7,40,-1:35,94,-1:15,94,-1:53,41,-1:50,95,-1" +
":41,96,-1:5,96,-1:65,42,-1:26,43,-1:51,75,-1:54,56,-1:6,58,-1:9,56,-1:2,58," +
"-1:31,74,-1:60,79,-1:12,79,-1:32,85,-1:16,85,-1:32,86,-1:72,88,-1:53,92,-1:" +
"33,83,-1:48");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.EQ); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.PLUS); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.DIV); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.MINUS); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.MULT); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.LT); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.DOT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.NEG); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.COMMA); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.SEMI); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.COLON); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.AT); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -19:
						break;
					case 19:
						{  }
					case -20:
						break;
					case 20:
						{ if (yytext().contains("\n")) curr_lineno++; System.out.println(curr_lineno); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.FI); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.IF); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.IN); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.OF); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.DARROW); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.LET); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.NEW); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.NOT); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.CASE); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LOOP); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.ELSE); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.ESAC); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.THEN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.POOL); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.CLASS); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.WHILE); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -39:
						break;
					case 39:
						{ 
              AbstractSymbol objid = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.OBJECTID, objid); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -41:
						break;
					case 41:
						{ 
              AbstractSymbol typeid = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.TYPEID, typeid); }
					case -42:
						break;
					case 42:
						{ 
              AbstractSymbol integer = AbstractTable.stringtable.addString(yytext());
              return new Symbol(TokenConstants.INT_CONST, integer); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -44:
						break;
					case 45:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -45:
						break;
					case 46:
						{  }
					case -46:
						break;
					case 47:
						{ return new Symbol(TokenConstants.IN); }
					case -47:
						break;
					case 49:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -48:
						break;
					case 51:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -49:
						break;
					case 53:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -50:
						break;
					case 55:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -51:
						break;
					case 57:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -52:
						break;
					case 59:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -53:
						break;
					case 61:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -54:
						break;
					case 63:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -55:
						break;
					case 65:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -56:
						break;
					case 67:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -57:
						break;
					case 69:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -58:
						break;
					case 71:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -59:
						break;
					case 98:
						{
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -60:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
