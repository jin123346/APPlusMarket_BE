package com.aplus.aplusmarket.dto.product;

import com.aplus.aplusmarket.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String categoryName;
    private List<Category> subCategoryList;


}
