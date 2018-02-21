import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by fengjiang on 2/19/18.
 */
public class MyVisitor extends XQueryBaseVisitor{
    @Override
    public Object visitAp(XQueryParser.ApContext ctx) {
        return null;
    }

    @Override
    public Object visitRp(XQueryParser.RpContext ctx) {
        return null;
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
        return ((List<Node>) this.visit(ctx.getChild(0))).size() != 0;
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
    public Object visit(ParseTree parseTree) {
        return super.visit(parseTree);
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
}
