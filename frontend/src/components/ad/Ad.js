import React from "react";

function Ad({ id, image, title, price }) {
    const imageSrc = image || "/images/malvestida-u79wy47kvVs-unsplash.jpg";

    return (
        <li className="ad" key={id}>
            {image ? (
                <img src={imageSrc} className="ad-img" alt={title || "product img"} />
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