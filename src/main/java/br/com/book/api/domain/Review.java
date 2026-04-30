package br.com.book.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseDomain {

    private Long id;
    private User user;
    private Book book;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

}
