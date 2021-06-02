package com.restaurant.web.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command that show thanks page.
 *
 * @author B.Loiko
 *
 */
@Slf4j
@Controller
public class ThanksCommand {

    @GetMapping("/thanks-page")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        log.debug("Command finished");
        return "thanks-page";
    }
}
