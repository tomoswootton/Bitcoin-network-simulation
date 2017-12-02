import java.awt.*;

import javax.swing.*;
import java.awt.GridBagLayout;

import java.awt.event.*;

import java.util.LinkedList;


public class Main {

  JFrame main = new JFrame();

  JButton startButton;
  JTextField globalHashPSTextField;
  JButton exitButton;
  JButton addNodeButton;
  JButton removeNodeButton;
  JLabel nodeIdLabel;
  JTextField nodeNameTextField;
  JTextField hashShareTextField;
  TextArea previewText;
  JComboBox<String> nodesDisplayedCBox;



  //variable used to save state of node name text field.
  //if a name has been typed but node not yet added, dont remove text upon clicked
  //true if clear upon click
  Boolean nodeNameTextFieldClear = true;
  Boolean hashShareFieldClear = true;
  Boolean globalHashPSTextFieldClear = true;


  //variable holds percentage of hash share available
  Double hashShareAvailable = 100.0;


  //nodesList list, contains node objects
  LinkedList<Node> nodesList = new LinkedList<Node>();

  public static void main(String[] args) {
    new Main();
  }

  public Main() {

    main.setSize(800, 600);

    main.setLocationRelativeTo(null);
    main.setResizable(false);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.setTitle("Menu");

    ListenForButton lForButton = new ListenForButton();

    //create UI
    JPanel page = new JPanel();
    page.setLayout(new GridBagLayout());

    //header
    JPanel headerPanel = new JPanel();

      JLabel title = new JLabel("Network Simulator");
      title.setFont(title.getFont().deriveFont(28.0f));
      headerPanel.add(title);

    GridBagConstraints headerPanelCons = new GridBagConstraints();
    setCons(headerPanelCons, 1,0,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(headerPanel, headerPanelCons);

    //settings
    JPanel settings = new JPanel();
    settings.setLayout(new GridBagLayout());

      //title
      JLabel settingsTitle = new JLabel("<HTML><U>settings</U></HTML>");
      settingsTitle.setFont(settingsTitle.getFont().deriveFont(16.0f));

      GridBagConstraints settingsTitleCons = new GridBagConstraints();
      setCons(settingsTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,10);
      settings.add(settingsTitle, settingsTitleCons);

      //global hashes input
      JLabel globalHashrateLabel = new JLabel("<html>Global hashes to be <br>performed per second:</html>");

      GridBagConstraints globalHashrateLabelCons = new GridBagConstraints();
      setCons(globalHashrateLabelCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      settings.add(globalHashrateLabel, globalHashrateLabelCons);


      globalHashPSTextField = new JTextField("4");
      globalHashPSTextField.setColumns(5);
      globalHashPSTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (globalHashPSTextFieldClear) {
            globalHashPSTextField.setText("");
            globalHashPSTextFieldClear = false;
          }
        }

      });

      GridBagConstraints globalHashPSTextFieldCons = new GridBagConstraints();
      setCons(globalHashPSTextFieldCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,0);
      settings.add(globalHashPSTextField, globalHashPSTextFieldCons);


    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,1,2,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(settings, settingsCons);


    //add_node
    JPanel addNode = new JPanel();
    addNode.setLayout(new GridBagLayout());

    JLabel addNodeTitle = new JLabel("<HTML><U>Add node</U></HTML>");
      //title
      addNodeTitle.setFont(addNodeTitle.getFont().deriveFont(16.0f));

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,10);
      addNode.add(addNodeTitle, addNodeTitleCons);

      //node name
      JLabel nodeNameTitle = new JLabel("Name:");

      GridBagConstraints nodeNameTitleCons = new GridBagConstraints();
      setCons(nodeNameTitleCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(nodeNameTitle, nodeNameTitleCons);


      nodeNameTextField = new JTextField("Node name");
      nodeNameTextField.setColumns(10);
      nodeNameTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (nodeNameTextFieldClear) {
            nodeNameTextField.setText("");
            nodeNameTextFieldClear = false;
          }
        }

      });
      GridBagConstraints nodeNameTextFieldCons = new GridBagConstraints();
      setCons(nodeNameTextFieldCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,0);
      addNode.add(nodeNameTextField, nodeNameTextFieldCons);

      //node id
      JLabel nodeIdTitle = new JLabel("Id: ");

      GridBagConstraints nodeIdTitleCons = new GridBagConstraints();
      setCons(nodeIdTitleCons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(nodeIdTitle, nodeIdTitleCons);


      nodeIdLabel = new JLabel(""+nodesList.size());

      GridBagConstraints nodeIdLabelCons = new GridBagConstraints();
      setCons(nodeIdLabelCons,1,2,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      addNode.add(nodeIdLabel, nodeIdLabelCons);

      //hash share
      JLabel hashShareTitle = new JLabel("Hash Share(%):");

      GridBagConstraints hashShareTitleCons = new GridBagConstraints();
      setCons(hashShareTitleCons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(hashShareTitle, hashShareTitleCons);


      hashShareTextField = new JTextField(Double.toString(hashShareAvailable));
      hashShareTextField.setColumns(10);
      hashShareTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (hashShareFieldClear) {
            hashShareTextField.setText("");
            hashShareFieldClear = false;
          }
        }
      });

      GridBagConstraints hashShareTextFieldCons = new GridBagConstraints();
      setCons(hashShareTextFieldCons,1,3,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,10);
      addNode.add(hashShareTextField, hashShareTextFieldCons);

      //addnode buttons
      JPanel addNodeButtons = new JPanel();

        addNodeButton = new JButton("Add node");
        addNodeButtons.add(addNodeButton);
        removeNodeButton = new JButton("Remove node");
        addNodeButtons.add(removeNodeButton);

        addNodeButton.addActionListener(lForButton);
        removeNodeButton.addActionListener(lForButton);

      GridBagConstraints addNodeButtonsCons = new GridBagConstraints();
      setCons(addNodeButtonsCons,0,4,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      addNode.add(addNodeButtons, addNodeButtonsCons);


    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,1,4,4,4,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(addNode, addNodeCons);


    //preview
    JPanel preview = new JPanel();
    preview.setLayout(new GridBagLayout());
    preview.setSize(800,800);

      //column titles
      JLabel previewTitlesIdLabel = new JLabel("id");

      GridBagConstraints previewTitlesIdLabelCons = new GridBagConstraints();
      setCons(previewTitlesIdLabelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,40,0);
      preview.add(previewTitlesIdLabel, previewTitlesIdLabelCons);

      JLabel previewTitlesNameLabel = new JLabel("Name");

      GridBagConstraints previewTitlesNameLabelCons = new GridBagConstraints();
      setCons(previewTitlesNameLabelCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,0);
      preview.add(previewTitlesNameLabel, previewTitlesNameLabelCons);

      JLabel previewTitlesHashrateLabel = new JLabel("hashrate share");

      GridBagConstraints previewTitlesHashrateLabelCons = new GridBagConstraints();
      setCons(previewTitlesHashrateLabelCons,2,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,0,0);
      preview.add(previewTitlesHashrateLabel, previewTitlesHashrateLabelCons);

      //text area
      previewText = new TextArea("",8,38,TextArea.SCROLLBARS_BOTH);
      previewText.setEditable(false);

      GridBagConstraints previewTextCons = new GridBagConstraints();
      setCons(previewTextCons,0,1,3,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,100,0);
      preview.add(previewText, previewTextCons);

      //nodes per page panel
      JPanel nodesPPPanel = new JPanel();

        JLabel nodesPPTitle = new JLabel("Display ");
        nodesPPPanel.add(nodesPPTitle);

        //combo box
        String[] nodesDisplayedList = {"3","6","9"};
        nodesDisplayedCBox = new JComboBox<String>(nodesDisplayedList);
        nodesPPPanel.add(nodesDisplayedCBox);

        JLabel nodesPPTitle2 = new JLabel("nodes per page.");
        nodesPPPanel.add(nodesPPTitle2);

      GridBagConstraints nodesPPPanelCons = new GridBagConstraints();
      setCons(nodesPPPanelCons,0,3,3,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      preview.add(nodesPPPanel, nodesPPPanelCons);

    GridBagConstraints previewCons = new GridBagConstraints();
    setCons(previewCons,0,8,6,3,GridBagConstraints.BOTH,GridBagConstraints.CENTER,10,10);
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
    setCons(buttonsCons,1,11,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttons, buttonsCons);



    //add page to JFrame
    main.add(page);
    main.setVisible(true);

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

    //input error catcher
    if (hashShareAvailable < Double.parseDouble(hashShareTextField.getText())) {
      // JOptionPane.showMessageDialog(main, "Unavailable hash share chosen");
      JOptionPane.showMessageDialog(main,hashShareAvailable+"% hash share available","Error",JOptionPane.PLAIN_MESSAGE);
      return;
    }

    //get input values
    String name = nodeNameTextField.getText();
    //percentage of share * global hashes per second
    Double mine_speed = (Double.parseDouble(hashShareTextField.getText())/100)*Double.parseDouble(globalHashPSTextField.getText());
    //add to preview
    printToPreview(nodeIdLabel.getText(),name,hashShareTextField.getText(),mine_speed);
    //add to nodesList array
    nodesList.add(new Node(nodeIdLabel.getText(),name,hashShareTextField.getText(),mine_speed));
    //++1 to label
    nodeIdLabel.setText(""+nodesList.size());
    //recalculate available hash share value
    refreshHashShareAvailble();
  }

  public void removeNode(int id) {
    for (Node node : nodesList) {
      if (node.id == id) {
        nodesList.remove(node);
        nodeIdLabel.setText(Integer.toString(Integer.parseInt(nodeIdLabel.getText())-1));
        refreshNodesList();
        refreshPreview();
        refreshHashShareAvailble();
        break;
      }
    }
  }


  public void refreshPreview() {
    System.out.println("got here");
    previewText.setText(null);
    for (Node node : nodesList) {
      printToPreview(Integer.toString(node.id),node.getName(),node.getHashShare(),node.getMineSpeed());
    }
  }

  //format and print nodes info to preview Text Area
  public void printToPreview(String id, String name, String hash_share, Double mine_speed) {
      if (name.length() > 15) {
        name = name.substring(0,13).concat("...   ");
      }
      String temp = "";
      int j = 30-name.length();
      for (int i=0;i<=j;i++) {
        temp = temp.concat("  ");
      }
      previewText.append(id+"            "+name+temp+hash_share+"%\n");
    }


  //when a node is removed, the id's of the remaining nodesList must be fixed
  public void refreshNodesList() {
    for (int i=0;i<nodesList.size();i++) {
      nodesList.get(i).id = i;
    }
    }

  public void refreshHashShareAvailble() {
    Double hsAvailble = 100.0;
    for (Node node : nodesList) {
      hsAvailble = hsAvailble - Double.parseDouble(node.getHashShare());
    }
    hashShareAvailable = hsAvailble;
    hashShareTextField.setText(Double.toString(hashShareAvailable));
  }

  private void startSimulation() {
    //send all nodes their nodesList
    //TODO add split chain option

    // temp so entire list can be passed through, while iterating through original list
    LinkedList<Node> nodesListTemp = this.nodesList;
    for (Node node : nodesList) {
      node.setNodesList(nodesListTemp);
    }

    Simulation simulation = new Simulation(Integer.parseInt(nodesDisplayedCBox.getSelectedItem().toString()), nodesList);
  }



  private class ListenForButton implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      if(e.getSource() == startButton) {
        startSimulation();
      } else if (e.getSource() == exitButton) {
        System.exit(0);
      } else if (e.getSource() == addNodeButton) {
        addNode();
        nodeNameTextField.setText("Node name");
        hashShareTextField.setText(Double.toString(hashShareAvailable));
        nodeNameTextFieldClear = true;
        hashShareFieldClear = true;
      } else if (e.getSource() == removeNodeButton) {
        removeNodeWindow();
      }
    }

    public void removeNodeWindow() {
      //catch if nodesList is empty
      int size = nodesList.size();
      if (size==0) {
        JOptionPane.showMessageDialog(null,"No nodes to remove.","Remove Node",JOptionPane.WARNING_MESSAGE);
        return;
      }
      int inputValue = Integer.parseInt(JOptionPane.showInputDialog(main,"Id of node to remove:","Remove Node",1));
      //catch value out of range
      if (inputValue<0 || inputValue>size-1) {
        JOptionPane.showMessageDialog(null,"Input value out of range.","Remove Node",JOptionPane.WARNING_MESSAGE);
        removeNodeWindow();
        return;
      }
      removeNode(inputValue);
    }
  }
}
