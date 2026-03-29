import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import Profile from "./Profile";

jest.mock("../Modal/Modal", () => {
    return function MockModal({ isVisible, closeModal, updatePassword }) {
        return (
            <div data-testid="profile-modal">
                <span>{isVisible ? "visible" : "hidden"}</span>
                <button onClick={() => updatePassword("newPassword123")}>
                    change-password
                </button>
                <button onClick={closeModal}>close-modal</button>
            </div>
        );
    };
});

jest.mock("../userForm/UserForm", () => {
    return function MockUserForm({
                                     title,
                                     userName,
                                     onSubmit,
                                     buttonText,
                                     children,
                                 }) {
        return (
            <form onSubmit={onSubmit}>
                <div>{title}</div>
                <div>{userName}</div>
                {children}
                <button type="submit">{buttonText}</button>
            </form>
        );
    };
});

describe("components/profile/Profile", () => {
    test("should render user profile data", () => {
        render(
            <Profile
                userInfo={{
                    firstName: "Ivan",
                    lastName: "Ivanov",
                    phone: "+79990000001",
                }}
                handleUpdateUser={jest.fn()}
                handleUpdatePassword={jest.fn()}
            />
        );

        expect(screen.getByDisplayValue("Ivan")).toBeInTheDocument();
        expect(screen.getByDisplayValue("Ivanov")).toBeInTheDocument();
        expect(screen.getByDisplayValue("+79990000001")).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /сохранить/i })
        ).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /сменить пароль/i })
        ).toBeInTheDocument();
    });

    test("should call handleUpdateUser on submit", () => {
        const handleUpdateUser = jest.fn();

        render(
            <Profile
                userInfo={{
                    firstName: "Ivan",
                    lastName: "Ivanov",
                    phone: "+79990000001",
                }}
                handleUpdateUser={handleUpdateUser}
                handleUpdatePassword={jest.fn()}
            />
        );

        fireEvent.change(screen.getByDisplayValue("Ivan"), {
            target: { name: "firstName", value: "Updated" },
        });

        fireEvent.change(screen.getByDisplayValue("Ivanov"), {
            target: { name: "lastName", value: "User" },
        });

        fireEvent.change(screen.getByDisplayValue("+79990000001"), {
            target: { name: "phone", value: "+79991112233" },
        });

        fireEvent.click(screen.getByRole("button", { name: /сохранить/i }));

        expect(handleUpdateUser).toHaveBeenCalledTimes(1);
        expect(handleUpdateUser).toHaveBeenCalledWith({
            firstName: "Updated",
            lastName: "User",
            phone: "+79991112233",
        });
    });

    test("should open password modal and call handleUpdatePassword", () => {
        const handleUpdatePassword = jest.fn();

        render(
            <Profile
                userInfo={{
                    firstName: "Ivan",
                    lastName: "Ivanov",
                    phone: "+79990000001",
                }}
                handleUpdateUser={jest.fn()}
                handleUpdatePassword={handleUpdatePassword}
            />
        );

        fireEvent.click(screen.getByRole("button", { name: /сменить пароль/i }));
        fireEvent.click(screen.getByRole("button", { name: "change-password" }));

        expect(handleUpdatePassword).toHaveBeenCalledTimes(1);
        expect(handleUpdatePassword).toHaveBeenCalledWith("newPassword123");
    });
});