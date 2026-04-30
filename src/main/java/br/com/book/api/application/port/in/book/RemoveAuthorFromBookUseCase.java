package br.com.book.api.application.port.in.book;

public interface RemoveAuthorFromBookUseCase {

    void removeAuthor(Long bookId, Long authorId);
}