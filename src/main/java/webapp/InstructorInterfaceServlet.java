package webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/instructor")
public class InstructorInterfaceServlet extends HttpServlet {
    private int courseId;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        String password = (String) session.getAttribute("password");
        String name = UserDao.getInstructorName(id);

        if (id != null && !id.isEmpty()) {
            request.setAttribute("instructorId", id);
            request.setAttribute("instructorName", name);
        }

        List<Course> courses = CourseDao.getCoursesInformation(id);
        request.setAttribute("courseList", courses);
        String selectedCourseIdParam = request.getParameter("selectedCourseId");


        Course selectedCourse =null;
        int selectedCourseId = -1; // Default value if the parameter is not provided or invalid


        if (selectedCourseIdParam != null && !selectedCourseIdParam.isEmpty()) {
            try {
                this.courseId = Integer.parseInt(selectedCourseIdParam);
                selectedCourseId = Integer.parseInt(selectedCourseIdParam);
                 selectedCourse = getCourseById(selectedCourseId, courses);
                List<Grade> students = InstructorDao.getStudentsInformation(selectedCourse, selectedCourse.getInstructorId());
                request.setAttribute("selectedCourseId", selectedCourseId);
                request.setAttribute("selectedCourseName", selectedCourse.getCourseName());

                if(selectedCourse.getExamType() == 1) {
                    request.setAttribute("remarks1","Mid Exam");
                    request.setAttribute("remarks2","Quizzes");
                }else {
                    request.setAttribute("remarks1","First Exam");
                    request.setAttribute("remarks2","Second Exam");
                }

                request.setAttribute("studentsList", students);
                request.setAttribute("selectedCourse", selectedCourse);

            } catch (NumberFormatException e) {
                selectedCourseId = 3;
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/Instructor.jsp").forward(request, response);
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String selectedCourseIdParam = request.getParameter("selectedCourseId");
        // Extract the JSON data from the request body
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        System.out.println("asadas");
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            System.out.println(line);
        }
        String json = sb.toString();

        // Parse the JSON data using Gson
        Gson gson = new Gson();
        Grade remarksData = gson.fromJson(json, Grade.class);

        // Now you have the data in 'remarksData', you can save it in the database
        // ... (Implement the database saving logic here)

        // Send the response back to the client
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{ \"success\": true }");
        out.flush();
    }



    protected Course getCourseById(int courseId , List<Course> courses){

        for (Course c : courses){
            if(c.getId() == courseId){
                return  c;

            }
        }

        return courses.get(0);
    }

}
