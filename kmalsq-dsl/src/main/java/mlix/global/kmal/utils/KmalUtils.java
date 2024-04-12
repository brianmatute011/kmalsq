package mlix.global.kmal.utils;

import mlix.global.kmal.parser.KmalsqLexer;
import mlix.global.kmal.parser.KmalsqParser;
import mlix.global.kmal.parser.listener.KmalsqParserErrorListener;
import mlix.global.kmal.transpiler.KamalAST;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;

public class KmalUtils {
    public static Object parseQuery(String query) {
        KmalsqLexer lexer = new KmalsqLexer(CharStreams.fromString(query));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new KmalsqParserErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KmalsqParser parser = new KmalsqParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new KmalsqParserErrorListener());

        RuleContext tree = parser.simple_filter();
        KamalAST visitor = new  KamalAST();
        return visitor.visit(tree);
    }
}
