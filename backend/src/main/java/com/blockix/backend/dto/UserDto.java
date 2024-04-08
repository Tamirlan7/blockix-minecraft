package com.blockix.backend.dto;

import com.blockix.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Role role;
}
