package com.example.demo.domain.customlist;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/list-entries")
public class ListEntryController {

    private final ListEntryService entryService;

    @Autowired
    public ListEntryController(ListEntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    public ResponseEntity<List<ListEntry>> getAllEntries() {
        List<ListEntry> entries = entryService.getAllEntries();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListEntry> getEntryById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(entryService.getEntryById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ListEntry>> getEntriesByUser(@PathVariable UUID id) {
        List<ListEntry> entries = entryService.getEntriesByUser(id);
        return ResponseEntity.ok(entries);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListEntry> updateEntry(@PathVariable UUID id, @RequestBody @Valid ListEntry entry) {
        try {
            entry.setId(id);
            entryService.updateEntry(entry);
            return ResponseEntity.ok(entry);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ListEntry> createEntry(@RequestBody @Valid ListEntry entry) {
        ListEntry savedEntry = entryService.saveEntry(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ListEntry> deleteEntry(@PathVariable UUID id) {
        entryService.deleteEntryById(id);
        return ResponseEntity.noContent().build();
    }
}
