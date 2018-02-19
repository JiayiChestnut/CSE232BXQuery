import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by fengjiang on 2/19/18.
 */
public class MyVisitor implements XQueryVisitor{
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
        return null;
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
}
