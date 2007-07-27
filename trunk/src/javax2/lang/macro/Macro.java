package javax2.lang.macro;

import com2.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public interface Macro {

    
    void apply(ParserFactory factory, JCCompilationUnit tree);

}
