/*
 *  The scanner definition for COOL.
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
int string_buf_ptr;
boolean is_closed;
boolean is_escaped;
int nest;
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
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int COMMENTb = 3;
	private final int COMMENTa = 2;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		50,
		53,
		58
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
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"8,9:8,10,7,9,10,6,9:18,10,9,1,9:5,2,5,3,11,17,4,15,12,41:10,19,18,14,13,23," +
"9,20,45,46,47,48,49,29,46,50,51,46:2,52,46,53,54,55,46,56,57,34,58,38,59,46" +
":3,9:4,60,9,26,61,24,40,28,44,61,32,30,61:2,25,61,31,35,36,61,33,27,42,43,3" +
"9,37,61:3,21,9,22,16,9,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,170,
"0,1:2,2,3,4,1:6,5,6,1:8,7,8,9,1:6,10,11,12,11:8,10,11:7,13,1:2,14,15,1:3,16" +
",1,17,11,17,18,19,11,10,20,10:8,11,10:5,21,22,23,24,25,26,1,27,28,29,30,31," +
"32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56," +
"57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81," +
"82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,11,10,97,98,99,100,101,102,103" +
",104,105")[0];

	private int yy_nxt[][] = unpackFromString(106,62,
"1,2,3,4,5,6,7,8,9:2,7,10,11,12,13,14,15,16,17,18,19,20,21,9,22,119,159:2,16" +
"1,23,63,121,159:2,64,86,163,165,160,61,159,24,167,159,89,160:2,162,160,164," +
"160,87,120,122,90,166,160:3,168,9,159,-1:65,25,-1:63,26,-1:60,27,-1:80,28,-" +
"1:42,29,-1:8,30,-1:72,159,169,123,159:18,123,159:6,169,159:9,-1:24,160:6,31" +
",160:20,31,160:10,-1:41,24,-1:44,160:38,-1:24,159:38,-1:24,159:8,147,159:17" +
",147,159:11,1,51,82:5,52,62,82:53,1,83,54,84,83,88,83,55,83:54,-1:3,56,-1:5" +
"8,1,85:5,-1,59,85:54,-1,52,60:5,52,60:54,-1:24,159:3,131,159,32,159,33,159:" +
"12,32,159:8,33,159:3,131,159:4,-1:24,160:8,124,160:17,124,160:11,-1:24,160:" +
"8,146,160:17,146,160:11,-1,52,82:5,52,60,82:53,-1,83,-1:2,83,-1,83,-1,83:54" +
",-1:5,57,-1:57,85:5,-1:2,85:54,-1:24,159:5,34,159:14,34,159:17,-1:24,160:3," +
"134,160,66,160,67,160:12,66,160:8,67,160:3,134,160:4,-1:24,159:2,141,159:3," +
"65,159:14,141,159:5,65,159:10,-1:24,160:5,68,160:14,68,160:17,-1:24,159:10," +
"35,159:7,35,159:19,-1:24,160:10,69,160:7,69,160:19,-1:24,159:13,36,159:21,3" +
"6,159:2,-1:24,160:13,70,160:21,70,160:2,-1:24,159:10,37,159:7,37,159:19,-1:" +
"24,160:10,71,160:7,71,160:19,-1:24,159:4,38,159:20,38,159:12,-1:24,160:7,42" +
",160:21,42,160:8,-1:24,159:12,39,159:18,39,159:6,-1:24,160:4,72,160:20,72,1" +
"60:12,-1:24,159:4,40,159:20,40,159:12,-1:24,160:4,74,160:20,74,160:12,-1:24" +
",41,159:22,41,159:14,-1:24,75,160:22,75,160:14,-1:24,159,43,159:26,43,159:9" +
",-1:24,160:12,73,160:18,73,160:6,-1:24,159:7,76,159:21,76,159:8,-1:24,160,7" +
"7,160:26,77,160:9,-1:24,159:4,44,159:20,44,159:12,-1:24,160:3,78,160:29,78," +
"160:4,-1:24,159:3,45,159:29,45,159:4,-1:24,160:4,79,160:20,79,160:12,-1:24," +
"159:4,46,159:20,46,159:12,-1:24,160:16,80,160:7,80,160:13,-1:24,159:4,47,15" +
"9:20,47,159:12,-1:24,160:3,81,160:29,81,160:4,-1:24,159:16,48,159:7,48,159:" +
"13,-1:24,159:3,49,159:29,49,159:4,-1:24,159:4,91,159:6,125,159:13,91,159:4," +
"125,159:7,-1:24,160:4,92,160:6,136,160:13,92,160:4,136,160:7,-1:24,159:4,93" +
",159:6,95,159:13,93,159:4,95,159:7,-1:24,160:4,94,160:6,96,160:13,94,160:4," +
"96,160:7,-1:24,159:3,97,159:29,97,159:4,-1:24,160:4,98,160:20,98,160:12,-1:" +
"24,159:11,99,159:18,99,159:7,-1:24,160:2,142,160:18,142,160:16,-1:24,159:3," +
"101,159:29,101,159:4,-1:24,160:3,100,160:29,100,160:4,-1:24,159:2,103,159:1" +
"8,103,159:16,-1:24,160:3,102,160:29,102,160:4,-1:24,159:14,145:2,159:22,-1:" +
"24,160:2,104,160:18,104,160:16,-1:24,159:11,105,159:18,105,159:7,-1:24,160:" +
"14,144:2,160:22,-1:24,159:6,149,159:20,149,159:10,-1:24,160:11,106,160:18,1" +
"06,160:7,-1:24,159:4,107,159:20,107,159:12,-1:24,160:11,108,160:18,108,160:" +
"7,-1:24,159:19,109,159:14,109,159:3,-1:24,160:6,148,160:20,148,160:10,-1:24" +
",159,151,159:26,151,159:9,-1:24,160:3,110,160:29,110,160:4,-1:24,159:3,111," +
"159:29,111,159:4,-1:24,160:11,150,160:18,150,160:7,-1:24,159:11,153,159:18," +
"153,159:7,-1:24,160:4,152,160:20,152,160:12,-1:24,159:4,155,159:20,155,159:" +
"12,-1:24,160,112,160:26,112,160:9,-1:24,159,113,159:26,113,159:9,-1:24,160:" +
"6,114,160:20,114,160:10,-1:24,159:3,115,159:29,115,159:4,-1:24,160:9,154,16" +
"0:22,154,160:5,-1:24,159:6,117,159:20,117,159:10,-1:24,160:6,156,160:20,156" +
",160:10,-1:24,159:9,157,159:22,157,159:5,-1:24,160:10,116,160:7,116,160:19," +
"-1:24,159:6,158,159:20,158,159:10,-1:24,159:10,118,159:7,118,159:19,-1:24,1" +
"59,127,159,129,159:24,127,159:4,129,159:4,-1:24,160,126,128,160:18,128,160:" +
"6,126,160:9,-1:24,159:11,133,159:18,133,159:7,-1:24,160,130,160,132,160:24," +
"130,160:4,132,160:4,-1:24,159:8,135,159:17,135,159:11,-1:24,160:11,138,160:" +
"18,138,160:7,-1:24,159:8,137,139,159:16,137,159:5,139,159:5,-1:24,160:8,140" +
",160:17,140,160:11,-1:24,159:2,143,159:18,143,159:16");

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
						{  string_buf.delete(0, string_buf.length()); 
                             is_closed = false; is_escaped = false;
                             string_buf_ptr = 0; 
                             yybegin(STRING); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MULT); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MINUS); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -7:
						break;
					case 7:
						{}
					case -8:
						break;
					case 8:
						{ curr_lineno++; }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.PLUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.DIV); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.EQ); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.LT); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.DOT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.NEG); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.COMMA); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.SEMI); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.COLON); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.AT); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -22:
						break;
					case 22:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -23:
						break;
					case 23:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -24:
						break;
					case 24:
						{ AbstractSymbol integer = AbstractTable.inttable.addString(yytext());
                                                     return new Symbol(TokenConstants.INT_CONST, integer); }
					case -25:
						break;
					case 25:
						{ yybegin(COMMENTa); nest = 1; }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -27:
						break;
					case 27:
						{ yybegin(COMMENTb); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.DARROW); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LE); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.FI); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.IF); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.IN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.OF); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{ /* Eat the line when the it is followed by EOF */ }
					case -51:
						break;
					case 51:
						{  yybegin(YYINITIAL); 
                             AbstractSymbol stringid = AbstractTable.stringtable.addString(string_buf.toString());
                             return new Symbol(TokenConstants.STR_CONST, stringid); 
                          }
					case -52:
						break;
					case 52:
						{
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
					case -53:
						break;
					case 53:
						{ /* eat comment in chunks */ }
					case -54:
						break;
					case 54:
						{ }
					case -55:
						break;
					case 55:
						{ curr_lineno++; }
					case -56:
						break;
					case 56:
						{ nest++; }
					case -57:
						break;
					case 57:
						{ nest--; if (nest==0) { yybegin(YYINITIAL); } }
					case -58:
						break;
					case 58:
						{ }
					case -59:
						break;
					case 59:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -60:
						break;
					case 61:
						{}
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -62:
						break;
					case 63:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -63:
						break;
					case 64:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.FI); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.IF); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.IN); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.OF); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.LET); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.NEW); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.NOT); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.CASE); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LOOP); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.ELSE); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.ESAC); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.THEN); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.POOL); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.CLASS); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.WHILE); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -81:
						break;
					case 82:
						{ /* Eat the line when the it is followed by EOF */ }
					case -82:
						break;
					case 83:
						{ /* eat comment in chunks */ }
					case -83:
						break;
					case 84:
						{ }
					case -84:
						break;
					case 85:
						{ }
					case -85:
						break;
					case 86:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -86:
						break;
					case 87:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -87:
						break;
					case 88:
						{ }
					case -88:
						break;
					case 89:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -89:
						break;
					case 90:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -90:
						break;
					case 91:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -91:
						break;
					case 92:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -92:
						break;
					case 93:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -93:
						break;
					case 94:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -94:
						break;
					case 95:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -95:
						break;
					case 96:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -96:
						break;
					case 97:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -97:
						break;
					case 98:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -98:
						break;
					case 99:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -99:
						break;
					case 100:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -100:
						break;
					case 101:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -101:
						break;
					case 102:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -102:
						break;
					case 103:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -103:
						break;
					case 104:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -104:
						break;
					case 105:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -105:
						break;
					case 106:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -106:
						break;
					case 107:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -107:
						break;
					case 108:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -108:
						break;
					case 109:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -109:
						break;
					case 110:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -110:
						break;
					case 111:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -111:
						break;
					case 112:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -112:
						break;
					case 113:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -113:
						break;
					case 114:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -114:
						break;
					case 115:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -115:
						break;
					case 116:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -116:
						break;
					case 117:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -117:
						break;
					case 118:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -118:
						break;
					case 119:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -119:
						break;
					case 120:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -120:
						break;
					case 121:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -121:
						break;
					case 122:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -122:
						break;
					case 123:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -123:
						break;
					case 124:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -124:
						break;
					case 125:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -125:
						break;
					case 126:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -126:
						break;
					case 127:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -127:
						break;
					case 128:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -128:
						break;
					case 129:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -129:
						break;
					case 130:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -130:
						break;
					case 131:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -131:
						break;
					case 132:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -132:
						break;
					case 133:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -133:
						break;
					case 134:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -134:
						break;
					case 135:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -135:
						break;
					case 136:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -136:
						break;
					case 137:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -137:
						break;
					case 138:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -138:
						break;
					case 139:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -139:
						break;
					case 140:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -140:
						break;
					case 141:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -141:
						break;
					case 142:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -142:
						break;
					case 143:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -143:
						break;
					case 144:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -144:
						break;
					case 145:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -145:
						break;
					case 146:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -146:
						break;
					case 147:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -147:
						break;
					case 148:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -148:
						break;
					case 149:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -149:
						break;
					case 150:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -150:
						break;
					case 151:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -151:
						break;
					case 152:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -152:
						break;
					case 153:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -153:
						break;
					case 154:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -154:
						break;
					case 155:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -155:
						break;
					case 156:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -156:
						break;
					case 157:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -157:
						break;
					case 158:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -158:
						break;
					case 159:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -159:
						break;
					case 160:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -160:
						break;
					case 161:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -161:
						break;
					case 162:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -162:
						break;
					case 163:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -163:
						break;
					case 164:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -164:
						break;
					case 165:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -165:
						break;
					case 166:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -166:
						break;
					case 167:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -167:
						break;
					case 168:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -168:
						break;
					case 169:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -169:
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
