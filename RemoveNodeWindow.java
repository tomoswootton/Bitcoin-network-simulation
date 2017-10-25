import javax.swing.*;
import java.awt.event.*;

public class RemoveNodeWindow extends JFrame {

  Main main;
  JTextField nodeId;
  JButton removeButton;
  JButton cancelButton;

  // public static void main(String[] args) {
  //   new RemoveNodeWindow();
  // }

  public RemoveNodeWindow(Main main) {
    this.main = main;
    ListenForButton lForButton = new ListenForButton();

    this.setSize(400, 100);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Remove Node");

    JPanel page = new JPanel();

      //components
      nodeId = new JTextField();
      nodeId.setColumns(5);
      page.add(nodeId);


      removeButton = new JButton("Remove Node");
      removeButton.addActionListener(lForButton);
      page.add(removeButton);

      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(lForButton);
      page.add(cancelButton);

    this.add(page);
    this.setVisible(true);
  }

  private class ListenForButton implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      if(e.getSource() == removeButton) {
        main.removeNode(Integer.parseInt(nodeId.getText()));
        dispose();
      } else if (e.getSource() == cancelButton) {
        dispose();
      }
    }
  }

}
