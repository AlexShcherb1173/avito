import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import AdsCardListButton from "./AdsCardListButton";

describe("components/adsCardListButton/AdsCardListButton", () => {
    test("should render button", () => {
        render(<AdsCardListButton showMoreAds={jest.fn()} />);
        expect(screen.getByRole("button", { name: "Еще" })).toBeInTheDocument();
    });

    test("should call showMoreAds on click", () => {
        const showMoreAds = jest.fn();

        render(<AdsCardListButton showMoreAds={showMoreAds} />);
        fireEvent.click(screen.getByRole("button", { name: "Еще" }));

        expect(showMoreAds).toHaveBeenCalledTimes(1);
    });
});