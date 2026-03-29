import api from "./api";
import base64 from "base-64";

describe("utils/api", () => {
    beforeEach(() => {
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("should get user info with basic auth header", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                id: 1,
                email: "user@example.com",
            }),
        });

        const result = await api.getUserInfo("user@example.com", "password123");

        expect(fetch).toHaveBeenCalledTimes(1);
        expect(fetch).toHaveBeenCalledWith("/api/users/me", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Basic ${base64.encode("user@example.com:password123")}`,
            },
        });

        expect(result).toEqual({
            id: 1,
            email: "user@example.com",
        });
    });

    test("should update password with patch request", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({ success: true }),
        });

        const result = await api.updatePassword(
            "user@example.com",
            "oldPassword123",
            "newPassword123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/users/set_password", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Basic ${base64.encode(
                    "user@example.com:oldPassword123"
                )}`,
            },
            body: JSON.stringify({
                currentPassword: "oldPassword123",
                newPassword: "newPassword123",
            }),
        });

        expect(result).toEqual({ success: true });
    });

    test("should get public ads without auth header", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                count: 1,
                results: [{ pk: 1, title: "Test ad" }],
            }),
        });

        const result = await api.getAds();

        expect(fetch).toHaveBeenCalledWith("/api/ads", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        expect(result).toEqual({
            count: 1,
            results: [{ pk: 1, title: "Test ad" }],
        });
    });

    test("should add comment with auth header", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                pk: 10,
                text: "Created comment text",
            }),
        });

        const payload = { text: "Created comment text" };

        const result = await api.addComment(
            5,
            payload,
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/ads/5/comments", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Basic ${base64.encode("user@example.com:password123")}`,
            },
            body: JSON.stringify(payload),
        });

        expect(result).toEqual({
            pk: 10,
            text: "Created comment text",
        });
    });

    test("should reject with backend message for json error", async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            status: 400,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                message: "Validation failed",
            }),
        });

        await expect(api.getAds()).rejects.toBe("Validation failed");
    });

    test("should reject with text error for plain text response", async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            status: 500,
            headers: {
                get: () => "text/plain",
            },
            text: async () => "Internal server error",
        });

        await expect(api.getAds()).rejects.toBe("Internal server error");
    });

    test("should return null for 204 response", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 204,
            headers: {
                get: () => "",
            },
        });

        const result = await api.deleteAdd(1, "user@example.com", "password123");

        expect(result).toBeNull();
    });
});