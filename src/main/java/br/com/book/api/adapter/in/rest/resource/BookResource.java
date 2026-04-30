package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.BookDTOMapper;
import br.com.book.api.application.port.in.book.*;
import br.com.book.api.domain.Book;
import br.com.book.api.gen.api.BooksApi;
import br.com.book.api.gen.model.BookCreateRequest;
import br.com.book.api.gen.model.BookResponse;
import br.com.book.api.gen.model.BookUpdateRequest;
import br.com.book.api.gen.model.DecrementStockRequest;
import br.com.book.api.gen.model.IncrementStockRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookResource implements BooksApi {

    @Inject
    CreateBookUseCase createBookUseCase;

    @Inject
    GetBookUseCase getBookUseCase;

    @Inject
    ListBooksUseCase listBooksUseCase;

    @Inject
    UpdateBookUseCase updateBookUseCase;

    @Inject
    DeleteBookUseCase deleteBookUseCase;

    @Inject
    FindBookByIsbnUseCase findBookByIsbnUseCase;

    @Inject
    AddAuthorToBookUseCase addAuthorToBookUseCase;

    @Inject
    RemoveAuthorFromBookUseCase removeAuthorFromBookUseCase;

    @Inject
    UpdateBookStockUseCase updateBookStockUseCase;

    @Inject
    BookDTOMapper mapper;

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        addAuthorToBookUseCase.addAuthor(bookId, authorId);
    }

    @Override
    public BookResponse createBook(BookCreateRequest bookCreateRequest) {
        CreateBookUseCase.CreateBookCommand command = mapper.toCreateCommand(bookCreateRequest);
        Book book = createBookUseCase.execute(command);
        return mapper.toResponse(book);
    }

    @Override
    public void decrementStock(Long bookId, DecrementStockRequest decrementStockRequest) {
        updateBookStockUseCase.decrementStock(bookId, decrementStockRequest.getQuantity());
    }

    @Override
    public void deleteBook(Long id) {
        deleteBookUseCase.delete(id);
    }

    @Override
    public List<BookResponse> getAllBooks(String title) {
        if (title != null && !title.isBlank()) {
            return mapper.toResponseList(listBooksUseCase.findByTitle(title));
        }
        return mapper.toResponseList(listBooksUseCase.findAll());
    }

    @Override
    public List<BookResponse> getAvailableBooks() {
        return mapper.toResponseList(listBooksUseCase.findAvailable());
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = getBookUseCase.findById(id);
        return mapper.toResponse(book);
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        Book book = findBookByIsbnUseCase.findByIsbn(isbn);
        return mapper.toResponse(book);
    }

    @Override
    public void incrementStock(Long bookId, IncrementStockRequest incrementStockRequest) {
        updateBookStockUseCase.incrementStock(bookId, incrementStockRequest.getQuantity());
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        removeAuthorFromBookUseCase.removeAuthor(bookId, authorId);
    }

    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        UpdateBookUseCase.UpdateBookCommand command = mapper.toUpdateCommand(bookUpdateRequest);
        Book book = updateBookUseCase.update(id, command);
        return mapper.toResponse(book);
    }
}