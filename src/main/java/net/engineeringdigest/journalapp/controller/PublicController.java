package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.entity.Users;
import net.engineeringdigest.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;
    @PostMapping("/create-user")
    public void createUser(@RequestBody Users users)
    {

        userService.saveNewUser(users);
    }
}
