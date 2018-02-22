import org.antlr.v4.runtime.*;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(
          "doc(\"j_caesar.xml\")//ACT[(./TITLE)==(./TITLE)]/*/SPEECH/../TITLE"
        );
        XQueryLexer lexer = new XQueryLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(commonTokenStream);
        XQueryParser.ApContext apContext = parser.ap();


//        System.out.println(inputStream);
//        System.out.println(apContext.fileName().getText());
//        XQueryParser.RpContext rpContext = apContext.rp();
//        System.out.println(rpContext.getText());
//        System.out.println(rpContext.getParent().getChildCount());

        MyVisitor visitor = new MyVisitor();
        List<Node> results = Helper.asListNode(visitor.visit(apContext));
        for (Node node : results)
            System.out.println(node.getTextContent());

    }

}