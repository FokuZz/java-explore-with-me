package ru.practicum.explore.dto.comment;

import lombok.*;
import ru.practicum.explore.model.CommentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
    private CommentStatus status;
}
