## PA2J work process

It took some time to figure out the process of implementation since all the details are hidden in the material and testing cases. So I wrote this as a guide of implementation although there could be smarter ways to work around.

### Steps one:
####Implement the rule for operators and reserved words. 

A glance of Utility.java is necessary. `AbstractSymbol` class cannot be instantialized. However, it can be created by static members in `AbstractTable` class. The java version of lexer has different understanding to "new line", which is `"(\r|\n|\r\n)"` instead of `"\n"`. The reason is unknown.


### Step two:
####Implement string and comment rules

Use state tracker. But the correct command to start a new tracker is `%state Something`. Also, one should use `yybegin(Somthing)` to start it. `%Start Someting` and `BEGIN` in the handout are for C++.

A `string_buf` is provided by the code skeleton to save the string token.


#####To declare a state tracker:

```
%eofval}

%class CoolLexer
%state STRING
     ...
put tracker declaration here
     ...
%cup
%%
```
#####To use a state tracker:
```
<YYINITIAL> { yybegin(STRING); } /* begin STRING*/
<STRING>\"  { Rule; yybegin(INITIAL); } /* end STRING */
<STRING>{pattern} { Rule; } /* in STRING state */
```

#####Important notes:
#####String rule
* Read string line by line. Use a boolean to check if the string is enclosed by `"..."`.
* To convert a possibly escaped sequence, the scanner has to look back. Use another boolean to save the escape state.
* Setting lexical rules in java is easier than in regex 
* if `"` is escaped, we have to look for the next `"`.
* non-escaped `\n` may not appear in string.

#####Comment rule
* There are two types of comment: block comment `(* ... *)` and line comment `--comment`. No rule for line comment. Just update the line numer.
* Use a state counter to determine valid parenthesis
* Parentheses match must be done first.
* Keep skipping characters which are not `\*`, `\(`, '\)', `\n` until the scanner hits `\(`, `\)`, or `\*`. Because the number of `(*` is GE to that of `*)`, we can filter `\(`, `\)`, or `\*` once.
* Update the line counter for a new line


















