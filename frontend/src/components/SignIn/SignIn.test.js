import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import SignIn from "./SignIn";
import auth from "../../utils/auth";

const mockedNavigate = jest.fn();

jest.mock("../../utils/auth", () => ({
    __esModule: true,
    default: {
        authentication: jest.fn(),
    },
}));

jest.mock("react-router-dom", () => {
    const actual = jest.requireActual("react-router-dom");
    return {
        ...actual,
        useNavigate: () => mockedNavigate,
    };
});

describe("components/SignIn/SignIn", () => {
    beforeEach(() => {
        jest.clearAllMocks();
        localStorage.clear();
    });

    test("should render form fields and link", () => {
        render(
            <MemoryRouter>
                <SignIn setIsLoggedIn={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.getByText("Рады видеть!")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Войти" })).toBeInTheDocument();
        expect(screen.getByText("Создать аккаунт")).toBeInTheDocument();
    });

    test("should update input values", () => {
        render(
            <MemoryRouter>
                <SignIn setIsLoggedIn={jest.fn()} />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByRole("textbox"), {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(screen.getByLabelText("Пароль"), {
            target: { name: "password", value: "password123" },
        });

        expect(screen.getByRole("textbox")).toHaveValue("user@example.com");
        expect(screen.getByLabelText("Пароль")).toHaveValue("password123");
    });

    test("should authenticate, save credentials and navigate on success", async () => {
        auth.authentication.mockResolvedValue({});
        const setIsLoggedIn = jest.fn();

        render(
            <MemoryRouter>
                <SignIn setIsLoggedIn={setIsLoggedIn} />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByRole("textbox"), {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(screen.getByLabelText("Пароль"), {
            target: { name: "password", value: "password123" },
        });

        fireEvent.click(screen.getByRole("button", { name: "Войти" }));

        await waitFor(() => {
            expect(auth.authentication).toHaveBeenCalledWith({
                username: "user@example.com",
                password: "password123",
            });
        });

        await waitFor(() => {
            expect(localStorage.getItem("login")).toBe("user@example.com");
            expect(localStorage.getItem("password")).toBe("password123");
            expect(setIsLoggedIn).toHaveBeenCalledWith(true);
            expect(mockedNavigate).toHaveBeenCalledWith("/");
        });
    });

    test("should show string error message on failed authentication", async () => {
        auth.authentication.mockRejectedValue("Invalid credentials");

        render(
            <MemoryRouter>
                <SignIn setIsLoggedIn={jest.fn()} />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByRole("textbox"), {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(screen.getByLabelText("Пароль"), {
            target: { name: "password", value: "password123" },
        });

        fireEvent.click(screen.getByRole("button", { name: "Войти" }));

        await waitFor(() => {
            expect(screen.getByText("Invalid credentials")).toBeInTheDocument();
        });
    });

    test("should show default error message when rejected value is not string", async () => {
        auth.authentication.mockRejectedValue(new Error("Server error"));

        render(
            <MemoryRouter>
                <SignIn setIsLoggedIn={jest.fn()} />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByRole("textbox"), {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(screen.getByLabelText("Пароль"), {
            target: { name: "password", value: "password123" },
        });

        fireEvent.click(screen.getByRole("button", { name: "Войти" }));

        await waitFor(() => {
            expect(screen.getByText("Ошибка авторизации")).toBeInTheDocument();
        });
    });
});