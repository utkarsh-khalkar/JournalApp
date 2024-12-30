package net.engineeringdigest.journalapp.service;

import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.Users;
import net.engineeringdigest.journalapp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntity, String username)
    {
        try {
            Users users=userService.findByUserName(username);
            JournalEntry savedEntry= journalEntryRepo.save(journalEntity);
            users.getJournalEntryList().add(savedEntry);
            userService.saveUser(users);
            System.out.println("Entry Saved Successfully...");
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error occurred while saving entry");
        }
    }
    public void saveEntry(JournalEntry journalEntity)
    {
        journalEntryRepo.save(journalEntity);
    }

    public List<JournalEntry> getAllEntries()
    {
        return journalEntryRepo.findAll();
    }
    public Optional<JournalEntry> getEntryById(ObjectId id)
    {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteEntry(ObjectId id, String username)
    {
        boolean removed=false;
        try {
            Users users=userService.findByUserName(username);
             removed= users.getJournalEntryList().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if (removed)
            {
                userService.saveUser(users);
                journalEntryRepo.deleteById(id);
            }
        }catch (Exception e)
        {
            System.out.println(e);
            throw  new RuntimeException("An error occurred while deleting entry");
        }
    return removed;

    }
    public void updateEntry(JournalEntry journalEntity)
    {

        journalEntryRepo.save(journalEntity);
    }
}
