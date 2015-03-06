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
		52,
		57
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
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
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
		/* 167 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"8,9:8,10,7,10:2,6,9:18,10,9,1,9:5,2,5,3,11,17,4,15,12,40:10,19,18,14,13,23," +
"9,20,44,45,46,47,48,29,45,49,50,45:2,51,45,52,53,54,45,55,56,34,57,58,59,45" +
":3,9:4,60,9,26,61,24,39,28,43,61,32,30,61:2,25,61,31,35,36,61,33,27,41,42,3" +
"8,37,61:3,21,9,22,16,9,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,168,
"0,1:2,2,3,4,1:6,5,6,1:8,7,8,9,1:6,10,11,12,11:8,10,11:7,13,1,14,15,1:3,16,1" +
",17:2,18,19,11,10,20,10:8,11,10:5,21,22,23,24,25,26,1,27,28,29,30,31,32,33," +
"34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58," +
"59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83," +
"84,85,86,87,88,89,90,91,92,93,94,95,96,11,10,97,98,99,100,101,102,103,104,1" +
"05")[0];

	private int yy_nxt[][] = unpackFromString(106,62,
"1,2,3,4,5,6,7,8,9:2,7,10,11,12,13,14,15,16,17,18,19,20,21,9,22,117,157:2,15" +
"9,23,61,119,157:2,62,84,161,163,157:2,24,165,157,87,158:2,160,158,162,158,8" +
"5,118,120,88,164,158:4,166,9,157,-1:65,25,-1:63,26,-1:60,27,-1:80,28,-1:42," +
"29,-1:8,30,-1:72,157,167,121,157:17,121,157:6,167,157:10,-1:24,158:6,31,158" +
":19,31,158:11,-1:40,24,-1:45,158:38,-1:24,157:38,-1:24,157:8,145,157:16,145" +
",157:12,1,51,80:5,51,60,80:53,1,81,53,82,81,86,81,54,81:54,-1:3,55,-1:58,1," +
"83:5,-1,58,83:54,-1,51,59:5,51,59:54,-1:24,157:3,129,157,32,157,33,157:11,3" +
"2,157:8,33,157:3,129,157:5,-1:24,158:8,122,158:16,122,158:12,-1:24,158:8,14" +
"4,158:16,144,158:12,-1,51,80:5,51,59,80:53,-1,81,-1:2,81,-1,81,-1,81:54,-1:" +
"5,56,-1:57,83:5,-1:2,83:54,-1:24,157:5,34,157:13,34,157:18,-1:24,158:3,132," +
"158,64,158,65,158:11,64,158:8,65,158:3,132,158:5,-1:24,157:2,139,157:3,63,1" +
"57:13,139,157:5,63,157:11,-1:24,158:5,66,158:13,66,158:18,-1:24,157:10,35,1" +
"57:6,35,157:20,-1:24,158:10,67,158:6,67,158:20,-1:24,157:13,36,157:21,36,15" +
"7:2,-1:24,158:13,68,158:21,68,158:2,-1:24,157:10,37,157:6,37,157:20,-1:24,1" +
"58:10,69,158:6,69,158:20,-1:24,157:4,38,157:19,38,157:13,-1:24,158:7,42,158" +
":20,42,158:9,-1:24,157:12,39,157:17,39,157:7,-1:24,158:4,70,158:19,70,158:1" +
"3,-1:24,157:4,40,157:19,40,157:13,-1:24,158:4,72,158:19,72,158:13,-1:24,41," +
"157:21,41,157:15,-1:24,73,158:21,73,158:15,-1:24,157,43,157:25,43,157:10,-1" +
":24,158:12,71,158:17,71,158:7,-1:24,157:7,74,157:20,74,157:9,-1:24,158,75,1" +
"58:25,75,158:10,-1:24,157:4,44,157:19,44,157:13,-1:24,158:3,76,158:28,76,15" +
"8:5,-1:24,157:3,45,157:28,45,157:5,-1:24,158:4,77,158:19,77,158:13,-1:24,15" +
"7:4,46,157:19,46,157:13,-1:24,158:15,78,158:7,78,158:14,-1:24,157:4,47,157:" +
"19,47,157:13,-1:24,158:3,79,158:28,79,158:5,-1:24,157:15,48,157:7,48,157:14" +
",-1:24,157:3,49,157:28,49,157:5,-1:24,157:4,89,157:6,123,157:12,89,157:4,12" +
"3,157:8,-1:24,158:4,90,158:6,134,158:12,90,158:4,134,158:8,-1:24,157:4,91,1" +
"57:6,93,157:12,91,157:4,93,157:8,-1:24,158:4,92,158:6,94,158:12,92,158:4,94" +
",158:8,-1:24,157:3,95,157:28,95,157:5,-1:24,158:4,96,158:19,96,158:13,-1:24" +
",157:11,97,157:17,97,157:8,-1:24,158:2,140,158:17,140,158:17,-1:24,157:3,99" +
",157:28,99,157:5,-1:24,158:3,98,158:28,98,158:5,-1:24,157:2,101,157:17,101," +
"157:17,-1:24,158:3,100,158:28,100,158:5,-1:24,157:14,143,157:19,143,157:3,-" +
"1:24,158:2,102,158:17,102,158:17,-1:24,157:11,103,157:17,103,157:8,-1:24,15" +
"8:14,142,158:19,142,158:3,-1:24,157:6,147,157:19,147,157:11,-1:24,158:11,10" +
"4,158:17,104,158:8,-1:24,157:4,105,157:19,105,157:13,-1:24,158:11,106,158:1" +
"7,106,158:8,-1:24,157:18,107,157:14,107,157:4,-1:24,158:6,146,158:19,146,15" +
"8:11,-1:24,157,149,157:25,149,157:10,-1:24,158:3,108,158:28,108,158:5,-1:24" +
",157:3,109,157:28,109,157:5,-1:24,158:11,148,158:17,148,158:8,-1:24,157:11," +
"151,157:17,151,157:8,-1:24,158:4,150,158:19,150,158:13,-1:24,157:4,153,157:" +
"19,153,157:13,-1:24,158,110,158:25,110,158:10,-1:24,157,111,157:25,111,157:" +
"10,-1:24,158:6,112,158:19,112,158:11,-1:24,157:3,113,157:28,113,157:5,-1:24" +
",158:9,152,158:21,152,158:6,-1:24,157:6,115,157:19,115,157:11,-1:24,158:6,1" +
"54,158:19,154,158:11,-1:24,157:9,155,157:21,155,157:6,-1:24,158:10,114,158:" +
"6,114,158:20,-1:24,157:6,156,157:19,156,157:11,-1:24,157:10,116,157:6,116,1" +
"57:20,-1:24,157,125,157,127,157:23,125,157:4,127,157:5,-1:24,158,124,126,15" +
"8:17,126,158:6,124,158:10,-1:24,157:11,131,157:17,131,157:8,-1:24,158,128,1" +
"58,130,158:23,128,158:4,130,158:5,-1:24,157:8,133,157:16,133,157:12,-1:24,1" +
"58:11,136,158:17,136,158:8,-1:24,157:8,135,137,157:15,135,157:5,137,157:6,-" +
"1:24,158:8,138,158:16,138,158:12,-1:24,157:2,141,157:17,141,157:17");

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
					case -52:
						break;
					case 52:
						{ /* eat comment in chunks */ }
					case -53:
						break;
					case 53:
						{ }
					case -54:
						break;
					case 54:
						{ curr_lineno++; }
					case -55:
						break;
					case 55:
						{ nest++; }
					case -56:
						break;
					case 56:
						{ nest--; if (nest==0) { yybegin(YYINITIAL); } }
					case -57:
						break;
					case 57:
						{ }
					case -58:
						break;
					case 58:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -59:
						break;
					case 60:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -60:
						break;
					case 61:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -61:
						break;
					case 62:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.FI); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.IF); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.IN); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.OF); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.LET); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.NEW); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.NOT); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.CASE); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.LOOP); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.ELSE); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.ESAC); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.THEN); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.POOL); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.CLASS); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.WHILE); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -79:
						break;
					case 80:
						{ /* Eat the line when the it is followed by EOF */ }
					case -80:
						break;
					case 81:
						{ /* eat comment in chunks */ }
					case -81:
						break;
					case 82:
						{ }
					case -82:
						break;
					case 83:
						{ }
					case -83:
						break;
					case 84:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -84:
						break;
					case 85:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
					case -85:
						break;
					case 86:
						{ }
					case -86:
						break;
					case 87:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -87:
						break;
					case 88:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
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
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -116:
						break;
					case 117:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -117:
						break;
					case 118:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
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
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -156:
						break;
					case 157:
						{ AbstractSymbol objid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.OBJECTID, objid); }
					case -157:
						break;
					case 158:
						{ AbstractSymbol typeid = AbstractTable.idtable.addString(yytext());
                                                     return new Symbol(TokenConstants.TYPEID, typeid); }
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
