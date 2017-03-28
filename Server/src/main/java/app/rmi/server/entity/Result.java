package app.rmi.server.entity;

/**
 * Created by Roman on 25.02.2017.
 */
public class Result extends Entity {
    private int studentId;
    private String subject;
    private int mark;

    public Result() {
    }

    public Result(String subject, int mark) {
        this.subject = subject;
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return subject + ": " + mark;
    }
}
