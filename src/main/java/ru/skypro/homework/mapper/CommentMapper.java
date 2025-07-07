package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.CommentModel;

import java.util.List;

@Component
public class CommentMapper extends MapperAbstract {
    public Comment makeComment(CommentModel commentModel) {
        Comment result = new Comment();
        result.setPk(commentModel.getId());
        result.setAuthor(commentModel.getAuthor().getId());
        result.setText(commentModel.getText());
        result.setAuthorFirstName(commentModel.getAuthor().getFirstName());
        result.setAuthorImage(getFullImageUrl(commentModel.getAuthor().getImage()));
        result.setCreatedAt(commentModel.getCreatedAt().getTime());

        return result;
    }

    public Comments makeComments(List<CommentModel> commentModels) {
        Comments comments = new Comments();

        for (CommentModel commentModel : commentModels) {
            comments.addComment(makeComment(commentModel));
        }

        return comments;
    }
}
