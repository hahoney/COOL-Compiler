import java.io.PrintStream;
import java.util.*;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private AbstractSymbol filename;
    private int semantErrors;
    private PrintStream errorStream;

    private Map<AbstractSymbol, class_c> basicClassMap;
    private Map<AbstractSymbol, class_c> classTableMap;
    private Map<AbstractSymbol, Features> featureTableMap;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class = 
	    new class_c(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

        /* Definition of SELF_TYPE */
        class_c Self_class = 
            new class_c (0, TreeConstants.SELF_TYPE, 
                            TreeConstants.Object_,
                            new Features(0),
                            filename
            );


	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */
        this.filename = filename;
        basicClassMap.put(TreeConstants.Object_, Object_class);
        basicClassMap.put(TreeConstants.IO, IO_class);
        basicClassMap.put(TreeConstants.Int, Int_class);
        basicClassMap.put(TreeConstants.Bool, Bool_class);
        basicClassMap.put(TreeConstants.Str, Str_class); 
        basicClassMap.put(TreeConstants.SELF_TYPE, Self_class); 
     
        classTableMap.putAll(basicClassMap);
    }

    public ClassTable(Classes cls) {
	semantErrors = 0;
	errorStream = System.err;
        basicClassMap = new HashMap<AbstractSymbol, class_c>();	
        classTableMap = new HashMap<AbstractSymbol, class_c>();
        featureTableMap = new HashMap<AbstractSymbol, Features>();
	/* fill this in */
        installBasicClasses();
        classTableMap = checkClasses(cls);
        checkCycle();
    }

    public boolean hasType(AbstractSymbol type) {
        return basicClassMap.containsKey(type) || classTableMap.containsKey(type);
    }

    public boolean isSubtype(AbstractSymbol sub, AbstractSymbol ancestor) {
        assert(hasType(sub) && hasType(ancestor));
        if (TreeConstants.SELF_TYPE.equals(sub) || TreeConstants.SELF_TYPE.equals(ancestor)) {
            return sub.equals(ancestor);
        }
        if (ancestor.equals(sub) || ancestor.equals(TreeConstants.Object_)) { return true; }
        if (basicClassMap.containsKey(sub)) { return false; }

        while (!sub.equals(TreeConstants.Object_)) {
            if (sub.equals(ancestor)) { return true; }
            sub = classTableMap.get(sub).parent;
        }
        return false; 
    }
    // get feature from curClass or its nearest ancestor
    public Feature getFeature(AbstractSymbol featureName, AbstractSymbol className, boolean isMethod) {
        if (classTableMap.containsKey(className)) {
            AbstractSymbol pt = className; 
            while (!TreeConstants.No_class.equals(pt)) {
                Features features = featureTableMap.get(pt);
                if (features != null) {
                    for (Enumeration e = features.getElements(); e.hasMoreElements();) {
                        Feature feature = (Feature) e.nextElement();
                        if (feature.getName().equals(featureName)) {
                            if ((feature instanceof method && isMethod) ||
                                (feature instanceof attr && !isMethod)) {
                                return feature;
                            }
                        }
                    }
                }
                pt = classTableMap.get(pt).getParent();
            }
        }
        return null; 
    }

    public void addFeature(Feature feature, class_c curClass) {
        Features features;
        if (!featureTableMap.containsKey(curClass.getName())) {
            features = new Features(0);
        } else {
            features = featureTableMap.get(curClass.getName());
        }
        featureTableMap.put(curClass.getName(), features.appendElement(feature));
    }

    public void dumpFeatures() {
        for (AbstractSymbol sym : featureTableMap.keySet()) {
            for (Enumeration e = featureTableMap.get(sym).getElements(); e.hasMoreElements();) {
                System.out.print(sym.toString() + "   ");
                System.out.println(((Feature)e.nextElement()).getName());
            }
        }
    }

    public AbstractSymbol getParent(AbstractSymbol className) {
        if (classTableMap.containsKey(className)) {
            return classTableMap.get(className).parent;
        }
        return TreeConstants.Object_;
    }

    public Classes getAllClasses() {
        Classes classes = new Classes(0);
        for (AbstractSymbol className : classTableMap.keySet()) {
            classes.addElement(classTableMap.get(className));
        }
        return classes;
    }

    public AbstractSymbol getLub(AbstractSymbol typeA, AbstractSymbol typeB) {
        if (typeA.equals(typeB) || isSubtype(typeB, typeA)) { return typeA; }
        if (isSubtype(typeA, typeB)) { return typeB; }
        return getLub(getParent(typeA), getParent(typeB));
    }

    private void checkCycle() {
        Set <AbstractSymbol> visited = new HashSet<AbstractSymbol>();
        Set <AbstractSymbol> undefined = new HashSet<AbstractSymbol>();
        for (AbstractSymbol key : classTableMap.keySet()) {
            class_c curClass = classTableMap.get(key);
            visited.clear();
            visited.add(curClass.name);
            while (!TreeConstants.Object_.equals(curClass.name) &&
                   !TreeConstants.IO.equals(curClass.name)) {
                if (!classTableMap.containsKey(curClass.parent)) {
                    if (!undefined.contains(curClass.parent)) {
                        undefined.add(curClass.parent);
                        emitErrorInheritUndefinedClass(curClass);
                    }
                    break;
                } else if (visited.contains(curClass.parent)) {
                    emitErrorInheritanceCycle(curClass);
                    break;
                } else {
                    curClass = classTableMap.get(curClass.parent);
                    visited.add(curClass.name);
                }
            }     
        }
    }

    private void emitErrorInheritUndefinedClass(class_c curClass) {
        semantError(curClass).println("Class " + curClass.name.toString() + 
                                      " inherits from an undefined class " +
                                      curClass.parent.toString() + ".");
    }

    private void emitErrorInheritanceCycle(class_c curClass) {
        semantError(curClass).println("Class " + curClass.parent.toString() +
                                      ", or an ancestor of " + curClass.parent.toString() +
                                      ", is involved in an inheritance cycle.");
    }

    // create mapping between class name and class
    private Map<AbstractSymbol, class_c> checkClasses(Classes cls) {
        class_c curClass;
	for (Enumeration e = cls.getElements(); e.hasMoreElements();) {
            curClass = (class_c) e.nextElement();
            AbstractSymbol parent = curClass.parent;
            if (basicClassMap.containsKey(curClass.name)) {
                emitErrorRedefineBasicClass(curClass);
            }
            if (parent.equals(TreeConstants.Int) || 
                parent.equals(TreeConstants.Bool) ||
                parent.equals(TreeConstants.Str) ||
                parent.equals(TreeConstants.SELF_TYPE)) {
                emitErrorCannotInheritClass(curClass);
            }
            // missing default parent is Object. No need to check
            if (classTableMap.containsKey(curClass.name)) {
                emitErrorClassAlreadyDefined(curClass);
            }
            classTableMap.put(curClass.name, curClass);
        }
        if (!classTableMap.containsKey(TreeConstants.Main)) {
            emitErrorMainNotDefined();
        }
        return classTableMap;
    }

    private void emitErrorRedefineBasicClass(class_c curClass) {
        semantError(curClass).println("Redefinition of basic class " + curClass.name.toString() + ".");
    }

    private void emitErrorCannotInheritClass(class_c curClass) {
        semantError(curClass).println("Class " + curClass.name.toString() + " cannot inherit class " +
                                      curClass.parent.toString() + ".");
    }

    private void emitErrorClassAlreadyDefined(class_c curClass) {
        semantError(curClass).println("Class " + curClass.name.toString() + " was previously defined.");
    }

    private void emitErrorMainNotDefined() {
        semantError().println("Class Main is not defined.");
    }
    
    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(class_c c) {
	return semantError(c.getFilename(), c);
    }

    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
	errorStream.print(filename + ":" + t.getLineNumber() + ": ");
	return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError() {
	semantErrors++;
	return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
	return semantErrors != 0;
    }

    public class_c getClass(AbstractSymbol className) {  
        return classTableMap.get(className);
    }
}
			  
    
