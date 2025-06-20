package Model;

public class TaskFactory {
    public static Task createTask(String id,String title,String type, String content, boolean isCompleted, String additionalInfo) {
        switch (type) {
            case "Study":
                return new StudyTask(id,title,content, isCompleted, additionalInfo);
            case "Sport":
                return new SportTask(id,title,content, isCompleted, additionalInfo);
            case "Work":
                return new WorkTask(id,title,content, isCompleted, additionalInfo);
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
     public static Task cloneTask(Task original) {
        return createTask(
            original.getId(),
            original.getTitle(),
            original.getType(),
            original.getContent(),
            original.isCompleted(),
            original.getInfo()
        );
    }
}
