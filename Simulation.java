import javax.swing.*;
import java.awt.FlowLayout;

import java.util.LinkedList;

public class Simulation  extends JFrame{

  //linked list used becasue blocks will only be added to the end
  public static LinkedList<Node> nodes = new LinkedList<Node>();

  public static void main(String[] args) {
    new Simulation();
  }

  public Simulation() {



    //get data from UI fields

    //1.create nodes
    //2.initialize nodes
    //3.start mine

    //create node
    Node node = new Node(500);
    this.nodes.add(node);
    node.mine(true);

    // //create new window
    // JPanel page = new JPanel();
    // page.setLayout(new FlowLayout());
    //
    //
    //
    //
    // this.add(page);
    // this.setVisible(true);
  }

//getters and setters
  public static int getNodesArraySize(){
    return nodes.size();
  }

//methods
  public static void addNode(int mineSpeed) {
    nodes.add(new Node(mineSpeed));
  }

  //state=true, start mine
  public static void run(boolean state) {
    for (Node node : nodes) {
      if (state) {
         node.mine(true);
      } else {
          node.mine(false);
      }
     }
   }
}
