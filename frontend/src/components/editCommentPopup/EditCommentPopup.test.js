import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import EditCommentPopup from "./EditCommentPopup";
import useFormValidation from "../../utils/hooks/useFormValidation";

jest.mock("../../utils/hooks/useFormValidation", () => ({
    __esModule: true,
    default: jest.fn(),
}));

jest.mock("../userForm/UserForm", () => {
    return function MockUserForm({
                                     title,
                                     buttonText,
                                     onSubmit,
                                     children,
                                     errors,
                                     disabled,
                                     id,
                                 }) {
        return (
            <div
                data-testid="mock-user-form"
                data-errors={String(errors)}
                data-disabled={String(disabled)}
                data-id={String(id)}
            >
                <h1>{title}</h1>
                <form onSubmit={onSubmit}>
                    {children}
                    <button type="submit" disabled={disabled}>
                        {buttonText}
                    </button>
                </form>
            </div>
        );
    };
});

describe("components/editCommentPopup/EditCommentPopup", () => {
    const mockHandleChange = jest.fn();

    const getDefaultHookState = () => ({
        values: { text: "Updated comment text" },
        handleChange: mockHandleChange,
        errors: {},
        isValid: true,
    });

    beforeEach(() => {
        jest.clearAllMocks();
        useFormValidation.mockReturnValue(getDefaultHookState());
    });

    test("should render closed popup by default", () => {
        const { container } = render(
            <EditCommentPopup
                isOpen={false}
                onClose={jest.fn()}
                commentText="Old comment"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        expect(container.querySelector(".popup")).not.toHaveClass("popup_is-opened");
    });

    test("should render opened popup", () => {
        const { container } = render(
            <EditCommentPopup
                isOpen={true}
                onClose={jest.fn()}
                commentText="Old comment"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        expect(container.querySelector(".popup")).toHaveClass("popup_is-opened");
        expect(screen.getByRole("heading", { name: "Изменить" })).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Изменить" })).toBeInTheDocument();
    });

    test("should call onClose when close button clicked", () => {
        const onClose = jest.fn();

        const { container } = render(
            <EditCommentPopup
                isOpen={true}
                onClose={onClose}
                commentText="Old comment"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        fireEvent.click(container.querySelector(".close-button"));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should submit edited comment and close popup", () => {
        const handleEdit = jest.fn();
        const onClose = jest.fn();

        render(
            <EditCommentPopup
                isOpen={true}
                onClose={onClose}
                commentText="Old comment"
                id={1}
                handleEdit={handleEdit}
            />
        );

        fireEvent.click(screen.getByRole("button", { name: "Изменить" }));

        expect(handleEdit).toHaveBeenCalledWith({
            text: "Updated comment text",
        });
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should call handleChange when input changes", () => {
        render(
            <EditCommentPopup
                isOpen={true}
                onClose={jest.fn()}
                commentText="Old comment"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        fireEvent.change(screen.getByDisplayValue("Updated comment text"), {
            target: { name: "text", value: "Another comment text" },
        });

        expect(mockHandleChange).toHaveBeenCalledTimes(1);
    });

    test("should render comment placeholder", () => {
        render(
            <EditCommentPopup
                isOpen={true}
                onClose={jest.fn()}
                commentText="Старый комментарий"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        expect(
            screen.getByPlaceholderText("Старый комментарий")
        ).toBeInTheDocument();
    });

    test("should pass disabled=true when form is invalid", () => {
        useFormValidation.mockReturnValue({
            values: { text: "" },
            handleChange: mockHandleChange,
            errors: { text: "Минимум 8 символов" },
            isValid: false,
        });

        render(
            <EditCommentPopup
                isOpen={true}
                onClose={jest.fn()}
                commentText="Old comment"
                id={1}
                handleEdit={jest.fn()}
            />
        );

        expect(screen.getByText("Минимум 8 символов")).toBeInTheDocument();
        expect(screen.getByTestId("mock-user-form")).toHaveAttribute(
            "data-errors",
            "true"
        );
        expect(screen.getByTestId("mock-user-form")).toHaveAttribute(
            "data-disabled",
            "true"
        );
        expect(screen.getByRole("button", { name: "Изменить" })).toBeDisabled();
    });

    test("should pass id into UserForm", () => {
        render(
            <EditCommentPopup
                isOpen={true}
                onClose={jest.fn()}
                commentText="Old comment"
                id={99}
                handleEdit={jest.fn()}
            />
        );

        expect(screen.getByTestId("mock-user-form")).toHaveAttribute(
            "data-id",
            "99"
        );
    });
});