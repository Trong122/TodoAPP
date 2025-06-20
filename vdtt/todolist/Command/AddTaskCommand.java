package Command;

import Model.Task;
import Model.TaskRepository;

public class AddTaskCommand implements Command {
    private TaskRepository repo;
    private Task task;
    public AddTaskCommand(TaskRepository repo, Task task) {
        this.repo = TaskRepository.getInstance();
        this.task = task;
    }
    @Override
    public void execute() {
            repo.addTask(task);
    }
    public void undo() {
        repo.removeTask(task.getId());
    }
    public void redo() {
        repo.addTask(task);
    }
}
