package ru.avito.service;

import org.springframework.stereotype.Service;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.ForbiddenException;

@Service
public class AccessService {

    public void checkAdEditAccess(User requester, Ad ad) {
        if (!isOwnerOrAdmin(requester, ad.getAuthor())) {
            throw new ForbiddenException("You cannot edit this ad");
        }
    }

    public void checkAdDeleteAccess(User requester, Ad ad) {
        if (!isOwnerOrAdmin(requester, ad.getAuthor())) {
            throw new ForbiddenException("You cannot delete this ad");
        }
    }

    public void checkAdImageAccess(User requester, Ad ad) {
        if (!isOwnerOrAdmin(requester, ad.getAuthor())) {
            throw new ForbiddenException("You cannot update image for this ad");
        }
    }

    public void checkCommentEditAccess(User requester, Comment comment) {
        if (!isOwnerOrAdmin(requester, comment.getAuthor())) {
            throw new ForbiddenException("You cannot edit this comment");
        }
    }

    public void checkCommentDeleteAccess(User requester, Comment comment) {
        if (!isOwnerOrAdmin(requester, comment.getAuthor())) {
            throw new ForbiddenException("You cannot delete this comment");
        }
    }

    public void checkUserAccess(User requester, User targetUser) {
        if (!isOwnerOrAdmin(requester, targetUser)) {
            throw new ForbiddenException("You cannot edit this user");
        }
    }

    private boolean isOwnerOrAdmin(User requester, User owner) {
        if (requester == null || owner == null || requester.getId() == null || owner.getId() == null) {
            return false;
        }

        return requester.getId().equals(owner.getId()) || requester.getRole() == Role.ADMIN;
    }
}