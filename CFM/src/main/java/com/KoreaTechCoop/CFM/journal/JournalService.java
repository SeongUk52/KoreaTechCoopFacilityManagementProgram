package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JournalService {
    private final JournalRepository journalRepository;
    public Page<Journal> getList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findAll(pageable);
    }
    public void createNewJournal(String campus, String category, String employee, String time, String workInfo, Boolean process, String note, SiteUser author) {
        Journal journal = new Journal();
        journal.setCampus(campus);
        journal.setCategory(category);
        journal.setEmployee(employee);
        journal.setTime(time);
        journal.setWorkInfo(workInfo);
        journal.setProcess(process);
        journal.setNote(note);
        journal.setSiteUser(author);
        this.journalRepository.save(journal);
    }
}
