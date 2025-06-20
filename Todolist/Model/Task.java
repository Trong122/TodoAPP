package Model;

public abstract class Task {
    protected String id;
    protected String title;
    protected boolean isCompleted;
    protected String content;

    public Task(String id,String title,String content, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isCompleted =  isCompleted;
    }
    public abstract String getType();
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public abstract String getInfo();
    public abstract Task clone();
}
