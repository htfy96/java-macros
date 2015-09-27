An enhancement to the JDK7 compiler (the GPL'd version) that adds support for compile-time macros.  Macros are Java classes that are instantiated and executed at compile-time.  The macros take in a source file's parse tree and a "ParserFactory" that can be used to parse dynamically generated code.

The prototype compiler is fully functional and include a simple proof-of-concept macro.  This proof-of-concept macro takes a Java class whose fields have been annotated in a special way and automatically produces getter and setter methods for those annotated fields.

Compile-time macros can be used to implement a variety of additional language features with no further modifications to the compiler.  Notable examples include C++-like templates, aspect-oriented programming and closures.  Or non-Java languages like Python can be embedded directly in a Java source code comment, and converted to a parse tree at the appropriate time.

This feature was trivial to add.  JavaCompiler.parse() was modified to scan the import statements for macros and then execute them on the recently parsed tree.  However, this naive implementation, while a good prototype, has several problems.

In the prototype presented here, macros are included in a Java source code program by simply imported the macro class.  The compiler scans the parsed source file for import statements, tries to instantiate each imported class, and if the instantiated class implements the Macro interface, it is constructed using a no-argument constructor and executed.

This works but is inefficient.  I propose a change to the Java Language Specification, that would re-use the "volatile" keyword for macro declarations.  Similar to "import static" for procedural programming, "import volatile" would be used to execute a compile-time macro.  This language change is not implemented in the prototype compiler, but it should be trivial to adapt the parser.

Macros currently use the API in com.sun.tools.javac.tree to represent the parse tree.  This is suboptimal.  The classes in javax.lang.model would be better, but they need to be modified to support full alterations of a parse tree.

Also, failure modes are not robust in the prototype compiler.  It is unclear what should happen when a macro throws an exception.  Obviously the compiler should generate a parse error for the class being altered by the macro, but the compiler does not yet have the ability to report a stack trace as part of a parse error.

Finally, javadoc was not altered for the prototype.  Ideally javadoc would also execute compile-time macros, so that automatically generated members could have full documentation.

As macros exist solely at compile-time, I do not believe this language feature requires any changes to the reflection APIs.

I have rationales.  Java is rapidly losing mindshare to dynamic languages like Ruby and Python.  Adding compile-time macros can automate many repetitive development tasks while retaining the static safety of Java.