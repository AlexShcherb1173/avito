import React, { useState } from "react";
import api from "../../utils/api";

import Buttons from "../buttons/Buttons";
import EditCommentPopup from "../editCommentPopup/EditCommentPopup";

function Comment({
                     text,
                     deleteComment1,
                     adId,
                     createdAt,
                     img,
                     commentId,
                     authorId: userId,
                     authorName,
                     currentUserId: user,
                     username,
                     password,
                     role,
                     setComments,
                 }) {
    const [isEdit, setEdit] = useState(false);

    const commentDate = createdAt ? new Date(createdAt) : null;

    const toggleEdit = () => setEdit((prev) => !prev);

    const handleEditComment = (data) => {
        api
            .editComment(adId, commentId, data, username, password)
            .then((updatedComment) => {
                if (typeof setComments === "function") {
                    setComments((prevComments) =>
                        prevComments.map((comment) =>
                            (comment.pk || comment.id) === commentId
                                ? { ...comment, ...updatedComment }
                                : comment
                        )
                    );
                }

                setEdit(false);
            })
            .catch((error) => console.log("error", error));
    };

    const onDelete = (e) => {
        e.preventDefault();
        deleteComment1(adId, commentId);
    };

    const imageSrc = img || "/src/images/greg-rakozy-oMpAz-DN-9I-unsplash.jpg";

    return (
        <li className="comment">
            <div className="comment-box">
                <img src={imageSrc} alt="user-img" className="comment-img" />

                <p className="comment-text comment__author-text">
                    {authorName || "Комментатор"}
                    <span>{commentDate ? commentDate.toLocaleString() : ""}</span>
                </p>
            </div>

            <div className="commentBox">
                <p className="comment-text comment-message">{text}</p>

                {user === userId || role === "ADMIN" ? (
                    <Buttons
                        className="comment-buttons"
                        classButton="comment-button"
                        onOpen={toggleEdit}
                        onSubmit={onDelete}
                    />
                ) : null}

                <EditCommentPopup
                    onClose={toggleEdit}
                    isOpen={isEdit}
                    id={adId}
                    commentText={text}
                    handleEdit={handleEditComment}
                    userId={user}
                    commentUserId={userId}
                    commentId={commentId}
                    currentComId={commentId}
                />
            </div>
        </li>
    );
}

export default Comment;