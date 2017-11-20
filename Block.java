class Block {

  public int id;
  private String prevBlockHash;
  private int nonce;
  private String hash;

  public static void main(String[] args) {
    // genPreviewString("2","CHINABIGMINER","100");
    // genPreviewString("2","japan","100");
  }

  // static void genPreviewString(String id, String name, String mine_speed) {
  //   System.out.println(id+"-----");
  //
  //   if (name.length() > 8) {
  //     name = name.substring(0,6).concat("..");
  //   }
  //   System.out.println(id+"-----"+name);
  //   String temp = "";
  //   int j = 8-name.length();
  //   for (int i=0;i<j;i++) {
  //     temp = temp.concat("-");
  //   }
  //   System.out.println(id+"     "+name+temp);
  //   System.out.println(id+"     "+name+temp+mine_speed);
  // }

  public Block(int id, String prevBlockHash) {
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    newNonce();
  }

//getters and setters
  public String getPrevBlockHash() {
    return this.prevBlockHash;
  }

  public int getNonce() {
    return this.nonce;
  }

  public String getHash() {
    return this.hash;
  }

//methods
  private void newNonce() {
    this.nonce = (int) (Math.random() * 1000);
  }


  private Boolean validNonce() {
    return true;
  }

  public int genHash() {
    //hash is simply nonce for now
    // return block.getNonce;
    newNonce();
    System.out.println("genHash hit: "+ this.nonce);
    return this.nonce;
  }

}
