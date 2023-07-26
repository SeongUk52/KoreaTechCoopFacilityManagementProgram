package com.KoreaTechCoop.CFM.journal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JournalService {
    private final JournalRepository journalRepository;
    public Page<Journal> getList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findAll(pageable);
    }
}
