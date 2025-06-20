package Command;

import Model.Task;
import Model.TaskRepository;

public class RemoveTaskCommand implements Command {
    private TaskRepository repo;
    private String id;
    private Task task;
    public RemoveTaskCommand(TaskRepository repo,String id) {
        this.repo = TaskRepository.getInstance();
        this.id = id;
        this.task = repo.getTask(id);
    }
   
    @Override
    public void execute() {
        repo.removeTask(id);
    }
    public void undo(){
        repo.addTask(task);
    }
    public void redo() {
        repo.removeTask(id);
    }
}