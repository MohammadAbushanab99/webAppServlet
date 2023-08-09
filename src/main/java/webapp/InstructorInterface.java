package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InstructorInterface {

    private String password,id;

    private List<Course> courses ;
    private List<Grade> studentGrades ;

    public InstructorInterface(String id ,String pass){
        this.password = pass;
        this.id = id;
        this.courses = new ArrayList<>();
        this.studentGrades = new ArrayList<>();
    }

    public void instructorInterface(){

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("=========================Instructor Page==============================");

        if(password.equals("123"))
            changePassword();

        System.out.print("id: ");
        System.out.println(this.id);
        System.out.print("Name: ");
        System.out.println(UserDao.getInstructorName(this.id));
        Client client = new Client(this.id,this.password) ;
        client.startClient();

        while (!exit) {
            List<Course> instructorCourse =new ArrayList<>();
            System.out.println("Select an option:");
            System.out.println("1. See My Courses Information");
            System.out.println("2. And Grades and see My Students Information");
            System.out.println("3. Add Course Criteria");
            System.out.println("4. Calculate Statistical Data");
            System.out.println("5. Log Out");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    instructorCourse = CourseDao.getCoursesInformation();
                    coursesInfo(instructorCourse);
                    break;
                case 2:
                    instructorCourse = CourseDao.getCoursesInformation();
                    coursesInfo(instructorCourse);
                    System.out.println("Chose Course id :");
                    int selectedId = scanner.nextInt();
                    Course course = choseCourse(selectedId);

                    while (course.equals(null)){
                        System.out.println("Chose Exist id :");
                        selectedId = scanner.nextInt();
                        course = choseCourse(selectedId);
                    }
                    List<Grade> students = InstructorDao.getStudentsInformation(course,this.id);
                    studentInfo(students , InstructorDao.getExamType(course.getExamType()));
                    String addGrades = "";
                    while (!addGrades.equalsIgnoreCase("yes")) {
                        InstructorDao.addGradesForStudents(course, scanner);
                        System.out.println("do you want to added grades(Yes/no)");
                        addGrades = scanner.next();
                    }
                    break;
                case 3:
                     instructorCourse =  CourseDao.getCoursesInformation();


                   InstructorDao.addCourseCriteria(scanner ,instructorCourse );
                case 4:
                    instructorCourse = CourseDao.getCoursesInformation();
                    coursesInfo(instructorCourse);
                    System.out.println("Chose Course id :");
                     selectedId = scanner.nextInt();
                     course = choseCourse(selectedId);

                    while (course.equals(null)){
                        System.out.println("Chose Exist id :");
                        selectedId = scanner.nextInt();
                        course = choseCourse(selectedId);
                    }


                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }



    }

    private static void coursesInfo (List<Course>  courses){

        for (Course course : courses)
         System.out.println(" Id : "+ course.getId() + " | " +"Course Name: " + course.getCourseName() + " | " + "Number Of Students" + course.getNumberOfStudents());
    }

    private static void studentInfo(List<Grade> students , String examTypeName){

        for (Grade student : students) {
            System.out.print("Student Id: " + student.getStudentId() +
                    " | " + "Student Name: " + student.getName());
            if (examTypeName.equals("Mid/Final")) {
                System.out.println(
                        " | " + "Mid Exam: " + student.getMidExam() +
                                " | " + "Quizzes: " + student.getQuizzes() +
                                " | " + "Final Exam: " + student.getFinalExam() +
                                " | " + "Total Grade: " + student.getTotalGrade()
                );
            } else {
                System.out.println(
                        " | " + "First Exam: " + student.getFirstExam() +
                                " | " + "Second Exam: " + student.getSecondExam() +
                                " | " + "Final Exam: " + student.getFinalExam() +
                                " | " + "Total Grade: " + student.getTotalGrade()
                );
            }
        }

    }

    private static void changePassword(){

    }

//    private String getInstructorName(){
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//        String name = "";
//        try {
//            String query = "SELECT name FROM instructors where instructor_id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setObject(1, this.id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                 name = resultSet.getString("name");
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//       return name;
//
//    }
//
//    private String getStudentName(String id){
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//        String name = null;
//
//        try {
//
//            String query = "SELECT name FROM students where student_id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//
//
//            if (resultSet.next()) {
//                name = resultSet.getString("name");
//
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return name;
//
//    }
//
//    private void getCoursesInformation(){
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//
//        if(!courses.isEmpty()){
//            courses.clear();
//        }
//
//        try {
//
//            String query = "SELECT  id,course_name,instructor_id,number_of_students,exam_type_id,grad_type_id FROM courses where instructor_id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, this.id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String courseName = resultSet.getString("course_name");
//                String instructorId = resultSet.getString("instructor_id");
//                int numbersOfStudents = resultSet.getObject("number_of_students") != null ? resultSet.getInt("number_of_students") : 0;
//                int examType = resultSet.getObject("exam_type_id") != null ? resultSet.getInt("exam_type_id") : 0;
//                int gradeType = resultSet.getObject("grad_type_id") != null ? resultSet.getInt("grad_type_id") : 0;
//
//                if (courseName.isEmpty()){
//                    System.out.println("No courses added by admin");
//                    break;
//                }
//                System.out.println(" Id : "+ id + " | " +"Course Name: " + courseName + " | " + "Number Of Students" + numbersOfStudents);
//
//
//                    Course course = new Course(id, courseName, numbersOfStudents,examType,gradeType,instructorId);
//                    courses.add(course);
//
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//    }
//
//    private String getExamType(int id){
//
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//        String name = null;
//
//        try {
//
//            String query = "SELECT type_name FROM exam_types where id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//
//
//            if (resultSet.next()) {
//                name = resultSet.getString("type_name");
//
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return name;
//
//    }
//
//
//    private void getStudentsInformation(Course course){
//
//
//
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//        if (!studentGrades.isEmpty()){
//            studentGrades.clear();
//        }
//
//        try {
//
//            String query = "SELECT  student_id,first_exam,second_exam,final_exam,mid_exam,quizzes,total_grade\n" +
//                    " FROM grade where id_course = ? and instructor_id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, course.id);
//            preparedStatement.setString(2, this.id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//
//            System.out.println("Course Name: " + course.courseName);
//            while (resultSet.next()) {
//                String studentId = resultSet.getString("student_id");
//                int firstExam = resultSet.getObject("first_exam") != null ? resultSet.getInt("first_exam") : 0;
//                int secondExam = resultSet.getObject("second_exam") != null ? resultSet.getInt("second_exam") : 0;
//                int finalExam = resultSet.getObject("final_exam") != null ? resultSet.getInt("final_exam") : 0;
//                int midExam = resultSet.getObject("mid_exam") != null ? resultSet.getInt("mid_exam") : 0;
//                int quizzes = resultSet.getObject("quizzes") != null ? resultSet.getInt("quizzes") : 0;
//                int totalGrade = resultSet.getObject("total_grade") != null ? resultSet.getInt("total_grade") : 0;
//
//
//
//                Grade grade = new Grade(studentId,firstExam,secondExam,finalExam,midExam,quizzes,totalGrade);
//                studentGrades.add(grade);
//                String examTypeName =getExamType(course.examType);
//                String studentName = getStudentName(studentId);
//
//                System.out.print("Student Id: " + studentId +
//                        " | " + "Student Name: " + studentName);
//                if(examTypeName.equals("Mid/Final")) {
//                    System.out.println(
//                            " | " + "Mid Exam: " + midExam +
//                                    " | " + "Quizzes: " + quizzes +
//                                    " | " + "Final Exam: " + firstExam +
//                                    " | " + "Total Grade: " + totalGrade
//                    );
//                }else {
//                    System.out.println(
//                            " | " + "First Exam: " + firstExam +
//                                    " | " + "Second Exam: " + secondExam +
//                                    " | " + "Final Exam: " + firstExam +
//                                    " | " + "Total Grade: " + totalGrade
//                    );
//                }
//
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    private String getGradeType(int courseId){
//
//
//        return "";
//    }
//
    private Course choseCourse(int courseId){

        Course tmpCourse = null;
        for (Course course1 : courses) {
            if (courseId == course1.getId()) {
                tmpCourse = course1;
                return tmpCourse;
            }
        }
        return tmpCourse;
    }
//
//    private void addCourseCriteria(Scanner scanner){
//
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//;
//
//        for (Course course : courses) {
//            System.out.println(" Id : "+ course.id + " | " +"Course Name: " + course.courseName );
//
//            if (course.gradeType == 0 || course.examType == 0) {
//
//                if (course.gradeType == 0) {
//                    System.out.println("Chose Grade Type");
//                    System.out.println("1. Letter Grade");
//                    System.out.println("2. Percentage Grade");
//                    course.gradeType = scanner.nextInt();
//                }
//
//                if (course.examType == 0) {
//                    System.out.println("Chose Exam Type");
//                    System.out.println("1. Mid/Final");
//                    System.out.println("2. First/Second/Final");
//                    course.examType = scanner.nextInt();
//                }
//
//                try {
//
//
//                    String query = "UPDATE COURSES SET exam_type_id = ?, grad_type_id = ? WHERE id = ? AND instructor_id = ?";
//                    preparedStatement = connection.prepareStatement(query);
//                    preparedStatement.setInt(1, course.gradeType);
//                    preparedStatement.setInt(2, course.examType);
//                    preparedStatement.setInt(3, course.id);
//                    preparedStatement.setString(4, course.instructorId);
//                    int rowsAffected = preparedStatement.executeUpdate();
//
//                    if (rowsAffected > 0) {
//                        System.out.println("Data inserted successfully.");
//                    } else {
//                        System.out.println("Failed to insert data.");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (preparedStatement != null) {
//                            preparedStatement.close();
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        System.out.println("all courses have Criteria");
//    }
//
//    private static void addGradesForStudents(Course course ,Scanner scanner){
//
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
//
//        try {
//            System.out.println();
//            System.out.print("Enter Student Id: ");
//            String studentId = scanner.next();
//            System.out.println();
//                if (course.examType == 1) {
//
//                    System.out.print("Enter Mid Grade or press Enter to skip: ");
//                    int midGrade = scanner.nextInt();
//                    if (midGrade > 0) {
//                        System.out.println("You entered: " + midGrade);
//                    }else
//                        midGrade = 0;
//
//                    System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                    int quizzesGrade = scanner.nextInt();
//                    if (quizzesGrade > 0) {
//
//                        System.out.println("You entered: " + quizzesGrade);
//                    }else
//                        quizzesGrade =0;
//
//                    System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                    int finalGrade = scanner.nextInt();
//                    if (finalGrade > 0) {
//                        System.out.println("You entered: " + finalGrade);
//                    }else
//                        finalGrade = 0;
//
//                    String query = "UPDATE grade SET mid_exam = ?, quizzes = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
//                    preparedStatement = connection.prepareStatement(query);
//                    preparedStatement.setInt(1, midGrade);
//                    preparedStatement.setInt(2, quizzesGrade);
//                    preparedStatement.setInt(3, finalGrade);
//                    preparedStatement.setString(4,studentId);
//                    preparedStatement.setInt(5,course.id);
//
//
//                    int rowsAffected = preparedStatement.executeUpdate();
//
//                    if (rowsAffected > 0) {
//                        System.out.println("Data inserted successfully.");
//                    } else {
//                        System.out.println("Failed to insert data.");
//                    }
//                }else
//                {
//                    System.out.print("Enter first Grade or press Enter to skip: ");
//                    String firstGrade = scanner.nextLine();
//                    if (!firstGrade.isEmpty()) {
//                        System.out.println("You entered: " + firstGrade);
//                    }else
//                        firstGrade ="0";
//
//                    System.out.print("Enter Second Grade or press Enter to skip: ");
//                    String secondGrade = scanner.nextLine();
//                    if (!secondGrade.isEmpty()) {
//
//                        System.out.println("You entered: " + secondGrade);
//                    }else
//                        secondGrade ="0";
//
//                    System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                    String finalGrade = scanner.nextLine();
//                    if (!finalGrade.isEmpty()) {
//
//                        System.out.println("You entered: " + finalGrade);
//                    }else
//                        finalGrade = "0";
//
//                    String query = "UPDATE grade SET first_exam = ?, second_exam = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
//                    preparedStatement = connection.prepareStatement(query);
//                    preparedStatement.setInt(1, Integer.parseInt(firstGrade));
//                    preparedStatement.setInt(2, Integer.parseInt(secondGrade));
//                    preparedStatement.setInt(3, Integer.parseInt(finalGrade));
//                    preparedStatement.setString(4,studentId);
//                    preparedStatement.setInt(5,course.id);
//
//
//                    int rowsAffected = preparedStatement.executeUpdate();
//
//                    if (rowsAffected > 0) {
//                        System.out.println("Data inserted successfully.");
//                    } else {
//                        System.out.println("Failed to insert data.");
//                    }
//
//
//                }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (preparedStatement != null) {
//                            preparedStatement.close();
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//
//
//    }

    private static void calculateStatisticalData(){

    }
//
//    static class Course {
//        private int id;
//        private String courseName;
//        private String instructorId;
//        private int numberOfStudents;
//        private int gradeType;
//        private int examType;
//
//        public Course(int id, String courseName, int numberOfStudents, int gradeType, int examType ,String instructorId) {
//            this.id = id;
//            this.courseName = courseName;
//            this.numberOfStudents = numberOfStudents;
//            this.gradeType = gradeType;
//            this.examType = examType;
//            this.instructorId =instructorId;
//
//        }
//    }
//
//    static class Grade {
//        private String studentId;
//        private int firstExam;
//        private int secondExam;
//        private int finalExam;
//        private int midExam;
//        private int quizzes;
//        private int totalGrade;
//
//        public Grade(String studentId, int firstExam, int secondExam, int finalExam, int midExam, int quizzes, int totalGrade) {
//            this.studentId = studentId;
//            this.firstExam = firstExam;
//            this.secondExam = secondExam;
//            this.finalExam = finalExam;
//            this.midExam = midExam;
//            this.quizzes = quizzes;
//            this.totalGrade = totalGrade;
//        }
//
//    }



}



