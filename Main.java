import java.awt.*;

import javax.swing.*;
import java.awt.GridBagLayout;

import java.awt.event.*;

import java.util.LinkedList;


public class Main {

  JFrame main = new JFrame();
  JPanel page;

  JButton startButton;
  JTextField globalHashPSTextField;
  JButton exitButton;
  JButton addNodeButton;
  JButton removeNodeButton;
  JLabel nodeIdLabel;
  JTextField nodeNameTextField;
  JTextField hashShareTextField;
  JComboBox<String> nodesDisplayedCBox;

  JPanel previewPanel;

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
    page = new JPanel();
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
    //title
    JLabel titleLabel = new JLabel("                Name                     id         power      blocks                                                  ");

    GridBagConstraints titleLabelCons = new GridBagConstraints();
    setCons(titleLabelCons, 0,8,2,1,GridBagConstraints.NONE,GridBagConstraints.PAGE_END,0,0);
    page.add(titleLabel, titleLabelCons);

    //scroll box
    previewPanel = new JPanel();
    // previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
    previewPanel.setLayout(new GridBagLayout());


    JScrollPane scroll = new JScrollPane(previewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setViewportView(previewPanel);
    scroll.setPreferredSize(new Dimension(500,100));


    GridBagConstraints previewPanelCons = new GridBagConstraints();
    setCons(previewPanelCons, 0,9,6,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(scroll, previewPanelCons);

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

  //extra method for if weight option is wanted
  private void setCons(GridBagConstraints gridCons,int x,int y,int width,int height,int fill,int anchor,int ipadx,int ipady,int weightx,int weighty) {
    setCons(gridCons,x,y,width,height,fill,anchor,ipadx,ipady);
    gridCons.weightx = weightx;
    gridCons.weighty = weighty;
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
    //create node
    Node node = new Node(nodeIdLabel.getText(),name,hashShareTextField.getText(),mine_speed);
    //add to nodesList array
    nodesList.add(node);
    //add to preview
    addNodeToPreview(node);
    //++1 to label
    nodeIdLabel.setText(""+nodesList.size());
    //recalculate available hash share value
    refreshHashShareAvailble();
  }

  private void addNodeToPreview(Node node) {
    // previewPanel.add(node.getNodeDispPanel());
    GridBagConstraints nodeCons = new GridBagConstraints();
    setCons(nodeCons,0,nodesList.size(),2,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.PAGE_START,10,0);
    previewPanel.add(node.getNodeDispPanel(), nodeCons);
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
    previewPanel.removeAll();
    previewPanel.repaint();
    // previewPanel.repaint();
    for (Node node : nodesList) {
      addNodeToPreview(node);
    }
  }

  //when a node is removed, the id's of the remaining nodesList must be fixed
  public void refreshNodesList() {
    for (int i=0;i<nodesList.size();i++) {
      //for each node, update id and re-make dispPanel with new id
      Node node = nodesList.get(i);
      node.id = i;
      node.makeNodeDispPanel();

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

    Simulation simulation = new Simulation(nodesList);
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
      String inputValue = JOptionPane.showInputDialog(main,"Id of node to remove:","Remove Node",1);

      if (inputValue.length() == 0) {
        System.out.println("inputValue = null");
        return;
      }
      int inputValueInt = Integer.parseInt(inputValue);
      //catch value out of range
      if (inputValueInt<0 || inputValueInt>size-1) {
        JOptionPane.showMessageDialog(null,"Input value out of range.","Remove Node",JOptionPane.WARNING_MESSAGE);
        removeNodeWindow();
        return;
      }
      removeNode(inputValueInt);
    }
  }
}
