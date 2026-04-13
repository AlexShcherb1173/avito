import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Header from "./Header";

jest.mock("react-responsive", () => {
    return ({ children }) => <>{children}</>;
});

jest.mock("../button/Button", () => {
    return function MockButton(props) {
        return (
            <button
                type="button"
                onClick={props.logOut || props.onClose}
                className={props.className}
            >
                {props.text}
            </button>
        );
    };
});

describe("components/header/Header", () => {
    test("should render logo link and login button for unauthorized user", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Header
                    onOpen={jest.fn()}
                    isAuthorized={false}
                    signOut={jest.fn()}
                />
            </MemoryRouter>
        );

        expect(screen.getByAltText("asd icon")).toBeInTheDocument();
        expect(screen.getByText("Войти")).toBeInTheDocument();
    });

    test("should render logo link on sign-up page", () => {
        render(
            <MemoryRouter initialEntries={["/sign-up"]}>
                <Header
                    onOpen={jest.fn()}
                    isAuthorized={false}
                    signOut={jest.fn()}
                />
            </MemoryRouter>
        );

        expect(screen.getByAltText("asd icon")).toBeInTheDocument();
        expect(screen.queryByText("Войти")).not.toBeInTheDocument();
    });

    test("should render authorized controls", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Header
                    onOpen={jest.fn()}
                    isAuthorized={true}
                    signOut={jest.fn()}
                />
            </MemoryRouter>
        );

        expect(screen.getByAltText("asd icon")).toBeInTheDocument();
        expect(screen.getByText("Выйти")).toBeInTheDocument();
        expect(screen.getByAltText("sandwich icon")).toBeInTheDocument();
    });

    test("should call signOut when logout button is clicked", () => {
        const signOut = jest.fn();

        render(
            <MemoryRouter initialEntries={["/"]}>
                <Header
                    onOpen={jest.fn()}
                    isAuthorized={true}
                    signOut={signOut}
                />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByText("Выйти"));

        expect(signOut).toHaveBeenCalledTimes(1);
    });

    test("should call onOpen when sandwich icon is clicked", () => {
        const onOpen = jest.fn();

        render(
            <MemoryRouter initialEntries={["/"]}>
                <Header
                    onOpen={onOpen}
                    isAuthorized={true}
                    signOut={jest.fn()}
                />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByAltText("sandwich icon"));

        expect(onOpen).toHaveBeenCalledTimes(1);
    });

    test("should render only logo on reset password page", () => {
        render(
            <MemoryRouter initialEntries={["/sign-in/email/newpassword"]}>
                <Header
                    onOpen={jest.fn()}
                    isAuthorized={false}
                    signOut={jest.fn()}
                />
            </MemoryRouter>
        );

        expect(screen.getByAltText("asd icon")).toBeInTheDocument();
        expect(screen.queryByText("Войти")).not.toBeInTheDocument();
        expect(screen.queryByText("Выйти")).not.toBeInTheDocument();
    });
});