import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.*;

public class Main {

  JFrame main = new JFrame();
  JPanel page;
  GlobalInfo globalInfo;

  JTextFieldWithID MaxHashValueTextField;
  JTextFieldWithID targetTextField;
  JTextFieldWithID globalHashShareTextField;
  ArrayList<JTextFieldWithID> userInputTextFields = new ArrayList<JTextFieldWithID>();
  JLabel nodeIdLabel;
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

  //constructors
  public static void main(String[] args) {
    new Main();
  }
  public Main() {
    makePage();
    globalInfo = new GlobalInfo(Integer.parseInt(getTextField("hashSize").getText()),Integer.parseInt(getTextField("target").getText()));
    makeTextFieldListeners();
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
    setCons(headerPanelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    page.add(headerPanel, headerPanelCons);

    //settings
    JPanel settingsPanel = new JPanel();
    settingsPanel.setLayout(new GridBagLayout());

      JLabel settingsTitle = new JLabel("<HTML><U>Settings</U></HTML>");

      //title
      settingsTitle.setFont(settingsTitle.getFont().deriveFont(16.0f));

      GridBagConstraints settingsTitleCons = new GridBagConstraints();
      setCons(settingsTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      settingsPanel.add(settingsTitle, settingsTitleCons);

      //add input boxes
      makeUserInputPanel("hashSize","Maximum hash value: ", "10000", settingsPanel,0,1);
      makeUserInputPanel("target","Target: ", "1000", settingsPanel, 0,2);
      makeUserInputPanel("hashPerSec","Global Hashes per second: ", "1", settingsPanel, 0,3);

        JPanel settingsfillerPanel = new JPanel();
        settingsfillerPanel.setPreferredSize(new Dimension(400,10));

        GridBagConstraints settingsfillerPanelCons = new GridBagConstraints();
        setCons(settingsfillerPanelCons,0,4,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        settingsPanel.add(settingsfillerPanel, settingsfillerPanelCons);

    GridBagConstraints settingsCons = new GridBagConstraints();
    setCons(settingsCons,0,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(settingsPanel, settingsCons);

    //add_node
    JPanel addNodePanel = new JPanel();
    addNodePanel.setLayout(new GridBagLayout());

      JLabel addNodeTitle = new JLabel("<HTML><U>Add node</U></HTML>");
      //title
      addNodeTitle.setFont(addNodeTitle.getFont().deriveFont(16.0f));

      GridBagConstraints addNodeTitleCons = new GridBagConstraints();
      setCons(addNodeTitleCons,0,0,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      addNodePanel.add(addNodeTitle, addNodeTitleCons);

      //node name
      makeUserInputPanel("nodeName","Name: ", "Node name", addNodePanel, 0,1);
      makeUserInputPanel("hashShare","Hash Share(%):", "50.0", addNodePanel, 0,3);


        JPanel addNodeFillerPanel = new JPanel();
        addNodeFillerPanel.setPreferredSize(new Dimension(200,10));

        GridBagConstraints addNodeFillerPanelCons = new GridBagConstraints();
        setCons(addNodeFillerPanelCons,0,4,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        addNodePanel.add(addNodeFillerPanel, addNodeFillerPanelCons);

      //buttons
      JPanel addNodeButtons = new JPanel();

        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(e.getSource() == addNodeButton) {
              addNode();
              getTextField("nodeName").setText("Node name");
              getTextField("hashShare").setText(Double.toString(hashShareAvailable));
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
      setCons(addNodeButtonsCons,0,5,2,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
      addNodePanel.add(addNodeButtons, addNodeButtonsCons);

        JPanel addNodefillerPanel = new JPanel();
        addNodefillerPanel.setPreferredSize(new Dimension(400,10));

        GridBagConstraints addNodefillerPanelCons = new GridBagConstraints();
        setCons(addNodefillerPanelCons,0,1,4,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
        addNodePanel.add(addNodefillerPanel, addNodefillerPanelCons);

    //add to page
    GridBagConstraints addNodeCons = new GridBagConstraints();
    setCons(addNodeCons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(addNodePanel, addNodeCons);

    //preview
    JPanel previewPanel = new JPanel();
    previewPanel.setLayout(new GridBagLayout());

    JLabel titleLabel = new JLabel("                                       Name                   id            power        blocks                                                                    ");
      //title

      GridBagConstraints titleLabelCons = new GridBagConstraints();
      setCons(titleLabelCons, 0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,0,0);
      previewPanel.add(titleLabel, titleLabelCons);

      //scroll box
      previewScrollPanel = new JPanel();
      previewScrollPanel.setLayout(new GridBagLayout());
      previewScrollPanel.setSize(new Dimension(500,160));

      JScrollPane scroll = new JScrollPane(previewScrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setViewportView(previewScrollPanel);
      scroll.setPreferredSize(new Dimension(500,150));

        constructInitNodeDispHolders();

      GridBagConstraints scrollCons = new GridBagConstraints();
      setCons(scrollCons, 0,1,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      previewPanel.add(scroll, scrollCons);

        JButton clearPreviewButton = new JButton("Remove All");
        clearPreviewButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == clearPreviewButton) {
            removeAllNodes();
          }
        }
      });

      GridBagConstraints clearPreviewButtonCons = new GridBagConstraints();
      setCons(clearPreviewButtonCons,0,3,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      previewPanel.add(clearPreviewButton, clearPreviewButtonCons);

    GridBagConstraints previewPanelCons = new GridBagConstraints();
    setCons(previewPanelCons,0,4,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(previewPanel, previewPanelCons);

    //buttons
    JPanel buttonsPanel = new JPanel();

      JButton startButton = new JButton("Start");
      startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == startButton) {
            startSimulation();
          }
        }
      });
      buttonsPanel.add(startButton);

      JButton exitButton = new JButton("Exit");
      exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == exitButton) {
            System.exit(0);
          }
        }
      });
      buttonsPanel.add(exitButton);

    GridBagConstraints buttonsPanelCons = new GridBagConstraints();
    setCons(buttonsPanelCons,0,5,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(buttonsPanel, buttonsPanelCons);

    //add page to JFrame
    main.add(page);
    main.setVisible(true);
  }
  private void makeUserInputPanel(String id, String labelText, String textFieldText, JPanel panelToAddTo, int x, int y) {
    JPanel panel = new JPanel(new GridBagLayout());

    JPanel labelPanel = new JPanel(new GridBagLayout());
    labelPanel.setMinimumSize(new Dimension(200,20));
    labelPanel.setPreferredSize(new Dimension(200,20));

    GridBagConstraints labelPlacCons = new GridBagConstraints();
    setCons(labelPlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_END,0,0);
    JLabel label = new JLabel(labelText);
    labelPanel.add(label, labelPlacCons);

    GridBagConstraints labelCons = new GridBagConstraints();
    setCons(labelCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    panel.add(labelPanel, labelCons);

    JPanel textFieldPanel = new JPanel(new GridBagLayout());
    textFieldPanel.setMinimumSize(new Dimension(200,20));
    textFieldPanel.setPreferredSize(new Dimension(200,20));

    GridBagConstraints textFieldPlacCons = new GridBagConstraints();
    setCons(textFieldPlacCons,0,0,1,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START,0,0);
    JTextFieldWithID textField = new JTextFieldWithID(textFieldText, id);
    textField.setColumns(8);
    textFieldPanel.add(textField, textFieldPlacCons);
    //add to array of input text fields
    userInputTextFields.add(textField);

    GridBagConstraints textFieldCons = new GridBagConstraints();
    setCons(textFieldCons,1,0,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    panel.add(textFieldPanel, textFieldCons);

    GridBagConstraints cons = new GridBagConstraints();
    setCons(cons,x,y,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,10,10);
    panelToAddTo.add(panel, cons);

  }
  private JTextFieldWithID getTextField(String id) {
    for (JTextFieldWithID textField : userInputTextFields) {
      if (textField.id == id) {
        return textField;
      }
    }
    System.out.println("Invalid textField id given to getTextField()");
    return null;
  }
  private void constructInitNodeDispHolders() {
    initNodeDispHolderList = new ArrayList<JPanel>();

    //populate scroll panel with holder panels to force shape of scroll panel within scroll pane
    for (int i=0;i<=2;i++) {
      JPanel nodeDispHolder = new JPanel();
      nodeDispHolder.setMinimumSize(new Dimension(500, 40));

      GridBagConstraints panelCons = new GridBagConstraints();
      setCons(panelCons,0,i,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
      previewScrollPanel.add(nodeDispHolder, panelCons);

      initNodeDispHolderList.add(nodeDispHolder);
    }
  }
  private void makeTextFieldListeners() {
    //update globalInfo when values change
    getTextField("hashSize").getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
          try {
          changeHashSize();
        } catch(NumberFormatException ex) {}
        }
        public void removeUpdate(DocumentEvent e) {
          try {
          changeHashSize();
        } catch(NumberFormatException ex) {}
        }
        public void insertUpdate(DocumentEvent e) {
          try {
          changeHashSize();
        } catch(NumberFormatException ex) {}
        }
        private void changeHashSize() {
          globalInfo.setHashSize(Integer.parseInt(getTextField("hashSize").getText()));
        }
    });
    getTextField("target").getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        try {
        changeTarget();
        } catch(NumberFormatException ex) {}
      }
      public void removeUpdate(DocumentEvent e) {
        try {
        changeTarget();
        } catch(NumberFormatException ex) {}
      }
      public void insertUpdate(DocumentEvent e) {
        try {
        changeTarget();
        return;
        } catch(NumberFormatException ex) {}
      }
      private void changeTarget() {
        globalInfo.setTarget(Integer.parseInt(getTextField("target").getText()));
      }
    });
    getTextField("hashPerSec").addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (globalHashShareTextFieldClear) {
          getTextField("hashPerSec").setText("");
          globalHashShareTextFieldClear = false;
        }
      }
    });
    getTextField("nodeName").addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (nodeNameTextFieldClear) {
          getTextField("nodeName").setText("");
          nodeNameTextFieldClear = false;
        }
      }

    });
      getTextField("hashShare").addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (hashShareTextFieldClear) {
          getTextField("hashShare").setText("");
          hashShareTextFieldClear = false;
        }
      }
    });
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
    Double mine_speed = (Double.parseDouble(hashShare)/100)*Double.parseDouble(getTextField("hashPerSec").getText());
    //create node
    Node node = new Node(globalInfo,name,hashShare,mine_speed);

    return node;
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
      nodeDispHolder.setMinimumSize(new Dimension(500, 40));
      //add node dispPanel to holder panel
      nodeDispHolder.add(node.getDispPanel());
      //adjust size of panel to allow for new block
      previewScrollPanel.setSize(new Dimension(500,(node.id-2)*40 + 160));
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
    if (hashShareAvailable < Double.parseDouble(getTextField("hashPerSec").getText())) {
      JOptionPane.showMessageDialog(main,hashShareAvailable+"% hash share left.","Error",JOptionPane.PLAIN_MESSAGE);
      return;
    }
    Node node = createNode(getTextField("nodeName").getText(), getTextField("hashShare").getText());
    globalInfo.addToNodesList(node);
    //recalculate available hash share form value
    refreshHashShareAvailble();
    addNodeToPreview(node);
  }
  private void addNodeFromImport(String name, String hashShare) {
    Node node = createNode(name, hashShare);
    globalInfo.addToNodesList(node);
    //recalculate available hash share form value
    refreshHashShareAvailble();
    addNodeToPreview(node);
  }

  //nodesList methods
  public void removeNode(int id) {
    for (Node node : globalInfo.getNodesList()) {
      if (node.id == id) {
        globalInfo.removeFromNodesList(node);
        refreshNodesList();
        refreshPreview();
        refreshHashShareAvailble();
        break;
      }
    }
  }
  private void removeAllNodes() {
    //clear nodesLsit
    globalInfo.clearNodeList();
    //clear previewPanel
    previewScrollPanel.removeAll();
    refreshPreview();
    refreshHashShareAvailble();
  }
  public void refreshNodesList() {
    //purpose: when a node is removed, the id's of the remaining nodesList must be fixed
    for (int i=0;i<globalInfo.nodesListSize();i++) {
      //for each node, update id and re-make dispPanel with new id
      Node node = globalInfo.getNodeFromNodesList(i);
      node.id = i;
      node.makeNodeDispPanel();
    }
  }

  //nodes preview methods
  public void refreshPreview() {
    previewScrollPanel.removeAll();
    constructInitNodeDispHolders();
    System.out.println("nodes list size: "+globalInfo.nodesListSize());
    for (Node node : globalInfo.getNodesList()) {
      //add to preview at row node.id
      addNodeToPreview(node);
    }
    previewScrollPanel.revalidate();
    previewScrollPanel.repaint();
  }

  //UI methods
  public void refreshHashShareAvailble() {
    Double hsAvailble = 100.0;
    for (Node node : globalInfo.getNodesList()) {
      hsAvailble = hsAvailble - Double.parseDouble(node.getHashShare());
    }
    hashShareAvailable = hsAvailble;
    getTextField("hashShare").setText(Double.toString(hashShareAvailable));
    System.out.println("hash share available: "+ hashShareAvailable);
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

        //var stops after 10 entries
        int lineNum = 0;

        while((line = bufferedReader.readLine()) != null) {
            //dont process start and end of JSON
            if(line.length() > 1 && lineNum < 11) {
              lineNum = lineNum + 1;

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
        System.out.println("hash share list: "+hashShareList);
        System.out.println("node names list: "+nodeNamesList);
        for (int i=0;i<nodeNamesList.size();i++) {
          addNodeFromImport(nodeNamesList.get(i),Double.toString(hashShareList.get(i)));
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

    //check for input errors
    try {
      int hashSize = Integer.parseInt(getTextField("hashSize").getText());
      if (hashSize < 0) {
        errorMsg("Hash size");
        return;
      }
    } catch(NumberFormatException ex) {
      errorMsg("Hash size");
      return;
    }
    try {
      int hashSize = Integer.parseInt(getTextField("target").getText());
      if (hashSize < 0) {
        errorMsg("Target");
        return;
      }
    } catch(NumberFormatException ex) {
      errorMsg("Target");
      return;
    }
    try {
      int hashSize = Integer.parseInt(getTextField("hashPerSec").getText());
      if (hashSize < 0) {
        errorMsg("Global hashes per second");
        return;
      }
    } catch(NumberFormatException ex) {
      errorMsg("Global hashes per second");
      return;
    }
    //make simulation object
    Simulation simulation = new Simulation(globalInfo);

    //add simulation object to nodes
    for (Node node : globalInfo.getNodesList()) {
      node.setSimulationObject(simulation);
    }
  }
  private void errorMsg(String var) {
    JOptionPane.showMessageDialog(null,
      "Error: "+var+" must be an integer bigger than 0", "Error Message",
      JOptionPane.ERROR_MESSAGE);
  }

  public void removeNodeWindow() {
      //catch if nodesList is empty
      int size = globalInfo.nodesListSize();
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

  class JTextFieldWithID extends JTextField {
    String id;
    public JTextFieldWithID(String text, String id) {
      super(text);
      this.id = id;
    }
  }
}
