import javax.swing.*;
import java.awt.FlowLayout;

import java.util.LinkedList;

public class Simulation  extends JFrame{

  //linked list used becasue blocks will only be added to the end
  public LinkedList<Node> nodes = new LinkedList<Node>();

  public Simulation(LinkedList<Node> nodes) {

    this.nodes = nodes;

    //get data from Main Class

    //1.create nodes
    //2.initialize nodes
    //3.start mine

    //create node
    Node node = new Node("name",500);
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
  public int getNodesArraySize(){
    return nodes.size();
  }

//methods
  public void addNode(String name, int mineSpeed) {
    nodes.add(new Node(name, mineSpeed));
  }

  //state=true, start mine
  public void run(boolean state) {
    for (Node node : nodes) {
      if (state) {
         node.mine(true);
      } else {
          node.mine(false);
      }
     }
   }
}
