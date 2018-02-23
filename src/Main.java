import org.antlr.v4.runtime.*;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(
          "empty(doc(\"j_caesar.xml\")//KLBJLHB, doc(\"j_caesar.xml\")//KLBJLHB)"
        );
        XQueryLexer lexer = new XQueryLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(commonTokenStream);
        XQueryParser.CondContext cContext = parser.cond();


        System.out.println(inputStream);
//        System.out.println(apContext.fileName().getText());
//        XQueryParser.RpContext rpContext = apContext.rp();
//        System.out.println(rpContext.getText());
//        System.out.println(rpContext.getParent().getChildCount());

        MyVisitor visitor = new MyVisitor();
        System.out.println((boolean)visitor.visit(cContext));
//        List<Node> results = Helper.asListNode(visitor.visit(cContext));
//        for (Node node : results)
//            System.out.println(node.getTextContent());

    }

}