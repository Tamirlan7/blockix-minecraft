package com.blockix.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class DeleteArticleResponse {
    private Long articleId;
    private boolean deletedSuccessfully = true;
}
