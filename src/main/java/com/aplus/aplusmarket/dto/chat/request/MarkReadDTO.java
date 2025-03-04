package com.aplus.aplusmarket.dto.chat.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkReadDTO {

   private int chatRoomId;
   private int userId;
   private LocalDateTime time;
}

