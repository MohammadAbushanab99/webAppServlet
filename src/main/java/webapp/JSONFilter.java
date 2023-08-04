package webapp;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = "/instructor")
public class JSONFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            // Only capture the JSON data for POST requests
            StringBuilder jsonBody = new StringBuilder();
            String line;
            try (BufferedReader reader = httpRequest.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }

            // Store the JSON data as a request attribute
            httpRequest.setAttribute("jsonBody", jsonBody.toString());
        }

        // Continue with the request handling
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}