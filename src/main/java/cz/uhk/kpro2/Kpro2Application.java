package cz.uhk.kpro2;

import cz.uhk.kpro2.model.User;
import cz.uhk.kpro2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Kpro2Application {

    private final UserService userService;

    @Autowired
    public Kpro2Application(UserService userService) {
        this.userService = userService;
    }

    private void addUser(User user) {
        userService.saveUser(user);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            User user = new User();
            user.setName("Admin");
            user.setUsername("admin");
            user.setPassword("heslo");
            user.setRole("ADMIN");
            addUser(user);

            User user2 = new User();
            user2.setName("User");
            user2.setUsername("user");
            user2.setPassword("heslo");
            user2.setRole("USER");
            addUser(user2);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Kpro2Application.class, args);
    }

}
