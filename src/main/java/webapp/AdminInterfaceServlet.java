package webapp;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/Admin")
public class AdminInterfaceServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String,String> instructors;
        instructors =UserDao.getInstructor();
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        String password = (String) session.getAttribute("password");


        if (id != null && !id.isEmpty()) {
            request.setAttribute("Admin", id);
        }


        request.setAttribute("InstructorList", instructors);


        List<Course> courses = CourseDao.getCoursesInformation();
        request.setAttribute("courseList", courses);

        request.getRequestDispatcher("/WEB-INF/views/Admin.jsp").forward(request, response);
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String,String> instructors;
        instructors =UserDao.getInstructor();
        String instructorName= request.getParameter("InstructorName");
        request.setAttribute("InstructorList", instructors);
        if(instructorName != null &&!instructorName.isEmpty()){
            System.out.println("hello");
            if(!UserDao.checkIfUserInputExist("instructors",instructorName)) {
                AdminDao.insertNewInstructor(instructorName);
                System.out.println("aa");
            }

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Instructor Name: " + instructorName + " inserted successfully!</h1>");
            out.println("</body></html>");
        }



        String courseName= request.getParameter("CourseId");
        String instructorIdCourse= request.getParameter("InstructorIdCourse");

        if(courseName != null &&!courseName.isEmpty()){
            if(instructorIdCourse != null &&!instructorIdCourse.isEmpty()) {
                if (instructors.containsKey(instructorIdCourse)){
                    AdminDao.insertNewCourse(courseName,instructorIdCourse);

                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h1>course Name: " + courseName + " inserted successfully!</h1>");
                    out.println("</body></html>");
                }

            }
        }

        String studentName= request.getParameter("StudentName");
        String studentMajor= request.getParameter("StudentMajor");
        String selectedCourseId = request.getParameter("selectedCourseId");
        String studentId;
        List<Course> courses = CourseDao.getCoursesInformation();
        request.setAttribute("courseList", courses);
        System.out.println("0");
        if(studentName != null &&!studentName.isEmpty()){
            System.out.println("1");
            if(studentMajor != null &&!studentMajor.isEmpty()) {
                System.out.println("2");
                if(!UserDao.checkIfUserInputExist("students",studentName)){
                    studentId =  AdminDao.insertNewStudent(studentName, studentMajor);
                }else
                    studentId = UserDao.getUserId("students",studentName);
                System.out.println(studentMajor);
                System.out.println(studentId);
                Course selectedCourse = getCourseById(Integer.parseInt(selectedCourseId), courses);
                AdminDao.addStudentToCourse(studentId, selectedCourse);

                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h1>studentName : " + studentName + " inserted successfully!</h1>");
                out.println("</body></html>");


            }
        }



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
