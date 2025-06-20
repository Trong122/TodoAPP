package Controller;

import Command.*;
import Model.*;
import java.util.List;

public class TaskController {
    private static TaskController instance;
    private final TaskRepository repository;
    private final CommandManager commandManager;

    private TaskController() {
        repository = TaskRepository.getInstance(); // Singleton model
        commandManager = new CommandManager();
    }

    public static TaskController getInstance() {
        if (instance == null) {
            instance = new TaskController();
        }
        return instance;
    }

    // ====== Thêm Task ======
    public void addTask(String id, String title, String type,String content,boolean isCompleted, String additionalInfo) {
         Task task = TaskFactory.createTask(id, title, type, content, isCompleted, additionalInfo);
        Command addCommand = new AddTaskCommand(repository, task);
        commandManager.executeCommand(addCommand);
    }

    // ====== Sửa Task ======
    public void updateTask(String id, String title, String type,String content,boolean isCompleted, String additionalInfo) {
        Task oldTask = repository.getTask(id); // tìm task cũ theo id
        if (oldTask != null) {
            Command updateCommand = new updateTaskCommand( id,title, content, isCompleted,  type,additionalInfo,oldTask ) ;
            commandManager.executeCommand(updateCommand);
        }
    }
    // ====== Cập nhật trạng thái hoàn thành ======
    public void updateCompletionStatus(String id, boolean completed) {
        Task task = repository.getTask(id);
        if (task != null) {
            Command updateCommand = new UpdateCompletionStatusCommand(repository, id, completed);
            commandManager.executeCommand(updateCommand);
        }
    }
    // ====== Xóa Task ======
    public void deleteTask(String id) {
        Task task = repository.getTask(id);
        if (task != null) {
            Command removeCommand = new RemoveTaskCommand(repository, id);
            commandManager.executeCommand(removeCommand);
        }
    }

    // ====== Lấy tất cả Task ======
    public List<Task> getAllTasks() {
        return repository.getTasks();
    }

    // ====== Undo / Redo ======
    public void undo() {
        commandManager.undo();
    }

    public void redo() {
        commandManager.redo();
    }
}
