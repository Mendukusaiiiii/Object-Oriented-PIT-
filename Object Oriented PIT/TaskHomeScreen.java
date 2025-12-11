import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TaskHomeScreen extends JFrame {
    private JPanel taskContainer;
    private JPanel taskPanel;
    private JPanel taskDonePanel;
    private JPanel folderPanel;

    private java.util.List<Task> tasks;        
    private java.util.List<String> folders;   

    // Task class to represent each task
    class Task {
        String name;
        boolean isCompleted;

        Task(String name, boolean isCompleted) {
            this.name = name;
            this.isCompleted = isCompleted;
        }

        void toggleCompletion() {
            this.isCompleted = !this.isCompleted;
        }
    }

    public TaskHomeScreen() {
        tasks = new ArrayList<>();
        folders = new ArrayList<>(); // ? ADDED: Initialize folders list

        setTitle("    App Title");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("App Title", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(title);

        main.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        main.add(sep);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel header = new JPanel(new BorderLayout());
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setOpaque(false);

        JLabel tasksLabel = new JLabel("Tasks");
        tasksLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] filterOptions = {"Tasks", "Tasks Done", "Folder"};

        header.add(tasksLabel, BorderLayout.WEST);
        header.add(createDropdown("Show", filterOptions), BorderLayout.EAST);
        main.add(header);

        main.add(Box.createRigidArea(new Dimension(0, 15)));

        taskContainer = new JPanel();
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));

        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        taskDonePanel = new JPanel();
        taskDonePanel.setLayout(new BoxLayout(taskDonePanel, BoxLayout.Y_AXIS));

        folderPanel = new JPanel();
        folderPanel.setLayout(new BoxLayout(folderPanel, BoxLayout.Y_AXIS));

        taskContainer.add(taskPanel);
        main.add(taskContainer);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setOpaque(false);

        JButton addTaskBtn = new JButton("Add Task");
        JButton createFolderBtn = new JButton("Create Folder");

        addTaskBtn.setPreferredSize(new Dimension(140, 40));
        createFolderBtn.setPreferredSize(new Dimension(140, 40));

        buttonPanel.add(addTaskBtn);
        buttonPanel.add(createFolderBtn);

        main.add(buttonPanel);

        addTaskBtn.addActionListener(e -> openAddTaskForm());
        createFolderBtn.addActionListener(e -> openCreateFolderForm());

        add(main);
        setVisible(true);
    }

    // ---------------------- Dropdown ----------------------
    private JPanel createDropdown(String labelText, String[] items) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        JLabel mainLabel = new JLabel(labelText);
        mainLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel dropdownBox = new JPanel(new BorderLayout());
        dropdownBox.setBackground(Color.WHITE);
        dropdownBox.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2, true));

        JLabel arrow = new JLabel("?");
        arrow.setFont(new Font("Arial", Font.BOLD, 16));
        arrow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        dropdownBox.add(mainLabel, BorderLayout.WEST);
        dropdownBox.add(arrow, BorderLayout.EAST);

        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));

        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item);
            menuItem.setFont(new Font("Arial", Font.PLAIN, 16));

            menuItem.addChangeListener(e -> {
                if (menuItem.isArmed())
                    menuItem.setBackground(new Color(220, 235, 255));
                else
                    menuItem.setBackground(Color.WHITE);
            });

            menuItem.addActionListener(a -> {
                mainLabel.setText(item);
                menu.setVisible(false);

                if (item.equals("Tasks")) {
                    taskContainer.removeAll();
                    taskContainer.add(taskPanel);

                } else if (item.equals("Tasks Done")) {
                    taskContainer.removeAll();
                    taskContainer.add(taskDonePanel);

                } else if (item.equals("Folder")) {
                    taskContainer.removeAll();
                    updateFolderPanel(); // ? ADDED
                    taskContainer.add(folderPanel);
                }

                taskContainer.revalidate();
                taskContainer.repaint();
            });

            menu.add(menuItem);
        }

        dropdownBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.show(dropdownBox, 0, dropdownBox.getHeight());
            }
        });

        container.add(dropdownBox);
        return container;
    }

    // ---------------------- Add Task Popup ----------------------
    private void openAddTaskForm() {
        JDialog form = new JDialog(this, "Add Task", true);
        form.setSize(500, 350);
        form.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel subjectLbl = new JLabel("Subject:");
        JLabel actName = new JLabel("Act Name:");
        JLabel Deadline = new JLabel("Deadline:");
        JLabel Taskdesc = new JLabel("Deadline:");
        JTextField subjectField = new JTextField(20);
        

        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.setPreferredSize(new Dimension(120, 35));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(subjectLbl, gbc);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(actName, gbc);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(Deadline, gbc);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(Taskdesc, gbc);
        gbc.gridx = 1; panel.add(subjectField, gbc);
        

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(confirmBtn, gbc);

        confirmBtn.addActionListener(e -> {
            String taskName = subjectField.getText();
            tasks.add(new Task(taskName, false));
            updateTaskPanels();
            form.dispose();
        });

        form.add(panel);
        form.setVisible(true);
    }

    // ---------------------- Task Panel Update ----------------------
    private void updateTaskPanels() {
        taskPanel.removeAll();
        taskDonePanel.removeAll();

        for (Task task : tasks) {
            JPanel taskItemPanel = createTaskPanel(
                    task.isCompleted ? Color.GREEN : Color.RED,
                    task.name
            );

            if (task.isCompleted) {
                taskDonePanel.add(taskItemPanel);
            } else {
                taskPanel.add(taskItemPanel);
            }
        }

        taskPanel.revalidate();
        taskPanel.repaint();
        taskDonePanel.revalidate();
        taskDonePanel.repaint();
    }

    // ---------------------- Create Task Panel ----------------------
    private JPanel createTaskPanel(Color statusColor, String taskName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JPanel indicator = new JPanel();
        indicator.setBackground(statusColor);
        indicator.setPreferredSize(new Dimension(15, 15));
        indicator.setMaximumSize(new Dimension(15, 15));

        JLabel nameLabel = new JLabel(taskName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        panel.add(indicator, BorderLayout.WEST);
        panel.add(nameLabel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Task t : tasks) {
                    if (t.name.equals(taskName)) {
                        t.toggleCompletion();
                        updateTaskPanels();
                        break;
                    }
                }
            }
        });

        return panel;
    }

    // ---------------------- ? Folder Panel Update ----------------------
    private void updateFolderPanel() {
        folderPanel.removeAll();

        JLabel title = new JLabel("Folders:");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        folderPanel.add(title);

        folderPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (String folder : folders) {
            JLabel folderLabel = new JLabel("? " + folder);
            folderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            folderLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            folderPanel.add(folderLabel);
        }

        folderPanel.revalidate();
        folderPanel.repaint();
    }

    // ---------------------- Create Folder Popup ----------------------
    private void openCreateFolderForm() {
        JDialog form = new JDialog(this, "Create Folder", true);
        form.setSize(400, 200);
        form.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Folder Name:");
        JTextField nameField = new JTextField(18);
        JButton createBtn = new JButton("Create Folder");
        createBtn.setPreferredSize(new Dimension(150, 35));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createBtn, gbc);

        // ? UPDATED: Now actually saves folder
        createBtn.addActionListener(e -> {
            String folderName = nameField.getText().trim();

            if (folderName.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Folder name cannot be empty.");
                return;
            }

            folders.add(folderName); // ? Save folder
            updateFolderPanel();     // ? Refresh folder list

            form.dispose();
        });

        form.add(panel);
        form.setVisible(true);
    }

    // ---------------------- MAIN ----------------------
    public static void main(String[] args) {
        new TaskHomeScreen();
    }
}
