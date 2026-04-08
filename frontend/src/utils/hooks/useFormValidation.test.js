import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import useFormValidation from "./useFormValidation";

function TestComponent() {
    const {
        values,
        handleChange,
        errors,
        isValid,
        resetForm,
        setValues,
    } = useFormValidation();

    return (
        <div>
            <form>
                <input
                    name="username"
                    type="email"
                    required
                    value={values.username || ""}
                    onChange={handleChange}
                    placeholder="username"
                />
                <input
                    name="password"
                    type="password"
                    required
                    minLength="8"
                    value={values.password || ""}
                    onChange={handleChange}
                    placeholder="password"
                />
            </form>

            <div data-testid="username-value">{values.username || ""}</div>
            <div data-testid="password-value">{values.password || ""}</div>
            <div data-testid="username-error">{errors.username || ""}</div>
            <div data-testid="password-error">{errors.password || ""}</div>
            <div data-testid="is-valid">{String(isValid)}</div>

            <button
                type="button"
                onClick={() =>
                    resetForm(
                        { username: "reset@example.com" },
                        { username: "reset error" },
                        true
                    )
                }
            >
                Reset form
            </button>

            <button
                type="button"
                onClick={() => setValues({ username: "manual@example.com" })}
            >
                Set values
            </button>
        </div>
    );
}

describe("utils/hooks/useFormValidation", () => {
    test("should update values on input change", () => {
        render(<TestComponent />);

        fireEvent.change(screen.getByPlaceholderText("username"), {
            target: { name: "username", value: "user@example.com" },
        });

        expect(screen.getByTestId("username-value")).toHaveTextContent(
            "user@example.com"
        );
    });

    test("should set validation error for invalid field", () => {
        render(<TestComponent />);

        fireEvent.change(screen.getByPlaceholderText("username"), {
            target: { name: "username", value: "invalid-email" },
        });

        expect(screen.getByTestId("username-error").textContent.length).toBeGreaterThan(0);
    });

    test("should set isValid to true for valid form", () => {
        render(<TestComponent />);

        fireEvent.change(screen.getByPlaceholderText("username"), {
            target: { name: "username", value: "user@example.com" },
        });

        fireEvent.change(screen.getByPlaceholderText("password"), {
            target: { name: "password", value: "password123" },
        });

        expect(screen.getByTestId("is-valid")).toHaveTextContent("true");
    });

    test("should reset form with resetForm", () => {
        render(<TestComponent />);

        fireEvent.click(screen.getByRole("button", { name: "Reset form" }));

        expect(screen.getByTestId("username-value")).toHaveTextContent(
            "reset@example.com"
        );
        expect(screen.getByTestId("username-error")).toHaveTextContent(
            "reset error"
        );
        expect(screen.getByTestId("is-valid")).toHaveTextContent("true");
    });

    test("should set values manually with setValues", () => {
        render(<TestComponent />);

        fireEvent.click(screen.getByRole("button", { name: "Set values" }));

        expect(screen.getByTestId("username-value")).toHaveTextContent(
            "manual@example.com"
        );
    });
});