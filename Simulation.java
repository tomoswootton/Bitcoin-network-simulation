import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.awt.GridBagLayout;
import java.util.LinkedList;


public abstract class Simulation {

  JFrame simulation = new JFrame();

  ListenForButton lForButton = new ListenForButton();

  JButton pauseButton;
  JButton exitButton;

  //linked list used becasue blocks will only be added to the end
   LinkedList<Node> nodesList;

  public Simulation(LinkedList<Node> nodesList) {

    this.nodesList = nodesList;
    //init
    simulation.setSize(1000,800);

    simulation.setLocationRelativeTo(null);
    simulation.setResizable(false);
    simulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    simulation.setTitle("Simulation");


    makePage();

    //1.create nodes
    //2.initialize nodes
    //3.start mine

  }

//getters and setters
  public int getNodesListSize(){
    return nodesList.size();
  }


//methods
  public void addNode(String id, String name, String mineSpeed) {
    nodesList.add(new Node(id, name, mineSpeed));
  }

  //state=true, start mine
  // public void run(boolean state) {
  //   for (Node node : nodesList) {
  //     if (state) {
  //        node.mine(true);
  //     } else {
  //         node.mine(false);
  //     }
  //    }
  //  }

  //JFrame stuff
  public void makePage() {

    //UI
    JPanel page = new JPanel();
    page.setLayout(new GridBagLayout());

    //header
    JPanel headerPanel = new JPanel();

      JLabel title = new JLabel("Network Simulator");
      title.setFont(title.getFont().deriveFont(28.0f));
      headerPanel.add(title);

    GridBagConstraints headerPanelCons = new GridBagConstraints();
    setCons(headerPanelCons, 1,0,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(headerPanel, headerPanelCons);

    //nodes
    JPanel nodesPanel = new JPanel();
    nodesPanel.setLayout(new GridBagLayout());

    makeNodesPanel(nodesPanel);

    GridBagConstraints nodesPanelCons = new GridBagConstraints();
    setCons(nodesPanelCons, 0,1,6,4,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(nodesPanel, nodesPanelCons);

    simulation.add(page);
    simulation.setVisible(true);

    //buttons
    JPanel buttons = new JPanel();

    pauseButton = new JButton("Start");
    buttons.add(pauseButton);
    exitButton = new JButton("Exit");
    buttons.add(exitButton);

    pauseButton.addActionListener(lForButton);
    exitButton.addActionListener(lForButton);

    GridBagConstraints buttonsCons = new GridBagConstraints();
    setCons(buttonsCons,1,11,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttons, buttonsCons);
  }

  public void makeNodesPanel(JPanel nodesPanel) {
    System.out.println("Failed to overwrite makeNoedsPanel method from abstract class Simulation.");
  }

  private void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int anchor, int ipadx, int ipady) {
    gridCons.gridx = x;
    gridCons.gridy = y;
    //number of col/row component takes up
    gridCons.gridwidth = width;
    gridCons.gridheight = height;
    //when components display area is larger than component.
    // NONE - default, HORIZONTAL - fill horizontal space, VERTICAL, BOTH
    gridCons.fill = fill;
    gridCons.ipadx = ipadx;
    gridCons.ipady = ipady;
    //used when component is smaller than display area to determine where to palce
    //CENTER default
    gridCons.anchor = anchor;
    //used for determining area between components in display area 0.0-1.0
    //keep as 0 for now,meaning cell fits to component
    gridCons.weightx = 0.2;
    gridCons.weighty = 0.2;
  }



   private class ListenForButton implements ActionListener {

     public void actionPerformed(ActionEvent e) {

       if(e.getSource() == pauseButton) {
         System.out.println("sim paused.");
       } else if (e.getSource() == exitButton) {
         simulation.dispose();
       }
     }
   }

}
