package ui;

import javax.swing.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Login");
        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField idField = new JTextField();
        JButton login = new JButton("Login");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Student ID:"));
        add(idField);
        add(login);

        login.addActionListener(e -> {
            int studentId = Integer.parseInt(idField.getText());
            new StudentRequirementOrganizer(studentId);
            dispose();
        });

        setVisible(true);
    }
}