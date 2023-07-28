package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import com.KoreaTechCoop.CFM.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequestMapping("/journal")
@RequiredArgsConstructor
@Controller
public class JournalController {

    private final JournalService journalService;
    private final UserService userService;

    @GetMapping("/list/{thisyear}")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page , @PathVariable("thisyear") Integer thisyear){
        Page<Journal> paging = this.journalService.getListByYear(page,thisyear);
        model.addAttribute("paging", paging);
        return "journal_list";
    }
    @GetMapping("/detail/{id}")
    public String detail(Model model,@PathVariable("id")Integer id){
        Journal journal = this.journalService.getJournal(id);
        model.addAttribute("journal",journal);
        return "journal_detail";
    }

    @GetMapping("/create")
    public String journalCreate(JournalForm journalForm){
        journalForm.setTime(Instant.now().toString().substring(0,10));  //오늘 날짜 기본입력
        return "journal_form";
    }

    @PostMapping("/create")
    public String journalCreate(Principal principal , @Valid JournalForm journalForm, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "journal_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (journalForm.getEmployee()==null){
            journalForm.setEmployee(siteUser.getRealName());
        }
        // 포맷터
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(journalForm.getTime());//journalForm.time은 문자열이고 엔티티의 time은 Date형식임


        this.journalService.createNewJournal(journalForm.getCampus(), journalForm.getCategory(), journalForm.getEmployee(),
                date, journalForm.getWorkInfo(), journalForm.getProcess(), journalForm.getNote(),siteUser);
        //
        return "redirect:/journal/list/"+ LocalDateTime.now().getYear();
    }

    @GetMapping("/modify/{id}")
    public String journalModify(Principal principal ,JournalForm journalForm,@PathVariable("id") Integer id){
        Journal journal = this.journalService.getJournal(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(!Objects.equals(siteUser.getUsername(), "admin") && siteUser!=journal.getSiteUser()){return "redirect:/journal/list";}

        journalForm.setCampus(journal.getCampus());
        journalForm.setCategory(journal.getCategory());
        journalForm.setEmployee(journal.getEmployee());
        journalForm.setTime(journal.getTime().toString().substring(0,10));
        journalForm.setWorkInfo(journal.getWorkInfo());
        journalForm.setProcess(journal.getProcess());
        journalForm.setNote(journal.getNote());
        return "journal_form";
    }

    @PostMapping("/modify/{id}")
    public String journalModify(Principal principal ,@Valid JournalForm journalForm, BindingResult bindingResult,@PathVariable("id") Integer id) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "journal_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (journalForm.getEmployee()==null){
            journalForm.setEmployee(siteUser.getRealName());
        }
        // 포맷터
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(journalForm.getTime());//journalForm.time은 문자열이고 엔티티의 time은 Date형식임

        Journal journal = this.journalService.getJournal(id);
        this.journalService.modify(journal,journalForm.getCampus(),journalForm.getCategory(), journalForm.getEmployee(),
                date, journalForm.getWorkInfo(), journalForm.getProcess(), journalForm.getNote());
        return "redirect:/journal/list/"+ journal.getThisyear();
    }



    @GetMapping("/delete/{id}")
    public String journalDelete(Principal principal ,@PathVariable("id") Integer id) {
        Journal journal = this.journalService.getJournal(id);

        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(!Objects.equals(siteUser.getUsername(), "admin") && siteUser!=journal.getSiteUser()){return "redirect:/journal/list";}
        this.journalService.delete(journal);
        return "redirect:/journal/list/"+ journal.getThisyear();

    }


    @GetMapping("/excel/{thisyear}")
    public void excelDownload(HttpServletResponse response, HttpServletRequest req , @PathVariable("thisyear") Integer thisyear) throws IOException {
        List<Journal> journalList = this.journalService.getListByYear(thisyear);
        this.journalService.excelDownload(response,req,journalList);
    }
}
