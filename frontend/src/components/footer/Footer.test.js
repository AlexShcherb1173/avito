import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Footer from "./Footer";

describe("components/footer/Footer", () => {
    test("should render copyright on regular page", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Footer />
            </MemoryRouter>
        );

        expect(screen.getByText(/Skypro/i)).toBeInTheDocument();
        expect(screen.getByText(/All rights reserved/i)).toBeInTheDocument();
    });

    test("should not render copyright on sign-up page", () => {
        render(
            <MemoryRouter initialEntries={["/sign-up"]}>
                <Footer />
            </MemoryRouter>
        );

        expect(screen.queryByText(/Skypro/i)).not.toBeInTheDocument();
    });

    test("should not render copyright on sign-in page", () => {
        render(
            <MemoryRouter initialEntries={["/sign-in"]}>
                <Footer />
            </MemoryRouter>
        );

        expect(screen.queryByText(/Skypro/i)).not.toBeInTheDocument();
    });
});