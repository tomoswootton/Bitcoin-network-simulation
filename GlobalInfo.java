import java.util.LinkedList;
import java.util.Date;

class GlobalInfo {
  //class holds data that all others may require
  int hashSize;
  double target;
  int hashPerSec;
  double desiredAverage;
  double desiredTarget;
  Date date = new Date();;

  private LinkedList<Node> nodesList = new LinkedList<Node>();

  public GlobalInfo(int hashSize, int target, int hashPerSec) {
    this.hashSize = hashSize;
    this.target = (double) target;
    this.hashPerSec = hashPerSec;
    //var holds desired average block find time, based off of inital input values above
    //used for difficulty adjustment: compare this value to average find time
    this.desiredAverage = (hashSize/target)*hashPerSec;
    this.desiredTarget = target;
  }
  public LinkedList<Node> getNodesList() {
    return nodesList;
  }
  public Node getNode(int i) {
    return nodesList.get(i);
  }
  public void addToNodesList(Node node) {
    nodesList.add(node);
  }
  public void removeFromNodesList(Node node) {
    nodesList.remove(node);
  }
  public void clearNodeList() {
    nodesList.clear();
  }
  public int nodesListSize() {
    if (nodesList == null) {
      return 0;
    }
    return nodesList.size();
  }
  public int getHashSize() {
    return this.hashSize;
  }
  public void setHashSize(int hashSize) {
    this.hashSize = hashSize;
  }
  public double getTarget() {
    return this.target;
  }
  public void setTarget(double target) {
    this.target = target;
  }
  public int getHashPerSec() {
    return this.hashPerSec;
  }
  public void setHashPerSec(int hashPerSec) {
    this.hashPerSec = hashPerSec;
    System.out.println("hash per sec changed "+hashPerSec);
  }
  public double getDesiredAverage() {
    return this.desiredAverage;
  }
  public long getTime() {
    date = new Date();
    return this.date.getTime();
  }
  public double getDesiredTarget() {
    return this.desiredTarget;
  }
}
