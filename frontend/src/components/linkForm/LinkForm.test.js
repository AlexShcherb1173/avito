import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import LinkForm from "./LinkForm";

describe("components/linkForm/LinkForm", () => {
    test("should render children and button text", () => {
        render(
            <LinkForm buttonName="Отправить">
                <input placeholder="email" />
            </LinkForm>
        );

        expect(screen.getByPlaceholderText("email")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Отправить" })).toBeInTheDocument();
    });

    test("should call onClick when button is clicked", () => {
        const onClick = jest.fn();

        render(
            <LinkForm buttonName="Отправить" onClick={onClick}>
                <input placeholder="email" />
            </LinkForm>
        );

        fireEvent.click(screen.getByRole("button", { name: "Отправить" }));

        expect(onClick).toHaveBeenCalledTimes(1);
    });

    test("should apply disabled class when error exists", () => {
        render(
            <LinkForm buttonName="Отправить" error="Ошибка">
                <input placeholder="email" />
            </LinkForm>
        );

        expect(screen.getByRole("button", { name: "Отправить" }).className)
            .toContain("linkForm__button-disabled");
    });

    test("should disable button when disabled prop is true", () => {
        render(
            <LinkForm buttonName="Отправить" disabled={true}>
                <input placeholder="email" />
            </LinkForm>
        );

        expect(screen.getByRole("button", { name: "Отправить" })).toBeDisabled();
    });
});