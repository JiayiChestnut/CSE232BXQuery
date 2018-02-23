import org.antlr.v4.runtime.*;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(
"for $a in doc(\"j_caesar.xml\")//PERSONAE, $b in $a/PERSONA \n" +
        "where not (($b/text() = \"JULIUS CAESAR\") or ($b/text() = \"Another Poet\") )\n" +
        "return $b"
        );
        XQueryLexer lexer = new XQueryLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(commonTokenStream);
        XQueryParser.QueryContext qContext = parser.query();


        System.out.println(inputStream);
//        System.out.println(apContext.fileName().getText());
//        XQueryParser.RpContext rpContext = apContext.rp();
//        System.out.println(rpContext.getText());
//        System.out.println(rpContext.getParent().getChildCount());

        MyVisitor visitor = new MyVisitor();
        List<Node> results = Helper.asListNode(visitor.visit(qContext));
        for (Node res : results)
            System.out.println(res.getTextContent());

    }

}