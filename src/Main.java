import org.antlr.v4.runtime.*;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(
"some $a in doc(\"j_caesar.xml\")//PERSONAE, $b in $a/PERSONA satisfies (($b/text() = \"JULIUS CAESAR\") or ($b/text() = \"Another Poet\") )"
        );
        XQueryLexer lexer = new XQueryLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(commonTokenStream);
        XQueryParser.CondContext qContext = parser.cond();


        System.out.println(inputStream);
//        System.out.println(apContext.fileName().getText());
//        XQueryParser.RpContext rpContext = apContext.rp();
//        System.out.println(rpContext.getText());
//        System.out.println(rpContext.getParent().getChildCount());

        MyVisitor visitor = new MyVisitor();
        System.out.println((boolean)visitor.visit(qContext));
//        List<Node> results = Helper.asListNode(visitor.visit(qContext));
//        for (Node res : results)
//            System.out.println(res.getTextContent());

    }

}