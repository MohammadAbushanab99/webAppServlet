package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {


    protected static List<Course> getCoursesInformation(){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        List<Course> courses = new ArrayList<>();
        if(!courses.isEmpty()){
            courses.clear();
        }

        try {

            String query = "SELECT  id,course_name,instructor_id,number_of_students,exam_type_id,grad_type_id FROM courses";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String courseName = resultSet.getString("course_name");
                String instructorId = resultSet.getString("instructor_id");
                int numbersOfStudents = resultSet.getObject("number_of_students") != null ? resultSet.getInt("number_of_students") : 0;
                int examType = resultSet.getObject("exam_type_id") != null ? resultSet.getInt("exam_type_id") : 0;
                int gradeType = resultSet.getObject("grad_type_id") != null ? resultSet.getInt("grad_type_id") : 0;

                if (courseName.isEmpty()){
                    System.out.println("No courses added by admin");
                    break;
                }
                // System.out.println(" Id : "+ id + " | " +"Course Name: " + courseName + " | " + "Number Of Students" + numbersOfStudents);

                Course course = new Course(id, courseName, numbersOfStudents,examType,gradeType,instructorId);
                courses.add(course);

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return courses;


    }

        protected static List<Course> getCoursesInformation(String id ){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        List<Course> courses = new ArrayList<>();

        try {

            String query = "SELECT  id,course_name,instructor_id,number_of_students,exam_type_id,grad_type_id FROM courses where instructor_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()) {
                int idCourse = resultSet.getInt("id");
                String courseName = resultSet.getString("course_name");
                String instructorId = resultSet.getString("instructor_id");
                int numbersOfStudents = resultSet.getObject("number_of_students") != null ? resultSet.getInt("number_of_students") : 0;
                int examType = resultSet.getObject("exam_type_id") != null ? resultSet.getInt("exam_type_id") : 0;
                int gradeType = resultSet.getObject("grad_type_id") != null ? resultSet.getInt("grad_type_id") : 0;

                if (courseName.isEmpty()){
                    System.out.println("No courses added by admin");
                    break;
                }
               // System.out.println(" Id : "+ idCourse + " | " +"Course Name: " + courseName + " | " + "Number Of Students" + numbersOfStudents);


                Course course = new Course(idCourse, courseName, numbersOfStudents,examType,gradeType,instructorId);
                courses.add(course);

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


   return courses;

    }

//         protected void getStudentsInformation(Course course ,String id){
//
//
//
//        Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement preparedStatement = null;
////        if (!studentGrades.isEmpty()){
////            studentGrades.clear();
////        }
//
//        try {
//
//            String query = "SELECT  student_id,first_exam,second_exam,final_exam,mid_exam,quizzes,total_grade\n" +
//                    " FROM grade where id_course = ? and instructor_id = ?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, course.getId());
//            preparedStatement.setString(2, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//
//            System.out.println("Course Name: " + course.getCourseName());
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


    //public List<Course> getCoursesInformation(String id)
}
