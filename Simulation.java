import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.awt.GridBagLayout;
import java.util.LinkedList;


public class Simulation {

  JFrame simulation = new JFrame();

  ListenForButton lForButton = new ListenForButton();

  // page variables
  Boolean running = false;

  JPanel page;
  JPanel nodesPanel;
  double totalPages;

  JButton startPauseButton;
  JButton exitButton;


  //linked list used becasue blocks will only be added to the end
   LinkedList<Node> nodesList;

   public static void main(String[] args) {
     LinkedList<Node> nodesList= new LinkedList<Node>();
     nodesList.add(new Node("0","test","30",0.5));
     nodesList.add(new Node("1","test2","30",0.8));
    //  new Simulation(nodesList);
   }

  public Simulation(LinkedList<Node> nodesList) {

    this.nodesList = nodesList;
    //init
    simulation.setSize(1200,900);

    simulation.setLocationRelativeTo(null);
    simulation.setResizable(false);
    simulation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    simulation.setTitle("Simulation");

    makePage();

  }

//getters and setters
  public int getNodesListSize(){
    return nodesList.size();
  }

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
      setCons(headerPanelCons,1,0,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      page.add(headerPanel, headerPanelCons);

      //nodes
      JPanel nodes = new JPanel();
      nodes.setLayout(new GridBagLayout());
      nodes.setBorder(BorderFactory.createLineBorder(Color.black));


        //titles
        JLabel nodesTitle = new JLabel("<HTML><U>Nodes</U></HTML>");
        nodesTitle.setFont(nodesTitle.getFont().deriveFont(16.0f));

        GridBagConstraints nodesTitleCons = new GridBagConstraints();
        setCons(nodesTitleCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        nodes.add(nodesTitle, nodesTitleCons);


        JLabel titleLabel = new JLabel("                Name                     id         power      blocks                                                  ");

        GridBagConstraints titleLabelCons = new GridBagConstraints();
        setCons(titleLabelCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        nodes.add(titleLabel, titleLabelCons);

        //scroll box
        nodesPanel = new JPanel();
        nodesPanel.setLayout(new BoxLayout(nodesPanel, BoxLayout.Y_AXIS));

        JScrollPane nodesScrollPane = new JScrollPane(nodesPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        nodesScrollPane.setViewportView(nodesPanel);
        nodesScrollPane.setPreferredSize(new Dimension(500,100));

          constructNodesPanels();

        GridBagConstraints nodesPanelCons = new GridBagConstraints();
        setCons(nodesPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        nodes.add(nodesScrollPane, nodesPanelCons);

      GridBagConstraints nodesCons = new GridBagConstraints();
      setCons(nodesCons, 0,1,5,3,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
      page.add(nodes, nodesCons);

      //chain
      JPanel chainPanel = new JPanel();
      chainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      chainPanel.setPreferredSize(new Dimension(700,300));

      chainPanel.add(new JLabel("hello"));

      JScrollPane chainScrollPane = new JScrollPane(chainPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      chainScrollPane.setViewportView(chainPanel);
      chainScrollPane.setPreferredSize(new Dimension(500,300));

      GridBagConstraints chainPanelCons = new GridBagConstraints();
      setCons(chainPanelCons,1,4,4,3,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      page.add(chainPanel, chainPanelCons);

      //buttons
      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new GridBagLayout());

        startPauseButton = new JButton("Start");

        GridBagConstraints startPauseButtonCons = new GridBagConstraints();
        setCons(startPauseButtonCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(startPauseButton, startPauseButtonCons);

        exitButton = new JButton("Exit");

        GridBagConstraints exitButtonCons = new GridBagConstraints();
        setCons(exitButtonCons, 1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(exitButton, exitButtonCons);

      startPauseButton.addActionListener(lForButton);
      exitButton.addActionListener(lForButton);


      GridBagConstraints buttonsPanelCons = new GridBagConstraints();
      setCons(buttonsPanelCons,1,7,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      page.add(buttonsPanel, buttonsPanelCons);

    simulation.add(page);
    simulation.setVisible(true);
  }

  //displays nodes in panel, pages start on 1
  public void constructNodesPanels() {
    nodesPanel.removeAll();
    for (Node node : nodesList) {
      nodesPanel.add(node.getNodeDispPanel());
    }
  }

    private void addNode(int node, int xpos, int ypos) {
      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,xpos*2,ypos,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,20,0);
      nodesPanel.add(nodesList.get(node).getLogPanel());
    }

  public void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int anchor, int ipadx, int ipady) {
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

  public void run(Boolean state) {
    for (Node node : nodesList) {
      node.runningState = state;
      node.mine(state);
    }
  }

  private class ListenForButton implements ActionListener {

   public void actionPerformed(ActionEvent e) {
     if(e.getSource() == startPauseButton) {
       if (running) {
         startPauseButton.setText("Start");
         run(false);
         running = false;
       } else {
         startPauseButton.setText("Pause");
         run(true);
         running = true;
       }


     } else if (e.getSource() == exitButton) {
       simulation.dispose();
     }
   }

 }
 }
