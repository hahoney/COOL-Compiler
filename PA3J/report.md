##PA2J work process
This assignment is to implement gramma given in Fig.1 in the manual. 

####Declare non-terminals and precedence of operators.

Read cool-tree.java to find all the nonterminal definitions. The precedence is given on page 16 of the manual

The assignment is not difficult. Pay attention to the following points.

#####Important notes
* add self object to short dispatch
* The part after `let` can be repeated.
* `case` uses three classes in `cool-tree.java`: `typcase`, `Cases`, and `branch`.
* fill absent expressions with `no_expr()`.
* set `=`, `<`, and `>` as `nonassoc`.  

Error handling simply ignor all the content before the next matching. My code can pass the test. but it does not handle two consecutive errors correctly.

Given a class

```
class A {
  f() : Object { {
    5;
    A;
    +;
    3;
  } };
};
``` 

output

```
line 4: parse error at or near TYPEID = A
```
