package com.KoreaTechCoop.CFM.journal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class JournalForm {

    private String campus;

    private String category;

    private String employee;

    private String time;

    @NotNull(message = "업무내용은 필수항목입니다.")
    private String workInfo;

    @NotNull(message = "처리결과는 필수항목입니다.")
    private Boolean process;

    private String note;

}
