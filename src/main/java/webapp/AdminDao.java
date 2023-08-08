package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {


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

    private static void updateNumberOfStudents(Course course){

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

        return id;
    }


    private static String generateAccount(String userType){


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


    private static String getLastUserId(String userType){

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

    private static String genrateUserId(int userTypeId,String last_id){
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

}
