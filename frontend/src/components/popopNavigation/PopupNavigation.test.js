import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import PopupNavigation from "./PopupNavigation";

jest.mock("../navigation/Navigation", () => {
    return function MockNavigation({ onClose }) {
        return (
            <button type="button" onClick={onClose}>
                Navigation
            </button>
        );
    };
});

jest.mock("../button/Button", () => {
    return function MockButton(props) {
        return (
            <button
                type="button"
                onClick={() => {
                    if (props.onClose) {
                        props.onClose();
                    }
                    if (props.logOut) {
                        props.logOut();
                    }
                }}
            >
                {props.text}
            </button>
        );
    };
});

describe("components/popopNavigation/PopupNavigation", () => {
    test("should render closed popup class when isOpen is false", () => {
        const { container } = render(
            <PopupNavigation
                isOpen={false}
                onClose={jest.fn()}
                logOut={jest.fn()}
            />
        );

        expect(container.querySelector(".popupNavigation")).toBeInTheDocument();
        expect(container.querySelector(".popup_is-opened")).not.toBeInTheDocument();
    });

    test("should render opened popup class when isOpen is true", () => {
        const { container } = render(
            <PopupNavigation
                isOpen={true}
                onClose={jest.fn()}
                logOut={jest.fn()}
            />
        );

        expect(container.querySelector(".popup_is-opened")).toBeInTheDocument();
    });

    test("should call onClose when close button is clicked", () => {
        const onClose = jest.fn();

        const { container } = render(
            <PopupNavigation
                isOpen={true}
                onClose={onClose}
                logOut={jest.fn()}
            />
        );

        fireEvent.click(container.querySelector(".popupNavigation__close-button"));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should render navigation and logout button", () => {
        render(
            <PopupNavigation
                isOpen={true}
                onClose={jest.fn()}
                logOut={jest.fn()}
            />
        );

        expect(screen.getByText("Navigation")).toBeInTheDocument();
        expect(screen.getByText("Выйти")).toBeInTheDocument();
    });

    test("should call onClose from navigation", () => {
        const onClose = jest.fn();

        render(
            <PopupNavigation
                isOpen={true}
                onClose={onClose}
                logOut={jest.fn()}
            />
        );

        fireEvent.click(screen.getByText("Navigation"));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("should call onClose and logOut from logout button", () => {
        const onClose = jest.fn();
        const logOut = jest.fn();

        render(
            <PopupNavigation
                isOpen={true}
                onClose={onClose}
                logOut={logOut}
            />
        );

        fireEvent.click(screen.getByText("Выйти"));

        expect(onClose).toHaveBeenCalledTimes(1);
        expect(logOut).toHaveBeenCalledTimes(1);
    });
});