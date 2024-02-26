package com.trimv.base.model.pageable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.io.Serializable;

@Builder
@ToString
@EqualsAndHashCode
public class OffsetPageable implements Pageable, Serializable {
        public static final String SORT_DELIMITER = ":";
        public static final String SORT_DESC = "desc";

        @Min(1)
        @Getter @Setter
        private int limit;
        @Min(0)
        @Setter
        private int offset;
        @Getter @Setter
        private Sort sort;

        @Override
        public int getPageNumber() {
            return offset / limit;
        }

        @Override
        public int getPageSize() {
            return limit;
        }

        @NotNull
        @Override
        public Pageable next() {
            return builder()
                    .limit(limit)
                    .offset(offset + limit)
                    .sort(sort)
                    .build();
        }

        @NotNull
        @Override
        public Pageable previousOrFirst() {
            return hasPrevious()?previous():first();
        }

        @NotNull
        @Override
        public Pageable first() {
            return builder().limit(limit).offset(0).sort(sort).build();
        }

        @NotNull
        @Override
        public Pageable withPage(int page) {
            return builder().limit(limit).offset(page*limit).sort(sort).build();
        }

        @Override
        public boolean hasPrevious() {
            return offset > limit;
        }

        public Pageable previous() {
            return !hasPrevious() ? this : builder().limit(limit).offset(offset - limit).sort(sort).build();
        }

        @Override
        public long getOffset() {
                return this.offset;
        }
}
