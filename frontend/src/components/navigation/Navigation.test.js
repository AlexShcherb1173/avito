import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Navigation from "./Navigation";

describe("components/navigation/Navigation", () => {
    test("should render navigation links", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Navigation />
            </MemoryRouter>
        );

        expect(screen.getByText("Главная")).toBeInTheDocument();
        expect(screen.getByText("Профиль")).toBeInTheDocument();
    });

    test("should apply active class for home page", () => {
        const { container } = render(
            <MemoryRouter initialEntries={["/"]}>
                <Navigation />
            </MemoryRouter>
        );

        const links = container.querySelectorAll(".navigation__link");
        expect(links[0].className).toContain("activeLink");
        expect(links[1].className).not.toContain("activeLink");
    });

    test("should apply active class for profile page", () => {
        const { container } = render(
            <MemoryRouter initialEntries={["/profile"]}>
                <Navigation />
            </MemoryRouter>
        );

        const links = container.querySelectorAll(".navigation__link");
        expect(links[0].className).not.toContain("activeLink");
        expect(links[1].className).toContain("activeLink");
    });

    test("should call onClose when list is clicked", () => {
        const onClose = jest.fn();

        render(
            <MemoryRouter initialEntries={["/"]}>
                <Navigation onClose={onClose} />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("list"));

        expect(onClose).toHaveBeenCalledTimes(1);
    });
});