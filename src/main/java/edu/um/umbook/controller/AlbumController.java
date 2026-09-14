package edu.um.umbook.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlbumController {
    @GetMapping("/albums")
    public String albums() {
        return "albums"; // Implementation for CU0001, CU0002
    }
}
