import javax.swing.*;
import java.awt.FlowLayout;

import java.util.ArrayList;
import java.util.LinkedList;

public class Simulation  extends JFrame{

  //linked list used becasue blocks will only be added to the end
  public LinkedList<Node> nodesList = new LinkedList<Node>();

  public Simulation(LinkedList<Node> nodesList) {
  //init

    //1.create nodes
    //2.initialize nodes
    //3.start mine

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
  public int getNodesListSize(){
    return nodesList.size();
  }

  public void addNode(String id, String name, String mineSpeed) {
//methods
    nodesList.add(new Node(id, name, mineSpeed));
  }



  //state=true, start mine
  public void run(boolean state) {
    for (Node node : nodesList) {
      if (state) {
         node.mine(true);
      } else {
          node.mine(false);
      }
     }
   }
}
