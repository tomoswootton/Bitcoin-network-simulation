import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.*;
import java.util.Timer;

public class Simulation {

  JFrame simulationFrame = new JFrame();
  JFrame simulationControlFrame = new JFrame();
  GlobalInfo globalInfo;

  // page variables
  Boolean running = false;
  Boolean inLatency = false;
  Boolean timerBool = false;

  //swing
  JPanel page;
  JPanel nodesPanel;
  JPanel nodesScrollPanel;
  JPanel chainPanel;
  JPanel chainScrollPanel;
  JScrollPane chainScrollPane;
  ArrayList<JPanelBlockDisp> blockDispHolderList;
  ArrayList<JTextFieldWithID> userInputTextFields = new ArrayList<JTextFieldWithID>(); 
  ArrayList<JButtonWithID> userInputButtons = new ArrayList<JButtonWithID>(); 
  JButtonWithID addBlockButton;
  JButtonWithID startNodeButton;
  JButtonWithID stopNodeButton;

  //display on control panel
  JLabel averageFindLabel;
  double averageFindTime;
  Queue<Double> averageFindTimeQueue = new LinkedList<Double>();
  ArrayList<Double> averageFindTime10BlockAverages = new ArrayList<Double>();
  
  JButton startPauseButton;
  JButton refreshAllButton;
  JButton exitButton;


  //list of blocks foundBlocks
  private LinkedList<Block> blocksFoundList = new LinkedList<Block>();

  //construtors
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
  private void setRunningVar(boolean state) {
    this.running = state;
    if (!state) {
      startPauseButton.setText("Start");
    } else {
      startPauseButton.setText("Pause");
    }
  }

  //JSwing stuff
  private void makePage() {
    if (page != null) {
      page.removeAll();
    }
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

   //control
   constructControlPanel();


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
              setRunningVar(false);
              run(false);
            } else {
              setRunningVar(true);
              run(true);
            }
          }
        }
      });

      GridBagConstraints startPauseButtonCons = new GridBagConstraints();
      setCons(startPauseButtonCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      buttonsPanel.add(startPauseButton, startPauseButtonCons);

      refreshAllButton = new JButton("Refresh");
      refreshAllButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshAllButton) {
          refreshAll();
        }
      }
    });

    GridBagConstraints refreshAllButtonCons = new GridBagConstraints();
    setCons(refreshAllButtonCons, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0);
    buttonsPanel.add(refreshAllButton, refreshAllButtonCons);

      exitButton = new JButton("Exit");
      exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == exitButton) {
            //stop mining
            if (running) {
              run(false);
            }
            simulationFrame.dispose();
          }
        }
      });

      GridBagConstraints exitButtonCons = new GridBagConstraints();
      setCons(exitButtonCons, 2,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      buttonsPanel.add(exitButton, exitButtonCons);

    GridBagConstraints buttonsPanelCons = new GridBagConstraints();
    setCons(buttonsPanelCons,1,8,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttonsPanel, buttonsPanelCons);

    simulationFrame.add(page);
    simulationFrame.setVisible(true);
  }
  private void constructControlPanel() {
    JPanel controlPanel = new JPanel(new GridBagLayout());
    controlPanel.setMinimumSize(new Dimension(700, 200));
    controlPanel.setPreferredSize(new Dimension(700, 200));

    
    //titles
    JPanel controlTitlePanel = new JPanel(new GridBagLayout()); 
    controlTitlePanel.setPreferredSize(new Dimension(700,50));
    JLabel controlTitle = new JLabel("<HTML><U>Node Control Panel</U></HTML>");
    controlTitle.setFont(controlTitle.getFont().deriveFont(16.0f));
    GridBagConstraints titlePlacCons = new GridBagConstraints();
    setCons(titlePlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    controlTitlePanel.add(controlTitle, titlePlacCons);

    GridBagConstraints controlTitlePanelCons = new GridBagConstraints();
    setCons(controlTitlePanelCons, 0,0,3,1,GridBagConstraints.BOTH,GridBagConstraints.CENTER,10,10);
    controlPanel.add(controlTitlePanel, controlTitlePanelCons);

    makeUserInputPanelButtonTextField("addBlock", "Force block find by node:","0",controlPanel, 1,1);
    makeUserInputPanelButtonTextField("startNode", "Force start node:","",controlPanel, 1,2);
    makeUserInputPanelButtonTextField("stopNode", "Force stop node:","",controlPanel, 1,3);

    makeUserInputButtonListeners();

    averageFindLabel = new JLabel("Average 10 block find time: " + this.averageFindTime + "s");
    GridBagConstraints averageFindLabelCons = new GridBagConstraints();
    setCons(averageFindLabelCons, 1, 4, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 10);
    controlPanel.add(averageFindLabel, averageFindLabelCons);

    GridBagConstraints controlPanelCons = new GridBagConstraints();
    setCons(controlPanelCons, 0, 3, 5, 2, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0);
    page.add(controlPanel, controlPanelCons);
  }

  private void makeUserInputPanelButtonTextField(String id, String labelText,String textFieldText, JPanel panelToAddTo, int x, int y) {
    JPanel panel = new JPanel(new GridBagLayout());
    //label
      JPanel labelPanel = new JPanel(new GridBagLayout());
      labelPanel.setMinimumSize(new Dimension(350,20));
      labelPanel.setPreferredSize(new Dimension(350,20));

      GridBagConstraints labelPlacCons = new GridBagConstraints();
      setCons(labelPlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,0,0);
      JLabel label = new JLabel(labelText);
      labelPanel.add(label, labelPlacCons);

      GridBagConstraints labelCons = new GridBagConstraints();
      setCons(labelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(labelPanel, labelCons);

      //text field
      JPanel textFieldPanel = new JPanel(new GridBagLayout());
      textFieldPanel.setMinimumSize(new Dimension(100,20));
      textFieldPanel.setPreferredSize(new Dimension(100,20));
      
      GridBagConstraints textFieldPlacCons = new GridBagConstraints();
      setCons(textFieldPlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      JTextFieldWithID textField = new JTextFieldWithID(textFieldText, id);
      textField.setColumns(2);
      textFieldPanel.add(textField, textFieldPlacCons);
      //add to array of input text fields
      userInputTextFields.add(textField);
      
      GridBagConstraints textFieldCons = new GridBagConstraints();
      setCons(textFieldCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(textFieldPanel, textFieldCons);

      //button
      JPanel buttonPanel = new JPanel(new GridBagLayout());
      buttonPanel.setMinimumSize(new Dimension(350,20));
      buttonPanel.setPreferredSize(new Dimension(350,20));

      GridBagConstraints buttonPlacCons = new GridBagConstraints();
      setCons(buttonPlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,0,0);
      JButtonWithID button = new JButtonWithID("Apply",id);
      buttonPanel.add(button, buttonPlacCons);
      //add button to array of user input buttons
      userInputButtons.add(button);

      GridBagConstraints buttonPanelCons = new GridBagConstraints();
      setCons(buttonPanelCons,2,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(buttonPanel, buttonPanelCons);

    GridBagConstraints cons = new GridBagConstraints();
    setCons(cons,x,y,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    panelToAddTo.add(panel, cons);

  }
  private void makeUserInputButtonListeners() {
    //make action listeners for control panel buttons
    //force block
    addBlockButton = userInputButtons.get(0);
    addBlockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBlockButton) {
          int textFieldText;
          try {
            textFieldText = Integer.parseInt(userInputTextFields.get(0).getText());
          }
          catch(NumberFormatException ex) {
            textFieldText = -1;
          }
          //first ensure user has specified node
          if (textFieldText < 0 || textFieldText > globalInfo.nodesListSize()-1 || globalInfo.nodesListSize() == 0) {
            JOptionPane.showMessageDialog(simulationFrame,"Invalid node id","Error",JOptionPane.PLAIN_MESSAGE);
            return;
          }
          //find blocks prevBlockHash
          String prevBlockHash;
          //if in split
          try {
            if (inLatency) {
              //use prev panel block
              if (blockDispHolderList.get(getCurrentBlockPanel().id - 1).split) {
                prevBlockHash = blockDispHolderList.get(getCurrentBlockPanel().id - 1).block2.getHash();
              } else {
                prevBlockHash = blockDispHolderList.get(getCurrentBlockPanel().id - 1).block.getHash();
              }
            } else {
              prevBlockHash = blockDispHolderList.get(getCurrentBlockPanel().id).block.getHash();
            }
          } catch (NullPointerException ex) {
            System.out.println("ERROR: Refresh all called by addBlockButton");
            return;
          }

          //make block
          Block fakeBlock = new Block(globalInfo, blocksFoundList.size(), prevBlockHash, globalInfo.getNode(textFieldText).getName());
          fakeBlock.setForcedHash();
          //add to node
          globalInfo.getNode(textFieldText).forceBlockFound(fakeBlock);
        }
      }
    });

    //force start node
    startNodeButton = userInputButtons.get(1);
    startNodeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startNodeButton) {
          int textFieldText;
          try {
            textFieldText = Integer.parseInt(userInputTextFields.get(1).getText());
          }
          catch(NumberFormatException ex) {
            textFieldText = -1;
          }
          //first ensure user has specified node
          if (textFieldText < 0 || textFieldText > globalInfo.nodesListSize()-1 || globalInfo.nodesListSize() == 0) {
            JOptionPane.showMessageDialog(simulationFrame,"Invalid node id","Error",JOptionPane.PLAIN_MESSAGE);
            return;
          }
          globalInfo.getNode(textFieldText).addToLog("\nForce restarted by simulation.\n\n");
          startNode(textFieldText);
        }
      }
    });

    // force stop
    stopNodeButton = userInputButtons.get(2);
    stopNodeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //open input window
        if (e.getSource() == stopNodeButton) {
          int textFieldText;
          try {
            textFieldText = Integer.parseInt(userInputTextFields.get(2).getText());
          }
          catch(NumberFormatException ex) {
            textFieldText = -1;
          }
          //first ensure user has specified node
          if (textFieldText < 0 || textFieldText > globalInfo.nodesListSize()-1 || globalInfo.nodesListSize() == 0) {
            JOptionPane.showMessageDialog(simulationFrame,"Invalid node id","Error",JOptionPane.PLAIN_MESSAGE);
            return;
          }
          //add force stop to nodes log
          globalInfo.getNode(textFieldText).addToLog("\nForce stopped by simulation.\n\n");
          stopNode(textFieldText);
        }
      }
    });
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

      JLabel titleLabel = new JLabel("                         Name                   id         power      blocks                                                  ");

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

    GridBagConstraints chainCons = new GridBagConstraints();
    setCons(chainCons, 0,5,5,3,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
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
    try{
      //add block dispPanel to holder panel
      blockDispHolderList.get(block.id).addBlockDispPanel(block);

      //adjust size of panel to allow for new block
      chainScrollPanel.setSize(new Dimension((block.id-9)*220 + 1100,220));
    }
    catch (IndexOutOfBoundsException ex) {
      refreshAll();
      System.out.println("refresh all called by addBlockToHolderPanel indexOutOfBounds");
    }
  }
  private void splitChain(Block block) {
    System.out.println("Chain Split. block "+block.id);
    //var lets other blocks build on split chain
    inLatency = false;

    //make currenct panel split, removing previous block
    JPanelBlockDisp current = getCurrentBlockPanel();
    current.makeSplitLayout();

    //add previous block and newly found block to split panel
    addBlocksToSplitPanel(current, getCurrentBlockPanel().block,block);

    current.revalidate();
    current.repaint();

    //send blocks across network
    shuffleSplitBlocksBetweenNodes();

    pushScrollBarToRight(chainScrollPane, chainScrollPanel);
  }
  private void addBlocksToSplitPanel(JPanelBlockDisp panel, Block block1, Block block2) {
    //add blocks
    try {
      panel.addBlockDispPanel(block1, block2);
    } catch (NullPointerException ex) {
      refreshAll();
      System.out.println("Refresh all called by addBlockDispPanel nullPointerExeption");
      
    }
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
      addBlockHolderPanel();
      addBlockToHolderPanel(block);
    }
    pushScrollBarToRight(chainScrollPane, chainScrollPanel);

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
  public synchronized void addBlockToGlobalChain(Block block) {
    //check block is of corret heigh
    if (block.id != blocksFoundList.size()) {
      refreshAll();
      System.out.println("refresh all called by addBlockToGlobalChain");
      
      return;
    }
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
        Block prevBlock = getBlockFromHash(block.getPrevBlockHash());
        blockDispHolderList.get(getCurrentBlockPanel().id).setWinnerBlock(prevBlock);
        updateNodesOfSplitwinner(prevBlock);
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
  private Block getBlockFromHash(String hash) {
    for (Block block : blocksFoundList) {
      if (block.getHash() == hash) {
        return block;
      }
    }
    System.out.println("ERROR: no hash match");
    return null;
  }
  private void propogateAndAddBlock(Block block) {
    //pause mining threads so block can be processed and propogated
    globalRun(false);
    //add block to network
    propogateBlock(block);
    System.out.println("Adding block " + block.id + " to found list and contstructing panel");
 
    // System.out.println("all nodes should be at even height here.");
    // for (Node node : globalInfo.getNodesList()) {
    //   System.out.println(node.getChainSize());
    // }
    
    //add to global chain
    addBlockToFoundList(block);
    updateAverageFindTimeLast10Blocks(block);
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
    //send block to all nodes in network
    for (Node node : globalInfo.getNodesList()) {
      node.receiveBlock(block);
    }
  }

  //running
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
            node.mine(state,true);
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
      System.out.println("stopping node "+node.getName());
      node.mine(state, true);
    }
    }
  }
  private void startNode(int id) {
    
    for (Node node : globalInfo.getNodesList()) {
      if (node.id == id) {
        System.out.println("Force starting node "+node.id);        
        node.userAllowRunning = true;
        node.mine(true,false);
      }
    }
  }
  private void stopNode(int id) {
    for (Node node : globalInfo.getNodesList()) {
      if (node.id == id) {
        System.out.println("Force stopping node "+node.id);
        node.userAllowRunning = false;
      }
    }
  }
  private void globalRun(boolean state) {
    //pause or unpause mining threads immmediatelty globally
    for (Node node : globalInfo.getNodesList()) {
      node.mine(state, false);
    }
  }
  private void updateNodesOfSplitwinner(Block prevBlock) {
    //set prev block in its corrcet position in each nodes chain
    for (Node node : globalInfo.getNodesList()) {
      node.setBlockInChain(prevBlock);
    }
  }

  //general
  private void updateAverageFindTimeLast10Blocks(Block block) {    
    int averageFindTimeSize = averageFindTimeQueue.size();
    //if full remove last element
    if (averageFindTimeSize == 10) {
      averageFindTimeQueue.remove();
    }
    //add new time to queue
    averageFindTimeQueue.add(block.getTimeElapsed());
    //calc average
    double sum = 0.0;
    for (Double time : averageFindTimeQueue) {
      sum = sum + time;
    }
    this.averageFindTime = Math.floor(((sum) / averageFindTimeSize)*100)/100;  
    averageFindLabel.setText("Average block find time (seconds): " + this.averageFindTime);
    if (block.id % 10 == 0) {
      //add 10 block average to list
      averageFindTime10BlockAverages.add(averageFindTime);
      adjustDifficulty();
    }
  } 
  private void adjustDifficulty() {
    //method uses value of previous 10 blocks average find time and compares to desired find time.
    double old_target = globalInfo.getTarget();
    double new_target;
    double desired_target = globalInfo.getDesiredTarget();
    double averageFindTime = averageFindTime10BlockAverages.get(averageFindTime10BlockAverages.size()-1);
    double desiredFindTime = globalInfo.getDesiredAverage();
      //alter target with new_target = (average/desired)*old_target
      new_target = Math.floor(((averageFindTime/desiredFindTime)*old_target)*100)/100;

    //enforce 4x rule
    if (new_target / 4 > old_target) {
      System.out.println("x4 upwards");
      new_target = old_target*4;
    }
    if (new_target*4 < old_target) {
      System.out.println("x4 downwards");      
      new_target = old_target/4;
    }
    System.out.println("averageFindtime: "+averageFindTime+"\nold target: "+old_target+"\nnew target: "+new_target);
    globalInfo.setTarget(new_target);
    
    //print to consol
    System.out.println("Difficulty adjustment: target set to "+(int) new_target);
    //print to nodes's logs
    for (Node node : globalInfo.getNodesList()) {
      node.addNewTargetToLog(averageFindTime10BlockAverages.get(averageFindTime10BlockAverages.size() - 1));
    }
  }
  private void refreshAll() {
    globalRun(false);
    //method reassigns each nodes chain to current global chain, revalidatd and repaints chain
    for (Node node : globalInfo.getNodesList()) {
      node.setChain(this.blocksFoundList);
    }
    chainScrollPane.revalidate();
    chainScrollPane.repaint();
    chainScrollPanel.revalidate();
    chainScrollPanel.repaint();
    globalRun(true);
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
        g.drawLine(0,h/4,w/2,h/4);
      }
      if (this.block1.getPrevBlockHash() == prevBlock2Hash) {
        g.drawLine(0,h/2,w/4,h/4);
        g.drawLine(w/4,h/4,w/2,h/4);
      }
      //if follow from block2
      if (this.block2.getPrevBlockHash() == prevBlock1Hash) {
        g.drawLine(0,h/2,w/4,3*(h/4));
        g.drawLine(w/4,3*(h/4),w/2,3*(h/4));
      }
      if (this.block2.getPrevBlockHash() == prevBlock2Hash) {
        g.drawLine(0,3*(h/4),w/2,3*(h/4));
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
    
    //the followinging code is called on the second to last panel in the list
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
    buffer2.removeAll();
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

  public void setWinnerBlock(Block block) {
    if (block == this.block1) {
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
