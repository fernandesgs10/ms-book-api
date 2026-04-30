package br.com.book.api.adapter.in.rest.resource;

import br.com.book.api.adapter.in.rest.mapper.BookDTOMapper;
import br.com.book.api.application.port.in.book.*;
import br.com.book.api.domain.Book;
import br.com.book.api.gen.model.BookCreateRequest;
import br.com.book.api.gen.model.BookResponse;
import br.com.book.api.gen.model.BookUpdateRequest;
import br.com.book.api.gen.model.DecrementStockRequest;
import br.com.book.api.gen.model.IncrementStockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookResourceTest {

    @Mock
    private CreateBookUseCase createBookUseCase;

    @Mock
    private GetBookUseCase getBookUseCase;

    @Mock
    private ListBooksUseCase listBooksUseCase;

    @Mock
    private UpdateBookUseCase updateBookUseCase;

    @Mock
    private DeleteBookUseCase deleteBookUseCase;

    @Mock
    private FindBookByIsbnUseCase findBookByIsbnUseCase;

    @Mock
    private AddAuthorToBookUseCase addAuthorToBookUseCase;

    @Mock
    private RemoveAuthorFromBookUseCase removeAuthorFromBookUseCase;

    @Mock
    private UpdateBookStockUseCase updateBookStockUseCase;

    @Mock
    private BookDTOMapper mapper;

    @InjectMocks
    private BookResource bookResource;

    private Book book;
    private BookResponse bookResponse;
    private BookCreateRequest createRequest;
    private BookUpdateRequest updateRequest;
    private IncrementStockRequest incrementRequest;
    private DecrementStockRequest decrementRequest;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Dom Casmurro");
        book.setIsbn("9788535902811");
        book.setPublicationYear(1899);
        book.setPrice(new BigDecimal("59.90"));

        bookResponse = new BookResponse();
        bookResponse.setId(1L);
        bookResponse.setTitle("Dom Casmurro");

        createRequest = new BookCreateRequest();
        createRequest.setTitle("Dom Casmurro");
        createRequest.setIsbn("9788535902811");

        updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("Dom Casmurro - Edição Especial");

        incrementRequest = new IncrementStockRequest();
        incrementRequest.setQuantity(5);

        decrementRequest = new DecrementStockRequest();
        decrementRequest.setQuantity(1);
    }

    // ========== CREATE BOOK TESTS ==========

    @Test
    void createBook_ShouldReturnBookResponse_WhenSuccessful() {
        CreateBookUseCase.CreateBookCommand command = mock(CreateBookUseCase.CreateBookCommand.class);
        when(mapper.toCreateCommand(createRequest)).thenReturn(command);
        when(createBookUseCase.execute(command)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        BookResponse response = bookResource.createBook(createRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(createBookUseCase).execute(command);
    }

    // ========== GET BOOK BY ID TESTS ==========

    @Test
    void getBookById_ShouldReturnBookResponse_WhenBookExists() {
        when(getBookUseCase.findById(1L)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        BookResponse response = bookResource.getBookById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(getBookUseCase).findById(1L);
    }

    // ========== GET ALL BOOKS TESTS ==========

    @Test
    void getAllBooks_ShouldReturnListOfBooks_WhenNoTitleFilter() {
        List<Book> books = Arrays.asList(book);
        List<BookResponse> responses = Arrays.asList(bookResponse);
        when(listBooksUseCase.findAll()).thenReturn(books);
        when(mapper.toResponseList(books)).thenReturn(responses);

        List<BookResponse> result = bookResource.getAllBooks(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listBooksUseCase).findAll();
    }

    @Test
    void getAllBooks_ShouldReturnFilteredBooks_WhenTitleProvided() {
        String title = "Dom";
        List<Book> books = Arrays.asList(book);
        List<BookResponse> responses = Arrays.asList(bookResponse);
        when(listBooksUseCase.findByTitle(title)).thenReturn(books);
        when(mapper.toResponseList(books)).thenReturn(responses);

        List<BookResponse> result = bookResource.getAllBooks(title);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listBooksUseCase).findByTitle(title);
    }

    // ========== GET AVAILABLE BOOKS TESTS ==========

    @Test
    void getAvailableBooks_ShouldReturnListOfAvailableBooks() {
        List<Book> books = Arrays.asList(book);
        List<BookResponse> responses = Arrays.asList(bookResponse);
        when(listBooksUseCase.findAvailable()).thenReturn(books);
        when(mapper.toResponseList(books)).thenReturn(responses);

        List<BookResponse> result = bookResource.getAvailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(listBooksUseCase).findAvailable();
    }

    // ========== GET BOOK BY ISBN TESTS ==========

    @Test
    void getBookByIsbn_ShouldReturnBookResponse_WhenBookExists() {
        String isbn = "9788535902811";
        when(findBookByIsbnUseCase.findByIsbn(isbn)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        BookResponse response = bookResource.getBookByIsbn(isbn);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(findBookByIsbnUseCase).findByIsbn(isbn);
    }

    // ========== UPDATE BOOK TESTS ==========

    @Test
    void updateBook_ShouldReturnUpdatedBookResponse_WhenSuccessful() {
        UpdateBookUseCase.UpdateBookCommand command = mock(UpdateBookUseCase.UpdateBookCommand.class);
        when(mapper.toUpdateCommand(updateRequest)).thenReturn(command);
        when(updateBookUseCase.update(1L, command)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        BookResponse response = bookResource.updateBook(1L, updateRequest);

        assertNotNull(response);
        verify(updateBookUseCase).update(1L, command);
    }

    // ========== DELETE BOOK TESTS ==========

    @Test
    void deleteBook_ShouldCallDeleteUseCase() {
        bookResource.deleteBook(1L);
        verify(deleteBookUseCase).delete(1L);
    }

    // ========== ADD AUTHOR TO BOOK TESTS ==========

    @Test
    void addAuthorToBook_ShouldCallAddAuthorUseCase() {
        bookResource.addAuthorToBook(1L, 2L);
        verify(addAuthorToBookUseCase).addAuthor(1L, 2L);
    }

    // ========== REMOVE AUTHOR FROM BOOK TESTS ==========

    @Test
    void removeAuthorFromBook_ShouldCallRemoveAuthorUseCase() {
        bookResource.removeAuthorFromBook(1L, 2L);
        verify(removeAuthorFromBookUseCase).removeAuthor(1L, 2L);
    }

    // ========== INCREMENT STOCK TESTS ==========

    @Test
    void incrementStock_ShouldCallIncrementStockUseCase() {
        bookResource.incrementStock(1L, incrementRequest);
        verify(updateBookStockUseCase).incrementStock(1L, 5);
    }

    // ========== DECREMENT STOCK TESTS ==========

    @Test
    void decrementStock_ShouldCallDecrementStockUseCase() {
        bookResource.decrementStock(1L, decrementRequest);
        verify(updateBookStockUseCase).decrementStock(1L, 1);
    }
}