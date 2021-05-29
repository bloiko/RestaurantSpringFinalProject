package  com.restaurant.controller;

import com.restaurant.web.command.Command;
import com.restaurant.web.command.CommandContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet controller.
 * 
 * @author B.Loiko
 * 
 */
@Slf4j

@Controller
public class MyController {


	@GetMapping("/controller")
/*	@ResponseBody*/
	protected String doGet(/*HttpServletRequest request,
			HttpServletResponse response*/) throws ServletException, IOException {
		//process(request, response);
		return "list-food";
	}
	@PostMapping
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * Main method of this controller.
	 */
	private void process(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Controller starts");

		// extract web.command name from the request
		String commandName = request.getParameter("command");
		log.trace("Request parameter: command --> " + commandName);

		// obtain web.command object by its name
		Command command = CommandContainer.get(commandName);
		log.trace("Obtained command --> " + command);

		// execute web.command and get forward address
		String forward = command.execute(request, response);
		log.trace("Forward address --> " + forward);

		log.debug("Controller finished, now go to forward address --> " + forward);

		// if the forward address is not null go to the address
		if (forward != null) {
			RequestDispatcher disp = request.getRequestDispatcher(forward);
			disp.forward(request, response);
		}
	}

}