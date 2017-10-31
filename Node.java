import java.util.LinkedList;
import java.util.*;

public class Node {

  // nodes version of chain
  public static LinkedList<Block> chain = new LinkedList<Block>();
  //id needs to be changed by main class
  public int id;
  private String name;
  //measured in new hash per second
  private double mineSpeed;
  private Timer timer = new Timer();
  private Block workingBlock;

  public Node(String id, String name, String mineSpeed) {
    this.id = Integer.parseInt(id);
    this.setName(name);
    this.mineSpeed = Double.parseDouble(mineSpeed);
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
}
