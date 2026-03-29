import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import SinglePage from "./SinglePage";

jest.mock("../../utils/api", () => ({
    __esModule: true,
    default: {
        getAd: jest.fn(),
        getComments: jest.fn(),
        addComment: jest.fn(),
        editComment: jest.fn(),
        deleteComment: jest.fn(),
        editAdd: jest.fn(),
        editAddPhoto: jest.fn(),
        deleteAdd: jest.fn(),
    },
}));

jest.mock("../ad/Ad", () => () => <div>Ad component</div>);
jest.mock("../commentContainer/CommentContainer", () => () => (
    <div>CommentContainer component</div>
));
jest.mock("../editAdPopup/EditAdPopup", () => () => <div>EditAdPopup component</div>);
jest.mock("../editPhotoAdPopup/EditPhotoAdPopup", () => () => (
    <div>EditPhotoAdPopup component</div>
));
jest.mock("../editCommentPopup/EditCommentPopup", () => () => (
    <div>EditCommentPopup component</div>
));

describe("components/singlePage/SinglePage", () => {
    test("should render without crashing", () => {
        render(
            <MemoryRouter initialEntries={["/ads/1"]}>
                <SinglePage
                    isEditPopupOpen={false}
                    isEditPhotoPopupOpen={false}
                    isComPopupOpen={false}
                    handleEditCommPopupOpen={jest.fn()}
                    handleOpenEditPopup={jest.fn()}
                    handleOpenEditPhotoPopup={jest.fn()}
                    onClose={jest.fn()}
                    isAuthorized={true}
                    setIsLoading={jest.fn()}
                    isLoading={false}
                    user={1}
                    setAds={jest.fn()}
                    username="user@example.com"
                    password="password123"
                    firstName="Ivan"
                    lastName="Ivanov"
                    phone="+79990000001"
                />
            </MemoryRouter>
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should mount child presentation components", () => {
        render(
            <MemoryRouter initialEntries={["/ads/1"]}>
                <SinglePage
                    isEditPopupOpen={false}
                    isEditPhotoPopupOpen={false}
                    isComPopupOpen={false}
                    handleEditCommPopupOpen={jest.fn()}
                    handleOpenEditPopup={jest.fn()}
                    handleOpenEditPhotoPopup={jest.fn()}
                    onClose={jest.fn()}
                    isAuthorized={true}
                    setIsLoading={jest.fn()}
                    isLoading={false}
                    user={1}
                    setAds={jest.fn()}
                    username="user@example.com"
                    password="password123"
                    firstName="Ivan"
                    lastName="Ivanov"
                    phone="+79990000001"
                />
            </MemoryRouter>
        );

        expect(screen.getByText("Ad component")).toBeInTheDocument();
        expect(screen.getByText("CommentContainer component")).toBeInTheDocument();
    });
});