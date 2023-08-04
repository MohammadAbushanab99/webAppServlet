package webapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AdminInterface {

    private static List<Course> courses ;

    public AdminInterface() {
        this.courses = new ArrayList<>();
    }

    public void adminInterface(){

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("=========================Admin Page==============================");
        Server server = new Server();
        server.startServer();
        while (!exit) {
            System.out.println("Select an option:");
            System.out.println("1. Add Instructor");
            System.out.println("2. Add Course");
            System.out.println("3. Add Student");
            System.out.println("4. Log Out");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addUserInformation("Instructor",scanner);
                    break;
                case 2:
                    addUserInformation("Course",scanner);
                    break;
                case 3:
                    addUserInformation("Student",scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }



    }

    private static void addUserInformation(String userType ,Scanner scanner){

        Map<String,String> instructors;
        switch (userType) {
            case "Instructor":
                System.out.print("Enter Instructor Name: ");
                String instructorName = scanner.nextLine();
                while (!isValidInput(instructorName)){
                    System.out.println("Please Enter Valid name:");
                    instructorName = scanner.nextLine();
                }
                if(checkIfUserInputExist("instructors",instructorName)){
                    System.out.println("The instructor is exist");
                    break;
                }else
                    insertNewInstructor(instructorName);
                break;
            case "Course":
                System.out.print("Enter Course Name: ");
                String courseName = scanner.nextLine();
                while (!isValidInput(courseName)){
                    System.out.println("Please Enter Valid Course:");
                    courseName = scanner.nextLine();
                }
                System.out.print("Enter instructor Name: ");
                instructors =getInstructor();
                String instructor = scanner.nextLine();
                while (!instructors.containsValue(instructor)){
                    System.out.println("Please Enter Valid Instructor:");
                    instructor = scanner.nextLine();
                }
                String id="";
                for (Map.Entry<String, String> entry : instructors.entrySet()) {
                    if (entry.getValue().equals(instructor)) {
                        id = entry.getKey();
                        break;
                    }
                }
                System.out.println(id);
                insertNewCourse(courseName,id);
                break;
            case "Student":
                String studentId ="";
                System.out.print("Enter Student Name: ");
                String studentName = scanner.nextLine();
                while (!isValidInput(studentName)){
                    System.out.println("Please Enter Valid Name:");
                    studentName = scanner.nextLine();
                }
                System.out.print("Enter Student Major: ");
                String studentMajor = scanner.nextLine();
                while (!isValidInput(studentMajor)){
                    System.out.println("Please Enter Valid Major:");
                    studentMajor = scanner.nextLine();
                }
                if(checkIfUserInputExist("students",studentName)){
                    System.out.println("The student is exist");
                    studentId =  insertNewStudent(studentName, studentMajor);
                }
                System.out.println("Add Students Courses");
                getCoursesInformation();

                boolean addedStudentCourse = true;
                while (addedStudentCourse) {
                System.out.println("Chose Course id :");
                int selectedId = scanner.nextInt();
                Course course = choseCourse(selectedId);

                while (course == null){
                    System.out.println("Chose Exist id :");
                    selectedId = scanner.nextInt();
                    course = choseCourse(selectedId);
                }

                    addStudentToCourse(studentId, course);

                    System.out.println("do you want add another course(Yes/no)");
                    System.out.println();
                    String userInput = scanner.nextLine();

                    if(userInput.equalsIgnoreCase("no"))
                        addedStudentCourse = false;

                }

                break;

            default:
                System.out.println("Invalid option.");
                break;
        }



    }


    private static boolean checkIfUserInputExist(String userType , String userName){
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
    private static String insertNewStudent(String name ,String major){

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

    private static void addStudentToCourse(String id, Course course){

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {


            String query = "INSERT INTO grade (student_id,id_course,instructor_id) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, course.id);
            preparedStatement.setString(3, course.instructorId);
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
                    preparedStatement.setInt(1, course.numberOfStudents + 1);
                    preparedStatement.setInt(2, course.id);
                    preparedStatement.setString(3, course.instructorId);
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



    private static void insertNewInstructor(String name){
        String id = generateAccount("Instructor");

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

    private static void insertNewCourse(String courseName, String instructorId){

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

    private static Map<String,String> getInstructor(){
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

        System.out.println("Instructor Names:");
        for (String name : instructorNames.values()) {
            System.out.println("Instructor name :"+ name);
        }

        return instructorNames;

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

    private static String getLastUserId(String userType){

        int userTypeId = checkUserTypeId(userType);

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String lastId = null;
        System.out.println(userTypeId);
        System.out.println(userTypeId);
        try {
            String query = "SELECT id, user_id\n" +
                    "FROM users\n" +
                    "WHERE user_type_id = ?\n" +
                    "AND id = (SELECT MAX(id) FROM users WHERE user_type_id = ? AND user_id = users.user_id);";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userTypeId);
            preparedStatement.setInt(2, userTypeId);

            resultSet = preparedStatement.executeQuery();
            System.out.println(lastId);

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

    private static int checkUserTypeId(String userType){

        if(userType.equalsIgnoreCase("Student"))
            return 2;
        else
            return 3;

    }

    private static String genrateUserId(int userTypeId,String last_id){
        System.out.println(last_id);
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

    private static boolean isValidInput(String name){
        return name.matches("^[A-Za-z](?=.{1,29}$)[A-Za-z]*(?:\\h+[A-Z][A-Za-z]*)*$");

}

    private static void getCoursesInformation(){
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;

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
                System.out.println(" Id : "+ id + " | " +"Course Name: " + courseName + " | " + "Number Of Students" + numbersOfStudents);

               Course course = new Course(id, courseName, numbersOfStudents,examType,gradeType,instructorId);
                courses.add(course);

            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }





    }
    private static Course choseCourse(int courseId){

       Course tmpCourse = null;
        for (Course course1 : courses) {
            System.out.println(course1.id);
            if (courseId == course1.id) {
                System.out.println(course1.id);
                tmpCourse = course1;
                return tmpCourse;
            }
        }

        return tmpCourse;
    }

    static class Course {
        private int id;
        private String courseName;
        private String instructorId;
        private int numberOfStudents;
        private int gradeType;
        private int examType;

        public Course(int id, String courseName, int numberOfStudents, int gradeType, int examType ,String instructorId) {
            this.id = id;
            this.courseName = courseName;
            this.numberOfStudents = numberOfStudents;
            this.gradeType = gradeType;
            this.examType = examType;
            this.instructorId =instructorId;

        }
    }

}
