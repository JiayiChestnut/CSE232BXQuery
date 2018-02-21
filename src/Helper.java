import com.sun.org.apache.xpath.internal.operations.Bool;
import org.w3c.dom.Node;

import java.util.*;

/**
 * Created by fengjiang on 2/20/18.
 */
public class Helper {
    public static List<Node> asListNode(Object obj){
        if(isListNode(obj)){
            return (List<Node>) obj;
        }else if(isNode(obj)){
            List<Node> result = new ArrayList<>();
            result.add((Node)obj);
            return result;
        }else{
            return new ArrayList<>();
        }
    }

    public static boolean isListNode(Object obj) { return obj instanceof List; }
    public static boolean isNode(Object obj) { return obj instanceof Node; }


    public static Boolean asBoolean(Object obj) { return (Boolean)obj; }
}
