package com.restaurant.web.command.menu;


import com.restaurant.database.entity.FoodItem;
import com.restaurant.database.entity.Item;
import com.restaurant.database.entity.MenuPage;
import com.restaurant.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Command show menu list in the main page.
 *
 * @author B.Loiko
 */
@Controller
@CrossOrigin(origins = "http://restaurant-spring-project.herokuapp.com/")
public class MenuListCommand {
    private static final int NUMBER_ITEMS_ON_PAGE = 5;
    public static final String FILTER = "filter";
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping(value = {"/menu","/"})
    public String execute(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        String filterBy = getFilter(request, session);
        session.setAttribute(FILTER, filterBy);

        String sort = request.getParameter("sort");

        String sessionSort = (String) session.getAttribute("sort");
        String order = (String) session.getAttribute("order");
        order = getOppositeOrder(sort, sessionSort, order);

        if (sort == null && sessionSort != null) {
            sort = sessionSort;
        }
        if (order == null) {
            order = "ASC";
        }
        session.setAttribute("order", order);
        session.setAttribute("sort", sort);

        int page = getPageNumber(request, session);
        List<FoodItem> foodItems = null;
        int numOfPages = 0;
        Sort.Direction direction = "ASC".equals(order)? Sort.Direction.ASC: Sort.Direction.DESC;
        MenuPage menuPage = new MenuPage(page - 1, NUMBER_ITEMS_ON_PAGE, direction, sort, filterBy);
        foodItems = foodItemService.getFoodItems(menuPage);
        numOfPages = filterBy == null || filterBy.isEmpty() ? getNumOfPages(foodItemService.getFoodItems()) : getNumOfPages(foodItems);
        model.addAttribute("pageNumbers", IntStream.iterate(1, i -> i + 1).limit(numOfPages).toArray());
        session.setAttribute("page", page);
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        model.addAttribute("cart_size", cart!=null?cart.size():0);
        model.addAttribute("categories", foodItemService.getCategories());
        model.addAttribute("FOOD_LIST", foodItems);
        return "list-food";
    }

    private String getOppositeOrder(String sort, String sessionSort, String order) {
        if (sort != null && sort.equals(sessionSort)) {
            if ("ASC".equals(order)) {
                order = "DESC";
            } else {
                order = "ASC";
            }
        }
        return order;
    }

    private String getFilter(HttpServletRequest request, HttpSession session) {
        String filterBy = request.getParameter(FILTER);
        if (filterBy == null) {
            filterBy = (String) session.getAttribute(FILTER);
        }
        if (filterBy != null && filterBy.equals("all_categories")) {
            filterBy = null;
        }
        if (filterBy != null && !filterBy.isEmpty()) {
            session.setAttribute("page", 1);
        }
        return filterBy;
    }

    private int getPageNumber(HttpServletRequest request, HttpSession session) {
        int page;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        } else if (session.getAttribute("page") != null) {
            page = (int) session.getAttribute("page");
        } else {
            page = 1;
        }
        return page;
    }

    private int getNumOfPages(List<FoodItem> foodItems) {
        int modOfTheDivision = foodItems.size() % NUMBER_ITEMS_ON_PAGE;
        int incorrectNumOfPages = foodItems.size() / NUMBER_ITEMS_ON_PAGE;
        return modOfTheDivision == 0 ? incorrectNumOfPages : incorrectNumOfPages + 1;
    }
}













