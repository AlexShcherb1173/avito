import React from "react";
import useFormValidation from "../../utils/hooks/useFormValidation";

function CommentForm({ addComment }) {
    const { values, handleChange, errors, isValid, resetForm } = useFormValidation();

    function handleAddComment(e) {
        e.preventDefault();

        if (!isValid) {
            return;
        }

        addComment({
            text: values.text,
        });

        if (typeof resetForm === "function") {
            resetForm();
        }
    }

    return (
        <form className="comment__form" onSubmit={handleAddComment}>
            <label className="comment-label">
                <h2 className="comment__form-title">Оставьте комментарий</h2>
                <input
                    value={values.text || ""}
                    name="text"
                    className="comment__input"
                    minLength="8"
                    maxLength="64"
                    required
                    onChange={handleChange}
                />
                <div className={`input-hidden ${errors.text ? "input-error" : ""}`}>
                    {errors.text}
                </div>
            </label>

            <button
                className={`comment__button comment__button-text ${
                    !isValid ? "comment__button_disabled" : ""
                }`}
                disabled={!isValid}
                type="submit"
            >
                Отправить
            </button>

            <div className="input-error input-hidden"></div>
        </form>
    );
}

export default CommentForm;