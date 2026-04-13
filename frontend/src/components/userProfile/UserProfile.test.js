import React from "react";
import { screen } from "@testing-library/react";
import UserProfile from "./UserProfile";
import { renderWithProviders } from "../../test-utils/renderWithProviders";

jest.mock("../profile/Profile", () => () => <div>Profile component</div>);
jest.mock("../ad/Ad", () => () => <div>Ad component</div>);
jest.mock("../editUserImgPopup/EditUserImgPopup", () => () => (
    <div>EditUserImgPopup component</div>
));
jest.mock("../adsCardListButton/AdsCardListButton", () => () => (
    <div>AdsCardListButton component</div>
));

describe("components/userProfile/UserProfile", () => {
    test("should render without crashing", () => {
        renderWithProviders(
            <UserProfile
                isAuthorized={true}
                isOpen={false}
                onOpen={jest.fn()}
                onClose={jest.fn()}
                userAds={[]}
                isLoading={false}
                handleUpdateUser={jest.fn()}
                handleUpdatePassword={jest.fn()}
                handleUpdateUserPhoto={jest.fn()}
                visiableAds={4}
                showMoreAds={jest.fn()}
            />,
            {
                route: "/profile",
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {},
                },
            }
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should render nested profile block", () => {
        renderWithProviders(
            <UserProfile
                isAuthorized={true}
                isOpen={false}
                onOpen={jest.fn()}
                onClose={jest.fn()}
                userAds={[]}
                isLoading={false}
                handleUpdateUser={jest.fn()}
                handleUpdatePassword={jest.fn()}
                handleUpdateUserPhoto={jest.fn()}
                visiableAds={4}
                showMoreAds={jest.fn()}
            />,
            {
                route: "/profile",
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {},
                },
            }
        );

        expect(screen.getByText("Profile component")).toBeInTheDocument();
    });

    test("should render ads list area when user ads exist", () => {
        renderWithProviders(
            <UserProfile
                isAuthorized={true}
                isOpen={false}
                onOpen={jest.fn()}
                onClose={jest.fn()}
                userAds={[
                    { pk: 1, title: "First ad" },
                    { pk: 2, title: "Second ad" },
                ]}
                isLoading={false}
                handleUpdateUser={jest.fn()}
                handleUpdatePassword={jest.fn()}
                handleUpdateUserPhoto={jest.fn()}
                visiableAds={4}
                showMoreAds={jest.fn()}
            />,
            {
                route: "/profile",
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {},
                },
            }
        );

        expect(screen.getByText("Profile component")).toBeInTheDocument();
    });
});