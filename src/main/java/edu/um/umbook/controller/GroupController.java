package edu.um.umbook.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {
    @GetMapping("/groups")
    public String groups() {
        return "groups"; // Implementation for CU0004, CU0015
    }
}
