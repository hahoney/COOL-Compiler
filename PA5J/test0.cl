
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)
class A {};
class B inherits A {};

class Main inherits IO {
  x:A<-new B;
  main():Object { out_string(x.type_name()) };
};

