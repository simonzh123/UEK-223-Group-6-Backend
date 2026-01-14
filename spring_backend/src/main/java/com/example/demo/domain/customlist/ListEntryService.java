package com.example.demo.domain.customlist;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ListEntryService {

    private final ListEntryRepository repository;

    public ListEntryService(ListEntryRepository Repository) {
        this.repository = Repository;
    }

    public List<ListEntry> getAllEntries() {
        return repository.findAll();
    }

    public List<ListEntry> getEntriesByUser(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional
    public void updateEntry(ListEntry oldEntry) {
        ListEntry updatedEntry = repository
                .findById(oldEntry.getId())
                .orElseThrow(() -> new NoSuchElementException("Entry not found"));
        updatedEntry.setTitle(oldEntry.getTitle());
        updatedEntry.setText(oldEntry.getText());
        updatedEntry.setImportance(oldEntry.getImportance());
        repository.save(updatedEntry);
    }

    @Transactional
    public void deleteEntryById(UUID id) {
        repository.deleteById(id);
    }

    @Transactional
    public ListEntry saveEntry(ListEntry listEntry) {
        return repository.save(listEntry);
    }
}
