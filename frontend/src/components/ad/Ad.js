import React from "react";

const BACKEND_URL = "http://localhost:8081";

function normalizeImageUrl(image) {
    if (!image) {
        return "";
    }

    if (image.startsWith("http://") || image.startsWith("https://")) {
        return image;
    }

    return `${BACKEND_URL}${image}`;
}

function Ad({ id, image, title, price }) {
    const imageSrc = normalizeImageUrl(image);

    return (
        <li className="ad" key={id}>
            {image ? (
                <img
                    src={imageSrc}
                    className="ad-img"
                    alt={title || "product img"}
                />
            ) : (
                <div className="ad-img_null" />
            )}

            <div className="ad__description">
                <h2 className="ad__title">{title}</h2>
                <p className="ad__price">{price} &#8381;</p>
            </div>
        </li>
    );
}

export default Ad;