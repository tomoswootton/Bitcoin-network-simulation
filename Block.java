import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagLayout;

class Block {
   GlobalInfo globalInfo;

  public int id;
  private String prevBlockHash;
  private String foundBy;
  private int nonce;
  private String hash;
  private long timeFound;
  private double timeElapsed;

  private JPanel dispPanel;

  //constructors
  public static void main(String[] args) {
    // new Block(1,"3455", "node name");
  }
  public Block(GlobalInfo globalInfo, int id, String prevBlockHash, String foundBy) {
    this.globalInfo = globalInfo;
    this.id = id;
    //prevBlockHash of nodes chain
    this.prevBlockHash = prevBlockHash;
    this.foundBy = foundBy;
    newNonce();
    this.genHash();
  }

  //getters and setters
  public String getPrevBlockHash() {
    return this.prevBlockHash;
  }
  public void setPrevBlockHash(String prevBlockHash) {
    this.prevBlockHash = prevBlockHash;
  }
  public int getNonce() {
    return this.nonce;
  }
  public String getHash() {
    return this.hash;
  }
  public void setHash(String hash) {
    this.hash = hash;
  }
  public void setForcedHash() {
    this.hash = formatHash((int) (Math.random() * (globalInfo.target)));
  }
  public JPanel getDispPanel() {
    if (dispPanel == null) {
      makeDispPanel();
    }
    return this.dispPanel;
  }
  public void setTimeElapsed(double time) {
    this.timeElapsed = Double.parseDouble(Double.toString(time).substring(0,3));
  }
  public double getTimeElapsed() {
    return this.timeElapsed;
  }
  public long getTimeFound() {
    return this.timeFound;
  }
  public void setTimeFound(long time) {
    this.timeFound = time;
  }
  public String getFoundBy() {
    return this.foundBy;
  }

  //methods
  public void newNonce() {
    this.nonce = (int) (Math.random() * globalInfo.hashSize);
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

  private void blockInfoDispPanel() {
    JFrame blockInfoWindow = new JFrame();
    blockInfoWindow.setPreferredSize(new Dimension(300,300));
    blockInfoWindow.setMinimumSize(new Dimension(300,300));
    blockInfoWindow.setLocationRelativeTo(null);
    blockInfoWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    blockInfoWindow.setTitle("Block "+this.id+" information");
    blockInfoWindow.setAlwaysOnTop(true);
    blockInfoWindow.setVisible(true);

    JPanel mainPanel = new JPanel(new GridBagLayout());

    JLabel heightLabel = new JLabel("Height: "+this.id);
    GridBagConstraints heightLabelCons = new GridBagConstraints();
    setCons(heightLabelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(heightLabel, heightLabelCons);

    JLabel hashLabel = new JLabel("Hash: "+this.hash);
    GridBagConstraints hashLabelCons = new GridBagConstraints();
    setCons(hashLabelCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(hashLabel, hashLabelCons);

    JLabel nonceLabel = new JLabel("Nonce: "+this.nonce);
    GridBagConstraints nonceLabelCons = new GridBagConstraints();
    setCons(nonceLabelCons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(nonceLabel, nonceLabelCons);
    blockInfoWindow.add(mainPanel);

    JLabel prevBlockHashLabel = new JLabel("Previous block hash: "+this.prevBlockHash);
    GridBagConstraints prevBlockHashLabelCons = new GridBagConstraints();
    setCons(prevBlockHashLabelCons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(prevBlockHashLabel, prevBlockHashLabelCons);

    JLabel foundByLabel = new JLabel("Found by: "+this.foundBy);
    GridBagConstraints foundByLabelCons = new GridBagConstraints();
    setCons(foundByLabelCons,0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(foundByLabel, foundByLabelCons);

    JLabel timeElapsedLabel = new JLabel("Time elapsed: "+this.timeElapsed+"s");
    GridBagConstraints timeElapsedLabelCons = new GridBagConstraints();
    setCons(timeElapsedLabelCons,0,5,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    mainPanel.add(timeElapsedLabel, timeElapsedLabelCons);
  }
  private void makeDispPanel() {
    dispPanel = new JPanel();
    dispPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    dispPanel.setBackground(Color.lightGray);
    dispPanel.setLayout(new GridBagLayout());
    dispPanel.setPreferredSize(new Dimension(90,90));

    JLabel block_id_label = new JLabel(""+this.id);

    GridBagConstraints block_id_label_cons = new GridBagConstraints();
    setCons(block_id_label_cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(block_id_label, block_id_label_cons);

    JLabel elapsed_label = new JLabel("Elapsed: ");

    GridBagConstraints elapsed_label_cons = new GridBagConstraints();
    setCons(elapsed_label_cons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(elapsed_label, elapsed_label_cons);

    JLabel elapsed2_label = new JLabel(this.timeElapsed+"s");

    GridBagConstraints elapsed2_label_cons = new GridBagConstraints();
    setCons(elapsed2_label_cons,0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(elapsed2_label, elapsed2_label_cons);


    JButton infoButton = new JButton("info");
    infoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == infoButton) {
          blockInfoDispPanel();
        }
      }
    });

    GridBagConstraints infoButtonCons = new GridBagConstraints();
    setCons(infoButtonCons, 0,5,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(infoButton, infoButtonCons);
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
