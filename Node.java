import java.awt.*;
import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.event.*;

import java.util.LinkedList;

//testing
public static void main(String[] args) {
  JFrame testFrame = new Jframe;
  testFrame.setSize(600,800);
  Node testNode = new Node(0,node-name,100);
  testPanel = testNode.getPanel();
  testPanel.setBackground(color.green);
  testFrame.add(testPanel);

}

public class Node {

  // nodes version of chain
  public static LinkedList<Block> chain = new LinkedList<Block>();
  //id needs to be changed by main class
  public int id;
  private String name;
  //measured in new hash per second
  private double mineSpeed;
  // private Timer timer = new Timer();
  private Block workingBlock;

  private JPanel panel;

  public Node(String id, String name, String mineSpeed) {
    this.id = Integer.parseInt(id);
    this.setName(name);
    this.mineSpeed = Double.parseDouble(mineSpeed);
    makePanel();
    //init chain with genesis block
    Block block = new Block(this.getChainSize(), "1234");
    chain.add(block);
    // System.out.print("chain = " +chain);
  }

//getter and setters
  public String getName() {
    return this.name;
  }
  private void setName(String name) {
    this.name = name;
  }
  public double getMineSpeed() {
    return this.mineSpeed;
  }

  public double setMineSpeed() {
    return this.mineSpeed;
  }

  public static int getChainSize(){
    return chain.size();
  }

  public static Block getChainLastElement() {
    return chain.getLast();
  }

  private void setChainLastElement(Block block) {
    chain.add(block);
  }

  private void setNewWorkingBlock() {
    workingBlock = new Block(this.getChainSize(), this.getChainLastElement().getNonce());
  }

  public JPanel getPanel() {
    return this.panel;
  }

//methodss
  // public void mine(Boolean state){
  //   if (state) {
  //     setNewWorkingBlock();
  //     //init block
  //     //timer mines
  //     timer.scheduleAtFixedRate(new TimerTask() {
  //       @Override
  //       public void run() {
  //         //check for valid hash
  //         //TODO add log of all attempted hashes
  //         if (checkHash(workingBlock.genHash())) {
  //           //TODO get vaildation from other blocks
  //           //add to chain
  //           setChainLastElement(workingBlock);
  //           System.out.println(getChainLastElement().getNonce());
  //           //start on new block
  //           setNewWorkingBlock();
  //         }
  //       }
  //       //task, delay from creation, time between task executions
  //     }, 0, this.mineSpeed);
  //   } else {
  //     timer.cancel();
  //   }
  //
  // }


  private Boolean checkHash(String hash) {
    return true;
  }

  public void verify(Block block) {
    // if (toString(block.hash)[0]
  }

  private void makePanel() {
    panel = new JPanel();
    panel.setLayout(new GridBagLayout());

      JLabel nameLabel = new JLabel(this.name);

      GridBagConstraints nameLabelCons = new GridBagConstraints();
      setCons(nameLabelCons,1,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(nameLabel, nameLabelCons);

      JLabel idLabel = new JLabel("id:");

      GridBagConstraints idLabelCons = new GridBagConstraints();
      setCons(idLabelCons,3,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(idLabel, idLabelCons);

      JLabel idLabel2 = new JLabel(Integer.toString(this.id));

      GridBagConstraints idLabel2Cons = new GridBagConstraints();
      setCons(idLabel2Cons,4,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      panel.add(idLabel2, idLabel2Cons);

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
