import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Student Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel idLabel = new JLabel("Student ID:");
        JTextField idField = new JTextField(15);

        // Limit to 10 digits only
        ((AbstractDocument) idField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 10 && string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length()) <= 10 && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JButton loginBtn = new JButton("Login");

        // Layout positioning
        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(idLabel, gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String studentId = idField.getText().trim();

            if (username.isEmpty() || studentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both fields.");
                return;
            }

            if (studentId.length() < 10) {
                JOptionPane.showMessageDialog(this, "Student ID must be exactly 10 digits.");
                return;
            }

            // Login successful
            SwingUtilities.invokeLater(() -> new StudentRequirementOrganizer());
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
