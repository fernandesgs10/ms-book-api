package br.com.book.api.application.port.in.book;

public interface AddAuthorToBookUseCase {

    void addAuthor(Long bookId, Long authorId);
}