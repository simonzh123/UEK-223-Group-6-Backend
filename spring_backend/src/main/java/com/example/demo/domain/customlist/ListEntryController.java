package com.example.demo.domain.customlist;


import jakarta.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@Log4j2
@RequestMapping("/list-entries")
public class ListEntryController {

    private final ListEntryService entryService;

    @Autowired
    public ListEntryController(ListEntryService entryService) {
        this.entryService = entryService;
    }

    private String getMailFromJWT(){return SecurityContextHolder.getContext().getAuthentication().getName(); }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<ListEntry>> getAllEntries() {
        return ResponseEntity.ok(entryService.getAllEntries());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') || @userPermissionEvaluator.isOwnEntryOrIsAdminEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntry> getEntryById(@PathVariable UUID id) {
        try {
            ListEntry entry = entryService.getEntryById(id);
            return ResponseEntity.ok(entry);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ListEntry>> getEntriesByUser() {
        List<ListEntry> entries = entryService.getEntriesByUser(getMailFromJWT());
        return ResponseEntity.ok(entries);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MODIFY') || @userPermissionEvaluator.isOwnEntryOrIsAdminEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntry> updateEntry(@PathVariable UUID id, @RequestBody @Valid ListEntry entry) {
        try {
            entry.setId(id);
            return ResponseEntity.ok(entryService.updateEntry(entry));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<ListEntry> createEntry(@RequestBody @Valid ListEntry entry) {
        ListEntry savedEntry = entryService.saveEntry(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DEACTIVATE')")
    public ResponseEntity<ListEntry> deleteEntry(@PathVariable UUID id) {
        entryService.deleteEntryById(id);
        return ResponseEntity.noContent().build();
    }
}
