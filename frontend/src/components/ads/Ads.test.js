import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Ads from "./Ads";

jest.mock("../ad/Ad", () => {
    return function MockAd({ title, price }) {
        return (
            <div>
                MockAd: {title} - {price}
            </div>
        );
    };
});

jest.mock("../adsCardListButton/AdsCardListButton", () => {
    return function MockAdsCardListButton({ showMoreAds }) {
        return (
            <button type="button" onClick={showMoreAds}>
                Еще
            </button>
        );
    };
});

describe("components/ads/Ads", () => {
    const ads = [
        { pk: 1, title: "Ad 1", price: 100, image: "/1.jpg" },
        { pk: 2, title: "Ad 2", price: 200, image: "/2.jpg" },
        { pk: 3, title: "Ad 3", price: 300, image: "/3.jpg" },
    ];

    test("should show empty text when there are no ads", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Ads ads={[]} isAuthorized={false} visiableAds={2} showMoreAds={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.getByText("У вас еще нет объявлений.")).toBeInTheDocument();
    });

    test("should render visible ads only", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Ads ads={ads} isAuthorized={false} visiableAds={2} showMoreAds={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.getByText("MockAd: Ad 1 - 100")).toBeInTheDocument();
        expect(screen.getByText("MockAd: Ad 2 - 200")).toBeInTheDocument();
        expect(screen.queryByText("MockAd: Ad 3 - 300")).not.toBeInTheDocument();
    });

    test("should render show more button when visible ads less than total", () => {
        const showMoreAds = jest.fn();

        render(
            <MemoryRouter initialEntries={["/"]}>
                <Ads ads={ads} isAuthorized={false} visiableAds={2} showMoreAds={showMoreAds} />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", { name: "Еще" }));
        expect(showMoreAds).toHaveBeenCalledTimes(1);
    });

    test("should not render show more button when all ads are visible", () => {
        render(
            <MemoryRouter initialEntries={["/"]}>
                <Ads ads={ads} isAuthorized={false} visiableAds={3} showMoreAds={jest.fn()} />
            </MemoryRouter>
        );

        expect(screen.queryByRole("button", { name: "Еще" })).not.toBeInTheDocument();
    });

    test("should use profile padding class on profile page", () => {
        const { container } = render(
            <MemoryRouter initialEntries={["/profile"]}>
                <Ads ads={ads} isAuthorized={true} visiableAds={2} showMoreAds={jest.fn()} />
            </MemoryRouter>
        );

        expect(container.querySelector(".ads")).toHaveClass("padding");
        expect(container.querySelector(".ads__container")).toHaveClass("ads__container-profile");
    });
});