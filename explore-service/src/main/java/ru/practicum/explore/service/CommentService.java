package ru.practicum.explore.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.comment.UpdateCommentAdminDto;
import ru.practicum.explore.dto.comment.UpdateCommentUserDto;
import ru.practicum.explore.exception.exceptions.BadRequestException;
import ru.practicum.explore.exception.exceptions.ForbiddenException;
import ru.practicum.explore.exception.exceptions.UserNotFoundException;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.model.*;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User author = getUserById(userId);
        Event event = getEventById(eventId);
        Comment comment = CommentMapper.toComment(newCommentDto, author, event);
        log.info("Create new comment: {}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateCommentByUser(UpdateCommentUserDto updateCommentUserDto, Long userId, Long commentId) {
        log.info("Update comment = {}", updateCommentUserDto);
        User user = getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Редактирование комментария доступно только автору комментария.");
        }
        if (updateCommentUserDto.getText() != null) {
            comment.setText(updateCommentUserDto.getText());
        }
        changeStatusComment(comment, updateCommentUserDto.getState());
        log.info("Updated comment = {}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public CommentDto getCommentsByIdByUser(Long userId, Long commentId) {
        log.info("Get comment with user, comment id ({},{})",userId, commentId);
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Полная информация о комментарии доступна только автору.");
        }
        return CommentMapper.toCommentDto(comment);
    }

    public void deleteCommentByUser(Long userId, Long commentId) {
        log.info("Delete comment with user, comment id ({},{})",userId, commentId);
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Удалить комментарий может только автор.");
        }
        commentRepository.delete(comment);
    }

    public CommentDto updateCommentByAdmin(Long commentId, UpdateCommentAdminDto updateCommentAdminDto) {
        log.info("Update comment Admin = {}", updateCommentAdminDto);
        Comment comment = getCommentById(commentId);
        if (updateCommentAdminDto.getText() != null) {
            comment.setText(updateCommentAdminDto.getText());
        }
        changeStatusComment(comment, updateCommentAdminDto.getState());
        log.info("Updated comment Admin = {}", comment);
        return CommentMapper.toCommentDto(comment);
    }

    public void deleteCommentByAdmin(Long commentId) {
        log.info("Delete comment Admin with CommentId = {}",commentId);
        getCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getAll(Long eventId, Pageable pageable) {
        log.info("GetAll with eventId = {}",eventId);
        getEventById(eventId);
        List<Comment> comments = commentRepository.findCommentsByEventId(eventId, pageable);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllByAdmin(Long eventId, PageRequest pageable) {
        log.info("GetAll Admin with eventId = {}",eventId);
        getEventById(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Комментарий с id=%d не найден.", commentId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id=%d не найден.", userId)));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Мероприятие с id=%d не найдено.", eventId)));
    }

    private void changeStatusComment(Comment comment, CommentStateAction state) {
        switch (state) {
            case CANCEL_REVIEW:
                comment.setStatus(CommentStatus.CANCELED);
                break;
            case SEND_TO_REVIEW:
                comment.setStatus(CommentStatus.PENDING);
                break;
            case PUBLISH_COMMENT:
                comment.setStatus(CommentStatus.PUBLISHED);
                break;
            case REJECT_COMMENT:
                comment.setStatus(CommentStatus.CANCELED);
                break;
            default:
                throw new BadRequestException("Неизвестное состояние.");
        }
    }
}
