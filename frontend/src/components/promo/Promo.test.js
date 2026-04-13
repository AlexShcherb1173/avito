import React from "react";
import { render, screen } from "@testing-library/react";
import Promo from "./Promo";

jest.mock("react-responsive", () => {
    return ({ children, minWidth, maxWidth }) => {
        if (minWidth === 801) {
            return <>{children}</>;
        }
        if (maxWidth === 800) {
            return null;
        }
        return <>{children}</>;
    };
});

jest.mock("../searchForm/SearchForm", () => {
    return function MockSearchForm({ ad }) {
        return <div>SearchForm: {ad}</div>;
    };
});

describe("components/promo/Promo", () => {
    test("should render title, subtitle and search form", () => {
        render(<Promo ad="phone" setAd={jest.fn()} />);

        expect(screen.getByText("Ads-Online")).toBeInTheDocument();
        expect(
            screen.getByText("Лучшая платформа для продажи вещей")
        ).toBeInTheDocument();
        expect(screen.getByText("SearchForm: phone")).toBeInTheDocument();
    });
});