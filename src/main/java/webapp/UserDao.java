package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {

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
}
