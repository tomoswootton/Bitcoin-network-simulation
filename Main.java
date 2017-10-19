import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class Main extends JFrame {

  JButton startButton;
  JButton exitButton;

  public static void main(String[] args) {
    new Main();
  }

  public Main() {

    this.setSize(800, 400);
    this.setLocationRelativeTo(null);
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Main Menu");

    //main- contains header, config, buttons panels
    JPanel main = new JPanel();

    //header
    JPanel header = new JPanel();

    JLabel heading = new JLabel("Main Menu");
    heading.setToolTipText("nothing");
    header.add(heading);

    main.add(header);

    //config
    JPanel config = new JPanel();

    JTextField text = new JTextField("config", 15);
    config.add(text);

    main.add(config);

    //buttons
    JPanel buttons = new JPanel();

    startButton = new JButton("Start");
    buttons.add(startButton);
    exitButton = new JButton("Exit");
    buttons.add(exitButton);

    ListenForButton lForButton = new ListenForButton();
    startButton.addActionListener(lForButton);
    exitButton.addActionListener(lForButton);

    main.add(buttons);

    this.add(main);
    this.setVisible(true);
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
