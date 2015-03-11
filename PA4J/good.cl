class C inherits Main {
	a : Int;
	b : Bool;
	init(x : Int, y : Bool) : C {
           {
		a <- x;
		b <- y;
		self;
           }
	};
};

Class Main inherits D {
	main():C {
	  (new C).init(1,true)
	};
	foo(a: Int, b: Int): C {
		(new C).init(1, 2)
	};
};

Class D inherits Object {
	main():C {
	  (new C).init(1,true)
	};
	foo(b: Int, c: C): Int {
		3
	};
};


