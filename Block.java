class Block {

  public int id;
  private String prevBlockHash;
  private String nonce;
  private int hash;

  public Block(int id, String prevBlockHash) {
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    this.nonce = newNonce();
  }

//getters and setters
  public String getPrevBlockHash() {
    return this.prevBlockHash;
  }

  public String getNonce() {
    return this.nonce;
  }

//methods
  private String newNonce() {
    int temp1 = (int) (Math.random() * 1000);
    String temp = Integer.toString(temp1);

    switch (temp.length()) {
      case 1:
        temp = "000" + temp;
        break;
      case 2:
        temp = "00" + temp;
        break;
      case 3:
        temp = "0" + temp;
        break;
    }
    return temp;
  }

  public String genHash() {
    //hash is simply nonce for now
    // return block.getNonce;
    return this.nonce;
  }
}
