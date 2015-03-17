
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)
class A { x:Int<-3; };
class B { x:Int<-f(4); y:Int<-200; f(y:Int):Int {y*y}; };

class Main {
  main():String { "asd" };
};

