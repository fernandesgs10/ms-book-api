package br.com.book.api.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Category extends BaseDomain {

    private Long id;
    private String name;
    private String description;
    private List<Book> books;

}
