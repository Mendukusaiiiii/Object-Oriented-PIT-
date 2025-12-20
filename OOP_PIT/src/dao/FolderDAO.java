package dao;

import db.DBConnection;
import model.Folder;

import java.sql.*;
import java.util.ArrayList;

public class FolderDAO {

    public static ArrayList<Folder> getFolders(int studentId) {
        ArrayList<Folder> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement("SELECT * FROM folder WHERE student_id=?")) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Folder(
                        rs.getInt("folder_id"),
                        rs.getString("folder_name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void addFolder(String name, int studentId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "INSERT INTO folder(folder_name, student_id) VALUES(?,?)")) {
            ps.setString(1, name);
            ps.setInt(2, studentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}