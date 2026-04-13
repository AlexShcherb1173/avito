import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Button from "./Button";

jest.mock("react-responsive", () => {
    return ({ children, minWidth, maxWidth }) => {
        if (minWidth === 1000) {
            return <>{children}</>;
        }
        if (maxWidth === 999) {
            return null;
        }
        return <>{children}</>;
    };
});

jest.mock("../navigation/Navigation", () => {
    return function MockNavigation() {
        return <div>Navigation</div>;
    };
});

describe("components/button/Button", () => {
    test("should render simple button when user is not provided", () => {
        render(
            <Button
                logOut={jest.fn()}
                text="Войти"
                className="button-link"
                user={false}
            />
        );

        expect(screen.getByRole("button", { name: "Войти" })).toBeInTheDocument();
        expect(screen.queryByText("Navigation")).not.toBeInTheDocument();
    });

    test("should render navigation and button when user exists on desktop", () => {
        render(
            <Button
                logOut={jest.fn()}
                text="Выйти"
                className="button-link"
                user={{ id: 1 }}
            />
        );

        expect(screen.getByText("Navigation")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Выйти" })).toBeInTheDocument();
    });

    test("should call logOut on button click", () => {
        const logOut = jest.fn();

        render(
            <Button
                logOut={logOut}
                text="Выйти"
                className="button-link"
                user={false}
            />
        );

        fireEvent.click(screen.getByRole("button", { name: "Выйти" }));

        expect(logOut).toHaveBeenCalledTimes(1);
    });
});