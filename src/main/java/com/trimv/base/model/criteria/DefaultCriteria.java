package com.trimv.base.model.criteria;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.trimv.base.model.pageable.OffsetPageable;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DefaultCriteria implements Serializable {
    public static final String SORT_DELIMITER = ":";
    public static final String SORT_DESC = "desc";

    @Min(0)
    @Parameter(description = "Offset of item", example = "0")
    private int offset = 0;
    @Min(1)
    @Parameter(description = "Limit of items", example = "25")
    private int limit = 25;
    @Parameter(description = "Sort of items by fields", example = "id:desc")
    private String sort;

    @JsonIgnore
    public Sort sortable() {
        Sort s = Sort.unsorted();
        if (StringUtils.hasText(sort)) {
            Set<String> sortParams = StringUtils.commaDelimitedListToSet(sort);
            List<Sort.Order> orders = new ArrayList<>();
            sortParams.forEach(it -> {
                String[] params = StringUtils.split(it, SORT_DELIMITER);
                if (params != null && params.length > 0) {
                    if (SORT_DESC.equalsIgnoreCase(params[1]))
                    orders.add(Sort.Order.desc(params[0]));
                    else
                        orders.add(Sort.Order.asc(params[0]));
                } else
                    orders.add(Sort.Order.asc(it));
            } );
            if (!orders.isEmpty())
                s = Sort.by(orders);
        }
        return s;
    }

    /**
    * Return pageable object
     */
    @JsonIgnore
    public Pageable pageable() {
        return OffsetPageable.builder().limit(limit).offset(offset).sort(sortable()).build();
    }
}
