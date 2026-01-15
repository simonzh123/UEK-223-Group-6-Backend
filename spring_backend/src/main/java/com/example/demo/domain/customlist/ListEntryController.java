package com.example.demo.domain.customlist;


import com.example.demo.domain.customlist.dto.ListEntryDTO;
import com.example.demo.domain.customlist.dto.ListEntryMapper;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Validated
@RestController
@Log4j2
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
    @PreAuthorize("hasAuthority('USER_READ_ALL')")
    public ResponseEntity<List<ListEntryDTO>> getAllEntries() {
        List<ListEntry> entries = entryService.getAllEntries();
        return new ResponseEntity<>(entryMapper.toDTOs(entries), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') || @userPermissionEvaluator.isOwnEntryOrIsAdminEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntryDTO> getEntryById(@PathVariable UUID id) {
        try {
            ListEntry entry = entryService.getEntryById(id);
            return new ResponseEntity<>(entryMapper.toDTO(entry), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ListEntryDTO>> getEntriesByUser() {
        List<ListEntry> entries = entryService.getEntriesByUser(getMailFromJWT());
        return new ResponseEntity<>(entryMapper.toDTOs(entries), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MODIFY') || @userPermissionEvaluator.isOwnEntryOrIsAdminEvaluator(authentication.principal.user,#id)")
    public ResponseEntity<ListEntryDTO> updateEntry(@PathVariable UUID id, @RequestBody @Valid ListEntryDTO entryDTO) {
        try {
            ListEntry entryToUpdate = entryMapper.fromDTO(entryDTO);
            entryToUpdate.setId(id);
            ListEntry updated = entryService.updateEntry(entryToUpdate);
            return ResponseEntity.ok(entryMapper.toDTO(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<ListEntryDTO> createEntry(@RequestBody @Valid ListEntryDTO entryDTO) {
        ListEntry saved = entryService.saveEntry(entryMapper.fromDTO(entryDTO));
        return new ResponseEntity<>(entryMapper.toDTO(saved), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DEACTIVATE')")
    public ResponseEntity<ListEntry> deleteEntry(@PathVariable UUID id) {
        entryService.deleteEntryById(id);
        return ResponseEntity.noContent().build();
    }
}
