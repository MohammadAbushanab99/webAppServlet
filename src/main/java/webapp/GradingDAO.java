package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradingDAO {


    protected static String checkUserInput(String id, String password){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int userTypeId = 0;
        String userType ="";

        try {
            String query = "SELECT * FROM USERS WHERE user_id = ? AND password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userTypeId = resultSet.getInt("user_type_id");
            }

            query = "SELECT * FROM USER_TYPES WHERE id = ? " ;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userTypeId);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                userType = resultSet.getString("type_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return userType;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userType;
    }

    protected static Map<String,String> getInstructor(){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        Map<String,String> instructorNames = new HashMap<>();

        try {

            String query = "SELECT name,instructor_id FROM instructors";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String id = resultSet.getString("instructor_id");
                instructorNames.put(id,name);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        System.out.println("Instructor Names:");
//        for (String name : instructorNames.values()) {
//            System.out.println("Instructor name :"+ name);
//        }

        return instructorNames;

    }

    protected static String getUserId(String userType , String userName){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isExist = false;

        String id="";
        try {
            String query = "SELECT name FROM "+ userType +" WHERE name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                 id = resultSet.getString("id");
                isExist = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return id;

    }

    protected static boolean checkIfUserInputExist(String userType , String userName){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isExist = false;


        try {
            String query = "SELECT name FROM "+ userType +" WHERE name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                isExist = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return isExist;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return isExist;

    }
    protected static void addStudentToCourse(String id, Course course){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {


            String query = "INSERT INTO grade (student_id,id_course,instructor_id) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, course.getId());
            preparedStatement.setString(3, course.getInstructorId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
            updateNumberOfStudents(course);
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

    protected static void updateNumberOfStudents(Course course){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try {

            String query = "UPDATE COURSES SET number_of_students = ? WHERE id = ? AND instructor_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, course.getNumberOfStudents() + 1);
            preparedStatement.setInt(2, course.getId());
            preparedStatement.setString(3, course.getInstructorId());
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
    protected static void insertNewCourse(String courseName, String instructorId){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            System.out.println(instructorId);

            String query = "INSERT INTO courses (course_name,instructor_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, instructorId);
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



    protected static void insertNewInstructor(String name){
        String id = generateAccount("Instructor");
        System.out.println("insert22222222");
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {


            String query = "INSERT INTO INSTRUCTORS (instructor_id,name) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
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

    protected static String insertNewStudent(String name ,String major){

        String id = generateAccount("Student");

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {


            String query = "INSERT INTO STUDENTS (student_id,name,major) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, major);
            int rowsAffected = preparedStatement.executeUpdate();
///////////////
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
            //////////////
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

        return id;
    }

    protected static String generateAccount(String userType){


        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String id =null;
        int userTypeId = 0;

        id =getLastUserId(userType);

        try {

            userTypeId = checkUserTypeId(userType);

            String query = "INSERT INTO USERS (user_id, password,user_type_id) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, "123");
            preparedStatement.setInt(3, userTypeId);
            int rowsAffected = preparedStatement.executeUpdate();
//////////////
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }

            /////////////
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

        return id;

    }
    protected static String getLastUserId(String userType){

        int userTypeId = checkUserTypeId(userType);

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            String query = "SELECT id, user_id\n" +
                    "FROM users\n" +
                    "WHERE user_type_id = ?\n" +
                    "AND id = (SELECT MAX(id) FROM users WHERE user_type_id = ? AND user_id = users.user_id);";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userTypeId);
            preparedStatement.setInt(2, userTypeId);

            resultSet = preparedStatement.executeQuery();
           // System.out.println(lastId);

            if (resultSet.next()) {
                lastId = resultSet.getString("user_id");
            }else {
                if(userTypeId == 2)
                    lastId ="0";
                else
                    lastId="1";
            }
        } catch (SQLException e) {
            if(userTypeId == 2)
                lastId ="0";
            else
                lastId="1";
        }

        String newId = genrateUserId(userTypeId,lastId);

        return newId;
    }

    protected static int checkUserTypeId(String userType){

        if(userType.equalsIgnoreCase("Student"))
            return 2;
        else
            return 3;

    }

    protected static String genrateUserId(int userTypeId,String last_id){
        String newId = String.valueOf(last_id.charAt(0));
        if(userTypeId == 2){
            if(!last_id.equals("0")) {
                String subId = last_id.substring(1);
                int id = Integer.parseInt(subId) + 1;
                newId += String.valueOf(id);
            }else {
                newId += "1";
            }

        }else{
            if(!last_id.equals("1")) {
                String subId = last_id.substring(1);
                int id = Integer.parseInt(subId) + 1;
                newId += String.valueOf(id);
            }else {
                newId += "1";
            }

        }

        return newId;
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

    //Instructor

    protected static String getInstructorName(String id){
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

    protected static String getStudentName(String id){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        String name = null;

        try {

            String query = "SELECT name FROM students where student_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
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

    protected String getExamType(int id){

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

    protected static List<Grade> getStudentsInformation(Course course ,String instructorId){




        List<Grade> studentGrades = new ArrayList<>();
        String examTypeName = "";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        if (!studentGrades.isEmpty()){
            studentGrades.clear();
        }

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

    protected static void addGradesForStudents(Course course , Grade grade){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try {
//            System.out.println();
//            System.out.print("Enter Student Id: ");
//            String studentId = scanner.next();
//            System.out.println();
            if (course.getExamType() == 1) {
//
//                System.out.print("Enter Mid Grade or press Enter to skip: ");
//                int midGrade = scanner.nextInt();
//                if (midGrade > 0) {
//                    System.out.println("You entered: " + midGrade);
//                }else
//                    midGrade = 0;
//
//                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                int quizzesGrade = scanner.nextInt();
//                if (quizzesGrade > 0) {
//
//                    System.out.println("You entered: " + quizzesGrade);
//                }else
//                    quizzesGrade =0;
//
//                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                int finalGrade = scanner.nextInt();
//                if (finalGrade > 0) {
//                    System.out.println("You entered: " + finalGrade);
//                }else
//                    finalGrade = 0;

                String query = "UPDATE grade SET mid_exam = ?, quizzes = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, grade.getMidExam());
                preparedStatement.setInt(2, grade.getQuizzes());
                preparedStatement.setInt(3, grade.getFinalExam());
                preparedStatement.setString(4,grade.getStudentId());
                preparedStatement.setInt(5,course.getId());


                int rowsAffected = preparedStatement.executeUpdate();
////////
                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Failed to insert data.");
                }

                ////////////
            }else
            {
//                System.out.print("Enter first Grade or press Enter to skip: ");
//                String firstGrade = scanner.nextLine();
//                if (!firstGrade.isEmpty()) {
//                    System.out.println("You entered: " + firstGrade);
//                }else
//                    firstGrade ="0";
//
//                System.out.print("Enter Second Grade or press Enter to skip: ");
//                String secondGrade = scanner.nextLine();
//                if (!secondGrade.isEmpty()) {
//
//                    System.out.println("You entered: " + secondGrade);
//                }else
//                    secondGrade ="0";
//
//                System.out.print("Enter Quizzes Grade or press Enter to skip: ");
//                String finalGrade = scanner.nextLine();
//                if (!finalGrade.isEmpty()) {
//
//                    System.out.println("You entered: " + finalGrade);
//                }else
//                    finalGrade = "0";

                String query = "UPDATE grade SET first_exam = ?, second_exam = ?, final_exam = ? WHERE student_id = ? AND id_course = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, grade.getFirstExam());
                preparedStatement.setInt(2, grade.getSecondExam());
                preparedStatement.setInt(3, grade.getFinalExam());
                preparedStatement.setString(4,grade.getStudentId());
                preparedStatement.setInt(5,course.getId());


                int rowsAffected = preparedStatement.executeUpdate();
/////////
                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Failed to insert data.");
                }
/////////////

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
    protected String getGradeType(int courseId){


        return "";
    }


    protected static void changePassword(){

    }
}
