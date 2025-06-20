package Model;

public class StudyTask extends Task {
    private String subjectName; 

    public StudyTask(String id,String title,String content, boolean isCompleted,  String subjectName) {
        super(id,title,content, isCompleted);   
        this.subjectName=subjectName;
    }
    @Override
    public String getType() {
        return "Study";
    }
    public String getsubjectName() {
        return subjectName;
    }
    public String getInfo() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    @Override
    public Task clone() {
        return new StudyTask(this.id, this.title, this.content, this.isCompleted, this.subjectName);
    }
}
