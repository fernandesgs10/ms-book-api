package br.com.book.api.application.port.in.book;

public interface UpdateBookStockUseCase {

    void incrementStock(Long bookId, int quantity);  // ← incrementStock
    void decrementStock(Long bookId, int quantity);  // ← decrementStock
}