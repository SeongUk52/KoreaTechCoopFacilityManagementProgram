package com.KoreaTechCoop.CFM;

import com.KoreaTechCoop.CFM.user.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class mainController {
    @GetMapping("/")
    public String indexPage() {
        int nowYear = LocalDateTime.now().getYear();
        return "redirect:/journal/list/"+nowYear;
    }
}
