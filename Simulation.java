import javax.swing.*;
import java.awt.FlowLayout;

import java.util.ArrayList;
import java.util.LinkedList;

public class Simulation  extends JFrame{

  //linked list used becasue blocks will only be added to the end
  public LinkedList<Node> nodes = new LinkedList<Node>();

  public Simulation(ArrayList<String[]> rawNodes) {

  //init
    //fill nodes linkedlist
    createNodeObjects(rawNodes);

    //1.create nodes
    //2.initialize nodes
    //3.start mine

    Node node = new Node(5,"name",500);
    //create node
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
  //convert rawNodes input into linkedlist of node object
  public void createNodeObjects(ArrayList<String[]> rawNodes) {
    for (String[] nodes : rawNodes) {
      addNode(Integer.parseInt(nodes[0]),nodes[1],Integer.parseInt(nodes[2]));
    }
  }

  public void addNode(int id, String name, int mineSpeed) {
    nodes.add(new Node(id, name, mineSpeed));
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
