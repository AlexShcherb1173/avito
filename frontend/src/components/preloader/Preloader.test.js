import React from "react";
import { render } from "@testing-library/react";
import Preloader from "./Preloader";

describe("components/preloader/Preloader", () => {
    test("should render loader container", () => {
        const { container } = render(<Preloader />);

        expect(container.querySelector(".container")).toBeInTheDocument();
        expect(container.querySelector(".loader")).toBeInTheDocument();
    });
});