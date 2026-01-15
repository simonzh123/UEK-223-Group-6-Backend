package com.example.demo.domain.customlist;

import com.example.demo.core.generic.AbstractRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ListEntryRepository extends AbstractRepository<ListEntry> {
    // TODO: Add filtering options
    List<ListEntry> findAllByUserId(UUID userId);
}