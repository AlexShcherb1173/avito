import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { connect } from "react-redux";

import api from "../../utils/api";
import CommentContainer from "../commentContainer/CommentContainer";
import EditAdPopup from "../editAdPopup/EditAdPopup";
import Buttons from "../buttons/Buttons";
import Preloader from "../preloader/Preloader";
import EditPhotoAdPopup from "../editPhotoAdPopup/EditPhotoAdPopup";

import { REDUCERS as adsReducers } from "../../redux/reducers/ads/ads";
import { REDUCERS as userAdsReducers } from "../../redux/reducers/ads/userAds";
import { REDUCERS as defaultAdsReducers } from "../../redux/reducers/ads/adsDefault";

const BACKEND_URL = "http://localhost:8081";

function normalizeImageUrl(image) {
    if (!image) {
        return "";
    }

    if (typeof image !== "string") {
        return "";
    }

    if (image.startsWith("http://") || image.startsWith("https://")) {
        return image;
    }

    return `${BACKEND_URL}${image}`;
}

function SinglePage(props) {
    const { id } = useParams();
    const navigate = useNavigate();

    const [ad, setAd] = useState(null);
    const [comments, setComments] = useState([]);

    const username = props.userInfo?.username || "";
    const password = props.userInfo?.password || "";
    const role = props.userInfo?.role || "";
    const currentUserId = props.user;

    const normalizeComments = (commentsData) => {
        if (Array.isArray(commentsData)) {
            return commentsData;
        }
        return commentsData?.results || [];
    };

    const normalizeImageResponse = (imageResponse) => {
        if (!imageResponse) {
            return "";
        }

        if (typeof imageResponse === "string") {
            return imageResponse;
        }

        return imageResponse.url || imageResponse.image || "";
    };

    useEffect(() => {
        if (!props.isAuthorized || !id || !username || !password) {
            return;
        }

        let isCancelled = false;

        props.setIsLoading(true);

        Promise.all([
            api.getComments(id, username, password),
            api.getAd(id, username, password),
        ])
            .then(([commentsData, adData]) => {
                if (isCancelled) {
                    return;
                }

                setComments(normalizeComments(commentsData));
                setAd(adData || null);
            })
            .catch((error) => {
                if (!isCancelled) {
                    console.log("error", error);
                }
            })
            .finally(() => {
                if (!isCancelled) {
                    setTimeout(() => props.setIsLoading(false), 700);
                }
            });

        return () => {
            isCancelled = true;
        };
    }, [id, props.isAuthorized, username, password, props.setIsLoading]);

    function handleEditAdd(data) {
        props.setIsLoading(true);

        api
            .editAdd(id, data, username, password)
            .then((result) => {
                const updatedAd = { ...ad, ...result };

                setAd(updatedAd);

                props.setAds((ads) =>
                    ads.map((item) =>
                        (item.pk || item.id) === (updatedAd.pk || updatedAd.id)
                            ? updatedAd
                            : item
                    )
                );

                props.onUpdateAdFromStore(parseInt(id, 10), result);
            })
            .catch((error) => console.log("error", error))
            .finally(() => setTimeout(() => props.setIsLoading(false), 700));
    }

    function handleEditPhotoAdd(imageFile) {
        api
            .editAddPhoto(id, imageFile, username, password)
            .then((imageResponse) => {
                const updatedImage = normalizeImageResponse(imageResponse);
                const updatedAd = { ...ad, image: updatedImage };

                setAd(updatedAd);

                props.setAds((ads) =>
                    ads.map((item) =>
                        (item.pk || item.id) === (updatedAd.pk || updatedAd.id)
                            ? updatedAd
                            : item
                    )
                );

                props.onUpdateAdFromStore(parseInt(id, 10), { image: updatedImage });
            })
            .catch((error) => console.log("error", error));
    }

    function handleDeleteAdd() {
        api
            .deleteAdd(id, username, password)
            .then(() => {
                props.setAds((ads) =>
                    ads.filter((item) => (item.pk || item.id) !== (ad?.pk || ad?.id))
                );

                props.onDeleteAdFromStore(parseInt(id, 10));
                navigate("/");
            })
            .catch((error) => console.log("error", error));
    }

    function handleAddComment(data) {
        api
            .addComment(id, data, username, password)
            .then((newComment) => {
                setComments((prevComments) => [newComment, ...prevComments]);
            })
            .catch((error) => console.log("error", error));
    }

    function handleDeleteComment(adId, commentId) {
        api
            .deleteComment(adId, commentId, username, password)
            .then(() => {
                setComments((prevComments) =>
                    prevComments.filter((item) => (item.pk || item.id) !== commentId)
                );
            })
            .catch((error) => console.log("error", error));
    }

    const canEdit = username === ad?.email || role === "ADMIN";
    const adImageUrl = normalizeImageUrl(ad?.image);

    return (
        <main className="cardInformation">
            {props.isLoading ? (
                <Preloader />
            ) : !ad ? null : (
                <>
                    <h1 className="cardInformation__title">{ad.title}</h1>

                    <div className="cardInformation__container">
                        {ad.image ? (
                            <div
                                style={{ backgroundImage: `url(${adImageUrl})` }}
                                className="cardInformation__img"
                            >
                                {canEdit ? (
                                    <button
                                        onClick={props.handleOpenEditPhotoPopup}
                                        className="cardInformation__img-change"
                                        type="button"
                                    />
                                ) : null}
                            </div>
                        ) : (
                            <div className="cardInformation__img-null">
                                {canEdit ? (
                                    <button
                                        onClick={props.handleOpenEditPhotoPopup}
                                        className="cardInformation__img-change"
                                        type="button"
                                    />
                                ) : null}
                            </div>
                        )}

                        {canEdit ? (
                            <Buttons
                                user={currentUserId}
                                product={ad}
                                onOpen={props.handleOpenEditPopup}
                                className="buttons"
                                classButton="buttons-item"
                                onSubmit={handleDeleteAdd}
                            />
                        ) : null}

                        <div className="cardInformation__box">
                            <div className="cardInformation__box_second">
                                <p className="cardInformation__contact">
                                    {ad.authorFirstName}&nbsp;
                                    {ad.authorLastName},&nbsp;
                                    {ad.phone}
                                </p>
                            </div>

                            <p className="cardInformation__price">
                                {ad.price} &#8381;
                            </p>
                        </div>

                        <div className="cardInformation__box">
                            <p className="cardInformation__description">{ad.description}</p>
                        </div>

                        <CommentContainer
                            comments={comments}
                            addComment={handleAddComment}
                            deleteComment={handleDeleteComment}
                            setComments={setComments}
                            user={currentUserId}
                            isComPopupOpen={props.isComPopupOpen}
                            handleEditCommPopupOpen={props.handleEditCommPopupOpen}
                            username={username}
                            password={password}
                            role={role}
                            adId={id}
                            onClose={props.onClose}
                        />
                    </div>

                    <EditAdPopup
                        isEditPopupOpen={props.isEditPopupOpen}
                        onClose={props.onClose}
                        handleEditAdd={handleEditAdd}
                        id={id}
                        ad={ad}
                    />

                    <EditPhotoAdPopup
                        id={id}
                        handleEdit={handleEditPhotoAdd}
                        isOpen={props.isEditPhotoPopupOpen}
                        onClose={props.onClose}
                    />
                </>
            )}
        </main>
    );
}

const ConnectedSinglePage = connect(
    (state) => ({ userInfo: { ...state.userInfo } }),
    (dispatch) => ({
        onDeleteAdFromStore(delId) {
            dispatch(defaultAdsReducers.DELETE(delId));
            dispatch(userAdsReducers.DELETE(delId));
            dispatch(adsReducers.DELETE(delId));
        },
        onUpdateAdFromStore(adId, newData) {
            dispatch(defaultAdsReducers.EDIT(adId, newData));
            dispatch(userAdsReducers.EDIT(adId, newData));
            dispatch(adsReducers.EDIT(adId, newData));
        },
    })
)(SinglePage);

export default ConnectedSinglePage;