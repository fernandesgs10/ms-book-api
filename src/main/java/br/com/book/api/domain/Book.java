package br.com.book.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseDomain {

    private Long id;
    private String title;
    private String subtitle;
    private String isbn;
    private Integer publicationYear;
    private Integer numberOfPages;
    private BigDecimal price;
    private Integer edition;
    private String synopsis;
    private Integer stockQuantity;
    private LocalDateTime publicationDate;

    // Relacionamentos (inicialize as listas)
    private Publisher publisher;
    private List<Author> authors = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    // ========== Métodos de negócio ==========

    public void addAuthor(Author author) {
        if (author != null && this.authors == null) {
            this.authors = new ArrayList<>();
        }
        if (author != null && !this.authors.contains(author)) {
            this.authors.add(author);
        }
    }

    public void removeAuthor(Author author) {
        if (this.authors != null) {
            this.authors.remove(author);
        }
    }

    public void addCategory(Category category) {
        if (category != null && this.categories == null) {
            this.categories = new ArrayList<>();
        }
        if (category != null && !this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public void removeCategory(Category category) {
        if (this.categories != null) {
            this.categories.remove(category);
        }
    }

    public boolean isAvailable() {
        return stockQuantity != null && stockQuantity > 0;
    }

    public void incrementStock(int quantity) {
        if (this.stockQuantity == null) {
            this.stockQuantity = 0;
        }
        this.stockQuantity += quantity;
    }

    public void decrementStock(int quantity) {
        if (this.stockQuantity == null || this.stockQuantity < quantity) {
            throw new IllegalStateException("Estoque insuficiente para o livro: " + title);
        }
        this.stockQuantity -= quantity;
    }

    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}