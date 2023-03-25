package com.restaurant.web.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Command that show thanks page.
 *
 * @author B.Loiko
 */
@Slf4j
@Controller
public class ThanksCommand {

    @GetMapping("/thanks-page")
    public String execute() throws IOException, ServletException {
        log.debug("Command starts");

        log.debug("Command finished");
        return "thanks-page";
    }
}
