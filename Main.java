import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridBagLayout;
import java.util.LinkedList;


public class Main extends JFrame {

  public Main main;

  JButton startButton;
  JButton exitButton;
  JButton addNodeButton;
  JButton removeNodeButton;
  JLabel nodeIdLabel;
  JTextField nodeNameTextfield;
  TextArea previewText;


  //raw nodes list, node objects created in simulation class
  public LinkedList<Node> rawNodes = new LinkedList<Node>();

  public static void main(String[] args) {
    new Main();
  }

  public Main() {

    main = this;

    ListenForButton lForButton = new ListenForButton();

    this.setSize(800, 400);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Network simulator");

    //create UI
    JPanel page = new JPanel();
    page.setLayout(new GridBagLayout());

    //header
    JPanel header = new JPanel();
    GridBagConstraints headerCons = new GridBagConstraints();
    setCons(headerCons, 0,0,1,1,GridBagConstraints.BOTH,0,0);

    JLabel title = new JLabel("Network Simulator");
    header.add(title);

    page.add(header, headerCons);

    //settings
    JPanel settings = new JPanel();
    settings.setLayout(new GridBagLayout());
    settings.setSize(400,600);

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
      globalHashrateTextField.setColumns(5);

      GridBagConstraints globalHashrateTextFieldCons = new GridBagConstraints();
      setCons(globalHashrateTextFieldCons,1,1,1,1,GridBagConstraints.NONE,10,15);
      settings.add(globalHashrateTextField, globalHashrateTextFieldCons);


    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,0,1,1,1,GridBagConstraints.VERTICAL,0,0);
    page.add(settings, settingsCons);


    //add_node
    JPanel addNode = new JPanel();
    addNode.setLayout(new GridBagLayout());
    addNode.setSize(800, 1000);

      //title
      JLabel addNodeTitle = new JLabel("Add node");

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.VERTICAL,0,25);
      addNode.add(addNodeTitle, addNodeTitleCons);

      //node name
      JLabel nodeNameTitle = new JLabel("Name:");

      GridBagConstraints nodeNameTitleCons = new GridBagConstraints();
      setCons(nodeNameTitleCons,0,1,1,1,GridBagConstraints.BOTH,10,15);
      addNode.add(nodeNameTitle, nodeNameTitleCons);


      nodeNameTextfield = new JTextField("Node name");
      nodeNameTextfield.setColumns(5);
      globalHashrateTextField.setColumns(5);
      //TODO remove text when clicked


      GridBagConstraints nodeNameTextfieldCons = new GridBagConstraints();
      setCons(nodeNameTextfieldCons,1,1,2,1,GridBagConstraints.BOTH,10,15);
      addNode.add(nodeNameTextfield, nodeNameTextfieldCons);

      //node id
      JLabel nodeIdTitle = new JLabel("Id: ");

      GridBagConstraints nodeIdTitleCons = new GridBagConstraints();
      setCons(nodeIdTitleCons,0,2,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeIdTitle, nodeIdTitleCons);


      nodeIdLabel = new JLabel(""+rawNodes.size());

      GridBagConstraints nodeIdLabelCons = new GridBagConstraints();
      setCons(nodeIdLabelCons,1,2,1,1,GridBagConstraints.NONE,10,15);
      addNode.add(nodeIdLabel, nodeIdLabelCons);

      //button add node
      addNodeButton = new JButton("Add node");
      addNodeButton.addActionListener(lForButton);

      GridBagConstraints addNodeButtonCons = new GridBagConstraints();
      setCons(addNodeButtonCons,0,4,2,1,GridBagConstraints.NONE,10,15);
      addNode.add(addNodeButton, addNodeButtonCons);

      //button remove node
      removeNodeButton = new JButton("Remove node");
      removeNodeButton.addActionListener(lForButton);

      GridBagConstraints removeNodeButtonCons = new GridBagConstraints();
      setCons(removeNodeButtonCons,0,5,2,1,GridBagConstraints.NONE,10,15);
      addNode.add(removeNodeButton, removeNodeButtonCons);

    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,0,2,2,1,GridBagConstraints.NONE,0,0);
    page.add(addNode, addNodeCons);


    //preview
    JPanel preview = new JPanel();
    preview.setLayout(new GridBagLayout());
    preview.setSize(800,800);

      //column titles
      JLabel previewTitlesLabel = new JLabel("name      hashrate share");

      GridBagConstraints previewTitlesLabelCons = new GridBagConstraints();
      setCons(previewTitlesLabelCons,0,0,1,1,GridBagConstraints.NONE,10,15);
      // previewTitlesLabelCons.anchor = GridBagConstraints.LINE_START;
      preview.add(previewTitlesLabel, previewTitlesLabelCons);

      //text area
      previewText = new TextArea("",5,20,TextArea.SCROLLBARS_BOTH);
      previewText.setEditable(false);

      GridBagConstraints previewTextCons = new GridBagConstraints();
      setCons(previewTextCons,0,1,1,1,GridBagConstraints.NONE,10,15);
      preview.add(previewText, previewTextCons);



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

    //set background colours for testing
      // preview.setBackground(Color.red);
      // addNode.setBackground(Color.cyan);
      // header.setBackground(Color.yellow);
      // settings.setBackground(Color.green);


    //add page to JFrame
    this.add(page);
    this.setVisible(true);

  }

  //method sets GridBagConstraints variables
  private void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int ipadx, int ipady) {
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


  //preview methods
  public void addNode() {
    int mine_speed = 100;
    String name = nodeNameTextfield.getText();
    previewText.append(nodeIdLabel.getText()+"       "+name+"       "+mine_speed+"\n");
    this.rawNodes.add(new Node(name, mine_speed));
    System.out.print("rawNodes: "+rawNodes+"\n");
    nodeIdLabel.setText(""+rawNodes.size());
  }

  public void removeNode(int id) {
    System.out.print("got to remove node method");
    for (Node node : rawNodes) {
      if (node.id == id) {
        rawNodes.remove(node);
        refreshPreview();
        System.out.print("rawNodes: "+rawNodes+"\n");
      }
    }
  }

  public void refreshPreview() {
    System.out.println("refresh method got to");
    previewText.setText("");
    System.out.println("text set to nothing");

    for (Node node : rawNodes) {
      previewText.append(node.getName()+"       "+ node.getMineSpeed()+"\n");
    }
  }


  private class ListenForButton implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      if(e.getSource() == startButton) {
        Simulation simulation = new Simulation(rawNodes);
      } else if (e.getSource() == exitButton) {
        System.exit(0);
      } else if (e.getSource() == addNodeButton) {
        addNode();
        nodeNameTextfield.setText("Node name");
      } else if (e.getSource() == removeNodeButton) {
        RemoveNodeWindow remove = new RemoveNodeWindow(main);
      }
  }
}
}
