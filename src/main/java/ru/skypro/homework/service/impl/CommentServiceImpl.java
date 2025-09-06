package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;

@Service
public class CommentServiceImpl implements CommentService {
    @Override public Comments getComments(int adId) {
        Comments c = new Comments();
        c.setCount(0);
        c.setResults(Collections.emptyList());
        return c;
    }
    @Override public Comment addComment(int adId, CreateOrUpdateComment dto) { return new Comment(); }
    @Override public void deleteComment(int adId, int commentId) { }
    @Override public Comment updateComment(int adId, int commentId, CreateOrUpdateComment dto) { return new Comment(); }
}
