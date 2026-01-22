package com.example.demo.domain.customlist;

import com.example.demo.core.exception.NoSuchListEntryException;
import com.example.demo.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.core.generic.AbstractServiceImpl;

import java.util.*;

@Service
public class ListEntryService extends AbstractServiceImpl<ListEntry> {

    private final UserService userService;
    private final ListEntryRepository repository;

    public ListEntryService(UserService userService, ListEntryRepository repository) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
    }

    public List<ListEntry> getAllEntries(Optional<Integer> page, String importance, String sortBy, Boolean isAscending, UUID userId) {
        return queryEntries(Optional.ofNullable(userId), page, importance, sortBy, isAscending);
    }

    public ListEntry getEntryById(UUID id) throws NoSuchListEntryException {
        return repository.findById(id).orElseThrow(() -> new NoSuchListEntryException(id));
    }

    public Integer getPages() {
        return Math.toIntExact(repository.getListEntryCount() / 10 + 1);
    }

    public Integer getPagesForUser(String email) {
        return Math.toIntExact(repository.countDistinctByUser_EmailLikeIgnoreCase(email) / 10 + 1);
    }

    public List<ListEntry> getEntriesByUser(String email, Optional<Integer> page, String importance, String sortBy, Boolean isAscending) {
        UUID userId = userService.getUserByMail(email).getId();
        return queryEntries(Optional.of(userId), page, importance, sortBy, isAscending);
    }

    private List<ListEntry> queryEntries(Optional<UUID> userId,
                                         Optional<Integer> page,
                                         String importance,
                                         String sortBy,
                                         Boolean isAscending) {
        String property = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        if ("user".equalsIgnoreCase(property)) {
            property = "user.lastName";
        }

        if ("importance".equalsIgnoreCase(property)) {
            if (userId.isPresent()) {
                List<ListEntry> data = repository.findAllByUserId(userId.get(), ListEntry.Importance.valueOf(importance), Sort.unsorted());
                Map<ListEntry.Importance, Integer> rank = new HashMap<>();
                rank.put(ListEntry.Importance.LOW, 0);
                rank.put(ListEntry.Importance.MEDIUM, 1);
                rank.put(ListEntry.Importance.HIGH, 2);
                Comparator<ListEntry> cmp = Comparator.comparing(e -> rank.getOrDefault(e.getImportance(), 0));
                if (isAscending == null || !isAscending) {
                    cmp = cmp.reversed();
                }
                data.sort(cmp);
                return data;
            } else {
                List<ListEntry> data = repository.findAllByImportance(ListEntry.Importance.valueOf(importance), Sort.unsorted());
                Map<ListEntry.Importance, Integer> rank = new HashMap<>();
                rank.put(ListEntry.Importance.LOW, 0);
                rank.put(ListEntry.Importance.MEDIUM, 1);
                rank.put(ListEntry.Importance.HIGH, 2);
                Comparator<ListEntry> cmp = Comparator.comparing(e -> rank.getOrDefault(e.getImportance(), 0));
                if (isAscending == null || !isAscending) {
                    cmp = cmp.reversed();
                }
                data.sort(cmp);
                return data;
            }
        }

        Sort sort = (isAscending != null && isAscending) ? Sort.by(property).ascending() : Sort.by(property).descending();
        PageRequest pageable = PageRequest.of(page.orElse(0), 10, sort);
        if (userId.isPresent()) {
            return repository.findAllByUserIdPageable(userId.get(), importance, pageable).getContent();
        }
        return repository.findAllPageable(importance, pageable).getContent();
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
