import javax.swing.JTextField;
public class JTextFieldWithID extends JTextField {
    private static final long serialVersionUID = 1L;
	  String id;
    public JTextFieldWithID(String text, String id) {
      super(text);
      this.id = id;
    }
  }