package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.Users;
import net.engineeringdigest.journalapp.service.JournalEntryService;
import net.engineeringdigest.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllUsers()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Users users=userService.findByUserName(authentication.getName());
        List<JournalEntry> journalEntryList = users.getJournalEntryList();
        if(journalEntryList != null && !journalEntryList.isEmpty()) {
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<JournalEntry>  createEntry(@RequestBody JournalEntry journalEntry)
    {
        try {

            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry,username);
            return new ResponseEntity<>( journalEntry,HttpStatus.CREATED);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Users users=userService.findByUserName(authentication.getName());
       List<JournalEntry> journalEntryList = users.getJournalEntryList().stream().filter(f->f.getId().equals(myId)).collect(Collectors.toList());
        if(!journalEntryList.isEmpty())
        {
            Optional<JournalEntry> journalEntity=journalEntryService.getEntryById(myId);
            if (journalEntity.isPresent()) {
                return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateEntry
            (@PathVariable ObjectId id,
             @RequestBody JournalEntry journalEntry
            ) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        Users users=userService.findByUserName(username);
        List<JournalEntry> journalEntryList = users.getJournalEntryList().stream().filter(f->f.getId().equals(id)).collect(Collectors.toList());

        if(!journalEntryList.isEmpty())
        {

            Optional<JournalEntry> journalEntry1=journalEntryService.getEntryById(id);
            if(journalEntry1.isPresent()) {
                JournalEntry old=journalEntry1.get();
                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() : old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        boolean remove=journalEntryService.deleteEntry(id,username);
        if (remove) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
