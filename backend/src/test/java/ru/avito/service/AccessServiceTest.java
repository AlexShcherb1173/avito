package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.ForbiddenException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessServiceTest {

    private AccessService accessService;

    @BeforeEach
    void setUp() {
        accessService = new AccessService();
    }

    @Test
    void shouldAllowAdminToEditAnyAd() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdEditAccess(admin, ad));
    }

    @Test
    void shouldAllowOwnerToEditOwnAd() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdEditAccess(owner, ad));
    }

    @Test
    void shouldDenyUserToEditForeignAd() {
        User owner = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdEditAccess(foreignUser, ad));
    }

    @Test
    void shouldAllowAdminToDeleteAnyAd() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdDeleteAccess(admin, ad));
    }

    @Test
    void shouldAllowOwnerToDeleteOwnAd() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdDeleteAccess(owner, ad));
    }

    @Test
    void shouldDenyUserToDeleteForeignAd() {
        User owner = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdDeleteAccess(foreignUser, ad));
    }

    @Test
    void shouldAllowAdminToUpdateForeignAdImage() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdImageAccess(admin, ad));
    }

    @Test
    void shouldAllowOwnerToUpdateOwnAdImage() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkAdImageAccess(owner, ad));
    }

    @Test
    void shouldDenyUserToUpdateForeignAdImage() {
        User owner = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdImageAccess(foreignUser, ad));
    }

    @Test
    void shouldAllowAdminToEditAnyComment() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkCommentEditAccess(admin, comment));
    }

    @Test
    void shouldAllowOwnerToEditOwnComment() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkCommentEditAccess(owner, comment));
    }

    @Test
    void shouldDenyUserToEditForeignComment() {
        User owner = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkCommentEditAccess(foreignUser, comment));
    }

    @Test
    void shouldAllowAdminToDeleteAnyComment() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkCommentDeleteAccess(admin, comment));
    }

    @Test
    void shouldAllowOwnerToDeleteOwnComment() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertDoesNotThrow(() -> accessService.checkCommentDeleteAccess(owner, comment));
    }

    @Test
    void shouldDenyUserToDeleteForeignComment() {
        User owner = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();
        Comment comment = Comment.builder().id(20).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkCommentDeleteAccess(foreignUser, comment));
    }

    @Test
    void shouldAllowAdminToEditAnyUserProfile() {
        User admin = User.builder().id(1).role(Role.ADMIN).build();
        User target = User.builder().id(2).role(Role.USER).build();

        assertDoesNotThrow(() -> accessService.checkUserAccess(admin, target));
    }

    @Test
    void shouldAllowUserToEditOwnProfile() {
        User user = User.builder().id(1).role(Role.USER).build();

        assertDoesNotThrow(() -> accessService.checkUserAccess(user, user));
    }

    @Test
    void shouldDenyUserToEditForeignProfile() {
        User user = User.builder().id(1).role(Role.USER).build();
        User foreignUser = User.builder().id(2).role(Role.USER).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkUserAccess(user, foreignUser));
    }

    @Test
    void shouldDenyWhenRequesterIsNull() {
        User owner = User.builder().id(1).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdEditAccess(null, ad));
    }

    @Test
    void shouldDenyWhenOwnerIsNull() {
        User requester = User.builder().id(1).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(null).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdEditAccess(requester, ad));
    }

    @Test
    void shouldDenyWhenRequesterIdIsNull() {
        User requester = User.builder().id(null).role(Role.USER).build();
        User owner = User.builder().id(2).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdEditAccess(requester, ad));
    }

    @Test
    void shouldDenyWhenOwnerIdIsNull() {
        User requester = User.builder().id(1).role(Role.USER).build();
        User owner = User.builder().id(null).role(Role.USER).build();
        Ad ad = Ad.builder().id(10).author(owner).build();

        assertThrows(ForbiddenException.class, () -> accessService.checkAdEditAccess(requester, ad));
    }
}