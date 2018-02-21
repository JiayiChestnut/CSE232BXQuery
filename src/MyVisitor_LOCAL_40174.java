import org.antlr.v4.runtime.tree.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.*;

/**
 * Created by fengjiang on 2/19/18.
 */
public class MyVisitor extends XQueryBaseVisitor{

    private List<Node> contextNodes;

    @Override
    public List<Node> visitAp(XQueryParser.ApContext ctx) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String fileName = ctx.fileName().getText().replace('\"', ' ').trim();
        List<Node> childNodes = new ArrayList<>();
        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            List<Node> prev = new ArrayList<>();

            Document doc = db.parse(fileName);
            prev.add(doc);

            //Visit children
            if (ctx.getChild(5).getText().equals("/")) {
                childNodes = getChildrenRecursive(prev);
            } else {
                childNodes = getChildren(prev);
            }
            contextNodes = childNodes;
            XQueryParser.RpContext rpContext = ctx.rp();
            this.visit(rpContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contextNodes;
    }

    // definition: [[@attName]]R(n) = attrib(n, attName)
    @Override
    public Object visitRp_attName(XQueryParser.Rp_attNameContext ctx) {
//        List<Node> next = new ArrayList<>();
//        String attrName = ctx.getText();
//        for (Node node : contextNodes) {
//            if (node.getNodeType() != Node.ELEMENT_NODE) {
//                continue;
//            }
//            Element element = (Element) node;
//            String attrContent = element.getAttribute(ctx.getText());
//            Node attrElem = createElement(attrName);
//            assert attrElem != null;
//            attrElem.setTextContent(attrContent);
//            next.add(attrElem);
//        }
//        contextNodes = next;
        return contextNodes;
    }

    // definition: [[tagName]]R(n) = < c | c ← [[∗]]R(n),tag(c) = tagName >
    @Override
    public List<Node> visitRp_tagNmae(XQueryParser.Rp_tagNmaeContext ctx) {
        List<Node> prev = contextNodes;
        List<Node> next = new ArrayList<>();
        for (Node node : prev) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equals(ctx.getText())) {
                    next.add(element);
                }
            }

        }
        contextNodes = next;
        return contextNodes;
    }

    // definition: [[..]]R(n) = parent(n)
    @Override
    public Object visitRp_parent(XQueryParser.Rp_parentContext ctx) {
        Set<Node> nextSet = new HashSet<>();
        for (Node node : contextNodes) {
            try {
                nextSet.add(node.getParentNode());
            } catch (Exception e) {
                nextSet.add(node.getOwnerDocument());
            }
        }
        contextNodes = new ArrayList<>(nextSet);
        return contextNodes;
    }

    // definition: [[(rp)]]R(n) = [[rp]]R(n)
    @Override
    public List<Node> visitRp_rp(XQueryParser.Rp_rpContext ctx) {
        contextNodes = this.visit(ctx.rp());
        return contextNodes;
    }

    // definition: [rp1/rp2]]R(n) = unique(< y | x ← [[rp1]]R(n), y ← [[rp2]]R(x) >)
    @Override
    public List<Node> visitRp_nextLevel(XQueryParser.Rp_nextLevelContext ctx) {
        List<Node> rp1 = this.visit(ctx.rp(0));
        List<Node> rp1ChildNodes = getChildren(rp1);
        contextNodes = rp1ChildNodes;
        this.visit(ctx.rp(1));
        return contextNodes;
    }

    // definition: [[rp1//rp2]]R(n) = unique([[rp1/rp2]]R(n), [[rp1/ ∗ //rp2]]R(n))
    @Override
    public Object visitRp_nextLevelRecursive(XQueryParser.Rp_nextLevelRecursiveContext ctx) {
        List<Node> rp1 = this.visit(ctx.rp(0));
        List<Node> rp1ChildNodes = getChildrenRecursive(rp1);
        contextNodes = rp1ChildNodes;
        this.visit(ctx.rp(1));
        return contextNodes;
    }

    // definition: [[∗]]R(n) = children(n)
    @Override
    public Object visitRp_children(XQueryParser.Rp_childrenContext ctx) {
        contextNodes = getChildren(contextNodes);
        return contextNodes;
    }

    // definition: [[.]]R(n) = < n >
    @Override
    public Object visitRp_current(XQueryParser.Rp_currentContext ctx) {
        Set<Node> nextSet = new HashSet<>();
        for (Node node : contextNodes)
            nextSet.add(node);
        contextNodes = new ArrayList<>(nextSet);
        return contextNodes;
    }

    // definition: [[text()]]R(n) = txt(n)
    @Override
    public Object visitRp_text(XQueryParser.Rp_textContext ctx) {
        List<Node> next = new ArrayList<>();
        for (Node node : contextNodes) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                next.add(node);
            }
        }
        contextNodes = next;
        return contextNodes;
    }

    // definition: [[rp1, rp2]]R(n) = [[rp1]]R(n), [[rp2]]R(n)
    @Override
    public Object visitRp_concat(XQueryParser.Rp_concatContext ctx) {
        // make a copy of the context nodes
        List<Node> prevContextNodes = new ArrayList<>();
        for (Node node : contextNodes)
            prevContextNodes.add(node);

        // get the nodes from rp1
        List<Node> rp1 = this.visit(ctx.rp(0));

        // switch to the original context nodes and get the nodes from rp2
        contextNodes = prevContextNodes;
        List<Node> rp2 = this.visit(ctx.rp(1));

        Set<Node> joinSet = new HashSet<>();
        joinSet.addAll(rp1);
        joinSet.addAll(rp2);
        contextNodes = new ArrayList<>(joinSet);
        return contextNodes;
    }

    @Override
    public Object visitRp_filter(XQueryParser.Rp_filterContext ctx) {
        return null;
    }

    @Override
    public Object visitF(XQueryParser.FContext ctx) {
        return null;
    }

    @Override
    public Object visitFileName(XQueryParser.FileNameContext ctx) {
        return null;
    }

    @Override
    public Object visitTagName(XQueryParser.TagNameContext ctx) {
        return null;
    }

    @Override
    public Object visitAttName(XQueryParser.AttNameContext ctx) {
        return null;
    }

    @Override
    public List<Node> visit(ParseTree tree) {
        return Helper.asListNode(super.visit(tree));
    }

    @Override
    public Object visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Object visitErrorNode(ErrorNode errorNode) {
        return null;
    }


    private List<Node> getChildren(List<Node> prev) {
        Set<Node> set4next = new HashSet<>();
        for (Node node : prev) {
            // only get the most immediate child
            Node childNode = node.getFirstChild();
            while (childNode != null) {
                set4next.add(childNode);
                childNode = childNode.getNextSibling();
            }
        }
        return new ArrayList<>(set4next);
    }

    private List<Node> getChildrenRecursive(List<Node> prev) {
        Set<Node> set4next = new HashSet<>();
        for (Node node : prev) {
            //First add its own children
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                set4next.add(children.item(i));
            }
            //Then add children's children.
            NodeList nodeList;
            if (node.getNodeType() == Node.DOCUMENT_NODE) {
                nodeList = ((Document) node).getElementsByTagName("*");
            } else {
                nodeList = ((Element) node).getElementsByTagName("*");
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList childrenList = nodeList.item(i).getChildNodes();
                for (int j = 0; j < childrenList.getLength(); j++) {
                    set4next.add(childrenList.item(j));
                }
            }
        }
        return new ArrayList<>(set4next);
    }
}
