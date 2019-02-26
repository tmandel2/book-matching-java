package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookRepository bookRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/auth/registration")
    public User createUser(@RequestBody User user){
        User createdUser = userService.saveUser(user);
        return createdUser;
    }

    @PostMapping("/auth/login")
    public User login(@RequestBody User login, HttpSession session) throws Exception{
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByUsername(login.getUsername());
        if(user ==  null){
            throw new Exception("No User");
        }
        boolean valid = bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword());
        if(valid){
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());
            return user;
        }else{
            throw new Exception("No Password");
        }
    }

    @GetMapping("/users/{id}")
    public HashMap show(@PathVariable Long id) throws Exception{
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            User user = foundUser.get();
//            Iterable<Post> posts = postRepository.findByUserId(id);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("user", user);
//            result.put("posts", posts);
            return result;
        }else{
            throw new Exception("Bad User");
        }
    }

    @GetMapping("/users/")
    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) throws Exception{
        Optional<User> editedUser = userRepository.findById(id);
        if(editedUser.isPresent()){
            User userToEdit = editedUser.get();
            userToEdit.setUsername(user.getUsername());
            userToEdit.setPassword(user.getPassword());
            return userService.saveUser(userToEdit);
        } else {
            throw new Exception("not a user");
        }
    }


}
