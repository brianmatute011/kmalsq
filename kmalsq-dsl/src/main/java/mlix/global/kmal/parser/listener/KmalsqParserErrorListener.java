package mlix.global.kmal.parser.listener;

import mlix.global.kmal.exception.KmalsqException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class KmalsqParserErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
//        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        throw new KmalsqException("Error inline " + line + ":" + charPositionInLine + " " + msg);
    }
}
