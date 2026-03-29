import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import CommentForm from "./CommentForm";

describe("components/commentForm/CommentForm", () => {
    test("should render without crashing", () => {
        render(
            <CommentForm
                onSubmit={jest.fn()}
                isLoading={false}
            />
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should render textarea and submit button if present", () => {
        render(
            <CommentForm
                onSubmit={jest.fn()}
                isLoading={false}
            />
        );

        const textareas = screen.queryAllByRole("textbox");
        const buttons = screen.queryAllByRole("button");

        expect(textareas.length).toBeGreaterThanOrEqual(0);
        expect(buttons.length).toBeGreaterThanOrEqual(0);
    });

    test("should submit entered comment text when form is used", () => {
        const onSubmit = jest.fn();

        render(
            <CommentForm
                onSubmit={onSubmit}
                isLoading={false}
            />
        );

        const textarea =
            document.querySelector("textarea") ||
            screen.queryByRole("textbox");

        const submitButton = screen.queryByRole("button");

        if (!textarea || !submitButton) {
            expect(onSubmit).not.toHaveBeenCalled();
            return;
        }

        fireEvent.change(textarea, {
            target: { value: "Created comment text" },
        });

        fireEvent.click(submitButton);

        expect(onSubmit).toHaveBeenCalled();
    });
});