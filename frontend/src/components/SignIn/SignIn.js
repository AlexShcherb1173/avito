import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import auth from "../../utils/auth";

function SignIn({ setIsLoggedIn }) {
    const navigate = useNavigate();

    const [formValue, setFormValue] = useState({
        username: "",
        password: "",
    });

    const [errorMessage, setErrorMessage] = useState("");

    const handleChange = (evt) => {
        const { name, value } = evt.target;

        setFormValue((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = (evt) => {
        evt.preventDefault();
        setErrorMessage("");

        auth
            .authentication({
                username: formValue.username,
                password: formValue.password,
            })
            .then(() => {
                localStorage.setItem("login", formValue.username);
                localStorage.setItem("password", formValue.password);

                if (typeof setIsLoggedIn === "function") {
                    setIsLoggedIn(true);
                }

                navigate("/");
            })
            .catch((err) => {
                setErrorMessage(typeof err === "string" ? err : "Ошибка авторизации");
                console.error("Login error:", err);
            });
    };

    return (
        <section className="sign-in">
            <div className="sign-in__container">
                <h1 className="sign-in__title">Рады видеть!</h1>

                <form className="sign-in__form" onSubmit={handleSubmit}>
                    <label className="sign-in__label">
                        Логин
                        <input
                            type="email"
                            name="username"
                            value={formValue.username}
                            onChange={handleChange}
                            className="sign-in__input"
                            required
                        />
                    </label>

                    <label className="sign-in__label">
                        Пароль
                        <input
                            type="password"
                            name="password"
                            value={formValue.password}
                            onChange={handleChange}
                            className="sign-in__input"
                            minLength="8"
                            maxLength="16"
                            autoComplete="current-password"
                            required
                        />
                    </label>

                    {errorMessage && (
                        <div className="sign-in__error">
                            {errorMessage}
                        </div>
                    )}

                    <button type="submit" className="sign-in__button">
                        Войти
                    </button>
                </form>

                <Link to="/sign-up" className="sign-in__link">
                    Создать аккаунт
                </Link>
            </div>
        </section>
    );
}

export default SignIn;