package duxeye.com.entourage.model;

/**
 * Created by User on 23-07-2016.
 */
public class PollData {
    private String poll_id;
    private String question;
    private String questionId;
    private String questionType;
    private String notes;
    private String poll_title;
    private String create_date;
    private String due_date;

    public PollData(String poll_id, String question, String questionId, String questionType, String notes, String poll_title, String create_date, String due_date) {
        this.poll_id = poll_id;
        this.question = question;
        this.questionId = questionId;
        this.questionType = questionType;
        this.notes = notes;
        this.poll_title = poll_title;
        this.create_date = create_date;
        this.due_date = due_date;
    }

    public String getPoll_id() {
        return poll_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getNotes() {
        return notes;
    }

    public String getPoll_title() {
        return poll_title;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getDue_date() {
        return due_date;
    }
}
