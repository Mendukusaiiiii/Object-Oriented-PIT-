package dao;

import db.DBConnection;
import model.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TaskDAO {

    /* ================= LOAD TASKS ================= */

    public static ArrayList<Task> getTasks(int studentId) {
        ArrayList<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM task WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Task(
                        rs.getInt("task_id"),
                        rs.getString("subject"),
                        rs.getString("activity_name"),
                        rs.getString("description"),
                        rs.getDate("date_created").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getBoolean("status"),
                        (Integer) rs.getObject("folder_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /* ================= ADD TASK ================= */

    public static void addTask(Task t, int studentId) {
        String sql =
                "INSERT INTO task " +
                        "(subject, activity_name, description, date_created, due_date, status, student_id, folder_id) " +
                        "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getSubject());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getDescription());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setDate(5, Date.valueOf(t.getDueDate()));
            ps.setBoolean(6, false);
            ps.setInt(7, studentId);
            ps.setObject(8, t.getFolderId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= MARK AS DONE ================= */

    public static void markDone(int taskId) {
        String sql = "UPDATE task SET status=1 WHERE task_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE TASK ================= */

    public static void delete(int taskId) {
        String sql = "DELETE FROM task WHERE task_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= UPDATE TASK (NEW – REQUIRED) ================= */
    // Used by: Edit Task dialog
    public static void updateTask(
            int taskId,
            String subject,
            String title,
            String description,
            LocalDate dueDate
    ) {
        String sql =
                "UPDATE task SET subject=?, activity_name=?, description=?, due_date=? " +
                        "WHERE task_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, subject);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setDate(4, Date.valueOf(dueDate));
            ps.setInt(5, taskId);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}