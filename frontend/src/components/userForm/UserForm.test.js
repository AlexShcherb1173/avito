import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import UserForm from "./UserForm";

describe("components/userForm/UserForm", () => {
    test("should render title, user name and children", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <UserForm
                    title="Профиль"
                    userName="Ivan"
                    buttonText="Сохранить"
                    onSubmit={jest.fn()}
                >
                    <input placeholder="phone" />
                </UserForm>
            </MemoryRouter>
        );

        expect(screen.getByText("Профиль")).toBeInTheDocument();
        expect(screen.getByText("Ivan")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("phone")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Сохранить" })).toBeInTheDocument();
    });

    test("should call onSubmit when form is submitted", () => {
        const onSubmit = jest.fn((e) => e.preventDefault());

        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <UserForm
                    title="Профиль"
                    userName="Ivan"
                    buttonText="Сохранить"
                    onSubmit={onSubmit}
                >
                    <input placeholder="phone" />
                </UserForm>
            </MemoryRouter>
        );

        fireEvent.submit(screen.getByRole("button", { name: "Сохранить" }).closest("form"));

        expect(onSubmit).toHaveBeenCalledTimes(1);
    });

    test("should disable button when disabled prop is true", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <UserForm
                    title="Профиль"
                    userName="Ivan"
                    buttonText="Сохранить"
                    onSubmit={jest.fn()}
                    disabled={true}
                >
                    <input placeholder="phone" />
                </UserForm>
            </MemoryRouter>
        );

        expect(screen.getByRole("button", { name: "Сохранить" })).toBeDisabled();
    });

    test("should apply profile button class on profile page", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <UserForm
                    title="Профиль"
                    userName="Ivan"
                    buttonText="Сохранить"
                    onSubmit={jest.fn()}
                >
                    <input placeholder="phone" />
                </UserForm>
            </MemoryRouter>
        );

        expect(screen.getByRole("button", { name: "Сохранить" }).className)
            .toContain("userForm__button-margin");
    });

    test("should apply disabled error class on non-profile page when errors exist", () => {
        render(
            <MemoryRouter initialEntries={["/other"]}>
                <UserForm
                    title="Редактирование"
                    userName="Ivan"
                    buttonText="Сохранить"
                    onSubmit={jest.fn()}
                    errors={{ phone: "error" }}
                >
                    <input placeholder="phone" />
                </UserForm>
            </MemoryRouter>
        );

        expect(screen.getByRole("button", { name: "Сохранить" }).className)
            .toContain("userForm__button_disabled");
    });
});