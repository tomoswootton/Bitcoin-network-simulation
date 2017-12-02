import java.awt.*;

import javax.swing.*;
import java.awt.GridBagLayout;

import java.awt.event.*;

import java.util.LinkedList;


public class test {

  JFrame main = new JFrame();

  public static void main(String[] args) {
    new test();
  }

  public test() {

    main.setSize(800, 600);

    main.setLocationRelativeTo(null);
    main.setResizable(false);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.setTitle("Menu");

    //create UI
    JPanel page = new JPanel();
    page.setLayout(new GridBagLayout());
    page.setBackground(Color.green);

    //header
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(Color.black);
    headerPanel.setPreferredSize(new Dimension(500,100));

    JScrollPane scroll = new JScrollPane(headerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setViewportView(headerPanel);
    scroll.setPreferredSize(new Dimension(100,100));

      JLabel title = new JLabel("Network Simulator");
      title.setFont(title.getFont().deriveFont(28.0f));

      headerPanel.add(title);

    GridBagConstraints headerPanelCons = new GridBagConstraints();
    setCons(headerPanelCons, 2,2,1,1,GridBagConstraints.NONE,GridBagConstraints.CENTER,0,0);
    page.add(scroll, headerPanelCons);

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
}
