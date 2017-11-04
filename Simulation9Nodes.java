import javax.swing.*;
import java.awt.event.*;

import java.awt.GridBagLayout;
import java.util.LinkedList;


public class Simulation9Nodes extends Simulation {

    public Simulation9Nodes(LinkedList<Node> nodesList) {
      super(nodesList);
      System.out.println("sim 9 selected");
    }

    public void constructNodesPanel(JPanel nodesPanel) {
      System.out.print("got to construct overwritten nodes panel");
    }

}
