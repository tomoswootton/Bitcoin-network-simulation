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
  ArrayList<JPanel> initBlockDispHolderList;

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
     nodesList.add(new Node("0","test","30",0.5));
     nodesList.add(new Node("1","test2","30",0.8));
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
    nodesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

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

      JScrollPane nodesScrollPane = new JScrollPane(nodesScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      nodesScrollPane.setViewportView(nodesScrollPanel);
      nodesScrollPane.setPreferredSize(new Dimension(500,100));

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
      nodesScrollPanel.add(node.getNodeDispPanel());
    }
  }

  //chain panel
  private void constructChainPanel() {
    //swing
    chainPanel = new JPanel();
    chainPanel.setLayout(new GridBagLayout());
    chainPanel.setBorder(BorderFactory.createLineBorder(Color.black));

      //titles
      JLabel chainTitle = new JLabel("<HTML><U>Chain</U></HTML>");
      chainTitle.setFont(chainTitle.getFont().deriveFont(16.0f));

      GridBagConstraints chainTitleCons = new GridBagConstraints();
      setCons(chainTitleCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainTitle, chainTitleCons);

      //scroll box
      chainScrollPanel = new JPanel();
      chainScrollPanel.setLayout(new GridBagLayout());
      chainScrollPanel.setSize(new Dimension(1000,200));

      JScrollPane chainScrollPane = new JScrollPane(chainScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      chainScrollPane.setViewportView(chainScrollPanel);
      chainScrollPane.setPreferredSize(new Dimension(1000,250));

      initBlockDispHolderList = new ArrayList<JPanel>();

      //populate scroll panel with holder panels to force shape of scroll panel within scroll pane
      for (int i=0;i<=4;i++) {
        JPanel blockDispHolder = new JPanel();
        blockDispHolder.setPreferredSize(new Dimension(200,200));
        blockDispHolder.setBackground(Color.yellow);
        blockDispHolder.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints panelCons = new GridBagConstraints();
        setCons(panelCons,i,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
        chainScrollPanel.add(blockDispHolder, panelCons);

        initBlockDispHolderList.add(blockDispHolder);
      }

        populateChainScrollPanel();

        // Block fakeBlock = new Block(blocksFoundList.size(), "1234");
        // addBlockToGlobalChain(fakeBlock);

      GridBagConstraints chainPanelCons = new GridBagConstraints();
      setCons(chainPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainScrollPane, chainPanelCons);

    //testing buttons
    JButton addFakeBlockButton = new JButton("add fake block");
    addFakeBlockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addFakeBlockButton) {
          Block fakeBlock = new Block(blocksFoundList.size(), "1234");
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
    System.out.println("adding block to panel, id: "+block.id);

    //if one of first 10 blocks, add to initBlockDispHolder
    if (block.id < 5) {
      initBlockDispHolderList.get(block.id).add(block.getDispPanel());
    } else {
      //create new holder panel
      JPanel blockDispHolder = new JPanel();
      blockDispHolder.setPreferredSize(new Dimension(200,200));
      blockDispHolder.setBackground(Color.yellow);
      blockDispHolder.setBorder(BorderFactory.createLineBorder(Color.black));


      //add block dispPanel to holder panel
      blockDispHolder.add(block.getDispPanel());

      //adjust size of panel to allow for new block
      chainScrollPanel.setSize(new Dimension((block.id-9)*200 + 1000,300));

      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,block.id,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      chainScrollPanel.add(blockDispHolder, panelCons);
    }
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();

  }
  private void refreshChainPanel() {
    //remove components
    chainPanel.removeAll();
    //clear initBlockDispHolder list for re-construction
    initBlockDispHolderList.clear();
    //re-construct
    constructChainPanel();

    System.out.println("blocks found list size: "+blocksFoundList.size());
    //add blocks from list
    for (Block block : blocksFoundList) {
      addBlockToChainPanel(block);
    }
  }

  public void run(Boolean state) {
    for (Node node : nodesList) {
      node.runningState = state;
      node.mine(state);
    }
  }

 }
