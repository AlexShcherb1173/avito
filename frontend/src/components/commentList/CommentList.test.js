import React from "react";
import { render, screen } from "@testing-library/react";
import CommentList from "./CommentList";

jest.mock("../comment/Comment", () => {
    return function MockComment({ text, commentId, authorName }) {
        return <div>{commentId}: {authorName} - {text}</div>;
    };
});

describe("components/commentList/CommentList", () => {
    test("should render empty message when comments are empty", () => {
        render(
            <CommentList
                comments={[]}
                setComments={jest.fn()}
                user={1}
                adId={10}
                username="user@example.com"
                password="password123"
                role="USER"
                handleEditCommPopupOpen={jest.fn()}
                deleteComment={jest.fn()}
                onClose={jest.fn()}
                isComPopupOpen={false}
            />
        );

        expect(screen.getByText("Оставьте комментарий первым")).toBeInTheDocument();
    });

    test("should render empty message when comments is not array", () => {
        render(
            <CommentList
                comments={null}
                setComments={jest.fn()}
                user={1}
                adId={10}
                username="user@example.com"
                password="password123"
                role="USER"
                handleEditCommPopupOpen={jest.fn()}
                deleteComment={jest.fn()}
                onClose={jest.fn()}
                isComPopupOpen={false}
            />
        );

        expect(screen.getByText("Оставьте комментарий первым")).toBeInTheDocument();
    });

    test("should render normalized comments", () => {
        render(
            <CommentList
                comments={[
                    {
                        pk: 1,
                        text: "First",
                        authorFirstName: "Ivan",
                        author: 1,
                    },
                    {
                        id: 2,
                        text: "Second",
                        authorFirstName: "Petr",
                        author: 2,
                    },
                ]}
                setComments={jest.fn()}
                user={1}
                adId={10}
                username="user@example.com"
                password="password123"
                role="USER"
                handleEditCommPopupOpen={jest.fn()}
                deleteComment={jest.fn()}
                onClose={jest.fn()}
                isComPopupOpen={false}
            />
        );

        expect(screen.getByText("1: Ivan - First")).toBeInTheDocument();
        expect(screen.getByText("2: Petr - Second")).toBeInTheDocument();
    });
});