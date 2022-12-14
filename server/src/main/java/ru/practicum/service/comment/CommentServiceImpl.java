package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.comment.dto.CommentCreateDto;
import ru.practicum.service.comment.dto.CommentFullDto;
import ru.practicum.service.comment.dto.CommentMapper;
import ru.practicum.service.comment.dto.CommentUpdateDto;
import ru.practicum.service.exception.comments.CommentNotFoundException;
import ru.practicum.service.exception.comments.CommentNotValidUpdateDate;
import ru.practicum.service.exception.comments.UserNotAuthorOfCommentException;
import ru.practicum.service.exception.user.UserNotActivatedException;
import ru.practicum.service.user.User;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public CommentFullDto getCommentById(long commentId) {
        Comment comment = getCommentByIdFromRep(commentId);
        return commentMapper.toFullDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByEvent(long eventId, int from, int size) {
        PageRequest request = PageRequest.of(from, size);
        return commentRepository.findByEventId(eventId, request);
    }

    @Override
    @Transactional
    public CommentFullDto addCommentByCurrentUser(Long userId, CommentCreateDto commentCreateDto) {
        User user = userService.getUserById(userId);
        isActivated(user);

        Comment comment = commentMapper.toComment(commentCreateDto);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toFullDto(savedComment);
    }

    @Override
    @Transactional
    public CommentFullDto updateCommentByCurrentUser(Long userId, CommentUpdateDto commentUpdateDto) {
        User user = userService.getUserById(userId);
        Comment comment = getCommentByIdFromRep(commentUpdateDto.getId());

        isValidUpdateDate(comment.getCreatedOn());
        isAuthor(comment, user);
        isActivated(user);

        comment.setText(commentUpdateDto.getText());

        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toFullDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteCommentByCurrentUser(Long userId, Long commentId) {
        User user = userService.getUserById(userId);

        Comment comment = getCommentByIdFromRep(commentId);

        isAuthor(comment, user);

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private void isAuthor(Comment comment, User user) {
        boolean isAuthorOfComment = comment.getUser().equals(user);
        if (!isAuthorOfComment) {
            throw new UserNotAuthorOfCommentException(
                    String.format("User with id%s not author of comment with id:%s", user, comment.getId())
            );
        }
    }

    private void isActivated(User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException(String.format("User with id:%s not activated", user.getId()));
        }
    }

    private void isValidUpdateDate(LocalDateTime createdOn) {
        LocalDateTime now = LocalDateTime.now();
        boolean checkTime = createdOn.plusDays(3).isBefore(now);
        if (checkTime) {
            throw new CommentNotValidUpdateDate(String.format("Comment with creation date:%s forbidden to change", createdOn));
        }
    }

    private Comment getCommentByIdFromRep(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Comment with id:%s not found", commentId))
        );
    }
}