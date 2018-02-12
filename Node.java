import java.awt.*;
import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.event.*;

import java.util.*;
import java.util.LinkedList;
import java.util.Timer;

import java.awt.event.ActionListener;



public class Node {

  //testing
  public static void main(String[] args) {
    JFrame testFrame = new JFrame();
    testFrame.setSize(500,500);

    Node testNode = new Node("0","node-name","0.1", 10.0);
    JPanel testPanel = testNode.getNodeDispPanel();


    testFrame.add(testPanel);
    testFrame.setVisible(true);
  }

  // nodes version of chain
  public LinkedList<Block> chain = new LinkedList<Block>();
  //id needs to be changed by main class
  public int id;
  private String name;
  //percentage of total hash rate
  private String hash_share;
  //measured in new hash per second
  private double mine_speed;
  private Block workingBlock;
  private ArrayList<String> log = new ArrayList<String>();
  private int blocks_mined;

  //list of nodes in network
  private LinkedList<Node> nodesList;

  private JPanel nodeDispPanel;
  private JLabel blocksMinedLabel;
  private JPanel logDispPanel;
  private TextArea logText;

  private Timer timer;
  private int timerExecutionTime;
  //stores true if node is mining
  public Boolean runningState;

  //log disp window
  private TextArea logDispTextArea = new TextArea("",8,38,TextArea.SCROLLBARS_BOTH);
  private JFrame logDispWindow = new JFrame();
  private Boolean logDispWindowOpen = false;


  public Node(String id, String name, String hash_share, Double mine_speed) {
    this.id = Integer.parseInt(id);
    this.setName(name);
    this.hash_share = hash_share;
    this.mine_speed = mine_speed;
    this.blocks_mined = 0;
    makeNodeDispPanel();
    //init chain with genesis block
    Block block = new Block(this.getChainSize(), "1234");
    chain.add(block);
    timer = new Timer();
    timerExecutionTime = (int) Math.ceil(1000/this.mine_speed);
  }

//getter and setters
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
  }

  private void setNewWorkingBlock() {
    workingBlock = new Block(getChainSize(), getChainLastElement().getHash());
  }

  public JPanel getNodeDispPanel() {
    return this.nodeDispPanel;
  }

  public JPanel getLogPanel() {
    return this.logDispPanel;
  }

  public void setNodesList(LinkedList<Node> nodesList) {
    this.nodesList = nodesList;
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

//timer methods
  public void mine(Boolean state) {
    //dont start timer if no mine speed, otherwise run() will be run once
    if (mine_speed == 0.0) {
      return;
    }
    //runningState prevents code from receivedBlock() restarting mining when global mining
    //has been paused
    if (state && runningState) {
      //init block
      setNewWorkingBlock();
      startTimer();
    } else if (!state) {
      pauseTimer();
    }
  }

  private void startTimer() {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        workingBlock.newNonce();
        String hash = workingBlock.genHash();
        //add to Log
        addToLog(hash+"\n");

        //check for valid hash
        if (checkHash(hash)) {
          //TODO get vaildation from other blocks
          blockFound(hash);
          setNewWorkingBlock();
        }
      }
    }, 0, timerExecutionTime);
  }

  private void pauseTimer() {
    // System.out.println("timer "+this.id+" stopped.");
    timer.cancel();
    timer.purge();
  }

  //methods

  private Boolean checkHash(String hash) {
    //nonce must by less than 4 digits
    if (Integer.parseInt(hash) < 1000) {
      return true;
    }
    return false;
  }

  private void blockFound(String hash) {
    //add to preview
    addToLog("\nValid hash found: "+hash+"\n");
    //add block to chain
    addToLog("Adding block to chain..\n");
    addBlockToChain(workingBlock);
    //propogate
    addToLog("Propogating across network..\n");
    propogateBlock(workingBlock);
    //start on new block
    addToLog("Find new block. id: "+getChainSize()+"\n\n");
    //update nodeDispPanel
    blocks_mined += 1;
    blocksMinedLabel.setText(Integer.toString(blocks_mined));

  }

  private void propogateBlock(Block block) {
    //send block to all nodes in network, apart from self
    for (Node node : nodesList) {
      if (node.id != this.id) {
        node.receiveBlock(block);
      }
    }
  }

  public void receiveBlock(Block block) {

    //pause mining for execution of new block code
    this.mine(false);

    addToLog("\nBlock received. Hash: "+block.getHash()+".\n");
    //check block hash is valid
    if (!checkHash(block.getHash())) {
      addToLog("Invalid block, return to mine block id: "+getChainSize()+"\n");
      return;
    }
    //add to chain
    addToLog("Block verified, adding to chain..\n");
    addBlockToChain(block);

    //continue mine
    addToLog("Find new block. id: "+getChainSize()+"\n\n");
    setNewWorkingBlock();

    //restart mining
    this.mine(true);
  }

  public void makeNodeDispPanel() {

    nodeDispPanel = new JPanel(new GridBagLayout());
    nodeDispPanel.setBorder(BorderFactory.createLineBorder(Color.black));

      //check for name length too long
      String name = this.name;
      if (name.length() > 10) {
        name = name.substring(0,10) + "..";
      }

      JLabel nameLabel = new JLabel(name);
      JPanel namePanel = new JPanel();
      namePanel.setPreferredSize(new Dimension(30,20));
      namePanel.setMaximumSize(new Dimension(30,20));

      namePanel.add(nameLabel);

      GridBagConstraints namePanelCons = new GridBagConstraints();
      setCons(namePanelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,100,10);
      nodeDispPanel.add(namePanel, namePanelCons);

      JLabel idLabel = new JLabel(Integer.toString(this.id));
      JPanel idPanel = new JPanel();
      idPanel.setPreferredSize(new Dimension(5,20));
      idPanel.setMaximumSize(new Dimension(50,20));
      idPanel.add(idLabel);

      GridBagConstraints idPanelCons = new GridBagConstraints();
      setCons(idPanelCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,30,10);
      nodeDispPanel.add(idPanel, idPanelCons);

      JLabel hashShareLabel = new JLabel(this.hash_share);
      JPanel hashSharePanel = new JPanel();
      hashSharePanel.setPreferredSize(new Dimension(10,20));
      hashSharePanel.setMaximumSize(new Dimension(50,20));
      hashSharePanel.add(hashShareLabel);

      GridBagConstraints hashSharePanelCons = new GridBagConstraints();
      setCons(hashSharePanelCons,2,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,30,10);
      nodeDispPanel.add(hashSharePanel, hashSharePanelCons);

      blocksMinedLabel = new JLabel("0");
      JPanel blocksMinedPanel = new JPanel();
      blocksMinedPanel.setPreferredSize(new Dimension(10,20));
      blocksMinedPanel.setMaximumSize(new Dimension(50,20));

      blocksMinedPanel.add(blocksMinedLabel);

      GridBagConstraints blocksMinedPanelCons = new GridBagConstraints();
      setCons(blocksMinedPanelCons,3,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,30,10);
      nodeDispPanel.add(blocksMinedPanel, blocksMinedPanelCons);

      JButton viewLogButton = new JButton("view Log");
      viewLogButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == viewLogButton) {
            dispLogDispWindow();
          }
        }
      });

      JPanel viewLogButtonPanel = new JPanel();
      viewLogButtonPanel.setPreferredSize(new Dimension(10,20));
      viewLogButtonPanel.setMaximumSize(new Dimension(50,20));
      viewLogButtonPanel.add(viewLogButton);

      GridBagConstraints viewLogButtonPanelCons = new GridBagConstraints();
      setCons(viewLogButtonPanelCons,4,0,1,1,GridBagConstraints.BOTH,GridBagConstraints.CENTER,100,20);
      nodeDispPanel.add(viewLogButtonPanel, viewLogButtonPanelCons);
  }

  //makes window that displays log
  private void dispLogDispWindow() {
    //make window each time
    logDispWindow.setSize(250,300);
    logDispWindow.setMinimumSize(new Dimension(100,200));
    logDispWindow.setLocationRelativeTo(null);
    logDispWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    logDispWindow.setTitle("Log");
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

  //method sets GridBagConstraints variables
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
