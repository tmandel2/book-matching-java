package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;


@RestController
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BookRepository bookRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/auth/registration")
    public Users createUser(@RequestBody Users users, HttpSession session){
        Users createdUsers = usersService.saveUser(users);
        session.setAttribute("username", createdUsers.getUsername());
        session.setAttribute("userId", createdUsers.getId());
        return createdUsers;
    }

    @PostMapping("/auth/login")
    public Users login(@RequestBody Users login, HttpSession session) throws Exception{
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Users users = usersRepository.findByUsername(login.getUsername());
        if(users ==  null){
            throw new Exception("No Users");
        }
        boolean valid = bCryptPasswordEncoder.matches(login.getPassword(), users.getPassword());
        if(valid){
            session.setAttribute("username", users.getUsername());
            session.setAttribute("userId", users.getId());
            return users;
        }else{
            throw new Exception("No Password");
        }
    }

    @GetMapping("/auth/logout")
    public void logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("userId");
        session.invalidate();
        return;
    }

    @GetMapping("/users/{id}")
    public HashMap show(@PathVariable Long id) throws Exception{
        Optional<Users> foundUser = usersRepository.findById(id);
        if(foundUser.isPresent()){
            Users users = foundUser.get();
            Set<Book> books = users.getLikedBooks();
            HashMap<Object, Object> result = new HashMap<Object, Object>();
            result.put("users", users);
            result.put("books", books);
            return result;
        }else{
            throw new Exception("Bad Users");
        }
    }

    @GetMapping("/users/")
    public Iterable<Users> getUsers(){
        return usersRepository.findAll();
    }

    @PutMapping("/users/{id}")
    public Users updateUser(@RequestBody String username, @PathVariable Long id, HttpSession session) throws Exception{
        Optional<Users> editedUser = usersRepository.findById(id);
        if(editedUser.isPresent()){
            Users usersToEdit = editedUser.get();
            usersToEdit.setUsername(username);
            session.setAttribute("username", usersToEdit.getUsername());
//            usersToEdit.setPassword(usersToEdit.getPassword());
            return usersRepository.save(usersToEdit);
        } else {
            throw new Exception("not a user");
        }
    }

    @DeleteMapping("/users/{id}")
    public Optional<Users> deleteUser(@PathVariable Long id, HttpSession session){
        Optional<Users> userToDelete = usersRepository.findById(id);
        session.removeAttribute("username");
        session.removeAttribute("userId");
        session.invalidate();
        usersRepository.deleteById(id);
        return userToDelete;
    }
}
