import javax.swing.*;
import java.awt.*;
import java.awt.GridBagLayout;

class Block {

  public int id;
  private String prevBlockHash;
  private String foundByNode;
  private int nonce;
  private String hash;

  private JPanel dispPanel;

  //constructors
  public static void main(String[] args) {
    new Block(1,"3455", "node name");
  }
  public Block(int id, String prevBlockHash, String foundByNode) {
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    this.foundByNode = foundByNode;
    newNonce();
    this.genHash();

    // testing
    // JFrame testMain = new JFrame();
    // this.makeDispPanel();
    // testMain.setSize(100, 100);
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
    dispPanel.setBackground(Color.lightGray);
    dispPanel.setLayout(new GridBagLayout());
    dispPanel.setPreferredSize(new Dimension(90,90));

    JLabel block_id_label = new JLabel("Id "+this.id);

    GridBagConstraints block_id_label_cons = new GridBagConstraints();
    setCons(block_id_label_cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(block_id_label, block_id_label_cons);

    JLabel node_name_label_1 = new JLabel("Found By:");;

    GridBagConstraints node_name_label_1_cons = new GridBagConstraints();
    setCons(node_name_label_1_cons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.SOUTH,0,0);
    dispPanel.add(node_name_label_1, node_name_label_1_cons);

    JLabel node_name_label_2 = new JLabel(foundByNode);

    GridBagConstraints node_name_label_2_cons = new GridBagConstraints();
    setCons(node_name_label_2_cons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.NORTH,0,0);
    dispPanel.add(node_name_label_2, node_name_label_2_cons);

    JLabel hash_label = new JLabel("Hash: "+this.hash);

    GridBagConstraints hash_label_cons = new GridBagConstraints();
    setCons(hash_label_cons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(hash_label, hash_label_cons);

    // JButton infoButton = new JButton("Info");
    //
    // JButton infoButton = new JButton("info");
    // infoButton.addActionListener(new ActionListener() {
    //   public void actionPerformed(ActionEvent e) {
    //     if(e.getSource() == infoButton) {
    //       blockInfoDispPanel();
    //     }
    //   }
    // });

    // GridBagConstraints infoButtonCons = new GridBagConstraints();
    // setCons(infoButtonCons, 0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    // chainPanel.add(infoButton, infoButtonCons);
  }

  public void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int anchor, int ipadx, int ipady) {
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
  public void setCons(GridBagConstraints gridCons,int x,int y,int width,int height,int fill,int anchor,int ipadx,int ipady,int weightx,int weighty) {
    //extra method for if weight option is wanted
    setCons(gridCons,x,y,width,height,fill,anchor,ipadx,ipady);
    gridCons.weightx = weightx;
    gridCons.weighty = weighty;
  }
}
