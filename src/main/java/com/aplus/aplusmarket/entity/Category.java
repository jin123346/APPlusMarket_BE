package com.aplus.aplusmarket.entity;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    private String categoryName;
    private Long parentId;

}
