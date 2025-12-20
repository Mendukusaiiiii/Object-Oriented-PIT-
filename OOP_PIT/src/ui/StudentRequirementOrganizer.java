package ui;

import dao.FolderDAO;
import dao.TaskDAO;
import model.Folder;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class StudentRequirementOrganizer extends JFrame {

    private int studentId;
    private ArrayList<Task> tasks;
    private ArrayList<Folder> folders;

    private JPanel container;
    private JComboBox<String> filterBox;

    public StudentRequirementOrganizer(int studentId) {
        this.studentId = studentId;

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

        loadData();
        refreshView();
        setVisible(true);
    }

    private void loadData() {
        tasks = TaskDAO.getTasks(studentId);
        folders = FolderDAO.getFolders(studentId);
    }

    private void refreshView() {
        container.removeAll();
        String filter = (String) filterBox.getSelectedItem();

        if ("Folders".equals(filter)) {
            for (Folder f : folders) {
                JButton btn = new JButton(f.getName());
                btn.addActionListener(e -> openFolderView(f));
                container.add(btn);
            }
        } else {
            tasks.sort(Comparator.comparing(Task::daysRemaining));
            for (Task t : tasks) {
                if ("Tasks".equals(filter) && t.isDone()) continue;
                if ("Done".equals(filter) && !t.isDone()) continue;
                container.add(createTaskCard(t));
            }
        }

        container.revalidate();
        container.repaint();
    }

    /* ===== DASHBOARD CARD ===== */
    private JPanel createTaskCard(Task task) {
        JPanel panel = new JPanel(new GridLayout(2,1));
        panel.setMaximumSize(new Dimension(800, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(task.getUrgencyColor());

        JLabel subject = new JLabel(task.getSubject());
        JLabel title = new JLabel(task.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(subject);
        panel.add(title);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openTaskDetails(task);
            }
        });

        return panel;
    }

    /* ===== TASK DETAILS ===== */
    private void openTaskDetails(Task task) {
        JDialog dialog = new JDialog(this, "Task Details", true);
        dialog.setSize(450, 420);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        dialog.add(new JLabel("Activity Name: " + task.getTitle()));
        dialog.add(new JLabel("Description: " + task.getDescription()));

        if (task.isDone()) {
            dialog.add(new JLabel("Status: DONE"));
        } else {
            dialog.add(new JLabel("Time Remaining: " + task.timeRemainingText()));
        }

        JProgressBar bar = new JProgressBar();
        bar.setPreferredSize(new Dimension(380, 25));
        bar.setMaximum(100);
        bar.setValue(100);
        bar.setStringPainted(false);

        bar.setForeground(task.isDone() ? Color.GREEN : task.getUrgencyColor());
        dialog.add(bar);

        if (!task.isDone()) {
            JButton edit = new JButton("Edit Task");
            JButton done = new JButton("Mark as Done");

            done.addActionListener(e -> {
                TaskDAO.markDone(task.getId());
                loadData();
                dialog.dispose();
                refreshView();
            });

            dialog.add(edit);
            dialog.add(done);
        }

        JButton delete = new JButton("Delete Task");
        delete.addActionListener(e -> {
            TaskDAO.delete(task.getId());
            loadData();
            dialog.dispose();
            refreshView();
        });

        dialog.add(delete);
        dialog.setVisible(true);
    }

    /* ===== ADD TASK (WITH FOLDER) ===== */
    private void openAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add Task", true);
        dialog.setSize(400, 420);
        dialog.setLayout(new GridLayout(0,2,10,10));

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
        dialog.add(new JLabel()); dialog.add(save);

        save.addActionListener(e -> {
            Integer folderId = null;
            if (!folderBox.getSelectedItem().equals("None")) {
                folderId = folders.stream()
                        .filter(f -> f.getName().equals(folderBox.getSelectedItem()))
                        .findFirst().get().getId();
            }

            TaskDAO.addTask(
                    new Task(0, subject.getText(), title.getText(), desc.getText(),
                            LocalDate.now(), LocalDate.parse(due.getText()), false, folderId),
                    studentId
            );

            loadData();
            dialog.dispose();
            refreshView();
        });

        dialog.setVisible(true);
    }

    private void openCreateFolderDialog() {
        String name = JOptionPane.showInputDialog(this, "Folder Name:");
        if (name != null && !name.isEmpty()) {
            FolderDAO.addFolder(name, studentId);
            loadData();
            refreshView();
        }
    }

    private void openFolderView(Folder folder) {
        container.removeAll();
        tasks.stream()
                .filter(t -> t.getFolderId() != null && t.getFolderId() == folder.getId())
                .sorted(Comparator.comparing(Task::daysRemaining))
                .forEach(t -> container.add(createTaskCard(t)));

        container.revalidate();
        container.repaint();
    }
}