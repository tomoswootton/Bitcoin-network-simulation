import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.*;
import java.util.Timer;

public class Simulation {

  JFrame simulationFrame = new JFrame();
  GlobalInfo globalInfo;

  // page variables
  Boolean running = false;
  Boolean inLatency = false;
  Boolean timerBool = false;

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


  //list of blocks foundBlocks
  private LinkedList<Block> blocksFoundList = new LinkedList<Block>();

  //construtors
  public static void main(String[] args) {
     // LinkedList<Node> nodesList = new LinkedList<Node>();
     // // nodesList.add(new Node(0,"test","30",0.5,10000));
     // nodesList.add(new Node(1,"test2","30",0.8,10000));
    //  new Simulation(nodesList);
   }
  public Simulation(GlobalInfo globalInfo) {
    this.globalInfo = globalInfo;
    //init
    simulationFrame.setSize(1200,900);
    simulationFrame.setLocationRelativeTo(null);
    simulationFrame.setResizable(false);
    simulationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    simulationFrame.setTitle("Simulation");

    makePage();
  }

  public LinkedList<Block> getBlocksFoundList() {
    return this.blocksFoundList;
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
          } else {
            startPauseButton.setText("Pause");
            run(true);
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
    for (Node node : globalInfo.getNodesList()) {
      nodesScrollPanel.add(node.getDispPanel());
    }
  }

  //chain panel swing
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
        GenBlock genBlock = new GenBlock(globalInfo,0,"1234", null);
        addBlockToGlobalChain(genBlock);
        addBlockToFoundList(genBlock);

      GridBagConstraints chainPanelCons = new GridBagConstraints();
      setCons(chainPanelCons, 0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      chainPanel.add(chainScrollPane, chainPanelCons);

    //testing buttons
    JButton addFakeBlockButton = new JButton("add block");
    addFakeBlockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addFakeBlockButton) {
          String prevBlockHash;
          //if in split
          if (inLatency) {
            //use prev panel block
            prevBlockHash = blockDispHolderList.get(getCurrentBlockPanel().id-1).block.getHash();
          } else {
            prevBlockHash = blockDispHolderList.get(getCurrentBlockPanel().id).block.getHash();
          }
          Block fakeBlock = new Block(globalInfo, blocksFoundList.size(), prevBlockHash, "None");
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
    setCons(refreshButtonCons, 0,5,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    chainPanel.add(refreshButton, refreshButtonCons);

    GridBagConstraints chainCons = new GridBagConstraints();
    setCons(chainCons, 0,3,5,3,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
    page.add(chainPanel, chainCons);
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
    pushScrollBarToRight(chainScrollPane, chainScrollPanel);

  }
  private void addBlockToHolderPanel(Block block) {

    //add block dispPanel to holder panel
    blockDispHolderList.get(block.id).addBlockDispPanel(block);

    //adjust size of panel to allow for new block
    chainScrollPanel.setSize(new Dimension((block.id-9)*220 + 1100,220));
  }
  private void splitChain(Block block) {
    System.out.println("Chain Split. block "+block.id);
    //var lets other blocks build on split chain
    inLatency = false;

    //make currenct panel split, removing previous block
    JPanelBlockDisp current = getCurrentBlockPanel();
    current.makeSplitLayout();

    //add previous block and newly found block to split panel
    addBlocksToSplitPanel(getCurrentBlockPanel().block,block);

    current.revalidate();
    current.repaint();

    //send blocks across network
    shuffleSplitBlocksBetweenNodes();

    pushScrollBarToRight(chainScrollPane, chainScrollPanel);
  }
  private void addBlocksToSplitPanel(Block block1, Block block2) {
    //add blocks
    blockDispHolderList.get(block1.id).addBlockDispPanel(block1, block2);
    //adjust size of panel to allow for new block
    chainScrollPanel.setSize(new Dimension((block1.id-9)*220 + 1100,220));
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();
  }
  //chain panel methods
  private void pushScrollBarToRight(JScrollPane scrollPane, JPanel scrollPanel) {
    scrollPanel.revalidate();
    scrollPanel.repaint();
    scrollPane.revalidate();
    scrollPane.repaint();
    JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
    horizontal.setValue(horizontal.getMaximum());
    scrollPanel.revalidate();
    scrollPanel.repaint();
    scrollPane.revalidate();
    scrollPane.repaint();
  }
  private void populateChainScrollPanel() {
    //displays already found blocks in panel
    for (Block block : blocksFoundList) {
      addBlockToHolderPanel(block);
    }

  }
  private JPanelBlockDisp getCurrentBlockPanel() {
    JPanelBlockDisp panel = blockDispHolderList.get(blockDispHolderList.size()-1);
    //if in first 5 panels find largest height that is occupied
    if (panel.id == 4) {
      for(int i=0;i<blockDispHolderList.size();i++) {
        if (!blockDispHolderList.get(i).isOccupied()) {
          return  blockDispHolderList.get(i-1);
        }
      }
    }
    return panel;
  }
  public void addBlockToGlobalChain(Block block) {
    
    //set blocks timeElasped var and check for split
    if (block.id > 0) {
      long timeElapsedMiliSec = ((globalInfo.getTime() - blocksFoundList.get(block.id-1).getTimeFound()));
      double timeElapsedSec = (double) timeElapsedMiliSec/1000;
      block.setTimeElapsed(timeElapsedSec);
      block.setTimeFound(globalInfo.getTime());
      if (inLatency) {
        splitChain(block);
        return;
      }
      //if previous block panel is split, tell it which block won
      if (blockDispHolderList.get(getCurrentBlockPanel().id).split) {
        blockDispHolderList.get(getCurrentBlockPanel().id).setWinnerBlock(block.getPrevBlockHash());
      }
      
      //create holder panel
      addBlockHolderPanel();
      addBlockToHolderPanel(block);
      pushScrollBarToRight(chainScrollPane, chainScrollPanel);
      
      //wait for splitDelay seconds before propogating
      inLatency = true;
      timerBool = true;
      // System.out.println("in latency");
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          //run through once, wait 2 seconds
          if (timerBool) {
            timerBool = false;
            return;
          }
          inLatency = false;
          timer.cancel();
          timer.purge();
          propogateAndAddBlock(block);
        }
      }, 0, 2000);
      //if gen block
    } else {
      System.out.println("Adding block "+block.id+" to found list and contstructing panel");
      //create holder panel
      addBlockHolderPanel();
      addBlockToHolderPanel(block);
      pushScrollBarToRight(chainScrollPane, chainScrollPanel);
    }

  }
  private void propogateAndAddBlock(Block block) {
    //pause mining so block can be processed and propogated
    globalRun(false);
    //add block to network
    propogateBlock(block);
    System.out.println("Adding block " + block.id + " to found list and contstructing panel");
 
    System.out.println("all ndoes should be at even height here.");
    for (Node node : globalInfo.getNodesList()) {
      System.out.println(node.getChainSize());
    }
    System.out.println();
    //restart mining
    globalRun(true);
  }
  private void addBlockToFoundList(Block block) {
    blocksFoundList.add(block);
  }
  private void refreshChainPanel() {
    //re-construct
    constructChainPanel();
  }
  private void shuffleSplitBlocksBetweenNodes() {
    //randomly assign each node to one of split blocks
    for (Node node : globalInfo.getNodesList()) {
      if (Math.random() < 0.5) {
        node.setWorkingBlock(getCurrentBlockPanel().block1);
      } else {
        node.setWorkingBlock(getCurrentBlockPanel().block2);
      }

    }
  }
  private void propogateBlock(Block block) {
    //add to global chain
    addBlockToFoundList(block);
    //send block to all nodes in network
    for (Node node : globalInfo.getNodesList()) {
      node.receiveBlock(block);
    }
  }

  public void run(Boolean state) {
    running = state;
    Timer timer = new Timer();
    if (state) {
      System.out.println("Simulation started");

    //start miners at even spacing
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        //if final node is not running
        for (Node node : globalInfo.getNodesList()) {
          if (!node.getRunningState()) {
            System.out.println("starting node "+node.getName());
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
    for (Node node : globalInfo.getNodesList()) {
      System.out.println("stopping node");
      node.mine(state);
    }
    }
  }

  private void globalRun(boolean state) {
    for (Node node : globalInfo.getNodesList()) {
      node.setRunningState(state);
    }
  }

class JPanelBlockDisp extends JPanel{
  private static final long serialVersionUID = 1L;
  JPanel buffer1;
  JPanel buffer2; //panel holds block
  JPanel buffer3;

  //store block on disp
  //whichever block 'wins' is set to block
  Block block;
  //holds split blocks temporarily
  Block block1;
  Block block2;

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


    //catch first panel
    if (id == 0) {
      if (getCurrentBlockPanel().id > 0) {  
      g.drawLine(w / 2, h / 2, w + 10, h / 2);            
      }
      return;
    }
    
    //paint first half
    JPanelBlockDisp prevBlockPanel = blockDispHolderList.get(id-1);
    
    //individual - individual
    if (!split) {
      //draw line through center
      g.drawLine(0,h/2,w/2,h/2);
      
      //individual - split
    } else if (!prevBlockPanel.split && split) {
      //draw lines that split from middle
      g.drawLine(0,h/2,w/4,3*(h/4));
      g.drawLine(0,h/2,w/4,h/4);
      g.drawLine(w/4,3*(h/4),w/2,3*(h/4));
      g.drawLine(w/4,h/4,w/2,h/4);
      
      //split - split
    } else if (prevBlockPanel.split && split) {
      String prevBlock1Hash = prevBlockPanel.block1.getHash();
      String prevBlock2Hash = prevBlockPanel.block2.getHash();
      //check each circumstance
      //if follow from block1
      if (this.block1.getPrevBlockHash() == prevBlock1Hash) {
        System.out.println("block1 to block1");
        g.drawLine(0,h/4,w/2,h/4);
      }
      if (this.block1.getPrevBlockHash() == prevBlock2Hash) {
        System.out.println("block1 to block2");
        g.drawLine(0,h/2,w/4,h/4);
        g.drawLine(w/4,h/4,w/2,h/4);
      }
      //if follow from block2
      if (this.block2.getPrevBlockHash() == prevBlock1Hash) {
        System.out.println("block2 to block1");
        g.drawLine(0,h/2,w/4,3*(h/4));
        g.drawLine(w/4,3*(h/4),w/2,3*(h/4));
      }
      if (this.block2.getPrevBlockHash() == prevBlock2Hash) {
        System.out.println("block2 to block2");
        g.drawLine(0,h/4,w/2,h/4);
      }
    }
    
    //end method here if panel is last in list
    try {
      if (!blockDispHolderList.get(this.id+1).isOccupied()) {
        return;
      }
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
    
    //paint second half
    
    //the remaining code is called on the second to last panel in the list
    JPanelBlockDisp nextBlockPanel = blockDispHolderList.get(id+1);
    //individual - individual
    if (!split) {
      //draw line through center
      g.drawLine(w/2,h/2,w,h/2);
      
      //split - individual
    } else if (!nextBlockPanel.split){
      //decide where to draw lines
      if (this.block1.getHash() == nextBlockPanel.block.getPrevBlockHash()){
        //draw lines that close split
        g.drawLine(w/2,h/4,3*(w/4),h/4);
        g.drawLine(3*(w/4),h/4,w,h/2);
      } else {
        g.drawLine(w/2,3*(h/4),3*(w/4),3*(h/4));
        g.drawLine(3*(w/4),3*(h/4),w,h/2);
      }
      //split - split
    } else if (nextBlockPanel.split) {
      String nextBlock1PrevHash = nextBlockPanel.block1.getPrevBlockHash();
      String nextBlock2PrevHash = nextBlockPanel.block2.getPrevBlockHash();
      //flip blocks in nextBlockPanel if necessary
      if ((this.block1.getHash() == nextBlock2PrevHash) && (this.block2.getHash() == nextBlock1PrevHash)){
        //flip blocks
        addBlockDispPanel(this.block2, this.block1);
        //reload variables
        nextBlockPanel = blockDispHolderList.get(id + 1);
        nextBlock1PrevHash = nextBlockPanel.block1.getPrevBlockHash();
        nextBlock2PrevHash = nextBlockPanel.block2.getPrevBlockHash();
      } 
      //check each circumstance
      //if follow from block1
      if (this.block1.getHash() == nextBlock1PrevHash) {
        g.drawLine(w/2,h/4,w,h/4);
      }
      if (this.block1.getHash() == nextBlock2PrevHash) {
        g.drawLine(w/2,h/4,3*(w/4),h/4);
        g.drawLine(3*(w/4),h/4,w,h/2);
      }
      //if follow from block2
      if (this.block2.getHash() == nextBlock1PrevHash) {
        g.drawLine(w/2,3*(h/4),3*(w/4),3*(h/4));
        g.drawLine(3*(w/4),3*(h/4),w,h/2);
      }
      if (this.block2.getHash() == nextBlock2PrevHash) {
        g.drawLine(w/2,3*(h/4),w,3*(h/4));
      }
    }
  }
  
  public void addBlockDispPanel(Block block) {
    this.block = block;
    GridBagConstraints cons = new GridBagConstraints();
    setCons(cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer2.add(block.getDispPanel(), cons);

    occupied = true;
  }
  public void addBlockDispPanel(Block block1, Block block2) {
    this.block1 = block1;
    this.block2 = block2;

    GridBagConstraints cons1 = new GridBagConstraints();
    setCons(cons1,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer1.add(block1.getDispPanel(), cons1);

    GridBagConstraints cons2 = new GridBagConstraints();
    setCons(cons2,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    buffer2.add(block2.getDispPanel(), cons2);

    occupied = true;
  }

  public void setWinnerBlock(String hash) {
    if (hash == this.block1.getHash()) {
      this.block = this.block1;
    } else {
      this.block = this.block2;
    }
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
}
