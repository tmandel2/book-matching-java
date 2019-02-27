package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;


@RestController

public class BookController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @PostMapping("/books")
    public Book createBook(@RequestBody Book book, HttpSession session) throws Exception{
        User user = userRepository.findByUsername(session.getAttribute("username").toString());
        if(user == null){
            throw new Exception("Log in");
        }
        Optional<Book> bookInDatabase = Optional.ofNullable(bookRepository.findByTitle(book.getTitle()));
        if(!bookInDatabase.isPresent()) {
            bookRepository.save(book);
        }
//            Set<User> users = book.getUsers();
//            users.add(user);
            Set<Book> likedBooks = user.getLikedBooks();
            likedBooks.add(book);
//            book.setUsers(users);
            user.setLikedBooks(likedBooks);
//            Book createdBook = bookRepository.save(book);
            userRepository.save(user);
            return book;
//        } else {
//
//            Set<Book> likedBooks = user.getLikedBooks();
//            likedBooks.add(createdBook);
//            user.setLikedBooks(likedBooks);
//            userRepository.save(user);
//            return createdBook;
//        }

    }

    @GetMapping("/books")
    public Iterable<Book> getBooks(){
        Iterable<Book> books = bookRepository.findAll();
        return books;
    }

    @GetMapping("/books/{id}")
    public Book showBook(@PathVariable Long id){
        Book foundBook = bookRepository.findById(id).get();
        return foundBook;
    }

    @PutMapping("/books/{id}")
    public Book updatePost(@RequestBody Book book, @PathVariable Long id) throws Exception{
        Optional<Book> editedBook = bookRepository.findById(id);
        if(editedBook.isPresent()){
            Book bookToEdit = editedBook.get();
            bookToEdit.setTitle(book.getTitle());
            bookToEdit.setAuthor(book.getAuthor());
            bookToEdit.setImage(book.getImage());
            return bookRepository.save(bookToEdit);
        } else {
            throw new Exception("book not here");
        }
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id){
        bookRepository.deleteById(id);
        return "book gone";
    }



}
