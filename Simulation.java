import java.util.LinkedList;
import java.util.ArrayList;
public class Simulation {

  //linked list used becasue blocks will only be added to the end
  public static LinkedList<Block> chain = new LinkedList<Block>();
  public static ArrayList<Node> nodes = new ArrayList<Node>();

  public Simulation() {
    //get data from UI fields

    //1.create nodes
    //2.initialize nodes
    //3.start mine

    //initial block
    chain.add(new Block("1837372986"));
    int counter = 0;
    for (Integer  i = 0; i < 2000; i++) {
      double temp = Math.random()*100;
      temp = (int) temp;
      if(temp == 0) {
        counter++;
      }
    }
    System.out.println(counter);
  }


  public static int getChainSize(){
    return chain.size();
  }

  public static int getNodesArraySize(){
    return nodes.size();
  }

  public static void addNode(float mineSpeed) {
    nodes.add(new Node(mineSpeed));
  }

  //state=true, start mine
  public static void mine(boolean state) {
    for (Node node : nodes) {
      if (state) {
         node.mineStart();
      } else {
          node.mineStop();
      }
     }
   }
}
