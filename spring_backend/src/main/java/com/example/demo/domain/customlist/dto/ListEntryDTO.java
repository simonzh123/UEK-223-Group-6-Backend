package com.example.demo.domain.customlist.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.customlist.ListEntry;
import com.example.demo.domain.user.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class ListEntryDTO extends AbstractDTO {
    @NotBlank(message = "Title can't be blank")
    @Size(min = 3, message = "Title must contain at least 3 characters")
    private String title;
    @NotBlank(message = "Text can't be blank")
    @Size(max = 500, message = "Text can contain 500 characters at max")
    private String text;
    @NotNull(message = "Importance must be set")
    private ListEntry.Importance importance;
    private UserDTO user;
    private LocalDateTime createdAt;
}
