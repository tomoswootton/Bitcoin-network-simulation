import java.util.LinkedList;
import java.util.Date;

class GlobalInfo {
  //class holds data that all others may require
  int maxTarget;
  double target;
  double difficulty;
  int hashPerSec;
  double desiredAverage;
  double desiredTarget;
  Date date = new Date();;

  private LinkedList<Node> nodesList = new LinkedList<Node>();

  public GlobalInfo(int maxTarget, int target, int hashPerSec) {
    this.maxTarget = maxTarget;
    this.target = (double) target;
    this.difficulty = maxTarget/target;
    this.hashPerSec = hashPerSec;
    //var holds desired average block find time, based off of inital input values above
    //used for difficulty adjustment: compare this value to average find time
    this.desiredAverage = (maxTarget/target)*hashPerSec;
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
  public int getMaxTarget() {
    return this.maxTarget;
  }
  public void setMaxTarget(int maxTarget) {
    this.maxTarget = maxTarget;
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
  public double getDifficulty() {
    return this.difficulty;
  }
  public void updateDifficulty() {
    this.difficulty = Math.floor((maxTarget/target)*100)/100;
  }
  public void updateDesiredAverage() {
    this.desiredAverage = Math.floor((maxTarget / target) * hashPerSec*100)/100;
  }
}
