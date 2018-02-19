import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

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
