import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Registration from "./Registration";

describe("components/registration/Registration", () => {
    test("should render registration form", () => {
        render(
            <MemoryRouter>
                <Registration handleRegistration={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.getByText("Добро пожаловать!")).toBeInTheDocument();
        expect(
            screen.getByRole("textbox", { name: "Логин (email) пользователя" })
        ).toBeInTheDocument();
        expect(screen.getByLabelText("Пароль")).toBeInTheDocument();
        expect(
            screen.getByRole("combobox", { name: "Роль" })
        ).toBeInTheDocument();
        expect(
            screen.getByRole("textbox", { name: "Имя" })
        ).toBeInTheDocument();
        expect(
            screen.getByRole("textbox", { name: "Фамилия" })
        ).toBeInTheDocument();
        expect(
            screen.getByRole("textbox", { name: "Телефон" })
        ).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /зарегистрироваться/i })
        ).toBeInTheDocument();
    });

    test("should call handleRegistration with entered values", () => {
        const handleRegistration = jest.fn();

        render(
            <MemoryRouter>
                <Registration handleRegistration={handleRegistration} />
            </MemoryRouter>
        );

        const emailInput = screen.getByRole("textbox", {
            name: "Логин (email) пользователя",
        });
        const passwordInput = screen.getByLabelText("Пароль");
        const roleSelect = screen.getByRole("combobox", {
            name: "Роль",
        });
        const firstNameInput = screen.getByRole("textbox", {
            name: "Имя",
        });
        const lastNameInput = screen.getByRole("textbox", {
            name: "Фамилия",
        });
        const phoneInput = screen.getByRole("textbox", {
            name: "Телефон",
        });
        const submitButton = screen.getByRole("button", {
            name: /зарегистрироваться/i,
        });

        fireEvent.change(emailInput, {
            target: { value: "user@example.com" },
        });
        fireEvent.change(passwordInput, {
            target: { value: "password123" },
        });
        fireEvent.change(roleSelect, {
            target: { value: "USER" },
        });
        fireEvent.change(firstNameInput, {
            target: { value: "Ivan" },
        });
        fireEvent.change(lastNameInput, {
            target: { value: "Ivanov" },
        });
        fireEvent.change(phoneInput, {
            target: { value: "+79990000001" },
        });

        fireEvent.click(submitButton);

        expect(handleRegistration).toHaveBeenCalledTimes(1);
        expect(handleRegistration).toHaveBeenCalledWith({
            username: "user@example.com",
            password: "password123",
            role: "USER",
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });
    });
});