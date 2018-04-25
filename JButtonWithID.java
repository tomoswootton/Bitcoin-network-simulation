import javax.swing.JButton;
public class JButtonWithID extends JButton {
    private static final long serialVersionUID = 1L;
    String id;

    public JButtonWithID(String text, String id) {
        super(text);
        this.id = id;
    }
}