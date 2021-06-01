package com.restaurant.web.command;

import com.restaurant.web.command.admin.AdminChangeOrderStatusCommand;
import com.restaurant.web.command.admin.AdminListCommand;
import com.restaurant.web.command.cart.CartDeleteItemCommand;
import com.restaurant.web.command.cart.CartListCommand;
import com.restaurant.web.command.cart.CartOrderItemCommand;
import com.restaurant.web.command.login.LoginAdminCommand;
import com.restaurant.web.command.login.LoginMainCommand;
import com.restaurant.web.command.login.LogoutCommand;
import com.restaurant.web.command.login.RegistrationCommand;
import com.restaurant.web.command.menu.MenuListCommand;
import com.restaurant.web.command.menu.MenuOrderCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author B.Loiko
 */
@Slf4j
public class CommandContainer {


    private static Map<String, Command> commands = new TreeMap<>();

    private CommandContainer() {
    }

    static {

        //admin
        commands.put("adminList", new AdminListCommand());
        commands.put("changeOrderStatus", new AdminChangeOrderStatusCommand());
        //cart
       // commands.put("cartList", new CartListCommand());
        commands.put("cartDeleteItem", new CartDeleteItemCommand());
       // commands.put("cartOrderItem", new CartOrderItemCommand());
        //thanks page
        commands.put("thanks", new ThanksCommand());
        //menu page
        //commands.put("menuList", new MenuListCommand());
        commands.put("menuOrder", new MenuOrderCommand());
        //my order page
      //  commands.put("myOrders", new MyOrdersCommand());
        //login and logout pages
        //commands.put("loginMain", new LoginMainCommand());
        commands.put("loginAdmin", new LoginAdminCommand());
        commands.put("logout", new LogoutCommand());
        //registration page
      //  commands.put("registration", new RegistrationCommand());
        //no command page
        commands.put("noCommand", new NoCommand());

        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns web.command object with the given name.
     *
     * @param commandName Name of the web.command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}