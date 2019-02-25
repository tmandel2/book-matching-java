package bookmatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

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



}
