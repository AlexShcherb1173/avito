import React from "react";
import { render, fireEvent } from "@testing-library/react";
import Buttons from "./Buttons";

describe("components/buttons/Buttons", () => {
    test("should render edit and delete buttons", () => {
        const { container } = render(
            <Buttons
                onOpen={jest.fn()}
                onSubmit={jest.fn()}
                className="wrapper"
                classButton="comment-button"
            />
        );

        expect(container.querySelector(".edit")).toBeInTheDocument();
        expect(container.querySelector(".delite")).toBeInTheDocument();
    });

    test("should call onOpen for edit button", () => {
        const onOpen = jest.fn();
        const { container } = render(
            <Buttons onOpen={onOpen} onSubmit={jest.fn()} className="x" classButton="y" />
        );

        fireEvent.click(container.querySelector(".edit"));
        expect(onOpen).toHaveBeenCalledTimes(1);
    });

    test("should call onSubmit for delete button", () => {
        const onSubmit = jest.fn();
        const { container } = render(
            <Buttons onOpen={jest.fn()} onSubmit={onSubmit} className="x" classButton="y" />
        );

        fireEvent.click(container.querySelector(".delite"));
        expect(onSubmit).toHaveBeenCalledTimes(1);
    });
});