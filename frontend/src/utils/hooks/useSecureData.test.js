import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import useSecureData from "./useSecureData";
import api from "../api";

jest.mock("../api", () => ({
    __esModule: true,
    default: {
        getUserPhoto: jest.fn(),
    },
}));

class MockFileReader {
    readAsDataURL(blob) {
        if (this.onload) {
            this.onload({
                target: { result: `data:image/mock;base64,${blob.mockValue}` },
            });
        }
    }
}

function TestComponent({ path, username, password }) {
    const result = useSecureData(path, username, password);

    if (result === null) {
        return <div>No image</div>;
    }

    const [imageData] = result;
    return <div>{imageData || "Empty image"}</div>;
}

describe("utils/hooks/useSecureData", () => {
    beforeEach(() => {
        jest.clearAllMocks();
        global.FileReader = MockFileReader;
    });

    test("should return null when userImagePath is empty", () => {
        render(
            <TestComponent path="" username="user@example.com" password="password123" />
        );

        expect(screen.getByText("No image")).toBeInTheDocument();
        expect(api.getUserPhoto).not.toHaveBeenCalled();
    });

    test("should request user photo and set image data", async () => {
        api.getUserPhoto.mockResolvedValue({ mockValue: "ABC123" });

        render(
            <TestComponent
                path="/images/photo.jpg"
                username="user@example.com"
                password="password123"
            />
        );

        await waitFor(() => {
            expect(api.getUserPhoto).toHaveBeenCalledWith(
                "/images/photo.jpg",
                "user@example.com",
                "password123"
            );
        });

        await waitFor(() => {
            expect(
                screen.getByText("data:image/mock;base64,ABC123")
            ).toBeInTheDocument();
        });
    });
});