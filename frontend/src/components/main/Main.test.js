import React from "react";
import { render, screen } from "@testing-library/react";
import Main from "./Main";

jest.mock("../promo/Promo", () => {
    return function MockPromo({ ad }) {
        return <div>Promo: {ad}</div>;
    };
});

jest.mock("../preloader/Preloader", () => {
    return function MockPreloader() {
        return <div>Preloader</div>;
    };
});

jest.mock("../ads/Ads", () => {
    return function MockAds(props) {
        return (
            <div>
                Ads component
                <span data-testid="ads-count">{props.ads.length}</span>
                <span data-testid="visible-ads">{props.visiableAds}</span>
                <span data-testid="authorized">
                    {String(props.isAuthorized)}
                </span>
            </div>
        );
    };
});

describe("components/main/Main", () => {
    test("should always render Promo", () => {
        render(
            <Main
                isAuthorized={false}
                adsDefault={[]}
                ads={[]}
                isLoading={false}
                ad="phone"
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={4}
            />
        );

        expect(screen.getByText("Promo: phone")).toBeInTheDocument();
    });

    test("should render Preloader when loading", () => {
        render(
            <Main
                isAuthorized={false}
                adsDefault={[]}
                ads={[]}
                isLoading={true}
                ad=""
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={4}
            />
        );

        expect(screen.getByText("Preloader")).toBeInTheDocument();
    });

    test("should render empty state for unauthorized user when adsDefault is empty", () => {
        render(
            <Main
                isAuthorized={false}
                adsDefault={[]}
                ads={[{ id: 1, title: "Private ad" }]}
                isLoading={false}
                ad=""
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={4}
            />
        );

        expect(
            screen.getByText("По вашему запросу ничего не найдено")
        ).toBeInTheDocument();
    });

    test("should render empty state for authorized user when ads is empty", () => {
        render(
            <Main
                isAuthorized={true}
                adsDefault={[{ id: 1, title: "Public ad" }]}
                ads={[]}
                isLoading={false}
                ad=""
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={4}
            />
        );

        expect(
            screen.getByText("По вашему запросу ничего не найдено")
        ).toBeInTheDocument();
    });

    test("should render Ads for unauthorized user when adsDefault exists", () => {
        render(
            <Main
                isAuthorized={false}
                adsDefault={[{ id: 1, title: "Public ad" }]}
                ads={[]}
                isLoading={false}
                ad=""
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={4}
            />
        );

        expect(screen.getByText("Ads component")).toBeInTheDocument();
        expect(screen.getByTestId("ads-count")).toHaveTextContent("0");
        expect(screen.getByTestId("authorized")).toHaveTextContent("false");
    });

    test("should render Ads for authorized user when ads exists", () => {
        render(
            <Main
                isAuthorized={true}
                adsDefault={[]}
                ads={[
                    { id: 1, title: "Ad 1" },
                    { id: 2, title: "Ad 2" },
                ]}
                isLoading={false}
                ad=""
                setAd={jest.fn()}
                showMoreAds={jest.fn()}
                visiableAds={6}
            />
        );

        expect(screen.getByText("Ads component")).toBeInTheDocument();
        expect(screen.getByTestId("ads-count")).toHaveTextContent("2");
        expect(screen.getByTestId("visible-ads")).toHaveTextContent("6");
        expect(screen.getByTestId("authorized")).toHaveTextContent("true");
    });
});