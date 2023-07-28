package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
    List<Journal> findByThisyear(Integer thisyear);
    Page<Journal> findByThisyear(Pageable pageable, Integer thisyear);
}
