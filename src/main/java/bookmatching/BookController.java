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

}
