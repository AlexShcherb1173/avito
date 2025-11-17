package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.MyUserDetailsManager;

import java.util.List;

/**
 * Service for dealing with comments (CRUD operations)
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MyUserDetailsManager myUserDetailsManager;
    private final CommentMapper commentMapper;
    private final AdServiceImpl adServiceImpl;

    public CommentServiceImpl(CommentRepository commentRepository, MyUserDetailsManager myUserDetailsManager, CommentMapper commentMapper, AdServiceImpl adServiceImpl) {
        this.commentRepository = commentRepository;
        this.myUserDetailsManager = myUserDetailsManager;
        this.commentMapper = commentMapper;
        this.adServiceImpl = adServiceImpl;
    }

    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    /**
     * Method for getting all the comments
     *
     * @param id ad id
     * @return returns CommentsDto with counter and List of CommentDto
     */
    @Override
    public CommentsDto getAllCommentByAdsId(Integer id) {
        logger.info("Method for getting all the comments was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        List<CommentDto> thisAdCommentsDto = commentMapper.CommentListToCommentDtoList(commentRepository.findAllCommentByAdsId(id));
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(thisAdCommentsDto.size());
        commentsDto.setResults(thisAdCommentsDto);
        logger.info("Method for getting all the comments was finished");
        return commentsDto;
    }

    /**
     * Method for adding a new comment
     *
     * @param id                       ad id
     * @param createOrUpdateCommentDto with comment info
     * @return returns CommentDto
     */
    @Override
    public CommentDto addComment(Integer id, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        logger.info("Method for adding a new comment was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        Comment comment = commentMapper.CreateOrUpdateCommentDtoToCommentEntity(createOrUpdateCommentDto);
        logger.info("Comment {} was created", comment.getId());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAd(adServiceImpl.findAdById(id));
        comment.setAuthor(myUserDetailsManager.getCurrentUser());
        logger.info("Comment {} was pinned to the user {}", comment.getId(), myUserDetailsManager.getCurrentUser().getId());
        commentRepository.save(comment);
        logger.info("Comment {} was saved to the repository", comment.getId());
        return commentMapper.CommentToCommentDto(comment);
    }

    /**
     * Method for removing a comment
     *
     * @param adId      ad id
     * @param commentId comment id
     */
    @Override
    public void removeComment(Integer adId, Integer commentId) {
        logger.info("Method for removing a comment was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        if (!findComment(commentId).getAuthor().equals(myUserDetailsManager.getCurrentUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        commentRepository.deleteById(commentId);
        logger.info("Comment {} was removed", commentId);
    }

    /**
     * Method for updating comment
     *
     * @param adId                     ad id
     * @param commentId                comment id
     * @param createOrUpdateCommentDto with comment info to be updated
     * @return returns updated CommentDto
     */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        logger.info("Method for updating comment was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        if (!findComment(commentId).getAuthor().equals(myUserDetailsManager.getCurrentUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setText(createOrUpdateCommentDto.getText());
        commentRepository.save(comment);
        logger.info("Comment {} was updated", commentId);
        return commentMapper.CommentToCommentDto(comment);
    }

    /**
     * Sub-method for finding comment by id
     *
     * @param id comment id
     * @return returns Comment
     */
    @Override
    public Comment findComment(Integer id) {
        logger.info("Sub-method for finding comment by id was invoked");
        return commentRepository.findById(id).orElseThrow();
    }
}
