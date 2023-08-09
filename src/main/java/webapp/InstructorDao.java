package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InstructorDao {

    protected static String getExamType(int id){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String name = null;

        try {

            String query = "SELECT type_name FROM exam_types where id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();



            if (resultSet.next()) {
                name = resultSet.getString("type_name");

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;

    }


    protected static List<Grade> getStudentsInformation(Course course , String instructorId){




        List<Grade> studentGrades = new ArrayList<>();
        String examTypeName = "";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
//        if (!studentGrades.isEmpty()){
//            studentGrades.clear();
//        }

        try {

            String query = "SELECT  student_id,first_exam,second_exam,final_exam,mid_exam,quizzes,total_grade\n" +
                    " FROM grade where id_course = ? and instructor_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, course.getId());
            preparedStatement.setString(2, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();


            //System.out.println("Course Name: " + course.courseName);
            while (resultSet.next()) {
                String studentId = resultSet.getString("student_id");
                int firstExam = resultSet.getObject("first_exam") != null ? resultSet.getInt("first_exam") : 0;
                int secondExam = resultSet.getObject("second_exam") != null ? resultSet.getInt("second_exam") : 0;
                int finalExam = resultSet.getObject("final_exam") != null ? resultSet.getInt("final_exam") : 0;
                int midExam = resultSet.getObject("mid_exam") != null ? resultSet.getInt("mid_exam") : 0;
                int quizzes = resultSet.getObject("quizzes") != null ? resultSet.getInt("quizzes") : 0;
                int totalGrade = resultSet.getObject("total_grade") != null ? resultSet.getInt("total_grade") : 0;



                Grade grade = new Grade(studentId,firstExam,secondExam,finalExam,midExam,quizzes,totalGrade);
                studentGrades.add(grade);
                //examTypeName = getExamType(course.getExamType());
                //String studentName = getStudentName(studentId);

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

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentGrades;
    }



    protected void getStudentsInformation(String studentId , List<StudentGrade> studentGrades ,List<StudentCourse> studentCourses){




        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try {

            String query = "SELECT first_exam,second_exam,final_exam,mid_exam,quizzes,total_grade,id_course,instructor_id FROM grade where student_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);

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

                    StudentCourse course =getCoursesInformation(courseId ,studentCourses);

                    StudentGrade grade = new StudentGrade(instructorId,firstExam,secondExam,finalExam,midExam,quizzes,totalGrade,courseId,course);
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

    protected static StudentCourse getCoursesInformation(int courseId, List<StudentCourse> studentCourses){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        StudentCourse course = null;

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


                course = new StudentCourse(id, courseName,getInstructorName(instructorId),examTypeId);
                studentCourses.add(course);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return course;

    }

        protected void addCourseCriteria(String gradeType ,String examType,Course course ){

        int gradeTypeId, examTypeId;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;


        //for (Course course : courses) {
            //System.out.println(" Id : "+ course.id + " | " +"Course Name: " + course.courseName );

            //if (course.getGradeType() == 0 || course.examType == 0) {

//                    System.out.println("Chose Grade Type");
//                    System.out.println("1. Letter Grade");
//                    System.out.println("2. Percentage Grade");

                    if (gradeType.equalsIgnoreCase("Letter Grade"))
                        gradeTypeId = 1;
                    else
                        gradeTypeId = 2;


//                    System.out.println("Chose Exam Type");
//                    System.out.println("1. Mid/Final");
//                    System.out.println("2. First/Second/Final");
                    if (gradeType.equalsIgnoreCase("Mid/Final"))
                        examTypeId = 1;
                    else
                        examTypeId = 2;


                try {


                    String query = "UPDATE COURSES SET exam_type_id = ?, grad_type_id = ? WHERE id = ? AND instructor_id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, examTypeId);
                    preparedStatement.setInt(2, gradeTypeId);
                    preparedStatement.setInt(3, course.getId());
                    preparedStatement.setString(4, course.getInstructorId());
                    int rowsAffected = preparedStatement.executeUpdate();
/////
                    if (rowsAffected > 0) {
                        System.out.println("Data inserted successfully.");
                    } else {
                        System.out.println("Failed to insert data.");
                    }
                    /////////
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            //}
     //   }
       // System.out.println("all courses have Criteria");
    }

    protected static void addCourseCriteria(Scanner scanner ,List<Course> courses){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;


        for (Course course : courses) {
            System.out.println(" Id : "+ course.getId() + " | " +"Course Name: " + course.getCourseName());

            if (course.getGradeType() == 0 || course.getExamType() == 0) {

                if (course.getGradeType() == 0) {
                    System.out.println("Chose Grade Type");
                    System.out.println("1. Letter Grade");
                    System.out.println("2. Percentage Grade");
                    course.setGradeType(scanner.nextInt());
                }

                if (course.getExamType() == 0) {
                    System.out.println("Chose Exam Type");
                    System.out.println("1. Mid/Final");
                    System.out.println("2. First/Second/Final");
                    course.setExamType(scanner.nextInt());
                }

                try {


                    String query = "UPDATE COURSES SET exam_type_id = ?, grad_type_id = ? WHERE id = ? AND instructor_id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, course.getGradeType());
                    preparedStatement.setInt(2, course.getExamType());
                    preparedStatement.setInt(3, course.getId());
                    preparedStatement.setString(4, course.getInstructorId());
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data inserted successfully.");
                    } else {
                        System.out.println("Failed to insert data.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("all courses have Criteria");
    }


    protected static void addGradesForStudents(Course course , Scanner scanner){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            System.out.println();
            System.out.print("Enter Student Id: ");
            String studentId = scanner.next();
            System.out.println();
            if (course.getExamType() == 1) {

                System.out.print("Enter Mid Grade or press Enter to skip: ");
                int midGrade = scanner.nextInt();
                if (midGrade > 0) {
                    System.out.println("You entered: " + midGrade);
                }else
                    midGrade = 0;

                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
                int quizzesGrade = scanner.nextInt();
                if (quizzesGrade > 0) {

                    System.out.println("You entered: " + quizzesGrade);
                }else
                    quizzesGrade =0;

                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
                int finalGrade = scanner.nextInt();
                if (finalGrade > 0) {
                    System.out.println("You entered: " + finalGrade);
                }else
                    finalGrade = 0;

                String query = "UPDATE grade SET mid_exam = ?, quizzes = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, midGrade);
                preparedStatement.setInt(2, quizzesGrade);
                preparedStatement.setInt(3, finalGrade);
                preparedStatement.setString(4,studentId);
                preparedStatement.setInt(5, course.getId());


                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Failed to insert data.");
                }
            }else
            {
                System.out.print("Enter first Grade or press Enter to skip: ");
                String firstGrade = scanner.nextLine();
                if (!firstGrade.isEmpty()) {
                    System.out.println("You entered: " + firstGrade);
                }else
                    firstGrade ="0";

                System.out.print("Enter Second Grade or press Enter to skip: ");
                String secondGrade = scanner.nextLine();
                if (!secondGrade.isEmpty()) {

                    System.out.println("You entered: " + secondGrade);
                }else
                    secondGrade ="0";

                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
                String finalGrade = scanner.nextLine();
                if (!finalGrade.isEmpty()) {

                    System.out.println("You entered: " + finalGrade);
                }else
                    finalGrade = "0";

                String query = "UPDATE grade SET first_exam = ?, second_exam = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(firstGrade));
                preparedStatement.setInt(2, Integer.parseInt(secondGrade));
                preparedStatement.setInt(3, Integer.parseInt(finalGrade));
                preparedStatement.setString(4,studentId);
                preparedStatement.setInt(5, course.getId());


                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Failed to insert data.");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




    }

    private static String getInstructorName(String id){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String name = "";
        try {
            String query = "SELECT name FROM instructors where instructor_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
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

}
