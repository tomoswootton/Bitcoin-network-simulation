import javax.swing.*;
import java.awt.*;
import java.awt.GridBagLayout;

class GenBlock extends Block {

  private JPanel dispPanel;

  public GenBlock(GlobalInfo globalInfo, int id, String prevBlockHash, String foundByNode) {
    super(globalInfo, id, prevBlockHash, foundByNode);
    this.id = 0;
    //set at 0 so no split happens with genesis
    this.setTimeFound(0);
  }

  public JPanel getDispPanel() {
    if (dispPanel == null) {
      makeDispPanel();
    }
    return this.dispPanel;
  }

  private void makeDispPanel() {
    dispPanel = new JPanel();
    dispPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    dispPanel.setBackground(Color.gray);
    dispPanel.setLayout(new GridBagLayout());
    dispPanel.setPreferredSize(new Dimension(100,100));

    JLabel block_id_label = new JLabel("Id "+this.id);

    GridBagConstraints block_id_label_cons = new GridBagConstraints();
    setCons(block_id_label_cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(block_id_label, block_id_label_cons);

    JLabel node_name_label_1 = new JLabel("Genesis");;

    GridBagConstraints node_name_label_1_cons = new GridBagConstraints();
    setCons(node_name_label_1_cons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.SOUTH,0,0);
    dispPanel.add(node_name_label_1, node_name_label_1_cons);

    JLabel node_name_label_2 = new JLabel("Block");;

    GridBagConstraints node_name_label_2_cons = new GridBagConstraints();
    setCons(node_name_label_2_cons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.NORTH,0,0);
    dispPanel.add(node_name_label_2, node_name_label_2_cons);

    JLabel hash_label = new JLabel(" ");

    GridBagConstraints hash_label_cons = new GridBagConstraints();
    setCons(hash_label_cons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    dispPanel.add(hash_label, hash_label_cons);
  }
}
