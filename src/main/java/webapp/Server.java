package webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {


        public void startServer() {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);


                    Socket clientSocket = serverSocket.accept();


                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String receivedStudentId = reader.readLine();
                System.out.println(reader.readLine());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));

                    boolean isAuthenticated = authenticateInstructor(receivedStudentId);

                if (isAuthenticated) {

                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        writer.println("authenticated");
                        writer.println(reader1.readLine());
                        writer.println("You can added student or course criteria");


                } else {
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    writer.println("authentication_failed");
                }

                    clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        private static boolean authenticateInstructor(String id) {
            boolean isAuthenticated = false;
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;


            try {


                String query = "SELECT * FROM USERS WHERE user_id = ? and user_type_id = 3";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, id);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    isAuthenticated = true;
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


            return isAuthenticated;
        }

    }

