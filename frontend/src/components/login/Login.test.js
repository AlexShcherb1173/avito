import React from "react";
import { screen, fireEvent } from "@testing-library/react";
import Login from "./Login";
import { renderWithProviders } from "../../test-utils/renderWithProviders";

jest.mock("../form/Form", () => {
    return function MockForm(props) {
        return (
            <div>
                <h1>{props.header}</h1>
                <form onSubmit={props.onSubmit}>
                    {props.children}
                    <button type="submit">{props.btn}</button>
                </form>
                <span>{props.linkTitle}</span>
            </div>
        );
    };
});

describe("components/login/Login", () => {
    test("should render login form", () => {
        renderWithProviders(<Login handleAuthorization={jest.fn()} />);

        expect(screen.getByText("Рады видеть!")).toBeInTheDocument();
        expect(screen.getByText("Логин")).toBeInTheDocument();
        expect(screen.getByText("Пароль")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Войти" })).toBeInTheDocument();
        expect(screen.getByText("Создать аккаунт")).toBeInTheDocument();
    });

    test("should update username and password fields", () => {
        renderWithProviders(<Login handleAuthorization={jest.fn()} />);

        const emailInput = screen.getByRole("textbox");
        const passwordInput = document.querySelector('input[name="password"]');

        fireEvent.change(emailInput, {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(passwordInput, {
            target: { name: "password", value: "password123" },
        });

        expect(emailInput).toHaveValue("user@example.com");
        expect(passwordInput).toHaveValue("password123");
    });

    test("should call handleAuthorization on submit", () => {
        const handleAuthorization = jest.fn();

        renderWithProviders(<Login handleAuthorization={handleAuthorization} />);

        const emailInput = screen.getByRole("textbox");
        const passwordInput = document.querySelector('input[name="password"]');

        fireEvent.change(emailInput, {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(passwordInput, {
            target: { name: "password", value: "password123" },
        });

        fireEvent.click(screen.getByRole("button", { name: "Войти" }));

        expect(handleAuthorization).toHaveBeenCalledWith({
            username: "user@example.com",
            password: "password123",
        });
    });
});