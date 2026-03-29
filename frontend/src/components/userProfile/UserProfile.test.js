import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import UserProfile from "./UserProfile";

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
        render(
            <MemoryRouter>
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
                />
            </MemoryRouter>
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should render nested profile block", () => {
        render(
            <MemoryRouter>
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
                />
            </MemoryRouter>
        );

        expect(screen.getByText("Profile component")).toBeInTheDocument();
    });

    test("should render ads list area when user ads exist", () => {
        render(
            <MemoryRouter>
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
                />
            </MemoryRouter>
        );

        expect(screen.getByText("Profile component")).toBeInTheDocument();
    });
});