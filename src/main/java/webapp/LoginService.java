package webapp;

public class LoginService {
	public boolean validateUser(String user, String password) {
		return user.equalsIgnoreCase("11") && password.equals("123");
	}

}