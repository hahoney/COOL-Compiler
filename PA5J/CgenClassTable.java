/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;

import java.util.*;


/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;
    private int objclasstag;
    private int ioclasstag;

    private Map<AbstractSymbol, Integer> mapClassToTag = new HashMap<AbstractSymbol, Integer>();
    private Map<Integer, CgenNode> mapTagToClass = new HashMap<Integer, CgenNode>();
    
    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }

    /** Create the Prototype objects
    **/
    private void codeClassTable() {
        codeClassNameTable();
        codeClassObjectTable();
        codeClassDispatchTables();
        codeClassObjectProt();
    }

    private void codeClassNameTable() {
        str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
        for (Object c : nds) {
            StringSymbol sym = (StringSymbol) AbstractTable.stringtable.lookup(((CgenNode)c).getName().toString());
            if (sym != null) {
                str.print(CgenSupport.WORD); 
                sym.codeRef(str); 
                str.println(""); 
            }          
        }
    }

    private void codeClassObjectTable() {
        str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
        for (Object c : nds) {
            str.print(CgenSupport.WORD);
            CgenSupport.emitProtObjRef(((CgenNode)c).getName(), str);
            str.println("");
            str.print(CgenSupport.WORD);
            CgenSupport.emitInitRef(((CgenNode)c).getName(), str);
            str.println("");
        }
    }

    private void codeClassDispatchTables() { 
        for (Object c : nds) {
            CgenNode node = (CgenNode) c;
            codeClassDispatchTable(node);
        }
    }

    // Very dirty implementation, will improve later
    // class A has f
    // class B inh A has B.g (A.f)
    // class C inh B has C.w, C.g, A.f
    private void codeClassDispatchTable(CgenNode c) {
        Map<AbstractSymbol, AbstractSymbol> methodMap = new HashMap<AbstractSymbol, AbstractSymbol>();
        List<CgenNode> classList = new ArrayList<CgenNode>();
 
        CgenNode pt = c;
        CgenSupport.emitDispTableRef(pt.getName(), str);
        str.print(CgenSupport.LABEL);
        Stack<CgenNode> classStack = new Stack<CgenNode>();            
        while (!TreeConstants.No_class.equals(pt.getName())) {
            classStack.push(pt);
            pt = pt.getParentNd();
        }
        // top down if feature already in map (upper class has it), overwrite it.
        // if A <= B (map.get(method) = A)
        // map.get(method) for B is B. If A > B, map.get(method) for B is A 
        while (!classStack.isEmpty()) {
            pt = classStack.pop(); 
            classList.add(pt);       
            for (Enumeration e = pt.getFeatures().getElements(); e.hasMoreElements();) {
                Feature feature = (Feature) e.nextElement();
                if (feature instanceof method) {
                    methodMap.put(feature.getName(), pt.getName());
                }
            }   
        }
        for (CgenNode node : classList) {
            for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements();) {
                Feature feature = (Feature) e.nextElement();
                if (feature instanceof method && methodMap.containsKey(feature.getName())) {
                    AbstractSymbol lastOverrideClassName = methodMap.get(feature.getName());  
                    AbstractSymbol className = isSubtype(node.getName(), lastOverrideClassName) ? node.getName() : lastOverrideClassName;
                    str.print(CgenSupport.WORD);
                    CgenSupport.emitMethodRef(className, feature.getName(), str);
                    methodMap.remove(feature.getName());
                    str.println("");
                }
            } 
        }  
 
    }

    private void codeClassObjectProt() {
        for (Object c : nds) {
            CgenNode pt = (CgenNode) c;
            // Eye catcher
            str.println(CgenSupport.WORD + "-1");
            // class label
            CgenSupport.emitProtObjRef(pt.getName(), str);
            str.print(CgenSupport.LABEL);
            // class tag
            int classTagNumber = mapClassToTag.get(pt.getName());
            str.println(CgenSupport.WORD + classTagNumber);

            int attrNumber = 0;
            CgenNode inheritPt = pt;
            List<AbstractSymbol> attrList = new ArrayList<AbstractSymbol>();
            while (!TreeConstants.Object_.equals(inheritPt.getName())) {
                for (Enumeration e = inheritPt.getFeatures().getElements(); e.hasMoreElements();) {
                    Feature feature = (Feature) e.nextElement();
                    if (feature instanceof attr) {
                        attrNumber++;
                        attrList.add(feature.getType());
                    }
                }
                inheritPt = inheritPt.getParentNd();
            }
            str.print(CgenSupport.WORD);
            str.println(CgenSupport.DEFAULT_OBJFIELDS + attrNumber);

            // dispatch table
            str.print(CgenSupport.WORD);
            CgenSupport.emitDispTableRef(pt.getName(), str);
            str.println("");

            // attr list
            int attrOffset = CgenSupport.DEFAULT_OBJFIELDS;
            for (AbstractSymbol attr : attrList) {
                str.print(CgenSupport.WORD);
                if (TreeConstants.Str.equals(attr)) {
                    StringSymbol strSymbol = (StringSymbol) AbstractTable.stringtable.lookup("");
                    strSymbol.codeRef(str);
                } else if (TreeConstants.Int.equals(attr)) {
                    IntSymbol intSymbol = (IntSymbol) AbstractTable.inttable.lookup("0");
                    intSymbol.codeRef(str);
                } else if (TreeConstants.Bool.equals(attr)){
                    new BoolConst(false).codeRef(str);
                } else {
                    str.print(0);
                }
                str.println("");
            }
        }
    }

    private void codeClassObjectInit() {
        for (Object c : nds) {
            CgenNode node = (CgenNode) c;
            int tempVarNumber = 0;
            for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements();) {
                tempVarNumber = CgenSupport.max(tempVarNumber, ((Feature) e.nextElement()).getTempNumber());
            }
            CgenSupport.emitInitRef(node.getName(), str);
            str.print(CgenSupport.LABEL);
            CgenSupport.emitEnterFunc(tempVarNumber, str);
            if (!TreeConstants.Object_.equals(node.getName())) {
                str.print(CgenSupport.JAL);
                CgenSupport.emitInitRef(node.getParentNd().getName(), str);
                str.println("");
            }
            for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements();) {
                Feature feature = (Feature) e.nextElement();
                if (feature instanceof attr) {
                    addId(feature.getName(), lookup(feature.getType()));
                    ((attr) feature).code(node, this, str);
                }
            }
            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
            CgenSupport.emitExitFunc(0, tempVarNumber, str);
        }
    }

    private void codeClassMethod() {
        List<CgenNode> list = new ArrayList<CgenNode>(nds);
        for (Object c : list) {
            CgenNode node = (CgenNode) c;
            if (!node.basic()) {
                for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements();) {
                    Feature feature = (Feature) e.nextElement();
                    if (feature instanceof method) {
                        ((method) feature).code(node, this, str);
                    }
                }
            }
        }   
    }

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class = 
	    new class_(0, 
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

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class = 
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
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

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class = 
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class = 
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
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
						  .appendElement(new formal(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	stringclasstag = 4 /* Change to your String class tag here */;
	intclasstag =    2 /* Change to your Int class tag here */;
	boolclasstag =   3 /* Change to your Bool class tag here */;
        objclasstag = 0;
        ioclasstag = 1;
        
 
	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
        setClassTags(root());
        // I don't know how type_name() works. when I reorganize the tagnumbers of all types,
        // the returned type name strings may change. As a result, I keep the basic type tag fixed.

	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	//                 Add your code to emit
	//                   - prototype objects
	//                   - class_nameTab
	//                   - dispatch tables
        if (Flags.cgen_debug) System.out.println("zy: coding protobj, class_nameTab, dispatch tables");
        codeClassTable();


	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	//                   - the class methods
	//                   - etc...
        if (Flags.cgen_debug) System.out.println("coding initialization");
        codeClassObjectInit();
     
        if (Flags.cgen_debug) System.out.println("coding methods");
        codeClassMethod();
    }

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)lookup(TreeConstants.Object_);
    }

    /* Need some utilities to find features as I wrote in the type checking */
    // get feature from curClass or its nearest ancestor
    // The class names are installed in two ways. One is to nds for CgenNode only
    // Two is to the symbolTable as (name, nd) name as AbstractSymbol and nd as CgenNode
 
    // find right method bottom to up find index top to bottom. find the index of last occurance
    public int getFeatureOffset(AbstractSymbol featureName, AbstractSymbol className, boolean isMethod) {
        int featureCount = -1;
        int lastOccurance = -1;
        Set<AbstractSymbol> methodSet = new HashSet<AbstractSymbol>();
        if (lookup(className) != null) { 
            List<CgenNode> classAncestors = reverseNds(getInheritance(className));
            for (CgenNode node : classAncestors) {
                Features features = node.getFeatures();
                if (features != null) {
                    for (Enumeration e = features.getElements(); e.hasMoreElements();) {                      
                        Feature feature = (Feature) e.nextElement();
                        if ((feature instanceof method && isMethod && !methodSet.contains(feature.getName())) ||
                            (feature instanceof attr && !isMethod)) {
                            if (isMethod) { methodSet.add(feature.getName()); }
                            featureCount++;     
                            if (feature.getName().equals(featureName)) {
                                lastOccurance = featureCount;
                            }                    
                        }  
                    }
                }
            }
        }
        return lastOccurance;
    }

    public int getAttrNumber(AbstractSymbol className) {
        int result = 0;
        if (lookup(className) != null) {
            CgenNode node = (CgenNode) lookup(className);
            for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements();) {
                Feature feature = (Feature) e.nextElement();
                if (feature instanceof attr) {
                    result++;
                }
            }
        }
        return result;
    }

   
    // Get all ancestor nodes from top to bottom
    private List<CgenNode> getInheritance(AbstractSymbol className) {
        List<CgenNode> result = new ArrayList<CgenNode>();
        CgenNode pt = (CgenNode) lookup(className);
        if (pt == null) { return result; }    
        while (!TreeConstants.No_class.equals(pt.getName())) {
            result.add(pt);
            pt = pt.getParentNd();
        }
        return result;
    }

    private List<CgenNode> reverseNds(List<CgenNode> nds) {
        Stack<CgenNode> stack = new Stack<CgenNode>();
        for (CgenNode e : nds) {
            stack.push(e);
        }
        List<CgenNode> result = new ArrayList<CgenNode>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;      
    }

    public boolean isSubtype(AbstractSymbol sub, AbstractSymbol ancestor) {
        if (TreeConstants.SELF_TYPE.equals(sub) || TreeConstants.SELF_TYPE.equals(ancestor)) {
            return sub.equals(ancestor);
        }

        if (ancestor.equals(sub) || ancestor.equals(TreeConstants.Object_)) { return true; }

        while (!sub.equals(TreeConstants.Object_)) {
            if (sub.equals(ancestor)) { return true; }
            else { sub = ((CgenNode)lookup(sub)).getParentNd().getName(); }
        }
        return false;
    }

    public int getTypeTag(AbstractSymbol classType) {
        Integer tagNumber = classToTag(classType);
        if (tagNumber == null) { return -1; }
        return tagNumber.intValue();
    }   

    public AbstractSymbol getTypeClass(int classTag) {
        CgenNode classNode = tagToClass(classTag);
        if (classNode == null) { return null; }
        return classNode.getName();
    }

    public AbstractSymbol getLowestSubtype(AbstractSymbol curType) {
        assert(curType != null);
        int maxSubtypeTag = -1;
        AbstractSymbol result = null;
        for (Object e : nds) {
            AbstractSymbol nodeType = ((CgenNode) e).getName();
            if (isSubtype(nodeType, curType)) {
                int curTag = getTypeTag(nodeType);
                if (curTag > maxSubtypeTag) {
                    maxSubtypeTag = curTag;
                    result = nodeType;
                }
            }
        }
        return result;
    }

    public int getClassTableSize() {
        return nds.size();
    }

    private CgenNode tagToClass(int tag) {
        return mapTagToClass.get(new Integer(tag));
    }

    private Integer classToTag(AbstractSymbol classType) {
        return mapClassToTag.get(classType);
    }

    private void setClassTags(CgenNode node) {
        int tagNumber = -1; 
        if (!node.basic()) {
            if (node.getName().equals(TreeConstants.Main)) {
                tagNumber = nds.size() - 1; 
            } else {
                tagNumber = CgenSupport.getTagNumber();
            }
            mapClassToTag.put(node.getName(), tagNumber);
            mapTagToClass.put(tagNumber, node);
        } else {
            setBasicTags(node);
        }
        for (Enumeration e = node.getChildren(); e.hasMoreElements();) {
            CgenNode childNode = (CgenNode) e.nextElement();
            setClassTags(childNode);
        }
    }

    private void setBasicTags(CgenNode node) {
        AbstractSymbol type = node.getName();
        int tagNumber = -1;
        if (TreeConstants.Object_.equals(type)) {
            tagNumber = objclasstag;
        } else if (TreeConstants.IO.equals(type)) {
            tagNumber = ioclasstag;
        } else if (TreeConstants.Int.equals(type)) {
            tagNumber = intclasstag;
        } else if (TreeConstants.Bool.equals(type)) {
            tagNumber = boolclasstag;
        } else if (TreeConstants.Str.equals(type)) {
            tagNumber = stringclasstag;
        }
        mapClassToTag.put(type, tagNumber);
        mapTagToClass.put(tagNumber, node); 
    }

}
			  
    
