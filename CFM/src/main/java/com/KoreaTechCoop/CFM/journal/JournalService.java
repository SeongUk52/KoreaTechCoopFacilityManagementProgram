package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.DataNotFoundException;
import com.KoreaTechCoop.CFM.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JournalService {
    private final JournalRepository journalRepository;
    public Page<Journal> getList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findAll(pageable);
    }
    public Journal getJournal(Integer id){
        Optional<Journal> journal = this.journalRepository.findById(id);
        if (journal.isPresent()) {
            return journal.get();
        } else {
            throw new DataNotFoundException("journal not found");
        }
    }

    public void delete(Journal journal) {
        this.journalRepository.delete(journal);
    }
    public void createNewJournal(String campus, String category, String employee, Date time, String workInfo, Boolean process, String note, SiteUser author) {
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
