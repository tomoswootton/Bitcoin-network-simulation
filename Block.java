class Block {

  public int id;
  private int prevBlockHash;
  private int nonce;
  private int hash;

  public Block(int id, int prevBlockHash) {
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    this.nonce = newNonce();
  }

//getters and setters
  public int getPrevBlockHash() {
    return this.prevBlockHash;
  }

  public int getNonce() {
    return this.nonce;
  }

//methods
  private int newNonce() {
    int temp1 = (int) (Math.random() * 10);
    String temp = Integer.toString(temp1);
    if (temp.length() == 1) {
      temp = "00" + temp;
    } else if (temp.length() == 3) {
      temp = "0" + temp;
    }
    return Integer.parseInt(temp);
  }

  public int genHash() {
    //hash is simply nonce for now
    // return block.getNonce;
    return this.nonce;
  }
}
