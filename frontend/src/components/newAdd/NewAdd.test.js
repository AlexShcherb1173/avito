import React from "react";
import { fireEvent, render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import NewAdd from "./NewAdd";

jest.mock("../addAd/AddAd", () => {
    return function MockAddAd({ handleAddAd }) {
        return (
            <div>
                <button
                    onClick={() =>
                        handleAddAd({
                            image: new File(["img"], "ad.jpg", { type: "image/jpeg" }),
                            title: "New ad title",
                            price: "25000",
                            description: "New ad description",
                        })
                    }
                >
                    mocked-add-ad-submit
                </button>
            </div>
        );
    };
});

describe("components/newAdd/NewAdd", () => {
    test("should render without crashing", () => {
        render(
            <MemoryRouter>
                <NewAdd
                    handleAddAd={jest.fn()}
                    isLoading={false}
                    userAds={[]}
                />
            </MemoryRouter>
        );

        expect(document.body).toBeInTheDocument();
    });

    test("should call handleAddAd from nested form component", () => {
        const handleAddAd = jest.fn();

        render(
            <MemoryRouter>
                <NewAdd
                    handleAddAd={handleAddAd}
                    isLoading={false}
                    userAds={[]}
                />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", { name: "mocked-add-ad-submit" }));

        expect(handleAddAd).toHaveBeenCalledTimes(1);
        expect(handleAddAd).toHaveBeenCalledWith({
            image: expect.any(File),
            title: "New ad title",
            price: "25000",
            description: "New ad description",
        });
    });
});