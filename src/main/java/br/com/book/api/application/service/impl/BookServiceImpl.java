package br.com.book.api.application.service.impl;

import br.com.book.api.application.port.in.book.*;
import br.com.book.api.application.port.out.AuthorRepositoryPort;
import br.com.book.api.application.port.out.BookRepositoryPort;
import br.com.book.api.application.port.out.PublisherRepositoryPort;

import br.com.book.api.domain.Author;
import br.com.book.api.domain.Book;
import br.com.book.api.domain.Publisher;
import br.com.book.api.shared.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class BookServiceImpl implements
        CreateBookUseCase,
        GetBookUseCase,
        ListBooksUseCase,
        UpdateBookUseCase,
        DeleteBookUseCase,
        FindBookByIsbnUseCase,
        AddAuthorToBookUseCase,
        RemoveAuthorFromBookUseCase,
        UpdateBookStockUseCase {

    @Inject
    BookRepositoryPort bookRepositoryPort;

    @Inject
    AuthorRepositoryPort authorRepositoryPort;

    @Inject
    PublisherRepositoryPort publisherRepositoryPort;

    // ========== CreateBookUseCase ==========
    @Override
    public Book execute(CreateBookCommand command) {

        if (bookRepositoryPort.existsBookByIsbn(command.getIsbn())) {
            throw new RuntimeException("Book already exists with ISBN: " + command.getIsbn());
        }

        Publisher publisher = null;
        if (command.getPublisherId() != null) {
            publisher = publisherRepositoryPort.findPublisherById(command.getPublisherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", command.getPublisherId()));
        }

        // 🔥 BUSCAR AUTORES PELOS IDs
        List<Author> authors = new ArrayList<>();
        if (command.getAuthorIds() != null && !command.getAuthorIds().isEmpty()) {
            for (Long authorId : command.getAuthorIds()) {
                Author author = authorRepositoryPort.findAuthorById(authorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Author", authorId));
                authors.add(author);
            }
        }

        Book book = new Book();
        book.setTitle(command.getTitle());
        book.setSubtitle(command.getSubtitle());
        book.setIsbn(command.getIsbn());
        book.setPublicationYear(command.getPublicationYear());
        book.setNumberOfPages(command.getNumberOfPages());
        book.setPrice(command.getPrice());
        book.setEdition(command.getEdition());
        book.setSynopsis(command.getSynopsis());
        book.setStockQuantity(command.getStockQuantity());
        book.setPublisher(publisher);
        book.setAuthors(authors);  // 🔥 SETAR AUTORES BUSCADOS
        book.setCategories(new ArrayList<>());  // Inicializar vazio

        return bookRepositoryPort.save(book);
    }

    // ========== GetBookUseCase ==========
    @Override
    public Book findById(Long id) {
        return bookRepositoryPort.findBookById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    // ========== ListBooksUseCase ==========
    @Override
    public List<Book> findAll() {
        return bookRepositoryPort.findAllBooks();  // ← corrigido
    }

    @Override
    public List<Book> findByTitle(String title) {
        if (title == null || title.isBlank()) {
            return findAll();
        }
        return bookRepositoryPort.findByTitle(title);
    }

    @Override
    public List<Book> findAvailable() {
        return bookRepositoryPort.findAvailableBooks();
    }

    // ========== UpdateBookUseCase ==========
    @Override
    public Book update(Long id, UpdateBookCommand command) {
        Book book = bookRepositoryPort.findBookById(id)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        Publisher publisher = null;
        if (command.getPublisherId() != null) {
            publisher = publisherRepositoryPort.findPublisherById(command.getPublisherId())  // ← verifique este método
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", command.getPublisherId()));
        }

        book.setTitle(command.getTitle());
        book.setSubtitle(command.getSubtitle());
        book.setIsbn(command.getIsbn());
        book.setPublicationYear(command.getPublicationYear());
        book.setNumberOfPages(command.getNumberOfPages());
        book.setPrice(command.getPrice());
        book.setEdition(command.getEdition());
        book.setSynopsis(command.getSynopsis());
        book.setStockQuantity(command.getStockQuantity());
        book.setPublisher(publisher);

        return bookRepositoryPort.save(book);
    }

    // ========== DeleteBookUseCase ==========
    @Override
    public void delete(Long id) {
        if (!bookRepositoryPort.existsBookById(id)) {  // ← corrigido
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepositoryPort.deleteBookById(id);  // ← corrigido
    }

    // ========== FindBookByIsbnUseCase ==========
    @Override
    public Book findByIsbn(String isbn) {
        return bookRepositoryPort.findBookByIsbn(isbn)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", "isbn", isbn));
    }

    // ========== AddAuthorToBookUseCase ==========
    @Override
    public void addAuthor(Long bookId, Long authorId) {
        Book book = bookRepositoryPort.findBookById(bookId)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));

        Author author = authorRepositoryPort.findAuthorById(authorId)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Author", authorId));

        book.addAuthor(author);
        bookRepositoryPort.save(book);
    }

    // ========== RemoveAuthorFromBookUseCase ==========
    @Override
    public void removeAuthor(Long bookId, Long authorId) {
        Book book = bookRepositoryPort.findBookById(bookId)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));

        Author author = authorRepositoryPort.findAuthorById(authorId)  // ← verifique este método
                .orElseThrow(() -> new ResourceNotFoundException("Author", authorId));

        book.removeAuthor(author);
        bookRepositoryPort.save(book);
    }

    // ========== UpdateBookStockUseCase ==========
    @Override
    public void incrementStock(Long bookId, int quantity) {
        Book book = bookRepositoryPort.findBookById(bookId)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
        book.incrementStock(quantity);
        bookRepositoryPort.save(book);
    }

    @Override
    public void decrementStock(Long bookId, int quantity) {
        Book book = bookRepositoryPort.findBookById(bookId)  // ← corrigido
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
        book.decrementStock(quantity);
        bookRepositoryPort.save(book);
    }
}