package com.restaurant.web;

import com.restaurant.service.MenuService;
import com.restaurant.web.dto.GetMenuResponse;
import com.restaurant.web.dto.MenuPage;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping(value = {"/menu1"})
    public GetMenuResponse getMenuPage(@RequestBody MenuPage menuPageRequest) {
        return menuService.getMenuPage(menuPageRequest);
    }
}
