import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Login from "./Login";

describe("components/login/Login", () => {
    test("should render login form", () => {
        render(
            <MemoryRouter>
                <Login handleAuthorization={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.getByText("Рады видеть!")).toBeInTheDocument();
        expect(screen.getByText("Логин")).toBeInTheDocument();
        expect(screen.getByText("Пароль")).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /войти/i })
        ).toBeInTheDocument();
    });

    test("should call handleAuthorization with entered values", () => {
        const handleAuthorization = jest.fn();

        render(
            <MemoryRouter>
                <Login handleAuthorization={handleAuthorization} />
            </MemoryRouter>
        );

        const emailInput = screen.getByRole("textbox");
        const passwordInput = screen.getByLabelText(/пароль/i, {
            selector: 'input[name="password"]',
        });
        const submitButton = screen.getByRole("button", { name: /войти/i });

        fireEvent.change(emailInput, {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(passwordInput, {
            target: { name: "password", value: "password123" },
        });

        fireEvent.click(submitButton);

        expect(handleAuthorization).toHaveBeenCalledTimes(1);
        expect(handleAuthorization).toHaveBeenCalledWith({
            username: "user@example.com",
            password: "password123",
        });
    });
});