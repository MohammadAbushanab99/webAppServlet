package webapp;

public class Grade {


    private String studentId;
    private int firstExam;
    private int secondExam;
    private int finalExam;
    private int midExam;
    private int quizzes;
    private int totalGrade;



    private String name;

    public Grade(String studentId, int firstExam, int secondExam, int finalExam, int midExam, int quizzes, int totalGrade) {
        this.studentId = studentId;
        this.firstExam = firstExam;
        this.secondExam = secondExam;
        this.finalExam = finalExam;
        this.midExam = midExam;
        this.quizzes = quizzes;
        this.totalGrade = totalGrade;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getFirstExam() {
        return firstExam;
    }

    public int getSecondExam() {
        return secondExam;
    }

    public int getFinalExam() {
        return finalExam;
    }

    public int getMidExam() {
        return midExam;
    }

    public int getQuizzes() {
        return quizzes;
    }

    public int getTotalGrade() {
        return totalGrade;
    }

    public String getName() {

        return UserDao.getStudentName(studentId);
    }
}
