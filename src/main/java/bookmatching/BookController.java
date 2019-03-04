package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController

public class BookController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BookRepository bookRepository;

//Automatically adds a book to the DB at the same time as adding to the logged in user's favorites.
    @PostMapping("/books")
    public Book createBook(@RequestBody Book book, HttpSession session) throws Exception{
        Users users = usersRepository.findByUsername(session.getAttribute("username").toString());
        if(users == null){
            throw new Exception("Log in");
        }
        Optional<Book> bookInDatabase = Optional.ofNullable(bookRepository.findByTitle(book.getTitle()));
        if(!bookInDatabase.isPresent()) {
            bookRepository.save(book);
        }
        Set<Book> likedBooks = users.getLikedBooks();
        likedBooks.add(book);
        users.setLikedBooks(likedBooks);
        usersRepository.save(users);
        return book;
    }
//This will show all the books.
    @GetMapping("/books")
    public Iterable<Book> getBooks(){
        Iterable<Book> books = bookRepository.findAll();
        return books;
    }
//This returns a specific book. This route and the above route are not both used, as the react app does
//    not currently support individual books, but views all the books on a single page.
    @GetMapping("/books/{id}")
    public Book showBook(@PathVariable Long id){
        Book foundBook = bookRepository.findById(id).get();
        return foundBook;
    }
//Cannot currently edit books through the app. But this route will allow it when that functionality is added to the react app.
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

    //    To remove from a users favorites
    @DeleteMapping("/users/books/{id}")
    public Users deleteFromFavorites(@PathVariable Long id, HttpSession session) throws Exception{
        Users user = usersRepository.findByUsername(session.getAttribute("username").toString());
        if(user == null){
            throw new Exception("Log in");
        }
        Book foundBook = bookRepository.findById(id).get();
            Set<Book> likedBooks = new HashSet<>(user.getLikedBooks());
            Set<Users> users = new HashSet<Users>(foundBook.getUsers());
            likedBooks.remove(foundBook);
            user.setLikedBooks(likedBooks);
            usersRepository.save(user);
            return user;
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id){
        bookRepository.deleteById(id);
        return "book gone";
    }


}
