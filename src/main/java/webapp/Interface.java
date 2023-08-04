package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Interface {


    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        boolean validUser = false;


        System.out.println("=========================Grading System==============================");
        System.out.println();
        System.out.println("=========================Log in============================");
        String id = "";
        String userType = "";
        String password = "";
        while (!validUser) {
            System.out.print("Enter Your Id: ");
            id = scanner.nextLine();
            while (!isValidId(id)){
                System.out.println("Please Enter Valid Id:");
                id = scanner.nextLine();
            }

            System.out.print("Enter Your Password: ");
             password = scanner.nextLine();

             userType = checkUserInput(id , password  );
            validUser = true;
        }


        choseInterfaceUser(userType,id,password);



    }

    private static String checkUserInput(String id, String password){

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
                System.out.println(userTypeId);
            }

            System.out.println(userTypeId);

            query = "SELECT * FROM USER_TYPES WHERE id = ? " ;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userTypeId);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                userType = resultSet.getString("type_name");
            }
            System.out.println(userType);
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

    private static boolean isValidId(String id){

        return id.matches("-?\\d+");
    }

    private static String decryptPassword(String password){


        return "";
    }

    private static void choseInterfaceUser(String userType,String id , String pass){


        switch (userType) {
            case "Instructor":
            InstructorInterface instructorPage= new InstructorInterface(id,pass);
                instructorPage.instructorInterface();
            break;
            case "admin":
            AdminInterface adminInterface = new AdminInterface();
            adminInterface.adminInterface();
            break;
            case "Student":
           StudentInterFace studentInterFace = new StudentInterFace(id,pass);
           studentInterFace.studentInterface();
            break;
            default:
                System.out.println("Invalid type.");
                break;
        }


    }

}