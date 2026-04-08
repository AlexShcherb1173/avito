import React, { useState, useEffect, useCallback } from "react";
import { Route, Routes, useNavigate } from "react-router-dom";
import { connect } from "react-redux";

import auth from "../../utils/auth";
import api from "../../utils/api";

import Header from "../header/Header";
import Main from "../main/Main";
import Footer from "../footer/Footer";
import Registration from "../registration/Registration";
import Login from "../login/Login";
import UserProfile from "../userProfile/UserProfile";
import SinglePage from "../singlePage/SinglePage";
import PopupNavigation from "../popopNavigation/PopupNavigation";
import NewAdd from "../newAdd/NewAdd";
import ProtectedRoute from "../protectedRoute/ProtectedRoute";

import { loadedUser, userAuth, userLogOut } from "../../redux/actions";
import { REDUCERS as adsReducers } from "../../redux/reducers/ads/ads";
import { REDUCERS as userAdsReducers } from "../../redux/reducers/ads/userAds";
import { REDUCERS as defaultAdsReducers } from "../../redux/reducers/ads/adsDefault";

function App({
                 onLoadUser,
                 onUserAuth,
                 onLogOut,
                 onLoadUserAds,
                 onLoadAds,
                 onAddAds,
                 onLoadDefaultAds,
                 userInfo: storeUserInfo,
                 ads: storeAds,
                 adsDefault: storeAdsDefault,
                 userAds: storeUserAds,
             }) {
    const navigate = useNavigate();

    const [isAuthorized, setIsAuthorized] = useState(false);
    const [isAuthChecked, setIsAuthChecked] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    // user
    const [userInfo, setUserInfo] = useState(
        storeUserInfo && typeof storeUserInfo === "object" ? storeUserInfo : {}
    );
    const [username, setUsername] = useState(storeUserInfo?.username || "");
    const [password, setPassword] = useState(storeUserInfo?.password || "");
    const [firstName, setFirstName] = useState(storeUserInfo?.firstName || "");
    const [lastName, setLastName] = useState(storeUserInfo?.lastName || "");
    const [phone, setPhone] = useState(storeUserInfo?.phone || "");

    // ads
    const [ad, setAd] = useState("");
    const [ads, setAds] = useState(Array.isArray(storeAds) ? storeAds : []);
    const [, setAdsDefault] = useState(
        Array.isArray(storeAdsDefault) ? storeAdsDefault : []
    );
    const [userAds, setUserAds] = useState(
        Array.isArray(storeUserAds) ? storeUserAds : []
    );
    const [visiableAds, setVisiableAds] = useState(4);

    // popups
    const [isPopupNavigatorOpen, setIsPopupNavigatorOpen] = useState(false);
    const [isUserPhotoPopupOpen, setIsUserPhotoPopupOpen] = useState(false);
    const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
    const [isEditPhotoPopupOpen, setIsEditPhotoPopupOpen] = useState(false);
    const [isComPopupOpen, setIsComPopupOpen] = useState(false);

    const handleUnauthorized = useCallback(() => {
        onLogOut();
        setAd("");
        setAds([]);
        setUserAds([]);
        setUserInfo({});
        setUsername("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setPhone("");
        setIsAuthorized(false);
    }, [onLogOut]);

    const signOut = useCallback(() => {
        handleUnauthorized();
        navigate("/sign-in");
    }, [handleUnauthorized, navigate]);

    const isUnauthorizedError = (error) => {
        const errorText = String(error || "");
        return (
            errorText.includes("401") ||
            errorText.toLowerCase().includes("unauthorized")
        );
    };

    function searcAd(cards, card) {
        return (cards || []).filter((value) =>
            value.title.toLowerCase().includes(card.toLowerCase())
        );
    }

    function showMoreAds() {
        setVisiableAds((prevValue) => prevValue + 2);
    }

    // Проверка сохранённых credentials при старте приложения.
    // ВАЖНО: эффект не зависит от storeUserInfo, иначе после onLoadUser()
    // произойдёт новый ререндер -> новый storeUserInfo -> бесконечный цикл.
    useEffect(() => {
        if (!username || !password) {
            setIsAuthorized(false);
            setIsAuthChecked(true);
            return;
        }

        setIsLoading(true);

        api
            .getUserInfo(username, password)
            .then((userData) => {
                const mergedUser = {
                    ...userData,
                    username,
                    password,
                };

                setUserInfo(mergedUser);
                setFirstName(userData?.firstName || "");
                setLastName(userData?.lastName || "");
                setPhone(userData?.phone || "");

                onLoadUser(mergedUser);
                setIsAuthorized(true);
            })
            .catch((error) => {
                console.log("Ошибка восстановления авторизации:", error);
                handleUnauthorized();
            })
            .finally(() => {
                setIsLoading(false);
                setIsAuthChecked(true);
            });
    }, [username, password, onLoadUser, handleUnauthorized]);

    // Загрузка данных авторизованного пользователя
    useEffect(() => {
        if (!isAuthorized || !isAuthChecked) {
            return;
        }

        setIsLoading(true);

        Promise.all([
            api.getUsersAds(username, password),
            api.getUserInfo(username, password),
        ])
            .then(([usersAdsResponse, userInformation]) => {
                const normalizedUser = {
                    ...userInformation,
                    username,
                    password,
                };

                const normalizedUserAds = usersAdsResponse?.results || [];

                onLoadUser(normalizedUser);
                onLoadUserAds(normalizedUserAds);

                setUserAds(normalizedUserAds);
                setUserInfo(normalizedUser);
                setFirstName(userInformation?.firstName || "");
                setLastName(userInformation?.lastName || "");
                setPhone(userInformation?.phone || "");
                console.log("usersAdsResponse", usersAdsResponse);
            })
            .catch((error) => {
                console.log("error", error);

                if (isUnauthorizedError(error)) {
                    signOut();
                }
            })
            .finally(() => {
                setTimeout(() => setIsLoading(false), 700);
            });
    }, [
        isAuthorized,
        isAuthChecked,
        username,
        password,
        onLoadUser,
        onLoadUserAds,
        signOut,
    ]);

    // Загрузка объявлений
    useEffect(() => {
        if (!isAuthChecked) {
            return;
        }

        setIsLoading(true);

        if (isAuthorized) {
            api
                .getHiddenAds(username, password)
                .then((response) => {
                    const normalizedAds = response?.results || [];
                    setAds(normalizedAds);
                    onLoadAds(normalizedAds);
                })
                .catch((error) => {
                    console.log("error", error);

                    if (isUnauthorizedError(error)) {
                        signOut();
                    }
                })
                .finally(() => setTimeout(() => setIsLoading(false), 500));
        } else {
            api
                .getAds()
                .then((data) => {
                    const normalizedAds = data?.results || [];
                    setAdsDefault(normalizedAds);
                    onLoadDefaultAds(normalizedAds);
                })
                .catch((error) => console.log("error", error))
                .finally(() => setTimeout(() => setIsLoading(false), 500));
        }
    }, [
        isAuthorized,
        isAuthChecked,
        username,
        password,
        onLoadAds,
        onLoadDefaultAds,
        signOut,
    ]);

    const filteredAds = isAuthorized
        ? searcAd(storeAds, ad)
        : searcAd(storeAdsDefault, ad);

    const handleRegistration = ({
                                    username,
                                    password,
                                    firstName,
                                    lastName,
                                    phone,
                                    role,
                                }) => {
        setIsLoading(true);

        auth
            .registration({ username, password, firstName, lastName, phone, role })
            .then(() => {
                navigate("/sign-in");
            })
            .catch((error) => {
                setIsAuthorized(false);

                if (error === "User with this email already exists") {
                    console.log("Пользователь с таким email уже существует");
                    return;
                }

                console.log("Ошибка регистрации:", error);
            })
            .finally(() => {
                setTimeout(() => setIsLoading(false), 700);
            });
    };

    const handleAuthorization = (data) => {
        setIsLoading(true);

        auth
            .authentication(data)
            .then((res) => {
                const nextUsername = data.username;
                const nextPassword = data.password;

                const normalizedUser = {
                    ...res,
                    username: nextUsername,
                    password: nextPassword,
                };

                onUserAuth({ username: nextUsername, password: nextPassword });
                onLoadUser(normalizedUser);

                setUsername(nextUsername);
                setPassword(nextPassword);
                setUserInfo(normalizedUser);
                setFirstName(res?.firstName || "");
                setLastName(res?.lastName || "");
                setPhone(res?.phone || "");
                setIsAuthorized(true);

                navigate("/");
            })
            .catch((error) => {
                setIsAuthorized(false);

                if (error === "Invalid credentials") {
                    console.log("Вы ввели неправильный логин или пароль.");
                    return;
                }

                if (error === "Все поля должны быть заполнены") {
                    console.log("Все поля должны быть заполнены");
                    return;
                }

                console.log("Ошибка авторизации:", error);
            })
            .finally(() => {
                setTimeout(() => setIsLoading(false), 700);
            });
    };

    const handleUpdateUser = ({ firstName, lastName, phone }) => {
        api
            .updateUser(
                {
                    firstName: `${firstName}`,
                    lastName: `${lastName}`,
                    phone: `${phone}`,
                },
                username,
                password
            )
            .then((res) => {
                const updatedUser = {
                    ...userInfo,
                    firstName: res.firstName,
                    lastName: res.lastName,
                    phone: res.phone,
                    username,
                    password,
                };

                onLoadUser(updatedUser);
                setUserInfo(updatedUser);
                setFirstName(res.firstName || "");
                setLastName(res.lastName || "");
                setPhone(res.phone || "");
            })
            .catch((error) => {
                console.log("error", error);

                if (isUnauthorizedError(error)) {
                    signOut();
                }
            });
    };

    const handleUpdatePassword = (newPassword) => {
        api
            .updatePassword(username, password, newPassword)
            .then(() => {
                signOut();
            })
            .catch((error) => {
                console.log("error", error);

                if (isUnauthorizedError(error)) {
                    signOut();
                }
            });
    };

    const handleUpdateUserPhoto = (image) => {
        api
            .updateUserPhoto(image, username, password)
            .then((res) => {
                const updatedUser = {
                    ...userInfo,
                    ...res,
                    username,
                    password,
                };

                setUserInfo(updatedUser);
                onLoadUser(updatedUser);
            })
            .catch((error) => {
                console.log("error", error);

                if (isUnauthorizedError(error)) {
                    signOut();
                }
            });
    };

    const handleAddAd = async (data) => {
        setIsLoading(true);

        try {
            const newAd = await api.addAd(data, username, password);

            setAds((prevAds) => [newAd, ...(prevAds || [])]);
            setUserAds((prevUserAds) => [newAd, ...(prevUserAds || [])]);

            onAddAds(newAd);

            return newAd;
        } catch (error) {
            console.log("error", error);

            if (isUnauthorizedError(error)) {
                signOut();
            }

            throw error;
        } finally {
            setTimeout(() => setIsLoading(false), 500);
        }
    };

    const handleOpenPopup = () => {
        setIsPopupNavigatorOpen(true);
    };

    const handleOpenUserPhotoPopup = () => {
        setIsUserPhotoPopupOpen(true);
    };

    const handleOpenEditPopup = () => {
        setIsEditPopupOpen(true);
    };

    const handleOpenEditPhotoPopup = () => {
        setIsEditPhotoPopupOpen(true);
    };

    const handleEditCommPopupOpen = () => {
        setIsComPopupOpen(true);
    };

    const closePopup = () => {
        setIsPopupNavigatorOpen(false);
        setIsUserPhotoPopupOpen(false);
        setIsEditPopupOpen(false);
        setIsEditPhotoPopupOpen(false);
        setIsComPopupOpen(false);
    };

    useEffect(() => {
        const handleEscClose = (event) => {
            if (event.key === "Escape") {
                closePopup();
            }
        };

        const handleCloseByOverlay = (evt) => {
            if (
                evt.target.classList.contains("popupNavigation_is-opened") ||
                evt.target.classList.contains("popupNavigation")
            ) {
                closePopup();
            }
        };

        document.addEventListener("click", handleCloseByOverlay);
        document.addEventListener("keydown", handleEscClose);

        return () => {
            document.removeEventListener("click", handleCloseByOverlay);
            document.removeEventListener("keydown", handleEscClose);
        };
    }, []);

    if (!isAuthChecked) {
        return (
            <div className="app">
                <Header
                    onOpen={handleOpenPopup}
                    isAuthorized={false}
                    signOut={signOut}
                />
                {isLoading ? null : null}
                <Footer />
            </div>
        );
    }

    return (
        <div className="app">
            <Header
                onOpen={handleOpenPopup}
                isAuthorized={isAuthorized}
                signOut={signOut}
            />

            <Routes>
                <Route
                    exact
                    path="/sign-in"
                    element={
                        <Login
                            handleAuthorization={handleAuthorization}
                            isLoading={isLoading}
                        />
                    }
                />

                <Route
                    exact
                    path="/sign-up"
                    element={<Registration handleRegistration={handleRegistration} />}
                />

                <Route
                    exact
                    path="/profile"
                    element={
                        <ProtectedRoute user={isAuthorized}>
                            <UserProfile
                                isAuthorized={isAuthorized}
                                isOpen={isUserPhotoPopupOpen}
                                onOpen={handleOpenUserPhotoPopup}
                                onClose={closePopup}
                                userAds={userAds}
                                isLoading={isLoading}
                                handleUpdateUser={handleUpdateUser}
                                handleUpdatePassword={handleUpdatePassword}
                                handleUpdateUserPhoto={handleUpdateUserPhoto}
                                visiableAds={visiableAds}
                                showMoreAds={showMoreAds}
                            />
                        </ProtectedRoute>
                    }
                />

                <Route
                    exact
                    path="/ads/:id"
                    element={
                        <ProtectedRoute user={isAuthorized}>
                            <SinglePage
                                isEditPopupOpen={isEditPopupOpen}
                                isEditPhotoPopupOpen={isEditPhotoPopupOpen}
                                isComPopupOpen={isComPopupOpen}
                                handleEditCommPopupOpen={handleEditCommPopupOpen}
                                handleOpenEditPopup={handleOpenEditPopup}
                                handleOpenEditPhotoPopup={handleOpenEditPhotoPopup}
                                onClose={closePopup}
                                isAuthorized={isAuthorized}
                                setIsLoading={setIsLoading}
                                isLoading={isLoading}
                                user={userInfo.id}
                                setAds={setAds}
                                username={username}
                                password={password}
                                firstName={firstName}
                                lastName={lastName}
                                phone={phone}
                            />
                        </ProtectedRoute>
                    }
                />

                <Route
                    exact
                    path="/profile/ads/:id/"
                    element={
                        <ProtectedRoute user={isAuthorized}>
                            <SinglePage
                                isEditPopupOpen={isEditPopupOpen}
                                isEditPhotoPopupOpen={isEditPhotoPopupOpen}
                                isComPopupOpen={isComPopupOpen}
                                handleEditCommPopupOpen={handleEditCommPopupOpen}
                                handleOpenEditPopup={handleOpenEditPopup}
                                handleOpenEditPhotoPopup={handleOpenEditPhotoPopup}
                                onClose={closePopup}
                                isAuthorized={isAuthorized}
                                setIsLoading={setIsLoading}
                                isLoading={isLoading}
                                user={userInfo.id}
                                setAds={setAds}
                                username={username}
                                password={password}
                            />
                        </ProtectedRoute>
                    }
                />

                <Route
                    exact
                    path="/newAd"
                    element={
                        <ProtectedRoute user={isAuthorized}>
                            <NewAdd
                                handleAddAd={handleAddAd}
                                isLoading={isLoading}
                                userAds={userAds}
                            />
                        </ProtectedRoute>
                    }
                />

                <Route
                    exact
                    path="/"
                    element={
                        <Main
                            isAuthorized={isAuthorized}
                            adsDefault={filteredAds}
                            ads={filteredAds}
                            isLoading={isLoading}
                            ad={ad}
                            setAd={setAd}
                            showMoreAds={showMoreAds}
                            visiableAds={visiableAds}
                        />
                    }
                />
            </Routes>

            <Footer />

            <PopupNavigation
                onClose={closePopup}
                isOpen={isPopupNavigatorOpen}
                logOut={signOut}
            />
        </div>
    );
}

const ConnectApp = connect(
    (state) => ({ ...state }),
    (dispatch) => ({
        onLoadUser(user) {
            dispatch(loadedUser(user));
        },
        onUserAuth(user) {
            dispatch(userAuth(user));
        },
        onLogOut() {
            dispatch(userLogOut());
        },
        onLoadUserAds(ads) {
            dispatch(userAdsReducers.LOAD(ads));
        },
        onAddUserAds(newAd) {
            dispatch(userAdsReducers.ADD(newAd));
        },
        onLoadAds(ads) {
            dispatch(adsReducers.LOAD(ads));
        },
        onAddAds(newAd) {
            dispatch(adsReducers.ADD(newAd));
        },
        onLoadDefaultAds(ads) {
            dispatch(defaultAdsReducers.LOAD(ads));
        },
        onAddDefaultAds(newAd) {
            dispatch(defaultAdsReducers.ADD(newAd));
        },
    })
)(App);

export default ConnectApp;