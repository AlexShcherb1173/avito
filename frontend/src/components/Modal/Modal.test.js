import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Modal from "./Modal";

describe("components/Modal/Modal", () => {
    test("should not render when isVisible is false", () => {
        render(
            <Modal
                isVisible={false}
                closeModal={jest.fn()}
                updatePassword={jest.fn()}
            />
        );

        expect(screen.queryByText("Сменить пароль")).not.toBeInTheDocument();
    });

    test("should render when isVisible is true", () => {
        render(
            <Modal
                isVisible={true}
                closeModal={jest.fn()}
                updatePassword={jest.fn()}
            />
        );

        // берем заголовок, а не кнопку
        expect(screen.getByRole("heading", { name: "Сменить пароль" }))
            .toBeInTheDocument();

        expect(
            screen.getByPlaceholderText("Введите новый пароль")
        ).toBeInTheDocument();

        expect(screen.getByText("Отмена")).toBeInTheDocument();
    });

    test("should update input value on change", () => {
        render(
            <Modal
                isVisible={true}
                closeModal={jest.fn()}
                updatePassword={jest.fn()}
            />
        );

        const input = screen.getByPlaceholderText("Введите новый пароль");

        fireEvent.change(input, {
            target: { value: "newStrongPass1" },
        });

        expect(input).toHaveValue("newStrongPass1");
    });

    test("should call updatePassword with entered password", () => {
        const updatePassword = jest.fn();

        render(
            <Modal
                isVisible={true}
                closeModal={jest.fn()}
                updatePassword={updatePassword}
            />
        );

        const input = screen.getByPlaceholderText("Введите новый пароль");

        fireEvent.change(input, {
            target: { value: "newStrongPass1" },
        });

        // берем кнопку точнее
        const submitButton = screen.getByRole("button", {
            name: "Сменить пароль",
        });

        fireEvent.click(submitButton);

        expect(updatePassword).toHaveBeenCalledWith("newStrongPass1");
        expect(updatePassword).toHaveBeenCalledTimes(1);
    });

    test("should call closeModal when cancel button is clicked", () => {
        const closeModal = jest.fn();

        render(
            <Modal
                isVisible={true}
                closeModal={closeModal}
                updatePassword={jest.fn()}
            />
        );

        const cancelButton = screen.getByRole("button", {
            name: "Отмена",
        });

        fireEvent.click(cancelButton);

        expect(closeModal).toHaveBeenCalledTimes(1);
    });
});