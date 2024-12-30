package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.config.SpringSecurity;
import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.Users;
import net.engineeringdigest.journalapp.repository.UserRepository;
import net.engineeringdigest.journalapp.service.JournalEntryService;
import net.engineeringdigest.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private UserRepository userRepository;
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users users)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        Users userInDB=userService.findByUserName(username);
        if (userInDB != null) {
            userInDB.setUserName(users.getUserName());
            userInDB.setPassword(users.getPassword());
            userService.saveNewUser(userInDB);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Without Using Security Context
//    @PutMapping("/{username}")
//    public ResponseEntity<?> updateUser(@RequestBody Users users,@PathVariable String username) {
//
//        Users userInDB=userService.findByUserName(username);
//        if (userInDB != null) {
//            userInDB.setUserName(users.getUserName());
//            userInDB.setPassword(users.getPassword());
//            userService.saveUser(userInDB);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping
    public ResponseEntity<?>  deleteByID()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
