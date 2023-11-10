package com.tmt.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailRequest {
    private Integer report_id;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body_file;
    private List<String> attachment;

}
