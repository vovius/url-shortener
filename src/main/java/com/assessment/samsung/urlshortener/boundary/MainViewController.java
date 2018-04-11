package com.assessment.samsung.urlshortener.boundary;

import com.assessment.samsung.urlshortener.entity.Link;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

    @GetMapping("/")
    public String mainView(Model model) {
        model.addAttribute("link", new Link());
        return "index";
    }
}
