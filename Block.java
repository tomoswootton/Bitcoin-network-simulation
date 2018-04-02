import javax.swing.*;
import java.awt.*;
import java.awt.GridBagLayout;

class Block {

  public int id;
  private String prevBlockHash;
  private int nonce;
  private String hash;

  private JPanel dispPanel;

  //constructors
  public static void main(String[] args) {
    new Block(1,"3455");
  }
  public Block(int id, String prevBlockHash) {
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    newNonce();
    this.genHash();

    //testing
    // JFrame testMain = new JFrame();
    // this.makeDispPanel();
    // testMain.setSize(200, 200);
    // testMain.setLocationRelativeTo(null);
    // testMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // testMain.add(this.dispPanel);
    // testMain.setVisible(true);

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
  public JPanel getDispPanel() {
    if (dispPanel == null) {
      makeDispPanel();
    }
    return this.dispPanel;
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

  //TODO set node_name as paraemter
  private void makeDispPanel() {
    dispPanel = new JPanel();
    dispPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    dispPanel.setMaximumSize(new Dimension(100,100));
    dispPanel.setMinimumSize(new Dimension(100,100));
    dispPanel.setBackground(Color.red);
    dispPanel.setLayout(new GridBagLayout());

    JLabel block_id_label = new JLabel("Block "+this.id);

    GridBagConstraints block_id_label_cons = new GridBagConstraints();
    setCons(block_id_label_cons,0,0,1,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,0,0);
    dispPanel.add(block_id_label, block_id_label_cons);

    JLabel node_name_label = new JLabel("node name");;

    GridBagConstraints node_name_label_cons = new GridBagConstraints();
    setCons(node_name_label_cons,0,1,1,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,0,0);
    dispPanel.add(node_name_label, node_name_label_cons);

  }

  private void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int anchor, int ipadx, int ipady) {
    gridCons.gridx = x;
    gridCons.gridy = y;
    //number of col/row component takes up
    gridCons.gridwidth = width;
    gridCons.gridheight = height;
    //when components display area is larger than component.
    // NONE - default, HORIZONTAL - fill horizontal space, VERTICAL, BOTH
    gridCons.fill = fill;
    gridCons.ipadx = ipadx;
    gridCons.ipady = ipady;
    //used when component is smaller than display area to determine where to palce
    //CENTER default
    gridCons.anchor = anchor;
    //used for determining area between components in display area 0.0-1.0
    //keep as 0 for now,meaning cell fits to component
    gridCons.weightx = 0.2;
    gridCons.weighty = 0.2;
  }
  private void setCons(GridBagConstraints gridCons,int x,int y,int width,int height,int fill,int anchor,int ipadx,int ipady,int weightx,int weighty) {
    //extra method for if weight option is wanted
    setCons(gridCons,x,y,width,height,fill,anchor,ipadx,ipady);
    gridCons.weightx = weightx;
    gridCons.weighty = weighty;
  }

}
