package com.KoreaTechCoop.CFM;

import com.KoreaTechCoop.CFM.user.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class mainController {
    @GetMapping("/")
    public String indexPage() {
        return "redirect:/journal/list";
    }
}
