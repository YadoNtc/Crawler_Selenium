package crawler;

public class JavaQuiz {
    private int id;
    private String numQuestion;
    private String question;
    private String partNumber;
    private String answer;

    public JavaQuiz() {
    }

    public JavaQuiz(String numQuestion, String question) {
        this.numQuestion = numQuestion;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getId() {
        return id;
    }

    public String getNumQuestion() {
        return numQuestion;
    }

    public void setNumQuestion(String numQuestion) {
        this.numQuestion = numQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
