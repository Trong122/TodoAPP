package View;
import Model.*;
import Controller.*;
import Observer.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TodoApp extends JFrame implements Observer{
    private JTextField idTask,title,content,subject,sport,work;
    private JComboBox<String> typeTask;
    private JTable taskTable;
    private JMenuBar jmb_file;
    private JMenuItem jit_undo, jit_redo;
    private DefaultTableModel tableModel;
    private TaskController taskController= TaskController.getInstance();
    public TodoApp() {
        setTitle("Todo List - Công việc hằng ngày");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //==Jmenu bar====
        jmb_file = new JMenuBar();
        JMenu Edit=new JMenu("Edit");
        jit_undo= new JMenuItem("Undo",KeyEvent.VK_Z);
        jit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        Edit.add(jit_undo);
        jit_redo = new JMenuItem("Redo",KeyEvent.VK_Y);
        jit_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        Edit.add(jit_redo);
        jmb_file.add(Edit);
        this.setJMenuBar(jmb_file);
        // ==== Panel nhập ====
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel jpn_input=new JPanel(new GridLayout(6,1));
        jpn_input.add(new JLabel("ID Công việc:"));
        idTask = new JTextField();
        jpn_input.add(idTask);
        jpn_input.add(new JLabel("Tiêu đề công việc:"));
        title = new JTextField();
        jpn_input.add(title);
        jpn_input.add(new JLabel("Nội dung chi tiết:"));
        content = new JTextField();
        jpn_input.add(content);
        jpn_input.add(new JLabel("Loại công việc:"));
        String[] taskTypes = {"Work", "Study", "Sport"};
        typeTask = new JComboBox<>(taskTypes);
        jpn_input.add(typeTask);
        //jtexFiel ẩn
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jlb_sj=new JLabel("Tên môn học");
        panelInfo.add(jlb_sj);
        jlb_sj.setVisible(false);
        subject = new JTextField(10);
        subject.setVisible(false);
        panelInfo.add(subject);
        jpn_input.add(panelInfo);
        JLabel jlb_sp=new JLabel("Tên môn thể thao");
        panelInfo.add(jlb_sp);
        jlb_sp.setVisible(false);
        sport = new JTextField(10);
        sport.setVisible(false);
        panelInfo.add(sport);
        JLabel jlb_wk=new JLabel("Tên công việc");
        panelInfo.add(jlb_wk);
        jlb_wk.setVisible(false);
        work = new JTextField(10);
        work.setVisible(false);
        panelInfo.add(work);
        //add button
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Thêm");
        JButton editBtn = new JButton("Sửa");
        JButton deleteBtn = new JButton("Xóa");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        inputPanel.add(jpn_input, BorderLayout.NORTH);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==== Bảng công việc ====
        String[] columns = {"Trạng thái công việc", "ID Công việc", "Tiêu đề", "Nội dung", "Loại công việc", "Thông tin"};
        tableModel = new DefaultTableModel(columns, 0) {
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
            public boolean isCellEditable(int row, int col) {
                return col == 0; // Chỉ cho phép tick checkbox
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(taskTable);
        // add sự kiện
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && taskTable.getSelectedRow() != -1) {
                    int selectedRow = taskTable.getSelectedRow();
                    idTask.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    title.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    content.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    String type = tableModel.getValueAt(selectedRow, 4).toString();
                    typeTask.setSelectedItem(type);
                    String info = tableModel.getValueAt(selectedRow, 5).toString();

                    subject.setText("");
                    sport.setText("");
                    work.setText("");
                    if ("Study".equals(type)) {
                    subject.setText(info);
                    } else if ("Sport".equals(type)) {
                    sport.setText(info);
                    } else if ("Work".equals(type)) {
                    work.setText(info);
                }
            }              
        });
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
    
             // Chỉ xử lý khi thay đổi ở cột "Hoàn thành" (cột 0)
            if (column == 0 && row >= 0) {
            Boolean isChecked = (Boolean) tableModel.getValueAt(row, 0);
            String taskId = (String) tableModel.getValueAt(row, 1);

            // Gọi controller cập nhật task
            taskController.updateCompletionStatus(taskId, isChecked);
            }
        });

        addBtn.addActionListener(_ -> {
            String id = idTask.getText().trim();
            String titleText = title.getText().trim();
            String contentText = content.getText().trim();
            String type = (String) typeTask.getSelectedItem();
            String info="";
            if(type.equals("Study")) {
               info= subject.getText().trim();
            } else if(type.equals("Sport")) {
               info= sport.getText().trim();
            } else if(type.equals("Work")) {
                info= work.getText().trim();
            }
            if (id.isEmpty() || titleText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID và Tiêu đề không được để trống");
                return;
            }
            taskController.addTask(id, titleText, type, contentText, false, info);
            clear();
        });
        editBtn.addActionListener(_ -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một công việc để sửa");
                return;
            }
            String id = (String) tableModel.getValueAt(selectedRow, 1);
            String titleText = title.getText().trim();
            String contentText = content.getText().trim();
            String type = (String) typeTask.getSelectedItem();
            String info="";
            if(type.equals("Study")) {
               info= subject.getText().trim();
            } else if(type.equals("Sport")) {
               info= sport.getText().trim();
            } else if(type.equals("Work")) {
                info= work.getText().trim();
            }
            taskController.updateTask(id, titleText, type, contentText, false, info);
            clear();
        });
        deleteBtn.addActionListener(_ -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một công việc để xóa");
                return;
            }
            String id = (String) tableModel.getValueAt(selectedRow, 1);
            taskController.deleteTask(id);
            clear();
        });
        jit_undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskController.undo();
                clear();
            }
        });
        jit_redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskController.redo();
                clear();
            }
        });
        typeTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeTask.getSelectedItem();
                if ("Study".equals(selectedType)) {
                   jlb_sj.setVisible(true);
                    jlb_sp.setVisible(false);
                    jlb_wk.setVisible(false);
                    subject.setVisible(true);// Hiển thị JTextField nếu chọn Fast Food
                    sport.setVisible(false);
                    work.setVisible(false);

                } else if("Sport".equals(selectedType)){
                    jlb_sj.setVisible(false);
                    jlb_sp.setVisible(true);
                    jlb_wk.setVisible(false);
                    subject.setVisible(false);
                    work.setVisible(false);
                    sport.setVisible(true);
                }
                else if("Work".equals(selectedType)){
                     jlb_sj.setVisible(false);
                    jlb_sp.setVisible(false);
                    jlb_wk.setVisible(true);
                    subject.setVisible(false);
                    work.setVisible(true);
                    sport.setVisible(false);
                }
                // Cập nhật giao diện
                revalidate();
                repaint();
            }
        });
        this.setVisible(true);
        // ==== Thêm vào JFrame ====
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        updateTable();
        TaskRepository.getInstance().addObserver(this);
        setVisible(true);
    }
    private void updateTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        ArrayList<Task> tasks = TaskRepository.getInstance().getTasks();
        for (Task task : tasks) {
            String info="";
            if(task.getType().equals("Study")) {
                info= task.getInfo().trim();
            } else if(task.getType().equals("Sport")) {
                info= task.getInfo().trim();
            } else if(task.getType().equals("Work")) {
                info= task.getInfo().trim();
            }
            Object[] rowData = {
            task.isCompleted(),
            task.getId(),
            task.getTitle(),
            task.getContent(),
            task.getType(),
            info
        };
        tableModel.addRow(rowData);
        }
    }
    @Override
    public void update(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        updateTable();
    }
    public void clear(){
        idTask.setText("");
        title.setText("");
        content.setText("");
        typeTask.setSelectedIndex(0);
        subject.setText("");
        sport.setText("");
        work.setText("");
        subject.setVisible(false);
        sport.setVisible(false);
        work.setVisible(false);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }
}
