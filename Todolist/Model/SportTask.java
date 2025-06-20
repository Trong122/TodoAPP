package Model;

public class SportTask extends Task {
    private String sportType;   

    public SportTask(String id,String title,String content, boolean isCompleted,  String sportType) {
        super(id,title,content, isCompleted);
        this.sportType = sportType;
    }
    @Override
    public String getType() {
        return "Sport";
    }

    public String getSportType() {
        return sportType;
    }
    public String getInfo() {
        return sportType;
    }
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }
    @Override
    public Task clone() {
        return new SportTask(this.id, this.title, this.content, this.isCompleted, this.sportType);
    }
}
