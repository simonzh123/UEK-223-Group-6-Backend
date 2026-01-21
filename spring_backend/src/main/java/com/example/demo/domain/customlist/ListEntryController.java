package com.example.demo.domain.customlist;

import com.example.demo.core.exception.NoSuchListEntryException;
import com.example.demo.domain.customlist.dto.ListEntryDTO;
import com.example.demo.domain.customlist.dto.ListEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/list-entries")
public class ListEntryController {

    private final ListEntryService entryService;
    private final ListEntryMapper entryMapper;

    @Autowired
    public ListEntryController(ListEntryService entryService, ListEntryMapper entryMapper) {
        this.entryService = entryService;
        this.entryMapper = entryMapper;
    }

    private String getMailFromJWT(){return SecurityContextHolder.getContext().getAuthentication().getName(); }

    @GetMapping
    @Operation(summary = "Show all entries", description = "Show all entries with their owner")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<ListEntryDTO>> getAllEntries() {
        List<ListEntry> entries = entryService.getAllEntries();
        return new ResponseEntity<>(entryMapper.toDTOs(entries), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Show a single entry", description = "Show one entry identified by its id")
    @PreAuthorize("hasAuthority('USER_READ') || @userPermissionEvaluator.isOwnEntryEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntryDTO> getEntryById(@PathVariable UUID id) throws NoSuchListEntryException {
            ListEntry entry = entryService.getEntryById(id);
            return new ResponseEntity<>(entryMapper.toDTO(entry), HttpStatus.OK);
    }

    @GetMapping("/user")
    @Operation(summary = "Show all entries of one user")
    public ResponseEntity<List<ListEntryDTO>> getEntriesByUser() {
        List<ListEntry> entries = entryService.getEntriesByUser(getMailFromJWT());
        return new ResponseEntity<>(entryMapper.toDTOs(entries), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an entry", description = "Update an entry based on its id")
    @PreAuthorize("@userPermissionEvaluator.isOwnEntryEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntryDTO> updateEntry(@PathVariable UUID id, @RequestBody @Valid ListEntryDTO entryDTO) {
            ListEntry entryToUpdate = entryMapper.fromDTO(entryDTO);
            entryToUpdate.setId(id);
            ListEntry updated = entryService.updateEntry(entryToUpdate);
            return ResponseEntity.ok(entryMapper.toDTO(updated));
    }

    @PostMapping
    @Operation(summary = "Create an entry", description = "Create one entry with its attributes")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<ListEntryDTO> createEntry(@RequestBody @Valid ListEntryDTO entryDTO) {
        ListEntry saved = entryService.saveEntry(entryMapper.fromDTO(entryDTO));
        return new ResponseEntity<>(entryMapper.toDTO(saved), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete one entry", description = "Delete an entry based on its id")
    @PreAuthorize("hasAuthority('USER_DEACTIVATE') || @userPermissionEvaluator.isOwnEntryEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntry> deleteEntry(@PathVariable UUID id) {
        entryService.deleteEntryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
