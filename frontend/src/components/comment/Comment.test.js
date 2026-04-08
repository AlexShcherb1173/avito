import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Comment from "./Comment";
import api from "../../utils/api";

jest.mock("../../utils/api", () => ({
    __esModule: true,
    default: {
        editComment: jest.fn(),
    },
}));

jest.mock("../buttons/Buttons", () => {
    return function MockButtons({ onOpen, onSubmit }) {
        return (
            <div>
                <button type="button" onClick={onOpen}>Open edit</button>
                <button type="button" onClick={onSubmit}>Delete comment</button>
            </div>
        );
    };
});

jest.mock("../editCommentPopup/EditCommentPopup", () => {
    return function MockEditCommentPopup({ isOpen, handleEdit, onClose }) {
        return (
            <div>
                <span>Popup: {String(isOpen)}</span>
                <button
                    type="button"
                    onClick={() => handleEdit({ text: "Updated text" })}
                >
                    Save edited comment
                </button>
                <button type="button" onClick={onClose}>
                    Close popup
                </button>
            </div>
        );
    };
});

describe("components/comment/Comment", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("should render author, text and fallback image", () => {
        render(
            <Comment
                text="Hello world"
                deleteComment1={jest.fn()}
                adId={10}
                createdAt="2026-01-01T10:00:00.000Z"
                commentId={5}
                authorId={2}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="USER"
                setComments={jest.fn()}
            />
        );

        expect(screen.getByText("Ivan")).toBeInTheDocument();
        expect(screen.getByText("Hello world")).toBeInTheDocument();
        expect(screen.getByAltText("user-img")).toBeInTheDocument();
    });

    test("should render action buttons for comment owner", () => {
        render(
            <Comment
                text="Hello world"
                deleteComment1={jest.fn()}
                adId={10}
                commentId={5}
                authorId={1}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="USER"
                setComments={jest.fn()}
            />
        );

        expect(screen.getByText("Open edit")).toBeInTheDocument();
        expect(screen.getByText("Delete comment")).toBeInTheDocument();
    });

    test("should render action buttons for admin", () => {
        render(
            <Comment
                text="Hello world"
                deleteComment1={jest.fn()}
                adId={10}
                commentId={5}
                authorId={2}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="ADMIN"
                setComments={jest.fn()}
            />
        );

        expect(screen.getByText("Open edit")).toBeInTheDocument();
        expect(screen.getByText("Delete comment")).toBeInTheDocument();
    });

    test("should not render action buttons for foreign non-admin user", () => {
        render(
            <Comment
                text="Hello world"
                deleteComment1={jest.fn()}
                adId={10}
                commentId={5}
                authorId={2}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="USER"
                setComments={jest.fn()}
            />
        );

        expect(screen.queryByText("Open edit")).not.toBeInTheDocument();
        expect(screen.queryByText("Delete comment")).not.toBeInTheDocument();
    });

    test("should call deleteComment1 on delete", () => {
        const deleteComment1 = jest.fn();

        render(
            <Comment
                text="Hello world"
                deleteComment1={deleteComment1}
                adId={10}
                commentId={5}
                authorId={1}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="USER"
                setComments={jest.fn()}
            />
        );

        fireEvent.click(screen.getByText("Delete comment"));

        expect(deleteComment1).toHaveBeenCalledWith(10, 5);
    });

    test("should toggle popup and edit comment through api", async () => {
        api.editComment.mockResolvedValue({ text: "Updated text" });
        const setComments = jest.fn((updater) =>
            updater([{ pk: 5, text: "Old text" }, { pk: 6, text: "Another" }])
        );

        render(
            <Comment
                text="Hello world"
                deleteComment1={jest.fn()}
                adId={10}
                commentId={5}
                authorId={1}
                authorName="Ivan"
                currentUserId={1}
                username="user@example.com"
                password="password123"
                role="USER"
                setComments={setComments}
            />
        );

        fireEvent.click(screen.getByText("Open edit"));
        expect(screen.getByText("Popup: true")).toBeInTheDocument();

        fireEvent.click(screen.getByText("Save edited comment"));

        await waitFor(() => {
            expect(api.editComment).toHaveBeenCalledWith(
                10,
                5,
                { text: "Updated text" },
                "user@example.com",
                "password123"
            );
        });

        expect(setComments).toHaveBeenCalled();
    });
});