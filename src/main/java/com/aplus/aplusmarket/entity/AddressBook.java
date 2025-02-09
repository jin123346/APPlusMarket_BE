package com.aplus.aplusmarket.entity;

import lombok.*;
/*
    20250209 하진희 주소록 엔티티
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook {

    private Long id;
    private Long userId;
    private boolean isDefault;
    private String title;
    private String postcode;
    private String address1;
    private String address2;
    private String message;
    private String receiverName;
    private String receiverPhone;





}
