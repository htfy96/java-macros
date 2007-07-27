package javax2.lang.macro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com2.sun.tools.javac.tree.JCTree;
import com2.sun.tools.javac.tree.JCTree.JCClassDecl;
import com2.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com2.sun.tools.javac.tree.JCTree.JCImport;
import com2.sun.tools.javac.tree.JCTree.Visitor;

public class MacroFinder {


    private MacroFinder() {
        
    }

    
    public static List<Macro> extractMacros(JCCompilationUnit unit) {
        List<JCTree> newDefs = new ArrayList<JCTree>();
        List<Macro> macros = new ArrayList<Macro>();
        Iterator<JCTree> iter = unit.defs.iterator();
        while (iter.hasNext()) {
            JCTree tree = iter.next();
            Macro m = extractMacro(tree);
            if (m == null) {
                newDefs.add(tree);
            } else {
                macros.add(m);
            }
        }
        unit.defs = 
        com2.sun.tools.javac.util.List.from(newDefs.toArray(new JCTree[0]));
        
        return macros;
    }
    
    
    private static Macro extractMacro(JCTree tree) {
        if (!(tree instanceof JCImport)) {
            return null;
        }
        JCImport jci = (JCImport)tree;
        if (!jci.isStatic()) {
            try {
                Class<?> c = Class.forName(jci.qualid.toString());
                if (Macro.class.isAssignableFrom(c)) {
                    Macro macro = c.asSubclass(Macro.class).newInstance();
                    return macro;
                }
            } catch (ClassNotFoundException e) {
                // Not an actual problem; class might not be compiled yet,
                // which means it's not a macro
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return null;
    }


    
}
