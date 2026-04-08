import React from "react";
import { act } from "react";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import SinglePage from "./SinglePage";
import { renderWithProviders } from "../../test-utils/renderWithProviders";
import api from "../../utils/api";

const mockedNavigate = jest.fn();

jest.mock("react-router-dom", () => {
    const actual = jest.requireActual("react-router-dom");
    return {
        ...actual,
        useParams: () => ({ id: "1" }),
        useNavigate: () => mockedNavigate,
    };
});

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

jest.mock("../buttons/Buttons", () => {
    return function MockButtons({ onOpen, onSubmit }) {
        return (
            <div>
                <button type="button" onClick={onOpen}>
                    Open edit popup
                </button>
                <button type="button" onClick={onSubmit}>
                    Delete ad
                </button>
            </div>
        );
    };
});

jest.mock("../preloader/Preloader", () => () => <div>Preloader</div>);

jest.mock("../commentContainer/CommentContainer", () => {
    return function MockCommentContainer(props) {
        return (
            <div>
                <div>CommentContainer component</div>
                <div data-testid="comments-count">{props.comments.length}</div>
                <div data-testid="comment-container-role">{props.role}</div>
                <div data-testid="comment-container-adid">{String(props.adId)}</div>
                <button
                    type="button"
                    onClick={() => props.addComment({ text: "New comment" })}
                >
                    Add comment
                </button>
                <button
                    type="button"
                    onClick={() => props.deleteComment("1", 101)}
                >
                    Delete comment
                </button>
            </div>
        );
    };
});

jest.mock("../editAdPopup/EditAdPopup", () => {
    return function MockEditAdPopup(props) {
        return (
            <div>
                <div>EditAdPopup component</div>
                <div data-testid="edit-ad-popup-open">
                    {String(props.isEditPopupOpen)}
                </div>
                <button
                    type="button"
                    onClick={() =>
                        props.handleEditAdd({
                            title: "Updated title",
                            price: 777,
                            description: "Updated description",
                        })
                    }
                >
                    Submit edit ad
                </button>
            </div>
        );
    };
});

jest.mock("../editPhotoAdPopup/EditPhotoAdPopup", () => {
    return function MockEditPhotoAdPopup(props) {
        return (
            <div>
                <div>EditPhotoAdPopup component</div>
                <div data-testid="edit-photo-popup-open">
                    {String(props.isOpen)}
                </div>
                <button
                    type="button"
                    onClick={() => props.handleEdit(new File(["x"], "photo.jpg"))}
                >
                    Submit edit photo
                </button>
            </div>
        );
    };
});

describe("components/singlePage/SinglePage", () => {
    const baseProps = {
        isEditPopupOpen: false,
        isEditPhotoPopupOpen: false,
        isComPopupOpen: false,
        handleEditCommPopupOpen: jest.fn(),
        handleOpenEditPopup: jest.fn(),
        handleOpenEditPhotoPopup: jest.fn(),
        onClose: jest.fn(),
        isAuthorized: true,
        setIsLoading: jest.fn(),
        isLoading: false,
        user: 1,
        setAds: jest.fn(),
        username: "user@example.com",
        password: "password123",
    };

    beforeEach(() => {
        jest.clearAllMocks();
        jest.useFakeTimers();

        jest.spyOn(console, "log").mockImplementation(() => {});
        jest.spyOn(console, "error").mockImplementation(() => {});

        api.getComments.mockResolvedValue({
            results: [
                { pk: 101, text: "Comment 1" },
                { pk: 102, text: "Comment 2" },
            ],
        });

        api.getAd.mockResolvedValue({
            pk: 1,
            title: "Test ad",
            image: "/img.jpg",
            price: 1000,
            description: "Test description",
            authorFirstName: "Ivan",
            authorLastName: "Ivanov",
            phone: "+79990000001",
            email: "user@example.com",
        });

        api.addComment.mockResolvedValue({ pk: 201, text: "New comment" });
        api.deleteComment.mockResolvedValue(null);
        api.editAdd.mockResolvedValue({
            title: "Updated title",
            price: 777,
            description: "Updated description",
        });
        api.editAddPhoto.mockResolvedValue({ image: "/updated.jpg" });
        api.deleteAdd.mockResolvedValue(null);
    });

    afterEach(() => {
        act(() => {
            jest.runOnlyPendingTimers();
        });
        jest.useRealTimers();
        jest.restoreAllMocks();
    });

    test("should render main container", () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        expect(document.querySelector(".cardInformation")).toBeInTheDocument();
    });

    test("should load ad and comments for authorized user", async () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(api.getComments).toHaveBeenCalledWith(
                "1",
                "user@example.com",
                "password123"
            );
        });

        await waitFor(() => {
            expect(api.getAd).toHaveBeenCalledWith(
                "1",
                "user@example.com",
                "password123"
            );
        });

        await waitFor(() => {
            expect(screen.getByText("Test ad")).toBeInTheDocument();
            expect(screen.getByText("Test description")).toBeInTheDocument();
            expect(screen.getByText("CommentContainer component")).toBeInTheDocument();
            expect(screen.getByTestId("comments-count")).toHaveTextContent("2");
            expect(screen.getByTestId("comment-container-role")).toHaveTextContent("USER");
            expect(screen.getByTestId("comment-container-adid")).toHaveTextContent("1");
        });
    });

    test("should not request ad data when not authorized", () => {
        renderWithProviders(
            <SinglePage {...baseProps} isAuthorized={false} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                    },
                },
            }
        );

        expect(api.getComments).not.toHaveBeenCalled();
        expect(api.getAd).not.toHaveBeenCalled();
    });

    test("should not request ad data when username is missing", () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        expect(api.getComments).not.toHaveBeenCalled();
        expect(api.getAd).not.toHaveBeenCalled();
    });

    test("should not request ad data when password is missing", () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "",
                    role: "USER",
                },
            },
        });

        expect(api.getComments).not.toHaveBeenCalled();
        expect(api.getAd).not.toHaveBeenCalled();
    });

    test("should render preloader when loading prop is true", () => {
        renderWithProviders(
            <SinglePage {...baseProps} isLoading={true} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                    },
                },
            }
        );

        expect(screen.getByText("Preloader")).toBeInTheDocument();
    });

    test("should render nothing but main when ad is not loaded yet", () => {
        api.getAd.mockResolvedValueOnce(null);

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        expect(document.querySelector(".cardInformation")).toBeInTheDocument();
    });

    test("should render edit controls for owner", async () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Open edit popup")).toBeInTheDocument();
            expect(screen.getByText("Delete ad")).toBeInTheDocument();
        });
    });

    test("should open edit popup from buttons for editable ad", async () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Open edit popup")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Open edit popup"));

        expect(baseProps.handleOpenEditPopup).toHaveBeenCalledTimes(1);
    });

    test("should allow opening edit photo popup from image button when image exists", async () => {
        const { container } = renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Test ad")).toBeInTheDocument();
        });

        const imageChangeButton = container.querySelector(".cardInformation__img-change");
        expect(imageChangeButton).toBeInTheDocument();

        fireEvent.click(imageChangeButton);

        expect(baseProps.handleOpenEditPhotoPopup).toHaveBeenCalledTimes(1);
    });

    test("should render null-image block and image edit button when ad has no image", async () => {
        api.getAd.mockResolvedValueOnce({
            pk: 1,
            title: "No image ad",
            image: "",
            price: 1000,
            description: "Test description",
            authorFirstName: "Ivan",
            authorLastName: "Ivanov",
            phone: "+79990000001",
            email: "user@example.com",
        });

        const { container } = renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("No image ad")).toBeInTheDocument();
        });

        expect(container.querySelector(".cardInformation__img-null")).toBeInTheDocument();
        expect(container.querySelector(".cardInformation__img-change")).toBeInTheDocument();
    });

    test("should hide edit buttons for foreign non-admin user", async () => {
        api.getAd.mockResolvedValueOnce({
            pk: 1,
            title: "Foreign ad",
            image: "/img.jpg",
            price: 1000,
            description: "Test description",
            authorFirstName: "Petr",
            authorLastName: "Petrov",
            phone: "+79990000001",
            email: "another@example.com",
        });

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Foreign ad")).toBeInTheDocument();
        });

        expect(screen.queryByText("Open edit popup")).not.toBeInTheDocument();
        expect(screen.queryByText("Delete ad")).not.toBeInTheDocument();
    });

    test("should allow admin to edit foreign ad", async () => {
        api.getAd.mockResolvedValueOnce({
            pk: 1,
            title: "Foreign ad",
            image: "/img.jpg",
            price: 1000,
            description: "Test description",
            authorFirstName: "Petr",
            authorLastName: "Petrov",
            phone: "+79990000001",
            email: "another@example.com",
        });

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "admin@example.com",
                    password: "password123",
                    role: "ADMIN",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Foreign ad")).toBeInTheDocument();
        });

        expect(screen.getByText("Open edit popup")).toBeInTheDocument();
        expect(screen.getByText("Delete ad")).toBeInTheDocument();
    });

    test("should edit ad through api and toggle loading", async () => {
        const setAds = jest.fn();

        renderWithProviders(
            <SinglePage {...baseProps} setAds={setAds} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                        role: "USER",
                    },
                },
            }
        );

        await waitFor(() => {
            expect(screen.getByText("Submit edit ad")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit ad"));

        expect(baseProps.setIsLoading).toHaveBeenCalledWith(true);

        await waitFor(() => {
            expect(api.editAdd).toHaveBeenCalledWith(
                "1",
                {
                    title: "Updated title",
                    price: 777,
                    description: "Updated description",
                },
                "user@example.com",
                "password123"
            );
        });

        expect(setAds).toHaveBeenCalled();

        act(() => {
            jest.advanceTimersByTime(700);
        });

        expect(baseProps.setIsLoading).toHaveBeenCalledWith(false);
    });

    test("should edit ad photo through api when image response is object", async () => {
        const setAds = jest.fn();
        api.editAddPhoto.mockResolvedValueOnce({ image: "/updated-object.jpg" });

        renderWithProviders(
            <SinglePage {...baseProps} setAds={setAds} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                        role: "USER",
                    },
                },
            }
        );

        await waitFor(() => {
            expect(screen.getByText("Submit edit photo")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit photo"));

        await waitFor(() => {
            expect(api.editAddPhoto).toHaveBeenCalled();
        });

        expect(setAds).toHaveBeenCalled();
    });

    test("should edit ad photo through api when image response is string", async () => {
        const setAds = jest.fn();
        api.editAddPhoto.mockResolvedValueOnce("/updated-string.jpg");

        renderWithProviders(
            <SinglePage {...baseProps} setAds={setAds} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                        role: "USER",
                    },
                },
            }
        );

        await waitFor(() => {
            expect(screen.getByText("Submit edit photo")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit photo"));

        await waitFor(() => {
            expect(api.editAddPhoto).toHaveBeenCalled();
        });

        expect(setAds).toHaveBeenCalled();
    });

    test("should edit ad photo through api when image response is empty", async () => {
        const setAds = jest.fn();
        api.editAddPhoto.mockResolvedValueOnce(null);

        renderWithProviders(
            <SinglePage {...baseProps} setAds={setAds} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                        role: "USER",
                    },
                },
            }
        );

        await waitFor(() => {
            expect(screen.getByText("Submit edit photo")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit photo"));

        await waitFor(() => {
            expect(api.editAddPhoto).toHaveBeenCalled();
        });

        expect(setAds).toHaveBeenCalled();
    });

    test("should delete ad and navigate home", async () => {
        const setAds = jest.fn();

        renderWithProviders(
            <SinglePage {...baseProps} setAds={setAds} />,
            {
                preloadedState: {
                    ads: [],
                    adsDefault: [],
                    userAds: [],
                    userInfo: {
                        username: "user@example.com",
                        password: "password123",
                        role: "USER",
                    },
                },
            }
        );

        await waitFor(() => {
            expect(screen.getByText("Delete ad")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Delete ad"));

        await waitFor(() => {
            expect(api.deleteAdd).toHaveBeenCalledWith(
                "1",
                "user@example.com",
                "password123"
            );
        });

        expect(setAds).toHaveBeenCalled();
        expect(mockedNavigate).toHaveBeenCalledWith("/");
    });

    test("should add comment through api", async () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Add comment")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Add comment"));

        await waitFor(() => {
            expect(api.addComment).toHaveBeenCalledWith(
                "1",
                { text: "New comment" },
                "user@example.com",
                "password123"
            );
        });
    });

    test("should delete comment through api", async () => {
        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Delete comment")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Delete comment"));

        await waitFor(() => {
            expect(api.deleteComment).toHaveBeenCalledWith(
                "1",
                101,
                "user@example.com",
                "password123"
            );
        });
    });

    test("should handle array comments response", async () => {
        api.getComments.mockResolvedValueOnce([
            { pk: 1, text: "A" },
            { pk: 2, text: "B" },
            { pk: 3, text: "C" },
        ]);

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByTestId("comments-count")).toHaveTextContent("3");
        });
    });

    test("should handle comments load error", async () => {
        api.getComments.mockRejectedValueOnce("Comments error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });
    });

    test("should handle edit add error and still stop loading", async () => {
        api.editAdd.mockRejectedValueOnce("Edit error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Submit edit ad")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit ad"));

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });

        act(() => {
            jest.advanceTimersByTime(700);
        });

        expect(baseProps.setIsLoading).toHaveBeenCalledWith(false);
    });

    test("should handle edit photo error", async () => {
        api.editAddPhoto.mockRejectedValueOnce("Photo error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Submit edit photo")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit edit photo"));

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });
    });

    test("should handle delete ad error", async () => {
        api.deleteAdd.mockRejectedValueOnce("Delete ad error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Delete ad")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Delete ad"));

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });

        expect(mockedNavigate).not.toHaveBeenCalledWith("/");
    });

    test("should handle add comment error", async () => {
        api.addComment.mockRejectedValueOnce("Add comment error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Add comment")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Add comment"));

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });
    });

    test("should handle delete comment error", async () => {
        api.deleteComment.mockRejectedValueOnce("Delete comment error");

        renderWithProviders(<SinglePage {...baseProps} />, {
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {
                    username: "user@example.com",
                    password: "password123",
                    role: "USER",
                },
            },
        });

        await waitFor(() => {
            expect(screen.getByText("Delete comment")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Delete comment"));

        await waitFor(() => {
            expect(console.log).toHaveBeenCalled();
        });
    });
});