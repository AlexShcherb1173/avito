import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import UserForm from "../userForm/UserForm";
import Preloader from "../preloader/Preloader";

function AddAd({ id, handleAddAd, isLoading }) {
  const [image, setImage] = useState(null);
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [description, setDescription] = useState("");
  const [validationErrors, setValidationErrors] = useState({
    image: "",
    title: "",
    price: "",
    description: "",
  });

  const location = useLocation().pathname;
  const navigate = useNavigate();

  const validateImage = (file) => {
    if (!file) {
      return "Загрузите фотографию";
    }

    if (!file.type.startsWith("image/")) {
      return "Можно загружать только изображения";
    }

    if (file.size > 1000000) {
      return "Размер изображения должен быть меньше 1 МБ";
    }

    return "";
  };

  const validateTitle = (value) => {
    if (!value.trim()) {
      return "Это поле не должно быть пустым";
    }

    if (value.trim().length < 8) {
      return "Минимальное количество символов - 8";
    }

    return "";
  };

  const validatePrice = (value) => {
    if (value === "" || value === null) {
      return "Это поле не должно быть пустым";
    }

    if (Number(value) <= 0) {
      return "Цена должна быть больше 0";
    }

    return "";
  };

  const validateDescription = (value) => {
    if (!value.trim()) {
      return "Это поле не должно быть пустым";
    }

    if (value.trim().length < 8) {
      return "Минимальное количество символов - 8";
    }

    return "";
  };

  const handleImageChange = (e) => {
    const file = e.target.files?.[0] || null;
    const imageError = validateImage(file);

    setValidationErrors((prev) => ({
      ...prev,
      image: imageError,
    }));

    if (!imageError) {
      setImage(file);
    } else {
      setImage(null);
    }
  };

  const handleTitleChange = (e) => {
    const { value } = e.target;
    setTitle(value);

    setValidationErrors((prev) => ({
      ...prev,
      title: validateTitle(value),
    }));
  };

  const handlePriceChange = (e) => {
    const { value } = e.target;
    setPrice(value);

    setValidationErrors((prev) => ({
      ...prev,
      price: validatePrice(value),
    }));
  };

  const handleDescriptionChange = (e) => {
    const { value } = e.target;
    setDescription(value);

    setValidationErrors((prev) => ({
      ...prev,
      description: validateDescription(value),
    }));
  };

  const addNewAd = async (e) => {
    e.preventDefault();

    const nextErrors = {
      image: validateImage(image),
      title: validateTitle(title),
      price: validatePrice(price),
      description: validateDescription(description),
    };

    setValidationErrors(nextErrors);

    const hasErrors = Object.values(nextErrors).some(Boolean);
    if (hasErrors) {
      return;
    }

    try {
      await handleAddAd({
        image,
        title: title.trim(),
        price: Number(price),
        description: description.trim(),
      });

      navigate("/profile");
    } catch (error) {
      console.log("Ошибка при создании объявления:", error);
    }
  };

  const hasFormErrors =
      !!validationErrors.image ||
      !!validationErrors.title ||
      !!validationErrors.price ||
      !!validationErrors.description ||
      !image ||
      !title.trim() ||
      !price ||
      !description.trim();

  return (
      <>
        <UserForm
            id={`${location === "/newAd" ? "" : id}`}
            title={location === "/newAd" ? "Добавить новый товар" : "Изменить товар"}
            buttonText={location === "/newAd" ? "Добавить" : "Изменить"}
            onSubmit={addNewAd}
            errors={hasFormErrors}
        >
          <div className="userForm__form-container userForm__form-box">
            <label className="userForm__label">
              <h2 className="userForm__subtitle">Название</h2>
              <input
                  className="userForm__input"
                  name="title"
                  type="text"
                  minLength="8"
                  maxLength="32"
                  value={title}
                  onChange={handleTitleChange}
              />
              <div
                  className={`input-hidden ${
                      validationErrors.title ? "input-error" : ""
                  }`}
              >
                {validationErrors.title}
              </div>
            </label>

            <label className="userForm__label">
              <h2 className="userForm__subtitle">Изображение</h2>
              <input
                  name="image"
                  className="userForm__input"
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
              />
              <div
                  className={`input-hidden ${
                      validationErrors.image ? "input-error" : ""
                  }`}
              >
                {validationErrors.image}
              </div>
            </label>
          </div>

          <div className="userForm__form-container">
            <label className="userForm__label">
              <h2 className="userForm__subtitle">Цена</h2>
              <input
                  className="userForm__input"
                  type="number"
                  name="price"
                  min="1"
                  max="10000000"
                  value={price}
                  onChange={handlePriceChange}
              />
              <div
                  className={`input-hidden ${
                      validationErrors.price ? "input-error" : ""
                  }`}
              >
                {validationErrors.price}
              </div>
            </label>

            <label className="userForm__label">
              <h2 className="userForm__subtitle">Описание</h2>
              <input
                  className="userForm__input"
                  name="description"
                  type="text"
                  minLength="8"
                  maxLength="64"
                  value={description}
                  onChange={handleDescriptionChange}
              />
              <div
                  className={`input-hidden ${
                      validationErrors.description ? "input-error" : ""
                  }`}
              >
                {validationErrors.description}
              </div>
            </label>
          </div>
        </UserForm>

        {isLoading ? <Preloader /> : null}
      </>
  );
}

export default AddAd;