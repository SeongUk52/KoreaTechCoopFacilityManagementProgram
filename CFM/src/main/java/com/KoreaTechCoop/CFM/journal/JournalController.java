package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/journal")
public class JournalController {

    private final JournalService journalService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page){
        Page<Journal> paging = this.journalService.getList(page);
        model.addAttribute("paging", paging);
        return "journal_list";
    }
}
