import React from "react";
import Comment from "../comment/Comment";

function CommentList({
                         comments,
                         setComments,
                         user,
                         adId,
                         username,
                         password,
                         role,
                         handleEditCommPopupOpen,
                         deleteComment,
                         onClose,
                         isComPopupOpen,
                     }) {
    const normalizedComments = Array.isArray(comments) ? comments : [];

    return (
        <>
            {!normalizedComments.length ? (
                <p className="comment-text">Оставьте комментарий первым</p>
            ) : (
                <ul className="comment-list">
                    {normalizedComments.map((comment) => (
                        <Comment
                            isComPopupOpen={isComPopupOpen}
                            key={comment.pk || comment.id}
                            text={comment.text}
                            deleteComment1={deleteComment}
                            adId={adId}
                            img={comment.authorImage}
                            authorName={comment.authorFirstName}
                            commentId={comment.pk || comment.id}
                            authorId={comment.author}
                            createdAt={comment.createdAt}
                            setComments={setComments}
                            handleEditCommPopupOpen={handleEditCommPopupOpen}
                            currentUserId={user}
                            username={username}
                            password={password}
                            role={role}
                            onClose={onClose}
                        />
                    ))}
                </ul>
            )}
        </>
    );
}

export default CommentList;