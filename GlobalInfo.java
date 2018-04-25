import java.util.LinkedList;
import java.util.Date;

class GlobalInfo {
  //class holds data that all others may require
  int hashSize;
  int target;
  Date date = new Date();;

  private LinkedList<Node> nodesList = new LinkedList<Node>();

  public GlobalInfo(int hashSize, int target) {
    this.hashSize = hashSize;
    this.target = target;
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
  public int getTarget() {
    return this.target;
  }
  public void setTarget(int target) {
    this.target = target;
  }
  public long getTime() {
    date = new Date();
    return this.date.getTime();
  }
}
