import base64 from "base-64";

const API_URL = "http://localhost:8081";

class Api {
    constructor(options) {
        this._url = options.url;
    }

    _getAuthHeader(username, password) {
        return {
            Authorization: "Basic " + base64.encode(`${username}:${password}`),
        };
    }

    _parseResponse = async (res) => {
        const contentType = res.headers.get("content-type") || "";

        if (!res.ok) {
            let errorMessage = `Error: ${res.status}`;

            try {
                if (contentType.includes("application/json")) {
                    const errorData = await res.json();
                    errorMessage =
                        errorData?.message ||
                        errorData?.error ||
                        errorData?.details ||
                        `Error: ${res.status}`;
                } else {
                    const errorText = await res.text();
                    errorMessage = errorText || `Error: ${res.status}`;
                }
            } catch {
                errorMessage = `Error: ${res.status}`;
            }

            return Promise.reject(errorMessage);
        }

        if (res.status === 204) {
            return null;
        }

        if (contentType.includes("application/json")) {
            return res.json();
        }

        return res.text();
    };

    _checkStatusOnly = async (res) => {
        if (!res.ok) {
            return Promise.reject(`Error: ${res.status}`);
        }
        return null;
    };

    // user
    getUserInfo(username, password) {
        return fetch(`${this._url}/users/me`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
        }).then(this._parseResponse);
    }

    getUsersAds(username, password) {
        return fetch(`${this._url}/ads/me`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
        }).then(this._parseResponse);
    }

    updateUser(userInfo, username, password) {
        return fetch(`${this._url}/users/me`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
            body: JSON.stringify(userInfo),
        }).then(this._parseResponse);
    }

    updateUserPhoto(image, username, password) {
        const formData = new FormData();
        formData.append("image", image);

        return fetch(`${this._url}/users/me/image`, {
            method: "PATCH",
            headers: {
                ...this._getAuthHeader(username, password),
            },
            body: formData,
        }).then(this._parseResponse);
    }

    getUserPhoto(imagePath, username, password) {
        return fetch(`${this._url}${imagePath}`, {
            method: "GET",
            headers: {
                ...this._getAuthHeader(username, password),
            },
        }).then(async (res) => {
            if (!res.ok) {
                return Promise.reject(`Error: ${res.status}`);
            }
            return res.blob();
        });
    }

    updatePassword(username, password, newPassword) {
        return fetch(`${this._url}/users/set_password`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
            body: JSON.stringify({
                currentPassword: password,
                newPassword: newPassword,
            }),
        }).then(this._parseResponse);
    }

    // comments
    getComments(adId, username, password) {
        return fetch(`${this._url}/ads/${adId}/comments`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
        }).then(this._parseResponse);
    }

    addComment(adId, data, username, password) {
        return fetch(`${this._url}/ads/${adId}/comments`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
            body: JSON.stringify(data),
        }).then(this._parseResponse);
    }

    editComment(adId, commentId, data, username, password) {
        return fetch(`${this._url}/ads/${adId}/comments/${commentId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
            body: JSON.stringify(data),
        }).then(this._parseResponse);
    }

    deleteComment(adId, commentId, username, password) {
        return fetch(`${this._url}/ads/${adId}/comments/${commentId}`, {
            method: "DELETE",
            headers: {
                ...this._getAuthHeader(username, password),
            },
        }).then(this._checkStatusOnly);
    }

    // ads
    getAds() {
        return fetch(`${this._url}/ads`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }).then(this._parseResponse);
    }

    getHiddenAds(username, password) {
        return fetch(`${this._url}/ads`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
        }).then(this._parseResponse);
    }

    getAd(id, username, password) {
        return fetch(`${this._url}/ads/${id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
        }).then(this._parseResponse);
    }

    addAd({ image, title, price, description }, username, password) {
        const formData = new FormData();

        formData.append("image", image);
        formData.append(
            "properties",
            new Blob(
                [
                    JSON.stringify({
                        title: title.trim(),
                        price: Number(price),
                        description: description.trim(),
                    }),
                ],
                { type: "application/json" }
            )
        );

        return fetch(`${this._url}/ads`, {
            method: "POST",
            headers: {
                ...this._getAuthHeader(username, password),
            },
            body: formData,
        }).then(this._parseResponse);
    }

    editAdd(id, data, username, password) {
        return fetch(`${this._url}/ads/${id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                ...this._getAuthHeader(username, password),
            },
            body: JSON.stringify(data),
        }).then(this._parseResponse);
    }

    editAddPhoto(id, image, username, password) {
        const formData = new FormData();
        formData.append("image", image);

        return fetch(`${this._url}/ads/${id}/image`, {
            method: "PATCH",
            headers: {
                ...this._getAuthHeader(username, password),
            },
            body: formData,
        }).then(this._parseResponse);
    }

    deleteAdd(id, username, password) {
        return fetch(`${this._url}/ads/${id}`, {
            method: "DELETE",
            headers: {
                ...this._getAuthHeader(username, password),
            },
        }).then(this._checkStatusOnly);
    }
}

const api = new Api({
    url: API_URL,
});

export default api;