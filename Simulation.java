import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.*;
import java.util.Timer;



public class Simulation {

  JFrame simulationFrame = new JFrame();

  // page variables
  Boolean running = false;

  JPanel page;
  JPanel nodesPanel;
  JPanel nodesScrollPanel;
  JPanel chainPanel;
  JPanel chainScrollPanel;
  JScrollPane chainScrollPane;
  ArrayList<JPanelBlockDisp> blockDispHolderList;

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
  private void setCons(GridBagConstraints gridCons,int x,int y,int width,int height,int fill,int anchor,int ipadx,int ipady,int weightx,int weighty) {
    //extra method for if weight option is wanted

    setCons(gridCons,x,y,width,height,fill,anchor,ipadx,ipady);
    gridCons.weightx = weightx;
    gridCons.weighty = weighty;
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

      chainScrollPane = new JScrollPane(chainScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      chainScrollPane.setViewportView(chainScrollPanel);
      chainScrollPane.setBorder(BorderFactory.createEmptyBorder());
      chainScrollPane.setPreferredSize(new Dimension(1100,220));

      blockDispHolderList = new ArrayList<JPanelBlockDisp>();

      //populate scroll panel with holder panels to force shape of scroll panel within scroll pane
      for (int i=0;i<=4;i++) {
        JPanelBlockDisp blockDispHolder = new JPanelBlockDisp();

        GridBagConstraints panelCons = new GridBagConstraints();
        setCons(panelCons,i,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0,0,0);
        chainScrollPanel.add(blockDispHolder, panelCons);

        blockDispHolderList.add(blockDispHolder);
      }

        populateChainScrollPanel();

        //add genesis block
        GenBlock genBlock = new GenBlock(0, "1234", null);
        addBlockToGlobalChain(genBlock);

      GridBagConstraints chainPanelCons = new GridBagConstraints();
      setCons(chainPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainScrollPane, chainPanelCons);

    //testing buttons
    JButton addFakeBlockButton = new JButton("add block");
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

    JButton addFakeSplitButton = new JButton("add split");
    addFakeSplitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addFakeSplitButton) {
          Block fakeBlock1 = new Block(blocksFoundList.size(), "4782", "name two");
          Block fakeBlock2 = new Block(blocksFoundList.size(), "1234", "name one");
          addSplitPanelToGlobalChain(fakeBlock1, fakeBlock2);
        }
      }
    });

    GridBagConstraints addFakeSplitButtonCons = new GridBagConstraints();
    setCons(addFakeSplitButtonCons, 0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    chainPanel.add(addFakeSplitButton, addFakeSplitButtonCons);

    JButton refreshButton = new JButton("refresh");
    refreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshButton) {
          refreshChainPanel();
        }
      }
    });

    GridBagConstraints refreshButtonCons = new GridBagConstraints();
    setCons(refreshButtonCons, 0,5,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    chainPanel.add(refreshButton, refreshButtonCons);

    GridBagConstraints chainCons = new GridBagConstraints();
    setCons(chainCons, 0,3,5,3,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
    page.add(chainPanel, chainCons);
  }
  private void populateChainScrollPanel() {
    //displays already found blocks in panel
    for (Block block : blocksFoundList) {
      addBlockToHolderPanel(block);
    }

  }
  public void addBlockToGlobalChain(Block block) {
    //TODO add checks for consensus here
    addBlockToFoundList(block);
    //create holder panel
    addBlockHolderPanel();

    addBlockToHolderPanel(block);
  }
  private void addBlockToFoundList(Block block) {
    blocksFoundList.add(block);
  }
  private void addBlockHolderPanel() {
    //if all first 5 blocks are occupied add new panel
    if (blockDispHolderList.get(4).isOccupied()) {
      //create new holder panel
      JPanelBlockDisp blockDispHolder = new JPanelBlockDisp();
      blockDispHolderList.add(blockDispHolder);

      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,blockDispHolderList.size(),0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      chainScrollPanel.add(blockDispHolder, panelCons);
    }

    JScrollBar horizontal = chainScrollPane.getHorizontalScrollBar();
    horizontal.setValue(horizontal.getMaximum());

  }
  private void addBlockToHolderPanel(Block block) {

    //add block dispPanel to holder panel
    blockDispHolderList.get(block.id).addBlockDispPanel(block.getDispPanel());

    //adjust size of panel to allow for new block
    chainScrollPanel.setSize(new Dimension((block.id-9)*220 + 1100,220));
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();

  }
  private void addSplitPanelToGlobalChain(Block block1, Block block2) {
    //temporerily add block1 to found list
    blocksFoundList.add(block1);

    //make new holder panel
    addBlockHolderPanel();
    //set holder to split layout
    JPanelBlockDisp holderPanel = blockDispHolderList.get(block1.id);
    holderPanel.makeSplitLayout();

    //add blocks
    addBlocksToSplitPanel(block1, block2);

  }
  private void addBlocksToSplitPanel(Block block1, Block block2) {
    //add blocks
    blockDispHolderList.get(block1.id).addBlockDispPanel(block1.getDispPanel(), block2.getDispPanel());

    //adjust size of panel to allow for new block
    chainScrollPanel.setSize(new Dimension((block1.id-9)*220 + 1100,220));
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();
  }
  private void refreshChainPanel() {
    //re-construct
    constructChainPanel();
  }

  public void run(Boolean state) {
    Timer timer = new Timer();
    if (state) {
      System.out.println("Simulation started");

    //start miners at even spacing
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        //if final node is not running
        for (Node node : nodesList) {
          if (!node.runningState) {
            System.out.println("starting node "+node.getName());
            node.runningState = state;
            node.mine(state);
            return;
          }
        }
        System.out.println("All nodes running");
        timer.cancel();
        timer.purge();
      }
    }, 0, 2000);
  } else {
    System.out.println("Simulation stopped");
    timer.cancel();
    timer.purge();
    for (Node node : nodesList) {
      System.out.println("stopping node");
      node.runningState = state;
      node.mine(state);
    }
  }
  }


class JPanelBlockDisp extends JPanel{
  JPanel buffer1;
  JPanel buffer2; //panel holds block
  JPanel buffer3;

  int id;

  Boolean occupied = false;
  Boolean split = false;

  public JPanelBlockDisp() {
    this.id = blockDispHolderList.size();
    makeIndividualBlockLayout();
  }

  public Boolean isOccupied() {
    if (occupied) {
      return true;
    }
    return false;
  }

  public void makeIndividualBlockLayout() {
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

  public void makeSplitLayout() {
    this.split = true;

    this.removeAll();
    this.setPreferredSize(new Dimension(220,220));
    this.setLayout(new GridBagLayout());
    /*layout non split:
    *buffer panel
    *block
    *block
    *buffer panel
    */
    buffer1 = new JPanel();
    buffer1.setLayout(new GridBagLayout());
    buffer1.setMinimumSize(new Dimension(220, 110));
    GridBagConstraints cons1 = new GridBagConstraints();
    setCons(cons1,0,0,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    this.add(buffer1, cons1);

    buffer2 = new JPanel();
    buffer2.setLayout(new GridBagLayout());
    buffer2.setMinimumSize(new Dimension(220, 110));
    GridBagConstraints cons2 = new GridBagConstraints();
    setCons(cons2,0,1,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    this.add(buffer2, cons2);

  }

  @Override
  protected void paintComponent(Graphics g2) {
    //no lines if not occupied
    if(!occupied) {
      return;
    }


    int h = getHeight();
    int w = getWidth();
    super.paintComponent(g2);
    Graphics2D g = (Graphics2D) g2;
    g.setColor(Color.black);
    g.setStroke(new BasicStroke(2));


    //if first panel
    if (id == 0 ) {
      //draw line beginning in middle to middle gridheight
      g.drawLine(h/2,h/2,w,h/2);
      return;
    }

    //paint first half
    Boolean prevBlockSplit = blockDispHolderList.get(id-1).split;

    //individual - individual
    if (!split) {
      //draw line through center
      g.drawLine(0,h/2,w/2,h/2);

      //individual - split
    } else if (!prevBlockSplit && split) {
      //draw lines that split from middle
      g.drawLine(0,h/2,w/4,3*(h/4));
      g.drawLine(0,h/2,w/4,h/4);
      g.drawLine(w/4,3*(h/4),w/2,3*(h/4));
      g.drawLine(w/4,h/4,w/2,h/4);

      //split - split
    } else if (prevBlockSplit && split) {
      g.drawLine(0,h/4,w/2,h/4);
      g.drawLine(0,3*(h/4),w/2,3*(h/4));

    }

    //end method here if panel is last in list
    if (blocksFoundList.get(blocksFoundList.size()-1).id == id) {
      return;
    }

    //paint second half

    //the remaining code is called on the second to last panel in the list
    Boolean nextBlockSplit = blockDispHolderList.get(id+1).split;
    //individual - individual
    if (!split) {
      //draw line through center
      g.drawLine(w/2,h/2,w+10,h/2);

      //split - individual
    } else if (!nextBlockSplit){
      //draw lines that close split
      g.drawLine(w/2,3*(h/4),3*(w/4),3*(h/4));
      g.drawLine(w/2,h/4,3*(w/4),h/4);
      g.drawLine(3*(w/4),3*(h/4),w,h/2);
      g.drawLine(3*(w/4),h/4,w,h/2);

      //split - split
    } else if (nextBlockSplit) {
      g.drawLine(w/2,3*(h/4),w,3*(h/4));
      g.drawLine(w/2,h/4,w,h/4);
    }
  }

  public void addBlockDispPanel(JPanel panel) {
    GridBagConstraints cons = new GridBagConstraints();
    setCons(cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer2.add(panel, cons);

    occupied = true;
  }

  public void addBlockDispPanel(JPanel panel1, JPanel panel2) {
    GridBagConstraints cons1 = new GridBagConstraints();
    setCons(cons1,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer1.add(panel1, cons1);

    GridBagConstraints cons2 = new GridBagConstraints();
    setCons(cons2,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer2.add(panel2, cons2);

    occupied = true;
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

  private void setCons(GridBagConstraints gridCons,int x,int y,int width,int height,int fill,int anchor,int ipadx,int ipady,int weightx,int weighty) {
    //extra method for if weight option is wanted

    setCons(gridCons,x,y,width,height,fill,anchor,ipadx,ipady);
    gridCons.weightx = weightx;
    gridCons.weighty = weighty;
  }

}
}
