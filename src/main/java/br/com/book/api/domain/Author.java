package br.com.book.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Author extends BaseDomain {

    private Long id;
    private String name;
    private String nationality;
    private LocalDate birthDate;
    private String biography;
    private List<Book> books;

}
