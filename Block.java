class Block {

  public int ID;
  private String prevBlockHash;
  private int Nonce;

  public Block(String prevBlockHash) {
    this.ID = Simulation.getChainSize();
    this.prevBlockHash = prevBlockHash;
    this.Nonce = newNonce();
  }

  public String getPrevBlockHash() {
    return this.prevBlockHash;
  }

  public int getNonce() {
    return this.Nonce;
  }

  private int newNonce() {
    return (int) (Math.random() * 1000);
  }

  public int genHash() {
    //hash is simply nonce for now
    return this.Nonce;
  }
}
