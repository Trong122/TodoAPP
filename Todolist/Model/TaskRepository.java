package Model;
import Observer.Observable;
import Observer.Observer;
import Config.MySQLConnect;

import java.sql.*;
import java.util.ArrayList;


public class TaskRepository implements Observable {

    private static TaskRepository instance;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();
    private static MySQLConnect mySQLConnect = MySQLConnect.getInstance();
    private TaskRepository() {}
    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }
    public void addTask(Task task) {
        String query = "INSERT INTO congviec (id, tieu_de, noi_dung, loai_cong_viec, thong_tin, hoan_thanh) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = mySQLConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getContent());
            pstmt.setString(4, task.getType());
            pstmt.setString(5, task.getInfo());
            pstmt.setBoolean(6, task.isCompleted());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                getTasks();
                notifyObservers("✅ Đã thêm công việc: " + task.getTitle());
            } else {
                 notifyObservers("⚠️ Không có dòng nào được thêm.");
            }

        } catch (SQLException e) {
             notifyObservers("⚠️ Không có dòng nào được thêm."+ e.getMessage());
        }
    }

    public Task getTask(String id) {
        for (Task t : tasks) {
            if (t.getId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }
    public ArrayList<Task> getTasks() {
        tasks.clear();
        String query = "SELECT * FROM congviec";

        try (Connection conn = mySQLConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String title = rs.getString("tieu_de");
                String content = rs.getString("noi_dung");
                boolean isCompleted = rs.getBoolean("hoan_thanh");
                String type = rs.getString("loai_cong_viec");
                String info = rs.getString("thong_tin");

                Task task = TaskFactory.createTask(id, title,type, content, isCompleted, info);
                tasks.add(task);
            }

        } catch (SQLException e) {
            notifyObservers("Lỗi khi lấy danh sách công việc" + e.getMessage());
        }

        return tasks;
    }
    public void updateTask(String id, Task newTask) {
        String query = "UPDATE CongViec SET tieu_de = ?, noi_dung = ?, loai_cong_viec = ?, thong_tin = ?, hoan_thanh = ? WHERE id = ?";

        try (Connection conn = mySQLConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newTask.getTitle());
            pstmt.setString(2, newTask.getContent());
            pstmt.setString(3, newTask.getType());
            pstmt.setString(4, newTask.getInfo());
            pstmt.setBoolean(5, newTask.isCompleted());
            pstmt.setInt(6, Integer.parseInt(id)); // Nếu id trong DB là INT

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                notifyObservers("✅Đã Cập nhập công việc: ");
                // Cập nhật trong ArrayList
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId().equalsIgnoreCase(id)) {
                        tasks.set(i, newTask);
                        return;
                    }
                }

            } else {
                notifyObservers("❌ Không tìm thấy công việc có ID = " + id);
            }

        } catch (SQLException e) {
             notifyObservers("❌ Lỗi khi cập nhập công việc" + e.getMessage());
        } catch (NumberFormatException e) {
            notifyObservers("❌ ID không hợp lệ" + e.getMessage());
        }
    }
    public void removeTask(String id) {
        String query = "DELETE FROM CongViec WHERE id = ?";

        try (Connection conn = mySQLConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(id)); // Nếu ID là số nguyên trong DB

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                 notifyObservers(" Đã xóa công việc " + id);
                // Xoá khỏi danh sách trong bộ nhớ
                tasks.removeIf(t -> t.getId().equalsIgnoreCase(id));
            } else {
                 notifyObservers("❌ Không tìm thấy id " + id + " trong cơ sở dữ liệu.");
            }

        } catch (SQLException e) {
            notifyObservers("❌ Lỗi khi xóa công việc " + e.getMessage());
        } catch (NumberFormatException e) {
             notifyObservers("❌ Id không hợp lệ " + e.getMessage());
        }
    }
    public void addObserver(Observer o) {
        observers.add(o);
    }
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    public void notifyObservers(String msg) {
        for (Observer o : observers) {
            o.update(msg);
        }
    }
}