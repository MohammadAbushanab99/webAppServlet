package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentInterFace {

    private String password,id;
    private List<Course> studdentCourses ;
    private List<Grade> studentGrades ;

    public StudentInterFace(String id,String pass){
        this.password = pass;
        this.id = id;
        this.studdentCourses = new ArrayList<>();
        this.studentGrades = new ArrayList<>();
        getStudentsInformation();
    }

    public void studentInterface(){

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("=========================Student Page==============================");

        if(password.equals("123"))
            changePassword();

        System.out.print("id: ");
        System.out.println(this.id);
        System.out.println("Name: ");
        System.out.print(getStudentName());

        while (!exit) {
            System.out.println("Select an option:");
            System.out.println("1. See My Courses Information");
            System.out.println("2. See My Grades");
            System.out.println("3. Log Out");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    getCourses();
                    System.out.println("sadfasd");
                    break;
                case 2:
                    getStudentGrade();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }



    }

    private String getStudentName(){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String name = "";

        try {

            String query = "SELECT name FROM students where student_id = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();



            if (resultSet.next()) {
                name = resultSet.getString("name");

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }

    private void getStudentsInformation(){




        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        System.out.println("this id :" + this.id);
        String n = this.id;
        try {

            String query = "SELECT first_exam,second_exam,final_exam,mid_exam,quizzes,total_grade,id_course,instructor_id FROM grade where student_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, n);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (!resultSet.isBeforeFirst()) {
                // No data found for the given student_id
                // Handle the empty result set as needed (e.g., display a message)
                System.out.println("No data found for the student.");
            } else {
                if (resultSet.next()) {
                    // Process the data as usual
                    // ...

                String instructorId = resultSet.getString("instructor_id");
                int courseId =resultSet.getInt("id_course");
                int firstExam = resultSet.getObject("first_exam") != null ? resultSet.getInt("first_exam") : 0;
                int secondExam = resultSet.getObject("second_exam") != null ? resultSet.getInt("second_exam") : 0;
                int finalExam = resultSet.getObject("final_exam") != null ? resultSet.getInt("final_exam") : 0;
                int midExam = resultSet.getObject("mid_exam") != null ? resultSet.getInt("mid_exam") : 0;
                int quizzes = resultSet.getObject("quizzes") != null ? resultSet.getInt("quizzes") : 0;
                int totalGrade = resultSet.getObject("total_grade") != null ? resultSet.getInt("total_grade") : 0;
                System.out.println(instructorId);
                Course course =getCoursesInformation(courseId);

                Grade grade = new Grade(instructorId,firstExam,secondExam,finalExam,midExam,quizzes,totalGrade,courseId,course);
                studentGrades.add(grade);



        }
    }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("4");
        }


    }

    private void getCourses(){

        for(Grade grades: studentGrades) {
            System.out.println("Course Name: " + grades.course.courseName + " | " + "Instructor Name: " + grades.course.instructorName);
        }
    }

    private void getStudentGrade(){

        for(Grade grades: studentGrades) {
            System.out.println("Course Name: " + grades.course.courseName +  " | " + "Instructor Name: " + grades.course.instructorName);
            if (grades.course.examType == 1) {
                System.out.println(
                         "Mid Exam: " + grades.midExam +
                                " | " + "Quizzes: " + grades.quizzes +
                                " | " + "Final Exam: " + grades.firstExam +
                                " | " + "Total Grade: " + grades.totalGrade
                );
            } else {
                System.out.println(
                        "First Exam: " + grades.firstExam +
                                " | " + "Second Exam: " + grades.secondExam +
                                " | " + "Final Exam: " + grades.firstExam +
                                " | " + "Total Grade: " + grades.totalGrade
                );

                System.out.println("-------------------------------------");

            }
        }

    }

    private Course getCoursesInformation(int courseId){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        Course course = null;

        try {

            String query = "SELECT  id,course_name,instructor_id,exam_type_id FROM courses where id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int examTypeId = resultSet.getInt("exam_type_id");
                String courseName = resultSet.getString("course_name");
                String instructorId = resultSet.getString("instructor_id");

                if (courseName.isEmpty()){
                    System.out.println("No courses added by admin");
                    break;
                }
                //System.out.println(" Id : "+ id + " | " +"Course Name: " + courseName + " | " + "Instructor Name" + getInstructorName( instructorId));


                 course = new Course(id, courseName,getInstructorName(instructorId),examTypeId);
                 studdentCourses.add(course);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return course;

    }

    private String getInstructorName(String instructorId){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String name = "";
        try {
            String query = "SELECT name FROM instructors where instructor_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString("name");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }

    private static void changePassword(){

    }



    static class Course {
        private int id;
        private String courseName;
        private String instructorName;
        private int examType;

        public Course(int id, String courseName ,String instructorName,int examType) {
            this.id = id;
            this.courseName = courseName;
            this.instructorName =instructorName;
            this.examType=examType;

        }
    }
    static class Grade {
        private String instructorId;
        private int courseId;
        private int firstExam;
        private int secondExam;
        private int finalExam;
        private int midExam;
        private int quizzes;
        private int totalGrade;
        private Course course;

        public Grade(String instructorId, int firstExam, int secondExam, int finalExam, int midExam, int quizzes, int totalGrade ,int courseId, Course course) {
            this.instructorId = instructorId;
            this.firstExam = firstExam;
            this.secondExam = secondExam;
            this.finalExam = finalExam;
            this.midExam = midExam;
            this.quizzes = quizzes;
            this.totalGrade = totalGrade;
            this.courseId =courseId;
            this.course = course;
        }

    }
}



