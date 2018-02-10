package fr.iai.filerepo.controller;

import fr.iai.filerepo.database.RoleRepository;
import fr.iai.filerepo.database.UserRepository;
import fr.iai.filerepo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${security.encoding-strength}")
    private Integer encodingStrength;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("create")
    public void createUserAccount(@RequestParam String login, @RequestParam String password,
                              @RequestParam String firstName, @RequestParam String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(login);
        String salt = KeyGenerators.string().generateKey();
        user.setPassword(new ShaPasswordEncoder(encodingStrength).encodePassword(password, salt));
        user.setSalt(salt);
        user.setEnabled(false);
        user.setRoles(Collections.singletonList(roleRepository.findOneByRoleName("STANDARD_USER")));
        userRepository.save(user);
    }

    @PostMapping("update/{username}")
    public void updateUserAccount(@PathVariable String username,
                                  @RequestParam(required = false) String login,
                                  @RequestParam(required = false) String password,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String lastName) {
        User user = userRepository.findByUsername(username);
        if (!StringUtils.isEmpty(firstName)) {
            user.setFirstName(firstName);
        }
        if (!StringUtils.isEmpty(lastName)) {
            user.setLastName(lastName);
        }
        if (!StringUtils.isEmpty(login)) {
            user.setUsername(login);
        }
        if (!StringUtils.isEmpty(password)) {
            String salt = KeyGenerators.string().generateKey();
            user.setPassword(new ShaPasswordEncoder(encodingStrength).encodePassword(password, salt));
            user.setSalt(salt);
        }
        userRepository.save(user);
    }

    @GetMapping("activate/{username}")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public void activate (@PathVariable String username) {
        User activatingUser = userRepository.findByUsername(username);
        activatingUser.setEnabled(true);
        userRepository.save(activatingUser);
    }


}
