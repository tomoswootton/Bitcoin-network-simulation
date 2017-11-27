import java.awt.*;
import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.event.*;

import java.util.*;
import java.util.LinkedList;
import java.util.Timer;



public class Node {

  //testing
  public static void main(String[] args) {
    JFrame testFrame = new JFrame();
    testFrame.setSize(300,400);

    Node testNode = new Node("0","node-name","0.1", 10.0);
    JPanel testPanel = testNode.getPanel();
    testPanel.setBackground(Color.green);
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

  //list of nodes in network
  private LinkedList<Node> nodesList;

  private JPanel panel;
  private TextArea logText;

  private Timer timer;
  private int timerExecutionTime;
  //used to catch running restart error
  public Boolean runningState;


  public Node(String id, String name, String hash_share, Double mine_speed) {
    this.id = Integer.parseInt(id);
    this.setName(name);
    this.hash_share = hash_share;
    this.mine_speed = mine_speed;
    makePanel();
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

  public JPanel getPanel() {
    return this.panel;
  }

  public void setNodesList(LinkedList<Node> nodesList) {
    this.nodesList = nodesList;
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
    } else {
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
        //check for valid hash
        logText.append(hash+"\n");
        //TODO add log of all attempted hashes

        if (checkHash(hash)) {
          //TODO get vaildation from other blocks


          //add to preview
          logText.append("Valid hash found: "+hash+"\n");

          //add block to chain
          logText.append("Adding block to chain..\n");
          addBlockToChain(workingBlock);

          //propogate
          logText.append("Propogating across network..\n");
          propogateBlock(workingBlock);

          //start on new block
          logText.append("Find new block. id: "+getChainSize()+"\n");
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

    logText.append("Block received. Hash: "+block.getHash()+".\n");
    //check block hash is valid
    if (!checkHash(block.getHash())) {
      logText.append("Invalid block, return to mine block id: "+getChainSize()+"\n");
      return;
    }
    //add to chain
    logText.append("Block verified, adding to chain..\n");
    addBlockToChain(block);

    //continue mine
    logText.append("Find new block. id: "+getChainSize()+"\n");
    setNewWorkingBlock();

    //restart mining
    this.mine(true);
  }

  private void makePanel() {
    panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    //node info
      JLabel nameLabel = new JLabel(this.name);

      GridBagConstraints nameLabelCons = new GridBagConstraints();
      setCons(nameLabelCons,1,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,20,10);
      panel.add(nameLabel, nameLabelCons);

      JLabel idLabel = new JLabel("id: " +Integer.toString(this.id));

      GridBagConstraints idLabelCons = new GridBagConstraints();
      setCons(idLabelCons,3,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,20,10);
      panel.add(idLabel, idLabelCons);

      JLabel hashShareLabel = new JLabel("Hash share: "+this.hash_share);

      GridBagConstraints hashShareLabelCons = new GridBagConstraints();
      setCons(hashShareLabelCons,2,2,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      panel.add(hashShareLabel, hashShareLabelCons);

      JLabel mineSpeedLabel = new JLabel("Hashes per second "+Double.toString(this.mine_speed));

      GridBagConstraints mineSpeedLabelCons = new GridBagConstraints();
      setCons(mineSpeedLabelCons,2,3,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      panel.add(mineSpeedLabel, mineSpeedLabelCons);


    //current block info


    //chain info


    //log
      logText = new TextArea("",8,20,TextArea.SCROLLBARS_BOTH);
      logText.setEditable(false);

      GridBagConstraints logTextCons = new GridBagConstraints();
      setCons(logTextCons,0,6,6,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,100,0);
      panel.add(logText, logTextCons);
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
