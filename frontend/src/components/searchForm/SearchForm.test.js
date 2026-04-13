import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import SearchForm from "./SearchForm";

describe("components/searchForm/SearchForm", () => {
    test("should render input with passed value", () => {
        render(<SearchForm ad="phone" setAd={jest.fn()} />);

        expect(screen.getByPlaceholderText("Поиск")).toHaveValue("phone");
    });

    test("should call setAd on input change", () => {
        const setAd = jest.fn();

        render(<SearchForm ad="" setAd={setAd} />);

        fireEvent.change(screen.getByPlaceholderText("Поиск"), {
            target: { value: "laptop" },
        });

        expect(setAd).toHaveBeenCalledWith("laptop");
    });

    test("should show validation error when input becomes empty", () => {
        const setAd = jest.fn();

        render(<SearchForm ad="start" setAd={setAd} />);

        fireEvent.change(screen.getByPlaceholderText("Поиск"), {
            target: { value: "" },
        });

        expect(setAd).toHaveBeenCalledWith("");
        expect(
            screen.getByText("Нужно ввести ключевое слово")
        ).toBeInTheDocument();
    });

    test("should hide validation error when input is not empty", () => {
        const setAd = jest.fn();

        render(<SearchForm ad="" setAd={setAd} />);

        fireEvent.change(screen.getByPlaceholderText("Поиск"), {
            target: { value: "camera" },
        });

        expect(setAd).toHaveBeenCalledWith("camera");
        expect(
            screen.queryByText("Нужно ввести ключевое слово")
        ).not.toBeInTheDocument();
    });

    test("should apply input-error class when validation error exists", () => {
        const { container } = render(<SearchForm ad="text" setAd={jest.fn()} />);

        fireEvent.change(screen.getByPlaceholderText("Поиск"), {
            target: { value: "" },
        });

        expect(container.querySelector(".input-error")).toBeInTheDocument();
    });
});