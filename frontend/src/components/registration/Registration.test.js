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
        expect(screen.getByText(/логин/i)).toBeInTheDocument();
        expect(screen.getByText("Пароль")).toBeInTheDocument();
        expect(screen.getByText("Роль")).toBeInTheDocument();
        expect(screen.getByText("Имя")).toBeInTheDocument();
        expect(screen.getByText("Фамилия")).toBeInTheDocument();
        expect(screen.getByText("Телефон")).toBeInTheDocument();
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
            name: "",
        });

        const passwordInput = document.querySelector('input[name="password"]');
        const roleSelect = document.querySelector('select[name="role"]');
        const firstNameInput = document.querySelector('input[name="firstName"]');
        const lastNameInput = document.querySelector('input[name="lastName"]');
        const phoneInput = document.querySelector('input[name="phone"]');
        const submitButton = screen.getByRole("button", {
            name: /зарегистрироваться/i,
        });

        fireEvent.change(emailInput, {
            target: { name: "username", value: "user@example.com" },
        });
        fireEvent.change(passwordInput, {
            target: { name: "password", value: "password123" },
        });
        fireEvent.change(roleSelect, {
            target: { name: "role", value: "USER" },
        });
        fireEvent.change(firstNameInput, {
            target: { name: "firstName", value: "Ivan" },
        });
        fireEvent.change(lastNameInput, {
            target: { name: "lastName", value: "Ivanov" },
        });
        fireEvent.change(phoneInput, {
            target: { name: "phone", value: "+79990000001" },
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