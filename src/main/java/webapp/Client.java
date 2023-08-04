package webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private String id,password;

    public Client(String id, String password) {
        this.id = id;
        this.password = password;
    }


    public void startClient() {
        try {
            Socket socket = new Socket("localhost", 8000);

            Thread serverThread = new Thread(() -> handleServerCommunication(socket));
            serverThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerCommunication(Socket socket) {
        try {

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(this.id);
            writer.println("i want to add grades or added student criteria my id " + this.id);


            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String authenticationResult = reader.readLine();
            String acceptFromServer = reader.readLine();

            if ("authenticated".equals(authenticationResult) && acceptFromServer.equalsIgnoreCase("yes") ) {
                String additionResult = reader.readLine();
                System.out.println(additionResult);
            } else {
                System.out.println("Authentication failed. You are not instructor.");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);


            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter Instructor ID: ");
            String instructorId = userInputReader.readLine();

            System.out.print("Enter Password: ");
            String password = userInputReader.readLine();


            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(instructorId);
            writer.println(password);


            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String authenticationResult = reader.readLine();

            if ("authenticated".equals(authenticationResult)) {
                // Authentication successful
                System.out.println("Authentication successful..");

                writer.println(instructorId);

                String courseAdditionResult = reader.readLine();
                System.out.println(courseAdditionResult);
            } else {
                // Authentication failed
                System.out.println("Authentication failed. You are not authorized to add courses.");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}