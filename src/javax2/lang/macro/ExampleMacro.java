package javax2.lang.macro;

import com2.sun.tools.javac.parser.Parser;
import com2.sun.tools.javac.tree.JCTree;
import com2.sun.tools.javac.tree.TreeScanner;
import com2.sun.tools.javac.tree.JCTree.JCAnnotation;
import com2.sun.tools.javac.tree.JCTree.JCClassDecl;
import com2.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com2.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com2.sun.tools.javac.tree.JCTree.Visitor;
import com2.sun.tools.javac.util.List;


public class ExampleMacro extends TreeScanner implements Macro {

    


    private ParserFactory factory;
    private JCClassDecl currentClass;

    public void apply(ParserFactory factory, JCCompilationUnit tree) {
        this.factory = factory;
        visitTopLevel(tree);
    }

    @Override
    public void visitClassDef(JCClassDecl that) {
        this.currentClass = that;
        super.visitClassDef(that);
    }

    @Override
    public void visitVarDef(JCVariableDecl that) {
        if (containsAnno(that, "GetSet")) {
            addGetter(that);
            addSetter(that);
        } else if (containsAnno(that, "Get")) {
            addGetter(that);
        } else if (containsAnno(that, "Set")) {
            addSetter(that);
        }
        super.visitVarDef(that);
    }
    
    
    private boolean containsAnno(JCVariableDecl var, String annoName) {
        for (JCAnnotation anno: var.mods.annotations) {
            if (anno.annotationType.toString().equals(annoName)) {
                return true;
            }
        }
        return false;
    }
    
    
    private void addMember(String code) {
        Parser parser = factory.newParser(code);
        List<JCTree> list = parser.classOrInterfaceBodyDeclaration(null, false);
        currentClass.defs = currentClass.defs.appendList(list);
    }
    
    
    private void addGetter(JCVariableDecl var) {
        String name = var.name.toString();
        String code = 
            "public " + var.vartype+ " " + name + "() { " +
                "return " + name + ";" +
             "}";
        addMember(code);
    }
    
    
    private void addSetter(JCVariableDecl var) {
        String name = var.name.toString();
        String code = 
            "public void set_" + name + "(" + var.vartype + " " + name + ") {";
        
        if (containsAnno(var, "NonNull")) {
            code = code +
            "if (" + name + " == null) { " +
                "throw new IllegalArgumentException(\"" + name + 
                " may not be null.\");" +
             "}";
        }
            
        code = code +
               "this." + name + " = " + name + ";" +
            "}";
        addMember(code);
    }

}
