public class Node {

  public int ID;
  //measured in new hash per second
  private float mineSpeed;
  private boolean running = false;

  public Node(float mineSpeed) {
    this.ID = Simulation.getNodesArraySize();
    this.mineSpeed = mineSpeed;
  }

  public void mineStart(){
    this.running = true;
    while (running) {

    }
  }

  public void mineStop() {
    this.running = false;
  }

  public float getMineSpeed() {
    return this.mineSpeed;
  }
}
