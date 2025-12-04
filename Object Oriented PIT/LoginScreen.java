import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Student Login");
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

      
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon logo = new ImageIcon("logo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        add(logoLabel, BorderLayout.NORTH);

     
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField usernameField = new JTextField(15);

        
        JTextField studentIdField = new JTextField(15);
        studentIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }

                
                if (studentIdField.getText().length() >= 10) {
                    e.consume();
                }
            }
        });

        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        formPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridy = 3;
        formPanel.add(studentIdField, gbc);

        gbc.gridy = 4;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

       
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String studentId = studentIdField.getText();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty.");
                return;
            }

            if (studentId.length() != 10) {
                JOptionPane.showMessageDialog(null,
                        "Student ID must be exactly 10 digits.");
                return;
            }

            JOptionPane.showMessageDialog(null, "Login Successful!");
        });
    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}
