package com.restaurant.web.command.cart;

import com.restaurant.web.command.Command;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command that shows cart list in the cart page.
 *
 * @author B.Loiko
 */
@Slf4j
public class CartListCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        log.debug("Command finished");
        return "cart.html";
    }

}
