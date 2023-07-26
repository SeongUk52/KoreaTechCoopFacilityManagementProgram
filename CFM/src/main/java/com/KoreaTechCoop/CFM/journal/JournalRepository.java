package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
}
