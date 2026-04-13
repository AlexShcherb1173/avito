import React from "react";
import { render, screen } from "@testing-library/react";
import Ad from "./Ad";

describe("components/ad/Ad", () => {
    test("should render title and price", () => {
        render(<Ad id={1} title="iPhone" price={5000} image="/img.jpg" />);

        expect(screen.getByText("iPhone")).toBeInTheDocument();
        expect(screen.getByText(/5000/)).toBeInTheDocument();
    });

    test("should render normalized image url when image exists", () => {
        render(<Ad id={1} title="iPhone" price={5000} image="/img.jpg" />);

        const image = screen.getByAltText("iPhone");
        expect(image).toBeInTheDocument();
        expect(image).toHaveAttribute("src", "http://localhost:8081/img.jpg");
    });

    test("should render placeholder div when image does not exist", () => {
        const { container } = render(
            <Ad id={1} title="No image ad" price={1000} />
        );

        expect(container.querySelector(".ad-img_null")).toBeInTheDocument();
    });

    test("should use fallback alt text when title does not exist", () => {
        render(<Ad id={1} price={1000} image="/img.jpg" />);

        expect(screen.getByAltText("product img")).toBeInTheDocument();
    });

    test("should keep absolute image url as is", () => {
        render(
            <Ad
                id={1}
                title="MacBook"
                price={150000}
                image="https://cdn.example.com/macbook.jpg"
            />
        );

        const image = screen.getByAltText("MacBook");
        expect(image).toHaveAttribute(
            "src",
            "https://cdn.example.com/macbook.jpg"
        );
    });
});