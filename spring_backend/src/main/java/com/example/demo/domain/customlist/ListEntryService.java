package com.example.demo.domain.customlist;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.example.demo.core.generic.AbstractServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ListEntryService extends AbstractServiceImpl<ListEntry> {

    private final  UserService userService;

    private final ListEntryRepository repository;

    public ListEntryService(UserService userService, ListEntryRepository Repository) {
        this.userService = userService;
        this.repository = Repository;
    }

    public List<ListEntry> getAllEntries() {
        return repository.findAll();
    }

    public ListEntry getEntryById(UUID id) throws Exception {
        return repository.findById(id).orElseThrow(Exception::new);
    }

    public List<ListEntry> getEntriesByUser(String email) {
        UUID userId = userService.getUserByMail(email).getId();
        return repository.findAllByUserId(userId);
    }

    @Transactional
    public ListEntry updateEntry(ListEntry oldEntry) throws NoSuchElementException {
        ListEntry updatedEntry = repository
                .findById(oldEntry.getId())
                .orElseThrow(() -> new NoSuchElementException("Entry not found"));
        updatedEntry.setTitle(oldEntry.getTitle());
        updatedEntry.setText(oldEntry.getText());
        updatedEntry.setImportance(oldEntry.getImportance());
        repository.save(updatedEntry);
        return updatedEntry;
    }

    @Transactional
    public void deleteEntryById(UUID id) {
        deleteById(id);
    }

    @Transactional
    public ListEntry saveEntry(ListEntry listEntry) {
        return save(listEntry);
    }
}
