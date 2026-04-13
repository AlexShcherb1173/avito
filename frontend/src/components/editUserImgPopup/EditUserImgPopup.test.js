import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import EditUserImgPopup from "./EditUserImgPopup";

jest.mock("../../utils/hooks/useFormValidation", () => {
    return () => ({
        handleChange: jest.fn(),
        errors: {},
        isValid: true,
    });
});

jest.mock("../userForm/UserForm", () => {
    return function MockUserForm({ title, buttonText, onSubmit, children }) {
        return (
            <div>
                <h1>{title}</h1>
                <form onSubmit={onSubmit}>
                    {children}
                    <button type="submit">{buttonText}</button>
                </form>
            </div>
        );
    };
});

describe("components/editUserImgPopup/EditUserImgPopup", () => {
    let setTimeoutSpy;

    beforeEach(() => {
        setTimeoutSpy = jest.spyOn(global, "setTimeout");
    });

    afterEach(() => {
        jest.restoreAllMocks();
    });

    test("should render opened popup", () => {
        const { container } = render(
            <EditUserImgPopup
                isOpen={true}
                onClose={jest.fn()}
                editUserPhoto={jest.fn()}
            />
        );

        expect(container.querySelector(".popup")).toHaveClass("popup_is-opened");
    });

    test("should call onClose when close button clicked", () => {
        const onClose = jest.fn();
        const { container } = render(
            <EditUserImgPopup
                isOpen={true}
                onClose={onClose}
                editUserPhoto={jest.fn()}
            />
        );

        fireEvent.click(container.querySelector(".close-button"));
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should submit selected image and schedule reload", () => {
        const editUserPhoto = jest.fn();

        render(
            <EditUserImgPopup
                isOpen={true}
                onClose={jest.fn()}
                editUserPhoto={editUserPhoto}
            />
        );

        const file = new File(["photo"], "avatar.jpg", { type: "image/jpeg" });
        const fileInput = document.querySelector('input[type="file"]');

        fireEvent.change(fileInput, {
            target: { files: [file] },
        });

        fireEvent.click(screen.getByRole("button", { name: "Изменить" }));

        expect(editUserPhoto).toHaveBeenCalledWith(file);
        expect(setTimeoutSpy).toHaveBeenCalledWith(expect.any(Function), 500);
    });
});