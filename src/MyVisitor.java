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
    public List<Node> visitRp_attName(XQueryParser.Rp_attNameContext ctx) {
        List<Node> next = new ArrayList<>();
        String attrName = ctx.getText();
        for (Node node : contextNodes) {
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) node;
            Attr attrNode = element.getAttributeNode(attrName);
            next.add(attrNode);
        }
        contextNodes = next;
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
    public List<Node> visitRp_parent(XQueryParser.Rp_parentContext ctx) {
        Set<Node> nextSet = new HashSet<>();
        for (Node node : contextNodes) {
            try {
                nextSet.add(node.getParentNode().getParentNode());
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
        contextNodes = Helper.asListNode(this.visit(ctx.rp()));
        return contextNodes;
    }

    // definition: [rp1/rp2]]R(n) = unique(< y | x ← [[rp1]]R(n), y ← [[rp2]]R(x) >)
    @Override
    public List<Node> visitRp_nextLevel(XQueryParser.Rp_nextLevelContext ctx) {
        List<Node> rp1 = Helper.asListNode(this.visit(ctx.rp(0)));
        List<Node> rp1ChildNodes = getChildren(rp1);
        contextNodes = rp1ChildNodes;
        this.visit(ctx.rp(1));
        return contextNodes;
    }

    // definition: [[rp1//rp2]]R(n) = unique([[rp1/rp2]]R(n), [[rp1/ ∗ //rp2]]R(n))
    @Override
    public List<Node> visitRp_nextLevelRecursive(XQueryParser.Rp_nextLevelRecursiveContext ctx) {
        List<Node> rp1 = Helper.asListNode(this.visit(ctx.rp(0)));
        List<Node> rp1ChildNodes = getChildrenRecursive(rp1);
        contextNodes = rp1ChildNodes;
        this.visit(ctx.rp(1));
        return contextNodes;
    }

    // definition: [[∗]]R(n) = children(n)
    @Override
    public List<Node> visitRp_children(XQueryParser.Rp_childrenContext ctx) {
        return contextNodes;
    }

    // definition: [[.]]R(n) = < n >
    @Override
    public List<Node> visitRp_current(XQueryParser.Rp_currentContext ctx) {
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

    // definition: [[text()]]R(n) = txt(n)
    @Override
    public List<Node> visitRp_text(XQueryParser.Rp_textContext ctx) {
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
    public List<Node> visitRp_concat(XQueryParser.Rp_concatContext ctx) {
        // make a copy of the context nodes
        List<Node> prevContextNodes = new ArrayList<>();
        for (Node node : contextNodes)
            prevContextNodes.add(node);

        // get the nodes from rp1
        List<Node> rp1 = Helper.asListNode(this.visit(ctx.rp(0)));

        // switch to the original context nodes and get the nodes from rp2
        contextNodes = prevContextNodes;
        List<Node> rp2 = Helper.asListNode(this.visit(ctx.rp(1)));

        Set<Node> joinSet = new HashSet<>();
        joinSet.addAll(rp1);
        joinSet.addAll(rp2);
        contextNodes = new ArrayList<>(joinSet);
        return contextNodes;
    }

    // definition: [[rp[f]]]R(n) = < x | x ← [[rp]]R(n), [[f]]F (x) >
    @Override
    public List<Node> visitRp_filter(XQueryParser.Rp_filterContext ctx) {
        List<Node> rpNodes = Helper.asListNode(this.visit(ctx.rp()));
        List<Node> next = new ArrayList<>();
        for (Node node : rpNodes) {
            List<Node> newContextNodes = new ArrayList<Node>();
            newContextNodes.add(node);
            contextNodes = newContextNodes;
            if (Helper.asBoolean(this.visit(ctx.f()))) {
                next.add(node);
            }
        }
        contextNodes = next;
        return contextNodes;
    }

    @Override
    public Object visitF(XQueryParser.FContext ctx) {
        if (ctx.getChildCount() == 1) {
            return this.visitF_rp(ctx);
        } else if (ctx.getChildCount() == 2) {
            return this.visitF_not(ctx);
        } else if (ctx.getChild(1).getText().equals("=") || ctx.getChild(1).getText().equals("eq")) {
            return this.visitF_eq(ctx);
        } else if (ctx.getChild(1).getText().equals("==") || ctx.getChild(1).getText().equals("is")) {
            return this.visitF_is(ctx);
        } else if (ctx.getChild(1).getText().equals("and")) {
            return this.visitF_and(ctx);
        } else if (ctx.getChild(1).getText().equals("or")) {
            return this.visitF_or(ctx);
        } else if (ctx.getChild(0).getText().equals("(") && ctx.getChild(2).getText().equals(")")) {
            return this.visitF_f(ctx);
        } else
            return null;
    }

    private Object visitF_rp(XQueryParser.FContext ctx) {
        XQueryParser.RpContext rp = ctx.rp(0);
        List<Node> rpNodes =  Helper.asListNode(this.visit(rp));
        return rpNodes.size() != 0;
    }

    private Object visitF_not(XQueryParser.FContext ctx) {
        return !((boolean) this.visit(ctx.getChild(1)));
    }

    private Object visitF_eq(XQueryParser.FContext ctx) {
        List<Node> l1 = (List<Node>) this.visit(ctx.getChild(0));
        List<Node> l2 = (List<Node>) this.visit(ctx.getChild(2));
        for (Node n1: l1)
            for (Node n2: l2) {
                if (n1.isEqualNode(n2))
                    return true;
            }
        return false;
    }

    private Object visitF_is(XQueryParser.FContext ctx) {
        List<Node> l1 = (List<Node>) this.visit(ctx.getChild(0));
        List<Node> l2 = (List<Node>) this.visit(ctx.getChild(2));
        for (Node n1: l1)
            for (Node n2: l2) {
                if (n1 == n2)
                    return true;
            }
        return false;
    }

    private Object visitF_f(XQueryParser.FContext ctx) {
        return this.visit(ctx.getChild(1));
    }

    private Object visitF_and(XQueryParser.FContext ctx) {
        return (boolean) this.visit(ctx.getChild(0)) && (boolean) this.visit(ctx.getChild(2));
    }

    private Object visitF_or(XQueryParser.FContext ctx) {
        return (boolean) this.visit(ctx.getChild(0)) || (boolean) this.visit(ctx.getChild(2));
    }

    @Override
    public Object visitFileName(XQueryParser.FileNameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitTagName(XQueryParser.TagNameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitAttName(XQueryParser.AttNameContext ctx) {
        return ctx.getText();
    }

    @Override

    public Object visit(ParseTree tree) {
        return super.visit(tree);
    }

    @Override
    public Object visitChildren(RuleNode ruleNode) {
        return super.visitChildren(ruleNode);
    }

    @Override
    public Object visitTerminal(TerminalNode terminalNode) {
        return super.visitTerminal(terminalNode);
    }

    @Override
    public Object visitErrorNode(ErrorNode errorNode) {
        return super.visitErrorNode(errorNode);
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
