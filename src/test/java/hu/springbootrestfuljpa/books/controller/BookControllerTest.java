package hu.springbootrestfuljpa.books.controller;


import hu.springbootrestfuljpa.books.controller.BookController;
import hu.springbootrestfuljpa.books.model.Book;
import hu.springbootrestfuljpa.books.model.Review;
import hu.springbootrestfuljpa.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    BookController bookController;

    //variables staticas para prueba
    private static Integer ID = 2;
    private static Integer RELEASE = 22;
    private static List<Review> REVIEW_LIST = new ArrayList<>();
    private static String AUTHOR = "HOMERO";
    private static String TITULO = "LA ODISEA";
    private static final Book BOOK = new Book();
    private static final Optional<Book> OPTIONAL_BOOK = Optional.of(BOOK);
    private static final Optional<Book> OPTIONAL_BOOK_EMPY = Optional.empty();
    private final Book book = new Book();


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        //para retrieveBookTest()
        BOOK.setAuthor(AUTHOR);
        BOOK.setId(ID);
        BOOK.setRelease(RELEASE);
        BOOK.setReviews(REVIEW_LIST);
        BOOK.setTitle(TITULO);


    }

    @Test
    public void retrieveAllBooksTest() {
        Mockito.when(this.bookRepository.findAll()).thenReturn(Arrays.asList(book));

        final List<Book> response = bookController.retrieveAllBooks();
        assertNotNull(response);// nuestro arreglo no es nulo
        assertFalse(response.isEmpty()); // nuestro arreglo no esta vacio
        assertEquals(response.size(), 1); //el tamanio de nuestro arreglo es mayor o igual a 1
    }

    @Test
    public void retrieveBookTest() {
        Mockito.when(this.bookRepository.findById(ID)).thenReturn(OPTIONAL_BOOK);

        ResponseEntity<Book> response = bookController.retrieveBook(ID);
        assertEquals(response.getBody().getAuthor(), AUTHOR);
        assertEquals(response.getBody().getTitle(), TITULO);

    }

    @Test
    public void retrieveBookNotFoundTest() {
        Mockito.when(this.bookRepository.findById(ID)).thenReturn(OPTIONAL_BOOK_EMPY);

        ResponseEntity<Book> httpResponse = bookController.retrieveBook(ID);
        assertEquals(httpResponse.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void createBookTest() {// si el libro no existe en la BBDD
        Mockito.when(this.bookRepository.existsById(BOOK.getId())).thenReturn(false);

        ResponseEntity<Object> httpResponse = bookController.createBook(BOOK);
        assertEquals(httpResponse.getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void createBookExistsByIdTest() {// si el libro ya existe en la BBDD
        Mockito.when(this.bookRepository.existsById(BOOK.getId())).thenReturn(true);

        ResponseEntity<Object> httpResponse = bookController.createBook(BOOK);
        assertEquals(httpResponse.getStatusCode(),HttpStatus.CONFLICT);
    }

    @Test
    public void deleteBookTest(){
        Mockito.when(this.bookRepository.findById(ID)).thenReturn(OPTIONAL_BOOK);

        ResponseEntity<Object> httpResponse = bookController.deleteBook(ID);
        assertEquals(httpResponse.getStatusCode(),HttpStatus.OK);

    }

    @Test
    public void deleteBookNotFoundTest(){
        Mockito.when(this.bookRepository.findById(ID)).thenReturn(OPTIONAL_BOOK_EMPY);

        ResponseEntity<Object> httpResponse = bookController.deleteBook(ID);
        assertEquals(httpResponse.getStatusCode(),HttpStatus.NOT_FOUND);

    }

    @Test
    void contextLoads() {
    }
}
