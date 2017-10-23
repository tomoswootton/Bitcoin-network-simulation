import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.GridBagLayout;

public class Main extends JFrame {

  JButton startButton;
  JButton exitButton;
  JButton addNodeButton;

  ListenForButton lForButton = new ListenForButton();

  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    //create UI
    JPanel page = new JPanel();
    page.setLayout(new GridBagLayout());

    this.setSize(800, 400);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Network simulator");


  //header
    JPanel header = new JPanel();
    header.setBackground(Color.yellow);
    GridBagConstraints headerCons = new GridBagConstraints();
    setCons(headerCons, 0,0,1,1,GridBagConstraints.BOTH,0,0);

    JLabel title = new JLabel("Network Simulator");
    header.add(title);

    page.add(header, headerCons);

  //settings
    JPanel settings = new JPanel();
    settings.setLayout(new GridBagLayout());
    settings.setSize(400,600);
    settings.setBackground(Color.green);

      //title
      JLabel settingsTitle = new JLabel("settings");

      GridBagConstraints settingsTitleCons = new GridBagConstraints();
      setCons(settingsTitleCons,0,0,2,1,GridBagConstraints.NONE,10,15);
      settings.add(settingsTitle, settingsTitleCons);

      //global hashes input
      JLabel globalHashrateLabel = new JLabel("Global hashrate");

      GridBagConstraints globalHashrateLabelCons = new GridBagConstraints();
      setCons(globalHashrateLabelCons,0,1,1,1,GridBagConstraints.NONE,10,15);
      settings.add(globalHashrateLabel, globalHashrateLabelCons);


      JTextField globalHashrateTextField = new JTextField("number");

      GridBagConstraints globalHashrateTextFieldCons = new GridBagConstraints();
      setCons(globalHashrateTextFieldCons,1,1,1,1,GridBagConstraints.NONE,10,15);
      settings.add(globalHashrateTextField, globalHashrateTextFieldCons);


    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,0,1,1,1,GridBagConstraints.VERTICAL,0,0);
    page.add(settings, settingsCons);


  //add_node
    JPanel addNode = new JPanel();
    addNode.setLayout(new GridBagLayout());
    addNode.setSize(400, 800);
    addNode.setBackground(Color.cyan);

    //title
      JLabel addNodeTitle = new JLabel("Add node");

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.VERTICAL,0,25);
      addNode.add(addNodeTitle, addNodeTitleCons);

    //node name
      JLabel nodeNameTitle = new JLabel("Name:");

      GridBagConstraints nodeNameTitleCons = new GridBagConstraints();
      setCons(nodeNameTitleCons,0,1,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeNameTitle, nodeNameTitleCons);


      JTextField nodeNameTextfield = new JTextField("Node name");

      GridBagConstraints nodeNameTextfieldCons = new GridBagConstraints();
      setCons(nodeNameTextfieldCons,1,1,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeNameTextfield, nodeNameTextfieldCons);

    //node id
      JLabel nodeIdTitle = new JLabel("Id:");

      GridBagConstraints nodeIdTitleCons = new GridBagConstraints();
      setCons(nodeIdTitleCons,0,2,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeIdTitle, nodeIdTitleCons);


      JLabel nodeIdLabel = new JLabel("$id");

      GridBagConstraints nodeIdLabelCons = new GridBagConstraints();
      setCons(nodeIdLabelCons,1,2,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeIdLabel, nodeIdLabelCons);

      //button add node
      addNodeButton = new JButton("Add node");

      GridBagConstraints addNodeButtonCons = new GridBagConstraints();
      setCons(addNodeButtonCons,0,4,2,1,GridBagConstraints.NONE,10,15);
      addNode.add(addNodeButton, addNodeButtonCons);

    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,0,2,1,1,GridBagConstraints.NONE,0,0);
    page.add(addNode, addNodeCons);


  //preview
    JPanel preview = new JPanel();
    preview.setSize(800,800);
    preview.setBackground(Color.red);

      //text area
      TextArea previewText = new TextArea("",30,50,TextArea.SCROLLBARS_BOTH);
      previewText.setEditable(false);
      preview.add(previewText);
      for (int i=0; i<30; i++) {
        previewText.append("beans");

        previewText.append("\nhello");
      }
      previewText.append("hello");

    GridBagConstraints previewCons = new GridBagConstraints();
    setCons(previewCons,0,3,2,1,GridBagConstraints.BOTH,0,0);
    page.add(preview, previewCons);

  //buttons
    JPanel buttons = new JPanel();

      startButton = new JButton("Start");
      buttons.add(startButton);
      exitButton = new JButton("Exit");
      buttons.add(exitButton);

      startButton.addActionListener(lForButton);
      exitButton.addActionListener(lForButton);

    GridBagConstraints buttonsCons = new GridBagConstraints();
    setCons(buttonsCons,0,4,1,1,GridBagConstraints.NONE,0,0);
    page.add(buttons, buttonsCons);

    //add page to JFrame
    this.add(page);
    this.setVisible(true);

  }
  //method sets GridBagConstraints variables
  public void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int ipadx, int ipady) {
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
    gridCons.anchor = GridBagConstraints.CENTER;
    //used for determining area between components in display area 0.0-1.0
    //keep as 0 for now,meaning cell fits to component
    gridCons.weightx = 0.1;
    gridCons.weighty = 0.1;
  }

  private class ListenForButton implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      if(e.getSource() == startButton) {
        Simulation simulation = new Simulation();
      } else if (e.getSource() == exitButton) {
        System.exit(0);
      }
    }
  }
}
