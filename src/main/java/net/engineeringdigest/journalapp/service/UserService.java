package net.engineeringdigest.journalapp.service;

import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.Users;
import net.engineeringdigest.journalapp.repository.JournalEntryRepo;
import net.engineeringdigest.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public void saveUser(Users users)
    {
        userRepository.save(users);
    }
    public void saveNewUser(Users users)
    {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRoles(Arrays.asList("USER"));
        userRepository.save(users);
    }

    public List<Users> getAllUsers()
    {
        return userRepository.findAll();
    }

    public Users findById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteById(ObjectId id) {
        Optional<Users> entity = userRepository.findById(id);
        if (entity.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Users findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
