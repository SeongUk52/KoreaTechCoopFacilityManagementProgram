package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campus;

    private String category;

    private String employee;//직원이 퇴사했을때를 고려해서 이름을 따로 기록함

    private Date time;

    private Integer thisyear;

    private String workInfo;//업무내용

    private Boolean process;

    private String note;

    @ManyToOne
    private SiteUser siteUser;

}
