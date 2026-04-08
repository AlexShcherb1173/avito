import React from "react";
import { act } from "react";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import App from "./App";
import { renderWithProviders } from "../../test-utils/renderWithProviders";
import api from "../../utils/api";
import auth from "../../utils/auth";

const mockedNavigate = jest.fn();

jest.mock("react-router-dom", () => {
    const actual = jest.requireActual("react-router-dom");
    return {
        ...actual,
        useNavigate: () => mockedNavigate,
    };
});

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

jest.mock("../header/Header", () => {
    return function MockHeader(props) {
        return (
            <div>
                <div>Header</div>
                <div data-testid="header-authorized">
                    {String(props.isAuthorized)}
                </div>
                <button type="button" onClick={props.onOpen}>
                    Open popup
                </button>
                <button type="button" onClick={props.signOut}>
                    Header signOut
                </button>
            </div>
        );
    };
});

jest.mock("../main/Main", () => {
    return function MockMain(props) {
        return (
            <div>
                <div>Main</div>
                <div data-testid="main-authorized">
                    {String(props.isAuthorized)}
                </div>
                <button type="button" onClick={props.showMoreAds}>
                    Show more ads
                </button>
                <button type="button" onClick={() => props.setAd("phone")}>
                    Set search
                </button>
            </div>
        );
    };
});

jest.mock("../footer/Footer", () => () => <div>Footer</div>);

jest.mock("../registration/Registration", () => {
    return function MockRegistration(props) {
        return (
            <div>
                <div>Registration</div>
                <button
                    type="button"
                    onClick={() =>
                        props.handleRegistration({
                            username: "new@example.com",
                            password: "password123",
                            firstName: "Ivan",
                            lastName: "Ivanov",
                            phone: "+79990000001",
                            role: "USER",
                        })
                    }
                >
                    Submit registration
                </button>
            </div>
        );
    };
});

jest.mock("../login/Login", () => {
    return function MockLogin(props) {
        return (
            <div>
                <div>Login</div>
                <button
                    type="button"
                    onClick={() =>
                        props.handleAuthorization({
                            username: "user@example.com",
                            password: "password123",
                        })
                    }
                >
                    Submit login
                </button>
            </div>
        );
    };
});

jest.mock("../userProfile/UserProfile", () => {
    return function MockUserProfile(props) {
        return (
            <div>
                <div>UserProfile</div>
                <button
                    type="button"
                    onClick={() =>
                        props.handleUpdateUser({
                            firstName: "New",
                            lastName: "Name",
                            phone: "+70000000000",
                        })
                    }
                >
                    Update user
                </button>
                <button
                    type="button"
                    onClick={() => props.handleUpdatePassword("newPassword123")}
                >
                    Update password
                </button>
                <button
                    type="button"
                    onClick={() =>
                        props.handleUpdateUserPhoto(new File(["x"], "avatar.jpg"))
                    }
                >
                    Update user photo
                </button>
                <button type="button" onClick={props.onOpen}>
                    Open user photo popup
                </button>
                <button type="button" onClick={props.showMoreAds}>
                    Profile show more
                </button>
            </div>
        );
    };
});

jest.mock("../singlePage/SinglePage", () => {
    return function MockSinglePage(props) {
        return (
            <div>
                <div>SinglePage</div>
                <button type="button" onClick={props.handleOpenEditPopup}>
                    Open edit popup
                </button>
                <button type="button" onClick={props.handleOpenEditPhotoPopup}>
                    Open edit photo popup
                </button>
                <button type="button" onClick={props.handleEditCommPopupOpen}>
                    Open comment popup
                </button>
                <button type="button" onClick={props.onClose}>
                    Close all popups
                </button>
            </div>
        );
    };
});

jest.mock("../popopNavigation/PopupNavigation", () => {
    return function MockPopupNavigation(props) {
        return (
            <div>
                <div>PopupNavigation</div>
                <div data-testid="popup-open">{String(props.isOpen)}</div>
                <button type="button" onClick={props.onClose}>
                    Close popup nav
                </button>
                <button type="button" onClick={props.logOut}>
                    Popup logout
                </button>
            </div>
        );
    };
});

jest.mock("../newAdd/NewAdd", () => {
    return function MockNewAdd(props) {
        return (
            <div>
                <div>NewAdd</div>
                <button
                    type="button"
                    onClick={() => {
                        Promise.resolve(
                            props.handleAddAd({
                                title: "New ad",
                                price: 1000,
                                description: "Description",
                                image: new File(["x"], "photo.jpg"),
                            })
                        ).catch(() => {});
                    }}
                >
                    Submit new ad
                </button>
            </div>
        );
    };
});

jest.mock("../protectedRoute/ProtectedRoute", () => ({
    __esModule: true,
    default: ({ children, user }) => (
        <div>
            <div data-testid="protected-user">{String(user)}</div>
            {children}
        </div>
    ),
}));

describe("components/app/App", () => {
    beforeEach(() => {
        jest.clearAllMocks();
        localStorage.clear();
        jest.useFakeTimers();

        jest.spyOn(console, "log").mockImplementation(() => {});
        jest.spyOn(console, "error").mockImplementation(() => {});
        jest.spyOn(console, "warn").mockImplementation(() => {});

        api.getAds.mockResolvedValue({ results: [] });
        api.getUserInfo.mockResolvedValue({
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });
        api.getUsersAds.mockResolvedValue({ results: [] });
        api.getHiddenAds.mockResolvedValue({ results: [] });
        api.updateUser.mockResolvedValue({
            firstName: "New",
            lastName: "Name",
            phone: "+70000000000",
        });
        api.updatePassword.mockResolvedValue({});
        api.updateUserPhoto.mockResolvedValue({ image: "/avatar.jpg" });
        api.addAd.mockResolvedValue({ pk: 1, title: "New ad" });

        auth.registration.mockResolvedValue({});
        auth.authentication.mockResolvedValue({
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });
    });

    afterEach(() => {
        act(() => {
            jest.runOnlyPendingTimers();
        });
        jest.useRealTimers();
        jest.restoreAllMocks();
    });

    test("should render app shell and load public ads", async () => {
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
            expect(api.getAds).toHaveBeenCalledTimes(1);
        });

        expect(screen.getByText("Main")).toBeInTheDocument();
    });

    test("should restore authorization from persisted credentials", async () => {
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
            expect(api.getUserInfo).toHaveBeenCalledWith(
                "user@example.com",
                "password123"
            );
        });

        await waitFor(() => {
            expect(api.getUsersAds).toHaveBeenCalledWith(
                "user@example.com",
                "password123"
            );
        });

        await waitFor(() => {
            expect(api.getHiddenAds).toHaveBeenCalledWith(
                "user@example.com",
                "password123"
            );
        });

        expect(screen.getByTestId("main-authorized")).toHaveTextContent("true");
    });

    test("should render login route", () => {
        renderWithProviders(<App />, {
            route: "/sign-in",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        expect(screen.getByText("Login")).toBeInTheDocument();
    });

    test("should render registration route", () => {
        renderWithProviders(<App />, {
            route: "/sign-up",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        expect(screen.getByText("Registration")).toBeInTheDocument();
    });

    test("should handle successful registration", async () => {
        renderWithProviders(<App />, {
            route: "/sign-up",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        fireEvent.click(screen.getByText("Submit registration"));

        await waitFor(() => {
            expect(auth.registration).toHaveBeenCalledWith({
                username: "new@example.com",
                password: "password123",
                firstName: "Ivan",
                lastName: "Ivanov",
                phone: "+79990000001",
                role: "USER",
            });
        });

        expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
    });

    test("should handle successful authorization", async () => {
        renderWithProviders(<App />, {
            route: "/sign-in",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        fireEvent.click(screen.getByText("Submit login"));

        await waitFor(() => {
            expect(auth.authentication).toHaveBeenCalledWith({
                username: "user@example.com",
                password: "password123",
            });
        });

        expect(mockedNavigate).toHaveBeenCalledWith("/");
    });

    test("should render profile route and update user", async () => {
        renderWithProviders(<App />, {
            route: "/profile",
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
            expect(screen.getByText("UserProfile")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Update user"));

        await waitFor(() => {
            expect(api.updateUser).toHaveBeenCalledWith(
                {
                    firstName: "New",
                    lastName: "Name",
                    phone: "+70000000000",
                },
                "user@example.com",
                "password123"
            );
        });
    });

    test("should update password and sign out", async () => {
        renderWithProviders(<App />, {
            route: "/profile",
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
            expect(screen.getByText("UserProfile")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Update password"));

        await waitFor(() => {
            expect(api.updatePassword).toHaveBeenCalledWith(
                "user@example.com",
                "password123",
                "newPassword123"
            );
        });

        expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
    });

    test("should update user photo", async () => {
        renderWithProviders(<App />, {
            route: "/profile",
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
            expect(screen.getByText("UserProfile")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Update user photo"));

        await waitFor(() => {
            expect(api.updateUserPhoto).toHaveBeenCalled();
        });
    });

    test("should render new ad route and add ad", async () => {
        renderWithProviders(<App />, {
            route: "/newAd",
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
            expect(screen.getByText("NewAdd")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit new ad"));

        await waitFor(() => {
            expect(api.addAd).toHaveBeenCalled();
        });
    });

    test("should render single page route and open/close popups", async () => {
        renderWithProviders(<App />, {
            route: "/ads/1",
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
            expect(screen.getByText("SinglePage")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Open edit popup"));
        fireEvent.click(screen.getByText("Open edit photo popup"));
        fireEvent.click(screen.getByText("Open comment popup"));
        fireEvent.click(screen.getByText("Close all popups"));

        expect(screen.getByText("PopupNavigation")).toBeInTheDocument();
    });

    test("should open and close navigation popup", async () => {
        renderWithProviders(<App />, {
            route: "/",
            preloadedState: {
                ads: [],
                adsDefault: [],
                userAds: [],
                userInfo: {},
            },
        });

        fireEvent.click(screen.getByText("Open popup"));
        expect(screen.getByTestId("popup-open")).toHaveTextContent("true");

        fireEvent.click(screen.getByText("Close popup nav"));
        expect(screen.getByTestId("popup-open")).toHaveTextContent("false");
    });

    test("should sign out from popup logout", async () => {
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
            expect(screen.getByText("PopupNavigation")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Popup logout"));

        expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
    });

    test("should sign out on unauthorized hidden ads error", async () => {
        api.getUserInfo.mockResolvedValue({
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });
        api.getUsersAds.mockResolvedValue({ results: [] });
        api.getHiddenAds.mockRejectedValue("401 Unauthorized");

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
            expect(api.getHiddenAds).toHaveBeenCalled();
        });

        await waitFor(() => {
            expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
        });
    });

    test("should sign out on unauthorized update user error", async () => {
        api.updateUser.mockRejectedValue("401 Unauthorized");

        renderWithProviders(<App />, {
            route: "/profile",
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
            expect(screen.getByText("UserProfile")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Update user"));

        await waitFor(() => {
            expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
        });
    });

    test("should sign out on unauthorized add ad error", async () => {
        api.addAd.mockRejectedValue("401 Unauthorized");

        renderWithProviders(<App />, {
            route: "/newAd",
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
            expect(screen.getByText("NewAdd")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Submit new ad"));

        await waitFor(() => {
            expect(mockedNavigate).toHaveBeenCalledWith("/sign-in");
        });
    });
});