package com.aplus.aplusmarket.dto.auth.response;

import com.aplus.aplusmarket.entity.AddressBook;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/*
    20250209 하진희 주소록 ResponseDTO
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookResponseDTO {

    private Long id;
    private Long userId;
    @JsonProperty("isDefault")
    private boolean isDefault;
    private String title;
    private String postcode;
    private String address1;
    private String address2;
    private String message;
    private String receiverName;
    private String receiverPhone;

    public AddressBookResponseDTO toResponseDTO(AddressBook entity) {
        return AddressBookResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .isDefault(entity.isDefault())
                .title(entity.getTitle())
                .postcode(entity.getPostcode())
                .address1(entity.getAddress1())
                .address2(entity.getAddress2())
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .message(entity.getMessage())
                .build();
    }

    public AddressBook toEntity(){
        return AddressBook.builder()
                .id(this.id)
                .userId(this.userId)
                .title(this.title)
                .message(this.message)
                .postcode(this.postcode)
                .address1(this.address1)
                .address2(this.address2)
                .receiverName(this.receiverName)
                .receiverPhone(this.receiverPhone)
                .isDefault(this.isDefault)
                .build();
    }
}
