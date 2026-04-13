import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import EditAdPopup from "./EditAdPopup";
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
                                     id,
                                 }) {
        return (
            <div
                data-testid="mock-user-form"
                data-errors={String(errors)}
                data-id={String(id)}
            >
                <h1>{title}</h1>
                <form onSubmit={onSubmit}>
                    {children}
                    <button type="submit">{buttonText}</button>
                </form>
            </div>
        );
    };
});

describe("components/editAdPopup/EditAdPopup", () => {
    const mockHandleChange = jest.fn();
    const mockSetValues = jest.fn();

    const getDefaultHookState = () => ({
        values: {
            title: "Old title",
            price: "1000",
            description: "Old description",
        },
        handleChange: mockHandleChange,
        errors: {},
        isValid: true,
        setValues: mockSetValues,
    });

    beforeEach(() => {
        jest.clearAllMocks();
        useFormValidation.mockReturnValue(getDefaultHookState());
    });

    test("should render opened popup", () => {
        const { container } = render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        expect(container.querySelector(".popup")).toHaveClass("popup_is-opened");
        expect(screen.getByText("Изменить товар")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Изменить" })).toBeInTheDocument();
    });

    test("should render closed popup when isEditPopupOpen is false", () => {
        const { container } = render(
            <EditAdPopup
                isEditPopupOpen={false}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        expect(container.querySelector(".popup")).not.toHaveClass("popup_is-opened");
    });

    test("should call onClose when close button is clicked", () => {
        const onClose = jest.fn();

        const { container } = render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={onClose}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        fireEvent.click(container.querySelector(".close-button"));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should submit edited ad", () => {
        const handleEditAdd = jest.fn();

        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={handleEditAdd}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        fireEvent.click(screen.getByRole("button", { name: "Изменить" }));

        expect(handleEditAdd).toHaveBeenCalledWith({
            title: "Old title",
            price: "1000",
            description: "Old description",
        });
    });

    test("should call setValues on mount with ad data", () => {
        const ad = {
            title: "Phone",
            price: 5000,
            description: "Nice phone",
        };

        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={10}
                ad={ad}
            />
        );

        expect(mockSetValues).toHaveBeenCalledWith(ad);
    });

    test("should call handleChange when title input changes", () => {
        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        fireEvent.change(screen.getByDisplayValue("Old title"), {
            target: { name: "title", value: "New title" },
        });

        expect(mockHandleChange).toHaveBeenCalledTimes(1);
    });

    test("should call handleChange when price input changes", () => {
        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        fireEvent.change(screen.getByDisplayValue("1000"), {
            target: { name: "price", value: "2000" },
        });

        expect(mockHandleChange).toHaveBeenCalledTimes(1);
    });

    test("should call handleChange when description input changes", () => {
        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{ title: "A", price: 1, description: "B" }}
            />
        );

        fireEvent.change(screen.getByDisplayValue("Old description"), {
            target: { name: "description", value: "New description" },
        });

        expect(mockHandleChange).toHaveBeenCalledTimes(1);
    });

    test("should render validation errors", () => {
        useFormValidation.mockReturnValue({
            values: {
                title: "",
                price: "",
                description: "",
            },
            handleChange: mockHandleChange,
            errors: {
                title: "Введите название",
                price: "Введите цену",
                description: "Введите описание",
            },
            isValid: false,
            setValues: mockSetValues,
        });

        render(
            <EditAdPopup
                isEditPopupOpen={true}
                onClose={jest.fn()}
                handleEditAdd={jest.fn()}
                id={1}
                ad={{}}
            />
        );

        expect(screen.getByText("Введите название")).toBeInTheDocument();
        expect(screen.getByText("Введите цену")).toBeInTheDocument();
        expect(screen.getByText("Введите описание")).toBeInTheDocument();
        expect(screen.getByTestId("mock-user-form")).toHaveAttribute(
            "data-errors",
            "true"
        );
    });
});