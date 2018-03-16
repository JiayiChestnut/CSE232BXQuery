import org.w3c.dom.*;

import java.util.ArrayList;

public class KeyWrapper{
    ArrayList<Node> keyNodes;

    KeyWrapper(){
        keyNodes = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof KeyWrapper)) {
            return false;
        }
        if (o == this) return true;
        KeyWrapper other = (KeyWrapper)o;
        if(keyNodes.size() != other.keyNodes.size()){
            return false;
        }

        for(int i = 0 , size = keyNodes.size() ; i < size ; i++){
            if(!keyNodes.get(i).getFirstChild().isEqualNode(other.keyNodes.get(i).getFirstChild())){
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode(){
        String totStr = "";
        for(Node node : keyNodes){
            totStr += node.getFirstChild().getTextContent();
        }
        return totStr.hashCode();
    }

}
