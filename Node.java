import java.awt.*;
import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.event.*;

import java.util.*;
import java.util.LinkedList;
import java.util.Timer;

import java.awt.event.ActionListener;

public class Node {

  //simulation object for methods access
  Simulation simulation;
  GlobalInfo globalInfo;
  
  //thread
  Thread thread;
  //stores true if node is mining
  volatile boolean isRunning;
  boolean userAllowRunning = true;

  int id;
  String name;
  //percentage of total hash rate
  String hash_share;
  //measurement: number of new hashes per second
  double mine_speed;
  Block workingBlock;
  ArrayList<String> log;
  int blocks_mined;
  int hashSize;

  // nodes version of chain
  LinkedList<Block> chain;

  JPanel nodeDispPanel;
  JLabel blocksMinedLabel;
  JPanel logDispPanel;
  TextArea logText;

  Timer timer;
  int timerExecutionTime;

  //log disp window
  TextArea logDispTextArea = new TextArea("",8,38,TextArea.SCROLLBARS_BOTH);
  JFrame logDispWindow = new JFrame();
  Boolean logDispWindowOpen = false;

  //constructors
  public static void main(String[] args) {
    //testing only
    JFrame testFrame = new JFrame();
    testFrame.setSize(500,500);
    testFrame.setLayout(new FlowLayout());
    testFrame.setVisible(true);
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    new Node(testFrame);
  }
  public Node(JFrame testFrame) {
    this.timerExecutionTime = 2000;
    JButton button = new JButton("mine");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
          thread = new Thread(new Mine());
          thread.start();
        }
      }
    });
    testFrame.add(button);
    
  }
  public Node(GlobalInfo globalInfo, String name, String hash_share, Double mine_speed) {
    System.out.println("new block hash share: "+hash_share+" mine_speed: "+mine_speed);
    this.globalInfo = globalInfo;
    this.id = globalInfo.nodesListSize();
    this.setName(name);
    this.hash_share = hash_share;
    this.mine_speed = mine_speed;
    this.blocks_mined = 0;
    makeNodeDispPanel();
    reset();
    
    //set timerExecTime variable
    if (mine_speed == 0) {
      timerExecutionTime = 0;
    } else if (mine_speed < 1) {
      //for mine_speeds < 1
      timerExecutionTime = (int) Math.floor(1000*(1/this.mine_speed));
    } else {
      //else simply divide 1 second by mine_speed
      timerExecutionTime = (int) Math.floor(1000/this.mine_speed);
    }
  }

  //getter and setters
  public void setSimulationObject(Simulation simulation) {
    this.simulation = simulation;
  }
  public String getName() {
    return this.name;
  }
  private void setName(String name) {
    this.name = name;
  }
  public String getHashShare() {
    return this.hash_share;
  }
  public void setHashShare(String hash_share) {
    this.hash_share = hash_share;
  }
  public double getMineSpeed() {
    return this.mine_speed;
  }
  public double setMineSpeed() {
    return this.mine_speed;
  }
  public int getChainSize(){
    return chain.size();
  }
  public Block getChainLastElement() {
    return chain.getLast();
  }
  private void addBlockToChain(Block block) {
    chain.add(block);
    //check if chain behind, if so get global chain from simulation
    if (chain.size() < block.id) {
      chain = simulation.getBlocksFoundList();
    }
  }
  public JPanel getDispPanel() {
    return this.nodeDispPanel;
  }
  public JPanel getLogPanel() {
    return this.logDispPanel;
  }
  public LinkedList<Block> getChain() {
    return this.chain;
  }
  public void setChain(LinkedList<Block> chain) {
    this.chain = chain;
  }
  public void setRunningState(boolean state) {
    if (isRunning != state) {
      isRunning = state;
    }
  }
  public boolean getRunningState() {
    return this.isRunning;
  }

  public void reset(){
    this.chain = new LinkedList<Block>();
    //init chain with genesis block
    GenBlock genBlock = new GenBlock(globalInfo, 0, "1234", null);
    chain.add(genBlock);
    this.log =  new ArrayList<String>();
  }


  //log methods
  private void addToLog(String string) {
    this.log.add(string);
    //if log is open, append to text area aswell as log array
    if (logDispWindowOpen) {
      logDispTextArea.append(string);
    }
  }
  private void printLog(TextArea textArea) {
    for (String string : log) {
      textArea.append(string);
    }
  }
  public void addNewTargetToLog() {
    addToLog("\nDifficulty adjustment.\nNew target: " + String.valueOf(globalInfo.getTarget())+"\n");
  }
  
  //mine methods
  public void mine(Boolean state, Boolean giveOutput) {
    System.out.println("putting node "+this.id+" in state "+state);
    //if running stopped manually in simulation window
    if (!userAllowRunning) {
      this.setRunningState(state);
      return;
    }
    if (state) {
      
      //dont start timer if no mine speed, otherwise run() will be run once
      if (mine_speed == 0.0) {
        return;
      }

      //init block
      setNewWorkingBlock();
      
      if (giveOutput) {
      addToLog("\nMining started\n\n");
      }
      
      // make thread and start
      thread = new Thread(new Mine());
      thread.start();
    } else {
      if(giveOutput) {
        addToLog("\nMining paused\n");        
      }
      try {
        thread.interrupt();
      } catch (NullPointerException ex) {}
    }
    this.setRunningState(state);
    // System.out.println("node "+this.id+" states: isrunning "+this.isRunning+" userAllowRunning "+this.userAllowRunning);
  }
  private void setNewWorkingBlock() {
    workingBlock = new Block(globalInfo, getChainSize(), getChainLastElement().getHash(), name);
  }
  public void setWorkingBlock(Block block) {
    //method replaces block node is working on with parameter node
    this.workingBlock = block;
    System.out.println("working block changed to block "+block.id);
  }
  private Boolean checkHash(String hash) {
    //nonce must by less than 4 digits
    if (Integer.parseInt(hash) < globalInfo.getTarget()) {
      return true;
    }
    return false;
  }
  private void blockFound() {
    thread.interrupt();
    //set time found
    workingBlock.setTimeFound(globalInfo.getTime());
    //propogate
    simulation.addBlockToGlobalChain(workingBlock);
  }
  public void receiveBlock(Block block) {
    //if found by this
    if (block.getFoundBy() == this.name) {
      //add to preview
      addToLog("\nValid hash found: "+workingBlock.getHash()+"\n");
      addToLog("Propogating across network..\n");
      //update nodeDispPanel
      blocks_mined += 1;
      blocksMinedLabel.setText(Integer.toString(blocks_mined));
    } else {
      addToLog("\nBlock found by: "+block.getFoundBy()+".\n");
    }
    //check block hash is valid
    if (!checkHash(block.getHash())) {
      addToLog("Invalid block, return to mine block id: "+getChainSize()+"\n");
      return;
    }
    //add to chain
    addToLog("Block verified, adding to chain..\n");
    addBlockToChain(block);
    //continue mine
    addToLog("Find new block. id: "+getChainSize()+"\n");
    setNewWorkingBlock();
  }
  public void forceBlockFound(Block block) {
    //halt mining
    try {
      thread.interrupt();
    } catch (NullPointerException ex) {
      System.out.println("ERROR: cannot force block upon non-mining node");
      return;
    }
    //set working block
    setWorkingBlock(block);
    //run blockFound 
    blockFound();
    System.out.println("Block "+block.id+" forced on node "+this.id+".");
  }

  //node disp
  public void makeNodeDispPanel() {
    nodeDispPanel = new JPanel(new GridBagLayout());
    nodeDispPanel.setPreferredSize(new Dimension(495,40));
    // nodeDispPanel.setBorder(BorderFactory.createLineBorder(Color.black));

      //check for name length too long
      String name = this.name;
      if (name.length() > 10) {
        name = name.substring(0,10) + "..";
      }

      JLabel nameLabel = new JLabel(name);
      JPanel nameLabelPanel = new JPanel();
      nameLabelPanel.add(nameLabel);
      nameLabelPanel.setPreferredSize(new Dimension(100,40));

      GridBagConstraints namePanelCons = new GridBagConstraints();
      setCons(namePanelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      nodeDispPanel.add(nameLabelPanel, namePanelCons);

      JLabel idLabel = new JLabel(Integer.toString(this.id));

      GridBagConstraints idPanelCons = new GridBagConstraints();
      setCons(idPanelCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      nodeDispPanel.add(idLabel, idPanelCons);

      JLabel hashShareLabel = new JLabel(this.hash_share);

      GridBagConstraints hashSharePanelCons = new GridBagConstraints();
      setCons(hashSharePanelCons,2,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      nodeDispPanel.add(hashShareLabel, hashSharePanelCons);

      blocksMinedLabel = new JLabel("0");

      GridBagConstraints blocksMinedPanelCons = new GridBagConstraints();
      setCons(blocksMinedPanelCons,3,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      nodeDispPanel.add(blocksMinedLabel, blocksMinedPanelCons);

      JButton viewLogButton = new JButton("view Log");
      viewLogButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == viewLogButton) {
            dispLogDispWindow();
          }
        }
      });

      GridBagConstraints viewLogButtonPanelCons = new GridBagConstraints();
      setCons(viewLogButtonPanelCons,4,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      nodeDispPanel.add(viewLogButton, viewLogButtonPanelCons);
  }


  //log
  private void dispLogDispWindow() {
    //makes window that displays log
    //window made each time
    logDispWindow.setSize(250,300);
    logDispWindow.setMinimumSize(new Dimension(100,200));
    logDispWindow.setLocationRelativeTo(null);
    logDispWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    logDispWindow.setTitle(name+" log");
    logDispWindow.setAlwaysOnTop(true);
    logDispWindowOpen = true;
    logDispWindow.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        logDispWindowOpen = false;
      }
    });

    //clear incase window has already been open
    logDispTextArea.setText("");
    logDispTextArea.setEditable(false);
    logDispWindow.add(logDispTextArea);

    printLog(logDispTextArea);

    logDispWindow.setVisible(true);
  }

  //other methods
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

  class Mine implements Runnable {

    public void run() {
      isRunning = true;
      //isRunning is node, userAllowrunning is user input on simulation, simulation.running is simulation wide running
      while (isRunning && userAllowRunning && simulation.running) {
        try {
          setNewWorkingBlock();
          workingBlock.newNonce();
          String hash = workingBlock.genHash();
          //add to Log
          addToLog(hash + "\n");
          //check for valid hash
          if (checkHash(hash)) {
              blockFound();
          }
          Thread.sleep(timerExecutionTime);
        } catch (InterruptedException ex) {
          isRunning = false;
        }
      }
    }

  }
  
}

