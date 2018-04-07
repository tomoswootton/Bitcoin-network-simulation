import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.LinkedList;
import java.util.ArrayList;


public class Simulation {

  JFrame simulationFrame = new JFrame();

  // page variables
  Boolean running = false;

  JPanel page;
  JPanel nodesPanel;
  JPanel nodesScrollPanel;
  JPanel chainPanel;
  JPanel chainScrollPanel;
  ArrayList<JPanelWithLine> initBlockDispHolderList;

  double totalPages;
  JPanel blocksPanel;

  JButton startPauseButton;
  JButton exitButton;

  //linked list used becasue blocks will only be added to the end
  LinkedList<Node> nodesList;

  //list of blocks foundBlocks
  private LinkedList<Block> blocksFoundList = new LinkedList<Block>();

  //construtors
  public static void main(String[] args) {
     LinkedList<Node> nodesList = new LinkedList<Node>();
     nodesList.add(new Node(0,"test","30",0.5));
     nodesList.add(new Node(1,"test2","30",0.8));
    //  new Simulation(nodesList);
   }
  public Simulation(LinkedList<Node> nodesList) {
    this.nodesList = nodesList;
    //init
    simulationFrame.setSize(1200,900);
    simulationFrame.setLocationRelativeTo(null);
    simulationFrame.setResizable(false);
    simulationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    simulationFrame.setTitle("Simulation");

    makePage();
  }

  //JSwing stuff
  private void makePage() {

    //UI
    page = new JPanel();
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
    constructNodesPanel();

    //chain
    constructChainPanel();

    //buttons
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridBagLayout());

    startPauseButton = new JButton("Start");
    startPauseButton.addActionListener(new ActionListener() {
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
        }
      }
    });

    GridBagConstraints startPauseButtonCons = new GridBagConstraints();
    setCons(startPauseButtonCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buttonsPanel.add(startPauseButton, startPauseButtonCons);

    exitButton = new JButton("Exit");
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitButton) {
          simulationFrame.dispose();
        }
      }
    });

    GridBagConstraints exitButtonCons = new GridBagConstraints();
    setCons(exitButtonCons, 1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buttonsPanel.add(exitButton, exitButtonCons);

    GridBagConstraints buttonsPanelCons = new GridBagConstraints();
    setCons(buttonsPanelCons,1,7,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttonsPanel, buttonsPanelCons);

    simulationFrame.add(page);
    simulationFrame.setVisible(true);
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

  //getters and setters
  public int getNodesListSize(){
    return nodesList.size();
  }

  //nodes panel
  private void constructNodesPanel() {
    //swing
    nodesPanel = new JPanel();
    nodesPanel.setLayout(new GridBagLayout());

      //titles
      JLabel nodesTitle = new JLabel("<HTML><U>Nodes</U></HTML>");
      nodesTitle.setFont(nodesTitle.getFont().deriveFont(16.0f));

      GridBagConstraints nodesTitleCons = new GridBagConstraints();
      setCons(nodesTitleCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      nodesPanel.add(nodesTitle, nodesTitleCons);

      JLabel titleLabel = new JLabel("                Name                     id         power      blocks                                                  ");

      GridBagConstraints titleLabelCons = new GridBagConstraints();
      setCons(titleLabelCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      nodesPanel.add(titleLabel, titleLabelCons);

      //scroll box
      nodesScrollPanel = new JPanel();
      nodesScrollPanel.setLayout(new BoxLayout(nodesScrollPanel, BoxLayout.Y_AXIS));
      nodesScrollPanel.setSize(new Dimension(500, 150));
      JScrollPane nodesScrollPane = new JScrollPane(nodesScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

      nodesScrollPane.setViewportView(nodesScrollPanel);
      nodesScrollPane.setPreferredSize(new Dimension(500,120));

        populateNodesScrollPanel();

      GridBagConstraints nodesPanelCons = new GridBagConstraints();
      setCons(nodesPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      nodesPanel.add(nodesScrollPane, nodesPanelCons);

    GridBagConstraints nodesCons = new GridBagConstraints();
    setCons(nodesCons, 0,1,5,2,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
    page.add(nodesPanel, nodesCons);
  }
  private void populateNodesScrollPanel() {
    //displays nodes in panel, pages start on 1
    nodesScrollPanel.removeAll();
    for (Node node : nodesList) {
      nodesScrollPanel.add(node.getDispPanel());
    }
  }

  //chain panel
  private void constructChainPanel() {
    //swing
    chainPanel = new JPanel();
    chainPanel.setLayout(new GridBagLayout());

      //titles
      JLabel chainTitle = new JLabel("<HTML><U>Chain</U></HTML>");
      chainTitle.setFont(chainTitle.getFont().deriveFont(16.0f));

      GridBagConstraints chainTitleCons = new GridBagConstraints();
      setCons(chainTitleCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainTitle, chainTitleCons);

      //scroll box
      chainScrollPanel = new JPanel();
      chainScrollPanel.setLayout(new GridBagLayout());
      chainScrollPanel.setSize(new Dimension(1100,220));

      JScrollPane chainScrollPane = new JScrollPane(chainScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      chainScrollPane.setViewportView(chainScrollPanel);
      chainScrollPane.setBorder(BorderFactory.createEmptyBorder());
      chainScrollPane.setPreferredSize(new Dimension(1100,220));

      initBlockDispHolderList = new ArrayList<JPanelWithLine>();

      //populate scroll panel with holder panels to force shape of scroll panel within scroll pane
      for (int i=0;i<=4;i++) {
        JPanelWithLine blockDispHolder = new JPanelWithLine();

        GridBagConstraints panelCons = new GridBagConstraints();
        setCons(panelCons,i,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        chainScrollPanel.add(blockDispHolder, panelCons);

        initBlockDispHolderList.add(blockDispHolder);
      }

        populateChainScrollPanel();

        //add genesis block
        GenBlock genBlock = new GenBlock(0, "1234", null);
        addBlockToGlobalChain(genBlock);

      GridBagConstraints chainPanelCons = new GridBagConstraints();
      setCons(chainPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainScrollPane, chainPanelCons);

    //testing buttons
    JButton addFakeBlockButton = new JButton("add fake block");
    addFakeBlockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addFakeBlockButton) {
          Block fakeBlock = new Block(blocksFoundList.size(), "1234", "None");
          addBlockToGlobalChain(fakeBlock);
        }
      }
    });

    GridBagConstraints addFakeBlockButtonCons = new GridBagConstraints();
    setCons(addFakeBlockButtonCons, 0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    chainPanel.add(addFakeBlockButton, addFakeBlockButtonCons);

    JButton refreshButton = new JButton("refresh");
    refreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshButton) {
          refreshChainPanel();
        }
      }
    });

    GridBagConstraints refreshButtonCons = new GridBagConstraints();
    setCons(refreshButtonCons, 0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    chainPanel.add(refreshButton, refreshButtonCons);

    GridBagConstraints chainCons = new GridBagConstraints();
    setCons(chainCons, 0,3,5,3,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
    page.add(chainPanel, chainCons);
  }
  private void populateChainScrollPanel() {
    //displays already found blocks in panel
    for (Block block : blocksFoundList) {
      addBlockToChainPanel(block);
    }

  }
  public void addBlockToGlobalChain(Block block) {
    //TODO add checks for consensus here
    addBlockToFoundList(block);
    addBlockToChainPanel(block);
  }
  private void addBlockToFoundList(Block block) {
    blocksFoundList.add(block);
  }
  private void addBlockToChainPanel(Block block) {
    //if one of first 5 blocks, add to initBlockDispHolder
    if (block.id < 5) {
      initBlockDispHolderList.get(block.id).addBlockDispPanel(block.getDispPanel());
    } else {
      //create new holder panel
      JPanelWithLine blockDispHolder = new JPanelWithLine();

      //add block dispPanel to holder panel
      blockDispHolder.addBlockDispPanel(block.getDispPanel());

      //adjust size of panel to allow for new block
      chainScrollPanel.setSize(new Dimension((block.id-9)*220 + 1100,220));

      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,block.id,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      chainScrollPanel.add(blockDispHolder, panelCons);
    }
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();

  }
  private void refreshChainPanel() {
    //re-construct
    constructChainPanel();
  }

  public void run(Boolean state) {
    for (Node node : nodesList) {
      node.runningState = state;
      node.mine(state);
    }
  }

}

class JPanelWithLine extends JPanel{
  JPanel buffer1;
  JPanel buffer2; //panel holds block
  JPanel buffer3;

  public JPanelWithLine() {
    this.setPreferredSize(new Dimension(220,220));
    this.setLayout(new GridBagLayout());
    /*layout non split:
    *buffer panel
    *block
    *block
    *buffer panel
    */
    buffer1 = new JPanel();
    buffer1.setMinimumSize(new Dimension(220, 55));
    GridBagConstraints cons1 = new GridBagConstraints();
    setCons(cons1,0,0,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    this.add(buffer1, cons1);

    buffer2 = new JPanel();
    buffer2.setLayout(new GridBagLayout());
    buffer2.setMinimumSize(new Dimension(220, 110));
    GridBagConstraints cons2 = new GridBagConstraints();
    setCons(cons2,0,1,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    this.add(buffer2, cons2);

    buffer3 = new JPanel();
    buffer3.setMinimumSize(new Dimension(220, 55));
    GridBagConstraints cons3 = new GridBagConstraints();
    setCons(cons3,0,4,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    this.add(buffer3, cons3);

  }

  @Override
  protected void paintComponent(Graphics g) {
      //draw line through center of panel to
      super.paintComponent(g);
      g.setColor(Color.black);
      g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
  }

  public void addBlockDispPanel(JPanel panel) {
    GridBagConstraints cons = new GridBagConstraints();
    setCons(cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer2.add(panel, cons);
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

}
