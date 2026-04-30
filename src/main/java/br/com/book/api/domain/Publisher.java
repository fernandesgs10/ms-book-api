package br.com.book.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Publisher extends BaseDomain {

    private Long id;
    private String name;
    private String cnpj;
    private String city;
    private String state;
    private String website;
    private String description;
    private List<Book> books;

}
