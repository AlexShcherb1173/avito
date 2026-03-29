import React from "react";
import { screen, waitFor } from "@testing-library/react";
import App from "./App";
import { renderWithProviders } from "../../test-utils/renderWithProviders";

jest.mock("../../utils/auth", () => ({
    __esModule: true,
    default: {
        registration: jest.fn(),
        authentication: jest.fn(),
    },
}));

jest.mock("../../utils/api", () => ({
    __esModule: true,
    default: {
        getUserInfo: jest.fn(),
        getUsersAds: jest.fn(),
        getHiddenAds: jest.fn(),
        getAds: jest.fn(),
        updateUser: jest.fn(),
        updatePassword: jest.fn(),
        updateUserPhoto: jest.fn(),
        addAd: jest.fn(),
    },
}));

jest.mock("../header/Header", () => () => <div>Header</div>);
jest.mock("../main/Main", () => () => <div>Main</div>);
jest.mock("../footer/Footer", () => () => <div>Footer</div>);
jest.mock("../registration/Registration", () => () => <div>Registration</div>);
jest.mock("../login/Login", () => () => <div>Login</div>);
jest.mock("../userProfile/UserProfile", () => () => <div>UserProfile</div>);
jest.mock("../singlePage/SinglePage", () => () => <div>SinglePage</div>);
jest.mock("../popopNavigation/PopupNavigation", () => () => <div>PopupNavigation</div>);
jest.mock("../newAdd/NewAdd", () => () => <div>NewAdd</div>);
jest.mock("../protectedRoute/ProtectedRoute", () => ({ children }) => <>{children}</>);

import api from "../../utils/api";

describe("components/app/App", () => {
    beforeEach(() => {
        jest.clearAllMocks();
        localStorage.clear();
    });

    test("should render app shell", async () => {
        api.getAds.mockResolvedValueOnce({
            results: [],
        });

        renderWithProviders(<App />, {
            route: "/",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        expect(screen.getByText("Header")).toBeInTheDocument();
        expect(screen.getByText("Footer")).toBeInTheDocument();

        await waitFor(() => {
            expect(api.getAds).toHaveBeenCalled();
        });
    });

    test("should restore authorization from persisted credentials", async () => {
        api.getUserInfo.mockResolvedValueOnce({
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });

        api.getUsersAds.mockResolvedValueOnce({
            results: [],
        });

        api.getUserInfo.mockResolvedValueOnce({
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });

        api.getHiddenAds.mockResolvedValueOnce({
            results: [],
        });

        renderWithProviders(<App />, {
            route: "/",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                },
            },
        });

        await waitFor(() => {
            expect(api.getUserInfo).toHaveBeenCalled();
        });

        expect(api.getUserInfo).toHaveBeenCalledWith(
            "user@example.com",
            "password123"
        );
    });

    test("should load public ads when user is not authorized", async () => {
        api.getAds.mockResolvedValueOnce({
            results: [{ pk: 1, title: "Public ad" }],
        });

        renderWithProviders(<App />, {
            route: "/",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        await waitFor(() => {
            expect(api.getAds).toHaveBeenCalledTimes(1);
        });
    });
});