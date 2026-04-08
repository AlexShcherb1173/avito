import React from "react";
import { render, screen } from "@testing-library/react";
import CommentContainer from "./CommentContainer";

jest.mock("../commentList/CommentList", () => {
    return function MockCommentList(props) {
        return <div>CommentList: {props.comments.length}</div>;
    };
});

jest.mock(
    "../comentForm/CommentForm",
    () => {
        return function MockCommentForm() {
            return <div>CommentForm</div>;
        };
    },
    { virtual: true }
);

describe("components/commentContainer/CommentContainer", () => {
    test("should render title, comment list and form", () => {
        render(
            <CommentContainer
                comments={[{ id: 1 }, { id: 2 }]}
                addComment={jest.fn()}
                deleteComment={jest.fn()}
                setComments={jest.fn()}
                user={1}
                isComPopupOpen={false}
                handleEditCommPopupOpen={jest.fn()}
                username="user@example.com"
                password="password123"
                role="USER"
                adId={10}
                onClose={jest.fn()}
            />
        );

        expect(screen.getByText("Комментарии")).toBeInTheDocument();
        expect(screen.getByText("CommentList: 2")).toBeInTheDocument();
        expect(screen.getByText("CommentForm")).toBeInTheDocument();
    });
});