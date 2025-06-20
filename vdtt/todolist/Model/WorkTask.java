package Model;

public class WorkTask extends Task {
   private String projectName;

    public WorkTask(String id,String title,String content, boolean isCompleted, String projectName) {
        super(id,title,content, isCompleted);
        this.projectName= projectName;
    }

    @Override
    public String getType() {
        return "Work";
    }

    public String getprojectName() {
        return projectName;
    }
    public String getInfo() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Override
    public Task clone() {
        return new WorkTask(this.id, this.title, this.content, this.isCompleted, this.projectName);
    }
}
