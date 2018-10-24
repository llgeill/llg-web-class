package com.eill.llg.pojo;

public class Operation {
    private Integer signConunt;
    private String problem;
    private String answer;
    private String user_answer;
    private boolean isCorrect;

    public Integer getSignConunt() {
        return signConunt;
    }

    public void setSignConunt(Integer signConunt) {
        this.signConunt = signConunt;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
