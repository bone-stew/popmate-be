package com.bonestew.popmate.chat.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatReport {
    private Long report_id;
    private Long reporter;
    private String chatId;
    private String message;
    private Integer status;
    private Long writer;
    private Integer reportCount;
    private String writerEmail;
}
