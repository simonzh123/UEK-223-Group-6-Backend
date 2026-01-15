package com.example.demo.domain.customlist.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.customlist.ListEntry;
import com.example.demo.domain.user.dto.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface ListEntryMapper extends AbstractMapper<ListEntry, ListEntryDTO> {
}
