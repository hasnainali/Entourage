package duxeye.com.entourage.model;

/**
 * Created by User on 20-07-2016.
 */
public class Answer {
    private String answer;
    private String answerId;

    public Answer(String answer, String answerId) {
        this.answer = answer;
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerId() {
        return answerId;
    }
}
