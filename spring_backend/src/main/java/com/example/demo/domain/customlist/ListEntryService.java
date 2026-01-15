package com.example.demo.domain.customlist;

import com.example.demo.core.generic.AbstractServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ListEntryService extends AbstractServiceImpl<ListEntry> {

    private final ListEntryRepository repository;

    public ListEntryService(ListEntryRepository Repository) {
        this.repository = Repository;
        super.repository = Repository;
    }

    public List<ListEntry> getAllEntries() {
        return findAll();
    }

    public ListEntry getEntryById(UUID id) {
        return findById(id);
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
        deleteById(id);
    }

    @Transactional
    public ListEntry saveEntry(ListEntry listEntry) {
        return save(listEntry);
    }
}
