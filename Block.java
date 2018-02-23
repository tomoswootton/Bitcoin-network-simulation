import javax.swing.*;
import java.awt.*;


class Block {

  public int id;
  private String prevBlockHash;
  private int nonce;
  private String hash;

  public static void main(String[] args) {
    new Block(1,"3455");

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
    this.genHash();
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
  public void newNonce() {
    this.nonce = (int) (Math.random() * 10000);
  }

  public String genHash() {
    //hash is simply nonce
    this.hash = formatHash(this.nonce);
    return this.hash;
  }

  private String formatHash(int hash) {
    String formatted_hash = Integer.toString(hash);
    switch (formatted_hash.length()) {
      case (1) :
        formatted_hash = "0000"+formatted_hash;
        break;
      case (2) :
        formatted_hash = "000"+formatted_hash;
        break;
      case (3) :
        formatted_hash = "00"+formatted_hash;
        break;
      case (4) :
        formatted_hash = "0"+formatted_hash;
        break;
    }
    return formatted_hash;
  }

  //only called for blocks counted in chain
  public JPanel MakeDisplayBlock() {
    JPanel frame = new JPanel();
    frame.setBorder(BorderFactory.createLineBorder(Color.black));
    return frame;
  }

}
