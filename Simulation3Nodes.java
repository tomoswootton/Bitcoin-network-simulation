import javax.swing.*;
import java.awt.event.*;

import java.awt.GridBagLayout;
import java.util.LinkedList;


public class Simulation3Nodes extends Simulation {

    public Simulation3Nodes(LinkedList<Node> nodesList) {
      System.out.print("sim 3 selected");
    }

    public void makeNodesPanel(JPanel nodesPanel) {
      System.out.print("got to construct overwritten nodes panel");
    }

}
