import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

/* =====================================================
   LOGIN SCREEN
   ===================================================== */
class LoginScreen extends JFrame {

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

        // 🔒 Limit Student ID to 10 digits only
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

            if (studentId.length() != 10) {
                JOptionPane.showMessageDialog(this, "Student ID must be exactly 10 digits.");
                return;
            }

            // ✅ Login success → open main system
            new StudentRequirementOrganizer();
            dispose();
        });

        setVisible(true);
    }
}

/* =====================================================
   MAIN APPLICATION
   ===================================================== */
class StudentRequirementOrganizer extends JFrame {

    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Folder> folders = new ArrayList<>();
    private JPanel container;
    private JComboBox<String> filterBox;

    public StudentRequirementOrganizer() {
        setTitle("Student Requirement Organizer");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addTaskBtn = new JButton("Add Task");
        JButton addFolderBtn = new JButton("Create Folder");

        filterBox = new JComboBox<>(new String[]{"Tasks", "Done", "Folders"});

        addTaskBtn.addActionListener(e -> openAddTaskDialog());
        addFolderBtn.addActionListener(e -> openCreateFolderDialog());
        filterBox.addActionListener(e -> refreshView());

        topPanel.add(addTaskBtn);
        topPanel.add(addFolderBtn);
        topPanel.add(new JLabel("Show:"));
        topPanel.add(filterBox);

        add(topPanel, BorderLayout.NORTH);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        add(new JScrollPane(container), BorderLayout.CENTER);

        setVisible(true);
    }

    private void refreshView() {
        container.removeAll();
        String filter = (String) filterBox.getSelectedItem();

        if (filter.equals("Folders")) {
            for (Folder folder : folders) {
                JButton btn = new JButton(folder.getName());
                btn.addActionListener(e -> openFolderView(folder));
                container.add(btn);
            }
        } else {
            tasks.sort(Comparator.comparing(Task::daysRemaining));
            for (Task task : tasks) {
                if (filter.equals("Tasks") && task.isDone()) continue;
                if (filter.equals("Done") && !task.isDone()) continue;
                container.add(createTaskCard(task));
            }
        }
        container.revalidate();
        container.repaint();
    }

    private JPanel createTaskCard(Task task) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(800, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(task.isDone() ? Color.LIGHT_GRAY : task.getUrgencyColor());

        JLabel label = new JLabel(task.getTitle() + " (" + task.getSubject() + ")");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openTaskDetails(task);
            }
        });

        return panel;
    }

    private void openAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add Task", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7,2,10,10));

        JTextField subject = new JTextField();
        JTextField title = new JTextField();
        JTextArea desc = new JTextArea();
        JTextField due = new JTextField("YYYY-MM-DD");

        JComboBox<String> folderBox = new JComboBox<>();
        folderBox.addItem("None");
        for (Folder f : folders) folderBox.addItem(f.getName());

        JButton save = new JButton("Save");

        dialog.add(new JLabel("Subject:")); dialog.add(subject);
        dialog.add(new JLabel("Activity Name:")); dialog.add(title);
        dialog.add(new JLabel("Description:")); dialog.add(desc);
        dialog.add(new JLabel("Due Date:")); dialog.add(due);
        dialog.add(new JLabel("Folder:")); dialog.add(folderBox);
        dialog.add(new JLabel("")); dialog.add(save);

        save.addActionListener(e -> {
            try {
                Task task = new Task(subject.getText(), title.getText(),
                        desc.getText(), LocalDate.parse(due.getText()));

                if (!folderBox.getSelectedItem().equals("None")) {
                    Folder f = getFolderByName(folderBox.getSelectedItem().toString());
                    task.setFolder(f);
                    f.addTask(task);
                }

                tasks.add(task);
                dialog.dispose();
                refreshView();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input.");
            }
        });

        dialog.setVisible(true);
    }

    private void openTaskDetails(Task task) {
        JDialog dialog = new JDialog(this, "Task Details", true);
        dialog.setSize(450, 420);
        dialog.setLayout(new GridLayout(0,1,10,10));

        dialog.add(new JLabel("Activity Name: " + task.getTitle()));
        dialog.add(new JLabel("Description: " + task.getDescription()));
        dialog.add(new JLabel("Date Created: " + task.getDateCreated()));
        dialog.add(new JLabel("Due Date: " + task.getDueDate()));

        JButton delete = new JButton("Delete Task");
        delete.addActionListener(e -> {
            tasks.remove(task);
            dialog.dispose();
            refreshView();
        });

        JButton done = new JButton("Mark as Done");
        done.addActionListener(e -> {
            task.setDone(true);
            dialog.dispose();
            refreshView();
        });

        dialog.add(delete);
        dialog.add(done);
        dialog.setVisible(true);
    }

    private void openCreateFolderDialog() {
        String name = JOptionPane.showInputDialog(this, "Folder Name:");
        if (name != null && !name.isEmpty()) {
            folders.add(new Folder(name));
            refreshView();
        }
    }

    private void openFolderView(Folder folder) {
        container.removeAll();
        for (Task t : folder.getTasks()) {
            container.add(createTaskCard(t));
        }
        container.revalidate();
        container.repaint();
    }

    private Folder getFolderByName(String name) {
        for (Folder f : folders)
            if (f.getName().equals(name)) return f;
        return null;
    }
}

/* =====================================================
   TASK CLASS
   ===================================================== */
class Task {
    private String subject, title, description;
    private LocalDate dateCreated = LocalDate.now(), dueDate;
    private boolean done;
    private Folder folder;

    public Task(String s, String t, String d, LocalDate due) {
        subject = s; title = t; description = d; dueDate = due;
    }

    public String getSubject(){return subject;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public LocalDate getDateCreated(){return dateCreated;}
    public LocalDate getDueDate(){return dueDate;}
    public boolean isDone(){return done;}

    public void setDone(boolean d){done=d;}
    public void setFolder(Folder f){folder=f;}

    public long daysRemaining() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public Color getUrgencyColor() {
        long d = daysRemaining();
        if (d <= 1) return Color.RED;
        if (d <= 3) return Color.YELLOW;
        return Color.GREEN;
    }
}

/* =====================================================
   FOLDER CLASS
   ===================================================== */
class Folder {
    private String name;
    private ArrayList<Task> tasks = new ArrayList<>();
    public Folder(String n){name=n;}
    public String getName(){return name;}
    public void addTask(Task t){tasks.add(t);}
    public ArrayList<Task> getTasks(){return tasks;}
}

/* =====================================================
   SINGLE ENTRY POINT
   ===================================================== */
public class StudentApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
