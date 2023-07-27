package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import com.KoreaTechCoop.CFM.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/journal")
@RequiredArgsConstructor
@Controller
public class JournalController {

    private final JournalService journalService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page){
        Page<Journal> paging = this.journalService.getList(page);
        model.addAttribute("paging", paging);
        return "journal_list";
    }

    @GetMapping("/create")
    public String journalCreate(JournalForm journalForm){
        return "journal_form";
    }

    @PostMapping("/create")
    public String journalCreate(Principal principal , @Valid JournalForm journalForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "journal_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (journalForm.getEmployee()==null){
            journalForm.setEmployee(siteUser.getRealName());
        }
        this.journalService.createNewJournal(journalForm.getCampus(), journalForm.getCategory(), journalForm.getEmployee(),
                journalForm.getTime(), journalForm.getWorkInfo(), journalForm.getProcess(), journalForm.getNote(),siteUser);
        //
        return "redirect:/journal/list";
    }
    /*
    @GetMapping("/modify/{id}")
    public String goodsModify(GoodsForm goodsForm,@PathVariable("id") Integer id){
        Goods goods = this.goodsService.getGoods(id);
        goodsForm.setGoodsName(goods.getGoodsName());
        goodsForm.setGoodsCategory(goods.getGoodsCategory());
        goodsForm.setGoodsIce(goods.getGoodsIce());
        goodsForm.setGoodsAmount(goods.getGoodsAmount());
        goodsForm.setGoodsPrice(goods.getGoodsPrice());

        return "goods_form";
    }

    @PostMapping("/modify/{id}")
    public String goodsModify(@Valid GoodsForm goodsForm, BindingResult bindingResult,@PathVariable("id") Integer id){
        if (bindingResult.hasErrors()) {
            return "goods_form";
        }
        Goods goods = this.goodsService.getGoods(id);
        this.goodsService.modify(goods,goodsForm.getGoodsName(),goodsForm.getGoodsCategory(),goodsForm.getGoodsIce(),goodsForm.getGoodsAmount(),goodsForm.getGoodsPrice());
        return "redirect:/goods/list";
    }

    @GetMapping("/delete/{id}")
    public String goodsDelete(@PathVariable("id") Integer id) {
        Goods goods = this.goodsService.getGoods(id);
        this.goodsService.delete(goods);
        return "redirect:/goods/list";

    }

     */
}
