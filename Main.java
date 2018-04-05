import java.awt.*;
import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.*;

public class Main {

  JFrame main = new JFrame();
  JPanel page;

  JTextField globalHashShareTextField;
  JLabel nodeIdLabel;
  JTextField nodeNameTextField;
  JTextField hashShareTextField;
  JComboBox<String> nodesDisplayedCBox;

  JPanel previewScrollPanel;
  ArrayList<JPanel> initNodeDispHolderList = new ArrayList<JPanel>();

  //variable used to save state of node name text field.
  //if a name has been typed but node not yet added, dont remove text upon clicked
  //true if clear upon click
  Boolean globalHashShareTextFieldClear = true;
  Boolean nodeNameTextFieldClear = true;
  Boolean hashShareTextFieldClear = true;

  //variable holds percentage of hash share available
  Double hashShareAvailable = 100.0;

  //nodesList list, contains node objects
  LinkedList<Node> nodesList = new LinkedList<Node>();

  //constructors
  public static void main(String[] args) {
    new Main();
  }
  public Main() {
    makePage();
  }

  //JSwing methods
  private void makePage() {
    main.setSize(800, 600);
    main.setLocationRelativeTo(null);
    main.setResizable(false);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.setTitle("Menu");

    //create UI
    page = new JPanel();
    page.setLayout(new GridBagLayout());

    //header
    JPanel headerPanel = new JPanel();

      JLabel title = new JLabel("Network Simulator");
      title.setFont(title.getFont().deriveFont(28.0f));
      headerPanel.add(title);

    GridBagConstraints headerPanelCons = new GridBagConstraints();
    setCons(headerPanelCons, 1,0,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(headerPanel, headerPanelCons);

    //settings
    JPanel settings = new JPanel();
    settings.setLayout(new GridBagLayout());
    settings.setBorder(BorderFactory.createLineBorder(Color.black));

    JLabel settingsTitle = new JLabel("<HTML><U>Settings</U></HTML>");

    //title
    settingsTitle.setFont(settingsTitle.getFont().deriveFont(16.0f));

    GridBagConstraints settingsTitleCons = new GridBagConstraints();
    setCons(settingsTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    settings.add(settingsTitle, settingsTitleCons);

    //hashshare
    JLabel globalHashShareTitle = new JLabel("Global HashShare:");

    GridBagConstraints globalHashShareTitleCons = new GridBagConstraints();
    setCons(globalHashShareTitleCons,0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
    settings.add(globalHashShareTitle, globalHashShareTitleCons);

    globalHashShareTextField = new JTextField("2");
    globalHashShareTextField.setColumns(10);
    globalHashShareTextField.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (globalHashShareTextFieldClear) {
          globalHashShareTextField.setText("");
          globalHashShareTextFieldClear = false;
        }
      }
    });

    GridBagConstraints globalHashShareTextFieldCons = new GridBagConstraints();
    setCons(globalHashShareTextFieldCons,1,1,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,0);
    settings.add(globalHashShareTextField, globalHashShareTextFieldCons);

      JPanel settingsfillerPanel = new JPanel();
      settingsfillerPanel.setPreferredSize(new Dimension(400,10));

      GridBagConstraints settingsfillerPanelCons = new GridBagConstraints();
      setCons(settingsfillerPanelCons,0,1,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      settings.add(settingsfillerPanel, settingsfillerPanelCons);

    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,1,2,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(settings, settingsCons);

    //add_node
    JPanel addNode = new JPanel();
    addNode.setLayout(new GridBagLayout());
    addNode.setBorder(BorderFactory.createLineBorder(Color.black));

    JLabel addNodeTitle = new JLabel("<HTML><U>Add node</U></HTML>");
      //title
      addNodeTitle.setFont(addNodeTitle.getFont().deriveFont(16.0f));

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
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

      //hash share
      JLabel hashShareTitle = new JLabel("Hash Share(%):");

      GridBagConstraints hashShareTitleCons = new GridBagConstraints();
      setCons(hashShareTitleCons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,10,10);
      addNode.add(hashShareTitle, hashShareTitleCons);

        hashShareTextField = new JTextField(Double.toString(hashShareAvailable));
        hashShareTextField.setColumns(10);
        hashShareTextField.setText("50.0");
        hashShareTextField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (hashShareTextFieldClear) {
            hashShareTextField.setText("");
            hashShareTextFieldClear = false;
          }
        }
      });

      GridBagConstraints hashShareTextFieldCons = new GridBagConstraints();
      setCons(hashShareTextFieldCons,1,3,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,10,0);
      addNode.add(hashShareTextField, hashShareTextFieldCons);

      //buttons
      JPanel addNodeButtons = new JPanel();

        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(e.getSource() == addNodeButton) {
              addNode();
              nodeNameTextField.setText("Node name");
              hashShareTextField.setText(Double.toString(hashShareAvailable));
              nodeNameTextFieldClear = true;
              globalHashShareTextFieldClear = true;
            }
          }
        });
        addNodeButtons.add(addNodeButton);

        JButton removeNodeButton = new JButton("Remove Node");
        removeNodeButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(e.getSource() == removeNodeButton) {
              removeNodeWindow();
            }
          }
        });
        addNodeButtons.add(removeNodeButton);

        JButton importButton = new JButton("Import");
        importButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(e.getSource() == importButton) {
              importStats();
            }
          }
        });
        addNodeButtons.add(importButton);

      GridBagConstraints addNodeButtonsCons = new GridBagConstraints();
      setCons(addNodeButtonsCons,0,4,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      addNode.add(addNodeButtons, addNodeButtonsCons);

      JPanel addNodefillerPanel = new JPanel();
      addNodefillerPanel.setPreferredSize(new Dimension(400,10));

      GridBagConstraints addNodefillerPanelCons = new GridBagConstraints();
      setCons(addNodefillerPanelCons,0,1,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      addNode.add(addNodefillerPanel, addNodefillerPanelCons);

    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,1,4,4,4,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(addNode, addNodeCons);

    //preview
    //title
    JLabel titleLabel = new JLabel("                                           Name                         id         power        blocks                                                  ");

    GridBagConstraints titleLabelCons = new GridBagConstraints();
    setCons(titleLabelCons, 2,8,2,1,GridBagConstraints.NONE,GridBagConstraints.LAST_LINE_END,0,0);
    page.add(titleLabel, titleLabelCons);

    //scroll box
    previewScrollPanel = new JPanel();
    previewScrollPanel.setLayout(new GridBagLayout());
    previewScrollPanel.setSize(new Dimension(500,150));

    JScrollPane scroll = new JScrollPane(previewScrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setViewportView(previewScrollPanel);
    scroll.setPreferredSize(new Dimension(500,120));

      constructInitNodeDispHolders();

    GridBagConstraints previewPanelCons = new GridBagConstraints();
    setCons(previewPanelCons, 0,9,6,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(scroll, previewPanelCons);

      JButton refreshPreviewButton = new JButton("refresh");
      refreshPreviewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshPreviewButton) {
          refreshPreview();
        }
      }
    });

    GridBagConstraints refreshPreviewButtonCons = new GridBagConstraints();
    setCons(refreshPreviewButtonCons, 4,9,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(refreshPreviewButton, refreshPreviewButtonCons);

      JButton clearPreviewButton = new JButton("Remove All");
      clearPreviewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(e.getSource() == clearPreviewButton) {
          removeAllNodes();
        }
      }
    });

    GridBagConstraints clearPreviewButtonCons = new GridBagConstraints();
    setCons(clearPreviewButtonCons, 4,10,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(clearPreviewButton, clearPreviewButtonCons);

    //buttons
    JPanel buttons = new JPanel();

      JButton startButton = new JButton("Start");
      startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == startButton) {
            startSimulation();
          }
        }
      });
      buttons.add(startButton);

      JButton exitButton = new JButton("Exit");
      exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == exitButton) {
            System.exit(0);
          }
        }
      });
      buttons.add(exitButton);

    GridBagConstraints buttonsCons = new GridBagConstraints();
    setCons(buttonsCons,1,11,4,2,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(buttons, buttonsCons);

    //add page to JFrame
    main.add(page);
    main.setVisible(true);
  }
  private void constructInitNodeDispHolders() {
    initNodeDispHolderList = new ArrayList<JPanel>();

    //populate scroll panel with holder panels to force shape of scroll panel within scroll pane
    for (int i=0;i<=2;i++) {
      JPanel nodeDispHolder = new JPanel();
      nodeDispHolder.setMinimumSize(new Dimension(500, 50));

      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,0,i,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      previewScrollPanel.add(nodeDispHolder, panelCons);

      initNodeDispHolderList.add(nodeDispHolder);
    }
  }
  private void setCons(GridBagConstraints gridCons, int x, int y, int width, int height, int fill, int anchor, int ipadx, int ipady) {
    //method sets GridBagConstraints variables

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

  //add nodes
  public Node createNode(String name, String hashShare) {
    Double mine_speed = (Double.parseDouble(hashShare)/100)*Double.parseDouble(globalHashShareTextField.getText());
    //create node
    Node node = new Node(nodesList.size(),name,hashShare,mine_speed);

    return node;
  }
  private void addNodeToNodesList(Node node) {
    //add to nodesList array
    nodesList.add(node);
  }
  private void addNodeToPreview(Node node) {
    //if one of first 5 blocks, add to initNodeDispHolder panel with corresponding id
    if (node.id < 3) {
      GridBagConstraints cons = new GridBagConstraints();
      setCons(cons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      initNodeDispHolderList.get(node.id).add(node.getDispPanel(), cons);
    } else {
      //create new holder panel
      JPanel nodeDispHolder = new JPanel();
      nodeDispHolder.setMinimumSize(new Dimension(500, 50));
      //add node dispPanel to holder panel
      nodeDispHolder.add(node.getDispPanel());
      //adjust size of panel to allow for new block
      previewScrollPanel.setSize(new Dimension(500,(node.id-2)*50 + 150));
      //add to new holder panel
      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,0,node.id,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      previewScrollPanel.add(nodeDispHolder, panelCons);
    }
    previewScrollPanel.revalidate();
    previewScrollPanel.repaint();
  }
  public void addNode() {
    //unavailable hashshare error catcher
    System.out.println("hashshareavailable: "+hashShareAvailable);
    if (hashShareAvailable < Double.parseDouble(hashShareTextField.getText())) {
      JOptionPane.showMessageDialog(main,hashShareAvailable+"% hash share left.","Error",JOptionPane.PLAIN_MESSAGE);
      return;
    }
    Node node = createNode(nodeNameTextField.getText(), hashShareTextField.getText());
    addNodeToNodesList(node);

    //recalculate available hash share form value
    refreshHashShareAvailble();

    addNodeToPreview(node);
  }

  //nodesList methods
  public void removeNode(int id) {
    for (Node node : nodesList) {
      if (node.id == id) {
        nodesList.remove(node);
        refreshNodesList();
        refreshPreview();
        refreshHashShareAvailble();
        break;
      }
    }
  }
  private void removeAllNodes() {
    //clear nodesLsit
    nodesList.clear();
    //clear previewPanel
    previewScrollPanel.removeAll();
    refreshPreview();
  }
  public void refreshNodesList() {
    //purpose: when a node is removed, the id's of the remaining nodesList must be fixed
    for (int i=0;i<nodesList.size();i++) {
      //for each node, update id and re-make dispPanel with new id
      Node node = nodesList.get(i);
      node.id = i;
      node.makeNodeDispPanel();
    }
  }

  //nodes preview methods
  public void refreshPreview() {
    previewScrollPanel.removeAll();
    constructInitNodeDispHolders();
    System.out.println("nodes list size: "+nodesList.size());
    for (Node node : nodesList) {
      //add to preview at row node.id
      addNodeToPreview(node);
    }
    previewScrollPanel.revalidate();
    previewScrollPanel.repaint();
  }

  //UI methods
  public void refreshHashShareAvailble() {
    Double hsAvailble = 100.0;
    for (Node node : nodesList) {
      hsAvailble = hsAvailble - Double.parseDouble(node.getHashShare());
    }
    hashShareAvailable = hsAvailble;
    hashShareTextField.setText(Double.toString(hashShareAvailable));
  }
  private void importStats() {
    //methods reads imported stats from JSON
    //input is name of pool and number of blocks mined in past however many days

    // The name of the file to open.
    String fileName = "pool_stats.txt";
    // This will reference one line at a time
    String line = null;

    try {
        // FileReader reads text files in the default encoding, bufferedReader groups into lines
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        //arrays as node info
        LinkedList<String> nodeNamesList = new LinkedList<String>();
        LinkedList<Double> hashShareList = new LinkedList<Double>();

        while((line = bufferedReader.readLine()) != null) {
            //dont process start and end of JSON
            if(line.length() > 1) {
              //find end of name in line
              int j = 3;
              while (line.charAt(j) != '\"') {
                j++;
              }
              //find name
              String nodeName = line.substring(3,j);
              //find number of blocks mined in timepspan
              String numberBlocksMined = line.substring(j+3,line.length());
              if (numberBlocksMined.charAt(numberBlocksMined.length()-1) == ',') {
                numberBlocksMined = numberBlocksMined.substring(0,numberBlocksMined.length()-1);
              }

              nodeNamesList.add(nodeName);
              hashShareList.add(Double.parseDouble(numberBlocksMined));
            }
        }
        //sum up total blocks mined
        Double sum = 0.0;
        for (Double hashShare : hashShareList) {
          sum += hashShare;
        }
        System.out.println("sum = "+sum);
        //change each value in hashShare to percentage of blocks (therefore hashShare value)
        for (int i=0;i<hashShareList.size();i++) {
          Double temp = (hashShareList.get(i)/sum)*100;
          String stringtemp = temp.toString();
          if (stringtemp.length() > 3) {
            stringtemp = stringtemp.substring(0,4);
          }
          temp = Double.parseDouble(stringtemp);
          hashShareList.set(i,temp);
        }
        //add each node
        for (int i=0;i<nodeNamesList.size();i++) {
          // addNode(nodeNamesList.get(i),Double.toString(hashShareList.get(i)));
        }

        //close file
        bufferedReader.close();
    }
    catch(FileNotFoundException ex) {
        System.out.println("Unable to open file '"+fileName + "'");
    }
    catch(IOException ex) {
        System.out.println("Error reading file '"+ fileName + "'");
        ex.printStackTrace();
    }
  }
  private void startSimulation() {
    //send all nodes their nodesList
    //TODO add split chain option


    // temp so entire list can be passed through, while iterating through original list
    //also pass simulation object so nodes can use simulation methods
    LinkedList<Node> nodesListTemp = this.nodesList;
    for (Node node : nodesList) {
      node.setNodesList(nodesListTemp);
    }

    //make simulation object
    Simulation simulation = new Simulation(nodesList);

    //add simulation object to nodes
    for (Node node : nodesList) {
      node.setSimulationObject(simulation);
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

      if(inputValue == null) {
        return;
      }
      if(inputValue.length() == 0) {
        System.out.println("inputValue = null");
        return;
      }
      int inputValueInt = Integer.parseInt(inputValue);
      //catch value out of range
      if(inputValueInt<0 || inputValueInt>size-1) {
        JOptionPane.showMessageDialog(null,"Input value out of range.","Remove Node",JOptionPane.WARNING_MESSAGE);
        removeNodeWindow();
        return;
      }
      removeNode(inputValueInt);
    }
}
