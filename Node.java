import java.util.LinkedList;
import java.util.*;

public class Node {

  // nodes version of chain
  public static LinkedList<Block> chain = new LinkedList<Block>();

  public int ID;
  //measured in new hash per second
  private int mineSpeed;
  private Timer timer = new Timer();
  private Block workingBlock;

  public Node(int mineSpeed) {
    // this.ID = simulation.getNodesArraySize();
    this.mineSpeed = mineSpeed;
    //init chain with genesis block
    Block block = new Block(this.getChainSize(), 1234);
    chain.add(block);
    System.out.print("chain = " +chain);
  }

//getter and setters
  public int getMineSpeed() {
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
  public void mine(Boolean state){
    if (state) {
      setNewWorkingBlock();
      //init block
      //timer mines
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          //check for valid hash
          //TODO add log of all attempted hashes
          if (checkHash(workingBlock.genHash())) {
            //TODO get vaildation from other blocks
            //add to chain
            setChainLastElement(workingBlock);
            System.out.println(getChainLastElement().getNonce());
            //start on new block
            setNewWorkingBlock();
          }
        }
        //task, delay from creation, time between task executions
      }, 0, this.mineSpeed);
    } else {
      timer.cancel();
    }

  }


  private Boolean checkHash(int hash) {
    return true;
  }

  public void verify(Block block) {
    // if (toString(block.hash)[0]
  }
}
