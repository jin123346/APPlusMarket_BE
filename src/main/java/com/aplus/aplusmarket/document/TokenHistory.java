package com.aplus.aplusmarket.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "token_history")
public class TokenHistory {
    @Id
    private String id;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Date issuedAt;
    private Date accessTokenExpiry;
    private Date refreshTokenExpiry;


}
