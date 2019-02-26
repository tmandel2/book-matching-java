package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;


@RestController

public class BookController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @PostMapping("/books")
    public Book createBook(@RequestBody Book book, HttpSession session) throws Exception{
//        User user = userRepository.findByUsername(session.getAttribute("username").toString());
//        if(user == null){
//            throw new Exception("You must log in");
//        }
//        book.setUser(author);
        Book createdBook = bookRepository.save(book);
        return createdBook;
    }


    @GetMapping("/books")
    public Iterable<Book> getBooks(){
        return bookRepository.findAll();
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

}
