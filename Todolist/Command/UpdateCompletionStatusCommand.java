package Command;

import Model.Task;
import Model.TaskRepository;

public class UpdateCompletionStatusCommand implements Command {
    private final TaskRepository repo;
    private final String id;
    private final Task oldTask;
    private final Task newTask;

    public UpdateCompletionStatusCommand(TaskRepository repo, String id, boolean completed) {
        this.repo = TaskRepository.getInstance();
        this.id = id;
        this.oldTask = repo.getTask(id).clone();  // clone old
        this.newTask = oldTask.clone();           // clone again
        this.newTask.setCompleted(completed);     // thay đổi trạng thái
    }

    @Override
    public void execute() {
        repo.updateTask(id, newTask);
    }

    @Override
    public void undo() {
        repo.updateTask(id, oldTask);
        repo.notifyObservers("↩️ Hoàn tác cập nhật trạng thái task: " + oldTask.getTitle());
    }

    @Override
    public void redo() {
        repo.updateTask(id, newTask);
        repo.notifyObservers("🔁 Làm lại cập nhật trạng thái task: " + newTask.getTitle());
    }
}
