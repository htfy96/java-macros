package javax2.lang.macro;

import com2.sun.tools.javac.parser.Parser;
import com2.sun.tools.javac.parser.Scanner;

public class ParserFactory {

    final private Parser.Factory parserFactory;
    final private Scanner.Factory lexerFactory;
    
    public ParserFactory(Parser.Factory parserFactory, Scanner.Factory lexerFactory) {
        this.parserFactory = parserFactory;
        this.lexerFactory = lexerFactory;
    }
    
    
    public Parser newParser(CharSequence code) {
        Scanner scanner = lexerFactory.newScanner(code);
        return parserFactory.newParser(scanner, false, false);
    }
    
    
}
