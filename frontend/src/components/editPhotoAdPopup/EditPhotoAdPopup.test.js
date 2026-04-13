import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import EditPhotoAdPopup from "./EditPhotoAdPopup";

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

describe("components/editPhotoAdPopup/EditPhotoAdPopup", () => {
    let setTimeoutSpy;

    beforeEach(() => {
        setTimeoutSpy = jest.spyOn(global, "setTimeout");
    });

    afterEach(() => {
        jest.restoreAllMocks();
    });

    test("should render opened popup", () => {
        const { container } = render(
            <EditPhotoAdPopup
                isOpen={true}
                onClose={jest.fn()}
                handleEdit={jest.fn()}
                id={1}
            />
        );

        expect(container.querySelector(".popup")).toHaveClass("popup_is-opened");
    });

    test("should call onClose when close button clicked", () => {
        const onClose = jest.fn();
        const { container } = render(
            <EditPhotoAdPopup
                isOpen={true}
                onClose={onClose}
                handleEdit={jest.fn()}
                id={1}
            />
        );

        fireEvent.click(container.querySelector(".close-button"));
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should submit selected image and schedule reload", () => {
        const handleEdit = jest.fn();

        render(
            <EditPhotoAdPopup
                isOpen={true}
                onClose={jest.fn()}
                handleEdit={handleEdit}
                id={1}
            />
        );

        const file = new File(["photo"], "photo.jpg", { type: "image/jpeg" });
        const fileInput = document.querySelector('input[type="file"]');

        fireEvent.change(fileInput, {
            target: { files: [file] },
        });

        fireEvent.click(screen.getByRole("button", { name: "Изменить" }));

        expect(handleEdit).toHaveBeenCalledWith(file);
        expect(setTimeoutSpy).toHaveBeenCalledWith(expect.any(Function), 700);
    });
});