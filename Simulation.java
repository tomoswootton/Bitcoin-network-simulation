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
  int nodesPP;

  JPanel page;
  JPanel nodesPanel;
  int currentPage;
  double totalPages;

  JLabel currentPageLabel;
  JButton startPauseButton;
  JButton exitButton;
  JButton nextPageButton;
  JButton prevPageButton;

  //linked list used becasue blocks will only be added to the end
   LinkedList<Node> nodesList;

  public Simulation(int nodesPP, LinkedList<Node> nodesList) {

    this.nodesPP = nodesPP;
    this.nodesList = nodesList;
    this.currentPage = 1;
    this.totalPages = Math.ceil((double) nodesList.size()/nodesPP);
    //init
    simulation.setSize(1200,900);

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
      setCons(headerPanelCons, 1,0,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      page.add(headerPanel, headerPanelCons);

      //nodes
      nodesPanel = new JPanel();
      nodesPanel.setLayout(new GridBagLayout());

        constructNodesPanels(currentPage);

      GridBagConstraints nodesPanelCons = new GridBagConstraints();
      setCons(nodesPanelCons, 0,1,6,4,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      page.add(nodesPanel, nodesPanelCons);


      //buttons
      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new GridBagLayout());

        prevPageButton = new JButton("<< previous page");

        GridBagConstraints prevPageButtonCons = new GridBagConstraints();
        setCons(prevPageButtonCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(prevPageButton, prevPageButtonCons);

        nextPageButton = new JButton("Next page >>");

        GridBagConstraints nextPageButtonCons = new GridBagConstraints();
        setCons(nextPageButtonCons, 1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(nextPageButton, nextPageButtonCons);

        startPauseButton = new JButton("Start");

        GridBagConstraints startPauseButtonCons = new GridBagConstraints();
        setCons(startPauseButtonCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(startPauseButton, startPauseButtonCons);

        exitButton = new JButton("Exit");

        GridBagConstraints exitButtonCons = new GridBagConstraints();
        setCons(exitButtonCons, 1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        buttonsPanel.add(exitButton, exitButtonCons);

      prevPageButton.addActionListener(lForButton);
      nextPageButton.addActionListener(lForButton);
      startPauseButton.addActionListener(lForButton);
      exitButton.addActionListener(lForButton);


      GridBagConstraints buttonsPanelCons = new GridBagConstraints();
      setCons(buttonsPanelCons,1,5,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      page.add(buttonsPanel, buttonsPanelCons);

    simulation.add(page);
    simulation.setVisible(true);
  }

  //displays nodes in panel, pages start on 1
  public void constructNodesPanels(int page) {
    nodesPanel.removeAll();
    for (int j=0;j<nodesPP/3;j++) {
      for (int i=0;i<=2;i++) {
        if(i+(j*3)+(page-1)*nodesPP > nodesList.size()-1) {
          break;
        }
        addNode(i+(j*3)+(page-1)*nodesPP,i,j);
      }
    }
  }

    private void addNode(int node, int xpos, int ypos) {
      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,xpos*2,ypos,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,20,0);
      nodesPanel.add(nodesList.get(node).getPanel(),panelCons);
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
     } else if (e.getSource() == prevPageButton) {
       if (currentPage > 1) {
         currentPage -= 1;
         currentPageLabel.setText("current page: "+ currentPage);
         constructNodesPanels(currentPage);
       }
     } else if (e.getSource() == nextPageButton) {
       if (currentPage < totalPages) {
         currentPage += 1;
         currentPageLabel.setText("current page: "+ currentPage);
         constructNodesPanels(currentPage);
       }
      }
     }
   }
}
