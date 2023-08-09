package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login.do")
public class LoginServlet extends HttpServlet {
	private String id,password;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(
				request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		 id = request.getParameter("id");
		 password = request.getParameter("password");
		String userType = UserDao.checkUserInput(id, password);
		if (userType != null && !userType.isEmpty()) {
		//	choseInterfaceUser(userType, id, password);
			handleLogin(request, response,userType);
		} else {
			request.setAttribute("errorMessage", "Invalid Credentials!!");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
	}



	protected void handleLogin(HttpServletRequest request, HttpServletResponse response ,String userType)
			throws ServletException, IOException {

		HttpSession session;
		if (userType != null) {
			switch (userType) {
				case "Instructor":
					session= request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("password", password);
					response.sendRedirect("instructor");

					break;
				case "admin":
					 session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("password", password);
					response.sendRedirect("Admin");
					break;
				case "Student":
					request.getRequestDispatcher("/WEB-INF/views/Student.jsp").forward(
							request, response);
					break;
				default:
					request.setAttribute("errorMessage", "Invalid User Type!");
					request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
					break;
			}
		} else {
			request.setAttribute("errorMessage", "User Type Not Found!");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
	}
	}

