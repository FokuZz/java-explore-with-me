package ru.practicum.explore.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.model.CommentStateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentAdminDto {
    @NotBlank
    @Size(min = 10, max = 500)
    private String info;
    @NotNull
    private CommentStateAction state;
}