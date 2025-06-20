package Command;

import Model.Task;
import Model.TaskFactory;
import Model.TaskRepository;

public class updateTaskCommand implements Command {
    private TaskRepository repo;
    private String id;
    private Task oldTask;
    private Task newTask;

    public updateTaskCommand(String id, String newTitle, String newContent, boolean isCompleted, String type, String additionalInfo,Task oldTask) {
        this.repo = TaskRepository.getInstance();
        this.id = id;
        this.oldTask = oldTask;
        this.newTask = TaskFactory.createTask(id, newTitle, type, newContent, isCompleted, additionalInfo);
    }

    @Override
    public void execute() {
        repo.updateTask(id, newTask);
    }

    public void undo() {
        repo.updateTask(id, oldTask);
    }

    public void redo() {
        repo.updateTask(id, newTask);
    }
}
