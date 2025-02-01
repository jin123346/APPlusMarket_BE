package com.aplus.aplusmarket.documents;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "token_history")
public class TokenHistory {

    @Id
    private String id;
    private String hashed_userid; //사용자uid;
    private String refreshToken;  //token
    private String deviceInfo;  //발급장소
    private String ipAddress;
    private LocalDateTime issuedAt;
    private Date expiresAt;
    private int refreshCount;
    private boolean revoked;

}
