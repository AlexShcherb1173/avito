import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import CommentForm from "./CommentForm";

describe("components/commentForm/CommentForm", () => {
    test("should render without crashing", () => {
        render(
            <CommentForm
                addComment={jest.fn()}
                isLoading={false}
            />
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should render textarea and submit button if present", () => {
        render(
            <CommentForm
                addComment={jest.fn()}
                isLoading={false}
            />
        );

        const textarea = screen.queryByRole("textbox");
        const button = screen.queryByRole("button");

        expect(textarea).toBeInTheDocument();
        expect(button).toBeInTheDocument();
    });

    test("should call addComment on submit", () => {
        const addComment = jest.fn();

        render(
            <CommentForm
                addComment={addComment}
                isLoading={false}
            />
        );

        const textarea = screen.getByRole("textbox");
        const button = screen.getByRole("button");

        fireEvent.change(textarea, {
            target: { value: "Test comment" },
        });

        fireEvent.click(button);

        expect(addComment).toHaveBeenCalledWith({
            text: "Test comment",
        });
    });
});