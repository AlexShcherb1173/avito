import React from "react";
import { screen } from "@testing-library/react";
import { renderWithProviders } from "./renderWithProviders";

function TestComponent() {
    return <div>Rendered with providers</div>;
}

describe("test-utils/renderWithProviders", () => {
    test("should render component with default store and router", () => {
        const result = renderWithProviders(<TestComponent />);

        expect(screen.getByText("Rendered with providers")).toBeInTheDocument();
        expect(result.store).toBeDefined();
        expect(typeof result.store.getState).toBe("function");
    });

    test("should use passed preloaded state", () => {
        const { store } = renderWithProviders(<TestComponent />, {
            preloadedState: {
                ads: [{ pk: 1 }],
                adsDefault: [],
                userAds: [],
                userInfo: { username: "user@example.com" },
            },
        });

        expect(store.getState()).toEqual({
            ads: [{ pk: 1 }],
            adsDefault: [],
            userAds: [],
            userInfo: { username: "user@example.com" },
        });
    });

    test("should use passed custom store", () => {
        const customStore = {
            getState: jest.fn(() => ({
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            })),
            subscribe: jest.fn(() => jest.fn()),
            dispatch: jest.fn(),
        };

        const result = renderWithProviders(<TestComponent />, {
            store: customStore,
        });

        expect(result.store).toBe(customStore);
        expect(screen.getByText("Rendered with providers")).toBeInTheDocument();
    });
});