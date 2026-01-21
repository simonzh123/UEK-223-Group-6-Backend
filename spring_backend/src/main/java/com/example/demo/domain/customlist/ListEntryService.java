package com.example.demo.domain.customlist;

import com.example.demo.core.exception.NoSuchListEntryException;
import com.example.demo.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.demo.core.generic.AbstractServiceImpl;

import java.util.*;

@Service
public class ListEntryService extends AbstractServiceImpl<ListEntry> {

    private final  UserService userService;
    private final ListEntryRepository repository;

    public ListEntryService(UserService userService, ListEntryRepository repository) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
    }

    public List<ListEntry> getAllEntries() {
        return repository.findAll();
    }

    public ListEntry getEntryById(UUID id) throws NoSuchListEntryException {
        return repository.findById(id).orElseThrow(() -> new NoSuchListEntryException(id));
    }

    public List<ListEntry> getEntriesByUser(String email) {
        UUID userId = userService.getUserByMail(email).getId();
        return repository.findAllByUserId(userId);
    }

    @Transactional
    public ListEntry updateEntry(ListEntry oldEntry) throws NoSuchElementException {
        ListEntry updatedEntry = repository
                .findById(oldEntry.getId())
                .orElseThrow(() -> new NoSuchListEntryException(oldEntry.getId()));
        updatedEntry.setTitle(oldEntry.getTitle());
        updatedEntry.setText(oldEntry.getText());
        updatedEntry.setImportance(oldEntry.getImportance());
        repository.save(updatedEntry);
        return updatedEntry;
    }

    public void deleteEntryById(UUID id) {
        repository.deleteById(id);
    }

    public ListEntry saveEntry(ListEntry listEntry) {
        return save(listEntry);
    }
}
