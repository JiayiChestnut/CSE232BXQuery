import org.antlr.v4.runtime.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        String fileIn = "xqueryRewrite.txt";
        String fileOut = "results.xml";
        ANTLRFileStream streamIn;
        XQueryLexer lexer;
        CommonTokenStream commonTokenStream;
        XQueryParser parser;
        XQueryParser.QueryContext qContext;

        if (true) {
            fileIn = "xquery.txt";
            fileOut = "xqueryRewrite.txt";
            streamIn = new ANTLRFileStream(fileIn);
            lexer = new XQueryLexer(streamIn);
            commonTokenStream = new CommonTokenStream(lexer);
            parser = new XQueryParser(commonTokenStream);
            qContext = parser.query();

            System.out.println("Input:");
            System.out.println(streamIn);
            System.out.println("Ouput:");

            QueryRewriter rewriter = new QueryRewriter();
            String res = rewriter.rewrite(qContext.xq());
            System.out.print(res);

            FileWriter fw = new FileWriter(fileOut);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(res);
            bw.close();

            fileIn = fileOut;
            fileOut = "results.xml";
        }

        streamIn = new ANTLRFileStream(fileIn);
        lexer = new XQueryLexer(streamIn);
        commonTokenStream = new CommonTokenStream(lexer);
        parser = new XQueryParser(commonTokenStream);
        qContext = parser.query();

        MyVisitor visitor = new MyVisitor();
        List<Node> results = Helper.asListNode(visitor.visit(qContext));

        for (Node node: results)
            System.out.println(node.getTextContent());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            Document doc = dbf.newDocumentBuilder().newDocument();
            Node root = null;
            if (results.size() != 1) {
                root = doc.createElement("result");
            }
            for (Node node: results) {
                Node temp = doc.importNode(node, true);
                if (results.size() == 1) {
                    doc.appendChild(temp);
                } else {
                    root.appendChild(temp);
                }
            }
            if (results.size() != 1)
                doc.appendChild(root);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.METHOD, "xml");
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            trans.transform(new DOMSource(doc), new StreamResult(fileOut));
            System.out.println("Success.");
        } catch (ParserConfigurationException e) {
            System.err.println("Parser Config Exception in Main.");
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            System.err.println("Transformer config Exception in Main.");
            e.printStackTrace();
        } catch (TransformerException e) {
            System.err.println("Transformer Exception in Main.");
            e.printStackTrace();
        }
    }

}
