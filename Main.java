import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridBagLayout;
import java.util.ArrayList;


public class Main extends JFrame {

  public Main main;

  JButton startButton;
  JTextField globalHashrateTextField;
  JButton exitButton;
  JButton addNodeButton;
  JButton removeNodeButton;
  JLabel nodeIdLabel;
  JTextField nodeNameTextfield;
  TextArea previewTextId;
  TextArea previewTextName;
  TextArea previewTextHashrate;


  //variable used to save state of node name text field.
  //if a name has been typed but node not yet added, dont remove text upon clicked
  //true if clear upon click
  Boolean nodeNameTextfieldClear = true;


  //raw nodes list, node objects created in simulation class
  //{id, name, mine speed}
  ArrayList<String[]> rawNodes = new ArrayList<String[]>();

  public static void main(String[] args) {
    new Main();
  }

  public Main() {

    main = this;
    this.setSize(800, 500);

    ListenForButton lForButton = new ListenForButton();

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
    setCons(headerCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);

      JLabel title = new JLabel("Network Simulator");
      title.setFont(title.getFont().deriveFont(28.0f));
      header.add(title);

    page.add(header, headerCons);

    //settings
    JPanel settings = new JPanel();
    settings.setLayout(new GridBagLayout());
    settings.setSize(800,1000);

      //title
      JLabel settingsTitle = new JLabel("<HTML><U>settings</U></HTML>");
      settingsTitle.setFont(settingsTitle.getFont().deriveFont(16.0f));

      GridBagConstraints settingsTitleCons = new GridBagConstraints();
      setCons(settingsTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,10);
      settings.add(settingsTitle, settingsTitleCons);

      //global hashes input
      JLabel globalHashrateLabel = new JLabel("Global hashes per second:");

      GridBagConstraints globalHashrateLabelCons = new GridBagConstraints();
      setCons(globalHashrateLabelCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      settings.add(globalHashrateLabel, globalHashrateLabelCons);


      globalHashrateTextField = new JTextField("number");
      globalHashrateTextField.setColumns(5);

      GridBagConstraints globalHashrateTextFieldCons = new GridBagConstraints();
      setCons(globalHashrateTextFieldCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      settings.add(globalHashrateTextField, globalHashrateTextFieldCons);


    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,0,1,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(settings, settingsCons);


    //add_node
    JPanel addNode = new JPanel();
    addNode.setLayout(new GridBagLayout());
    addNode.setSize(800, 1000);

    JLabel addNodeTitle = new JLabel("<HTML><U>Add node</U></HTML>");
      //title
      addNodeTitle.setFont(addNodeTitle.getFont().deriveFont(16.0f));

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,40);
      addNode.add(addNodeTitle, addNodeTitleCons);

      //node name
      JLabel nodeNameTitle = new JLabel("Name:");

      GridBagConstraints nodeNameTitleCons = new GridBagConstraints();
      setCons(nodeNameTitleCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(nodeNameTitle, nodeNameTitleCons);


      nodeNameTextfield = new JTextField("Node name");
      nodeNameTextfield.setColumns(5);
      nodeNameTextfield.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (nodeNameTextfieldClear) {
            nodeNameTextfield.setText("");
            nodeNameTextfieldClear = false;
          }
        }
      });
      //TODO remove text when clicked


      GridBagConstraints nodeNameTextfieldCons = new GridBagConstraints();
      setCons(nodeNameTextfieldCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      addNode.add(nodeNameTextfield, nodeNameTextfieldCons);

      //node id
      JLabel nodeIdTitle = new JLabel("Id: ");

      GridBagConstraints nodeIdTitleCons = new GridBagConstraints();
      setCons(nodeIdTitleCons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(nodeIdTitle, nodeIdTitleCons);


      nodeIdLabel = new JLabel(""+rawNodes.size());

      GridBagConstraints nodeIdLabelCons = new GridBagConstraints();
      setCons(nodeIdLabelCons,1,2,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      addNode.add(nodeIdLabel, nodeIdLabelCons);

      //addnode buttons
      JPanel addNodeButtons = new JPanel();

        addNodeButton = new JButton("Add node");
        addNodeButtons.add(addNodeButton);
        removeNodeButton = new JButton("Remove node");
        addNodeButtons.add(removeNodeButton);

        addNodeButton.addActionListener(lForButton);
        removeNodeButton.addActionListener(lForButton);

      GridBagConstraints addNodeButtonsCons = new GridBagConstraints();
      setCons(addNodeButtonsCons,0,4,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,20);
      addNode.add(addNodeButtons, addNodeButtonsCons);


    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,0,2,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(addNode, addNodeCons);


    //preview
    JPanel preview = new JPanel();
    preview.setLayout(new GridBagLayout());
    preview.setSize(800,800);

      //column titles
      JLabel previewTitlesIdLabel = new JLabel("id");

      GridBagConstraints previewTitlesIdLabelCons = new GridBagConstraints();
      setCons(previewTitlesIdLabelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      preview.add(previewTitlesIdLabel, previewTitlesIdLabelCons);

      JLabel previewTitlesNameLabel = new JLabel("Name");

      GridBagConstraints previewTitlesNameLabelCons = new GridBagConstraints();
      setCons(previewTitlesNameLabelCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      preview.add(previewTitlesNameLabel, previewTitlesNameLabelCons);

      JLabel previewTitlesHashrateLabel = new JLabel("hashrate share");

      GridBagConstraints previewTitlesHashrateLabelCons = new GridBagConstraints();
      setCons(previewTitlesHashrateLabelCons,2,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      preview.add(previewTitlesHashrateLabel, previewTitlesHashrateLabelCons);

      //text area
      previewTextId = new TextArea("",5,20,TextArea.SCROLLBARS_BOTH);
      previewTextId.setEditable(false);

      GridBagConstraints previewTextIdCons = new GridBagConstraints();
      setCons(previewTextIdCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,20);
      preview.add(previewTextId, previewTextIdCons);

      previewTextName = new TextArea("",5,20,TextArea.SCROLLBARS_BOTH);
      previewTextName.setEditable(false);

      GridBagConstraints previewTextNameCons = new GridBagConstraints();
      setCons(previewTextNameCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,20);
      preview.add(previewTextName, previewTextNameCons);

      previewTextHashrate = new TextArea("",5,20,TextArea.SCROLLBARS_BOTH);
      previewTextHashrate.setEditable(false);

      GridBagConstraints previewTextHashrateCons = new GridBagConstraints();
      setCons(previewTextHashrateCons,2,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,20);
      preview.add(previewTextHashrate, previewTextHashrateCons);


    GridBagConstraints previewCons = new GridBagConstraints();
    setCons(previewCons,0,3,2,1,GridBagConstraints.BOTH,GridBagConstraints.CENTER,10,10);
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
    setCons(buttonsCons,0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttons, buttonsCons);

    //set background colours for testing
      // header.setBackground(Color.yellow);
      // settings.setBackground(Color.green);
      // preview.setBackground(Color.red);
      // addNode.setBackground(Color.cyan);
      // buttons.setBackground(Color.blue);


    //add page to JFrame
    this.add(page);
    this.setVisible(true);

  }

  //method sets GridBagConstraints variables
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


  //preview methods
  public void addNode() {
    //get input values
    String mine_speed = "100";
    String name = nodeNameTextfield.getText();
    //add to preview
    printToPreview(nodeIdLabel.getText(), name,mine_speed);
    //add to rawNodes array
    String[] nodeArray = new String[3];
    nodeArray[0] = Integer.toString(rawNodes.size());
    nodeArray[1] = name;
    nodeArray[2] = mine_speed;
    rawNodes.add(nodeArray);
    // rawNodes.add({cInteger.toString(rawNodes.size()), name, mine_speed});
    //++1 to label
    nodeIdLabel.setText(""+rawNodes.size());
  }

  public void removeNode(int id) {
    for (String[] node : rawNodes) {
      if (Integer.parseInt(node[0]) == id) {
        rawNodes.remove(node);
        refreshRawNodesList();
        refreshPreview();
      }
    }
  }

  public void refreshPreview() {
    previewTextId.setText("");
    previewTextName.setText("");
    previewTextHashrate.setText("");
    for (String[] node : rawNodes) {
      printToPreview(node[0], node[1], node[2]);
    }
  }

  public void printToPreview(String id, String name, String mine_speed) {
    previewTextId.append(id+"\n");
    previewTextName.append(name+"\n");
    previewTextHashrate.append(mine_speed+"\n");
  }

  //when a node is removed, the id's of the remaining nodes must be fixed
  public void refreshRawNodesList() {
    for (int i=0;i<rawNodes.size();i++) {
      rawNodes.get(i)[0] = ""+i;
    }
    // for (int i=1;i<rawNodes.size()-1;i++) {
    //   if (rawNodes.get(i).id - rawNodes.get(i-1).id != 1) {
    //     while(i<=raw.Nodes.size()-1) {
    //       rawNodes(i+1)
    //     }
    //   }
    }



  private class ListenForButton implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      if(e.getSource() == startButton) {
        // Simulation simulation = new Simulation(rawNodes);
      } else if (e.getSource() == exitButton) {
        System.exit(0);
      } else if (e.getSource() == addNodeButton) {
        addNode();
        nodeNameTextfield.setText("Node name");
        nodeNameTextfieldClear = true;
      } else if (e.getSource() == removeNodeButton) {
        RemoveNodeWindow remove = new RemoveNodeWindow(main);
      }
    }
  }
}
