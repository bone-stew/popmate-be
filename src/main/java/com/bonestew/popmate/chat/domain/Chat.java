package com.bonestew.popmate.chat.domain;

import com.bonestew.popmate.date.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends BaseEntity {

    private Long chatRoomId;
}
