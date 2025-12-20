import javax.swing.SwingUtilities;
import ui.LoginScreen;

public class StudentApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}