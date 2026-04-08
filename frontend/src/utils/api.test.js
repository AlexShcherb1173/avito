import api from "./api";
import base64 from "base-64";

describe("utils/api", () => {
    beforeEach(() => {
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    const jsonResponse = (data, ok = true, status = 200) => ({
        ok,
        status,
        headers: {
            get: jest.fn().mockReturnValue("application/json"),
        },
        json: jest.fn().mockResolvedValue(data),
        text: jest.fn(),
        blob: jest.fn(),
    });

    const textResponse = (text, ok = true, status = 200) => ({
        ok,
        status,
        headers: {
            get: jest.fn().mockReturnValue("text/plain"),
        },
        json: jest.fn(),
        text: jest.fn().mockResolvedValue(text),
        blob: jest.fn(),
    });

    test("getUserInfo should call correct endpoint with auth header", async () => {
        fetch.mockResolvedValue(jsonResponse({ id: 1 }));

        const result = await api.getUserInfo("user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/users/me", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
        });
        expect(result).toEqual({ id: 1 });
    });

    test("getUsersAds should call correct endpoint", async () => {
        fetch.mockResolvedValue(jsonResponse({ results: [] }));

        const result = await api.getUsersAds("user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/ads/me", expect.any(Object));
        expect(result).toEqual({ results: [] });
    });

    test("updateUser should send PATCH with json body", async () => {
        fetch.mockResolvedValue(jsonResponse({ firstName: "Ivan" }));

        const payload = { firstName: "Ivan", lastName: "Ivanov" };
        const result = await api.updateUser(payload, "user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/users/me", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
            body: JSON.stringify(payload),
        });
        expect(result).toEqual({ firstName: "Ivan" });
    });

    test("updateUserPhoto should send form data", async () => {
        fetch.mockResolvedValue(jsonResponse({ image: "/img.jpg" }));

        const image = new File(["photo"], "photo.jpg", { type: "image/jpeg" });
        const result = await api.updateUserPhoto(
            image,
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/users/me/image", {
            method: "PATCH",
            headers: {
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
            body: expect.any(FormData),
        });
        expect(result).toEqual({ image: "/img.jpg" });
    });

    test("getUserPhoto should return blob on success", async () => {
        const blob = new Blob(["photo"]);
        fetch.mockResolvedValue({
            ok: true,
            status: 200,
            blob: jest.fn().mockResolvedValue(blob),
        });

        const result = await api.getUserPhoto(
            "/images/1.jpg",
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/images/1.jpg", {
            method: "GET",
            headers: {
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
        });
        expect(result).toBe(blob);
    });

    test("getUserPhoto should reject on error", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 404,
            blob: jest.fn(),
        });

        await expect(
            api.getUserPhoto("/images/1.jpg", "user@example.com", "password123")
        ).rejects.toBe("Error: 404");
    });

    test("updatePassword should send current and new password", async () => {
        fetch.mockResolvedValue(jsonResponse({}));

        await api.updatePassword("user@example.com", "oldPass", "newPass");

        expect(fetch).toHaveBeenCalledWith("/api/users/set_password", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization:
                    "Basic " + base64.encode("user@example.com:oldPass"),
            },
            body: JSON.stringify({
                currentPassword: "oldPass",
                newPassword: "newPass",
            }),
        });
    });

    test("getComments should call comments endpoint", async () => {
        fetch.mockResolvedValue(jsonResponse([{ id: 1 }]));

        const result = await api.getComments(10, "user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/ads/10/comments", expect.any(Object));
        expect(result).toEqual([{ id: 1 }]);
    });

    test("addComment should send POST with body", async () => {
        fetch.mockResolvedValue(jsonResponse({ id: 1, text: "Hi" }));

        const result = await api.addComment(
            10,
            { text: "Hi" },
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/ads/10/comments", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
            body: JSON.stringify({ text: "Hi" }),
        });
        expect(result).toEqual({ id: 1, text: "Hi" });
    });

    test("editComment should send PATCH with body", async () => {
        fetch.mockResolvedValue(jsonResponse({ id: 2, text: "Updated" }));

        const result = await api.editComment(
            10,
            2,
            { text: "Updated" },
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith(
            "/api/ads/10/comments/2",
            expect.objectContaining({
                method: "PATCH",
                body: JSON.stringify({ text: "Updated" }),
            })
        );
        expect(result).toEqual({ id: 2, text: "Updated" });
    });

    test("deleteComment should resolve null on success", async () => {
        fetch.mockResolvedValue({
            ok: true,
            status: 204,
        });

        const result = await api.deleteComment(
            10,
            2,
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith(
            "/api/ads/10/comments/2",
            expect.objectContaining({
                method: "DELETE",
            })
        );
        expect(result).toBeNull();
    });

    test("deleteComment should reject on error status", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 500,
        });

        await expect(
            api.deleteComment(10, 2, "user@example.com", "password123")
        ).rejects.toBe("Error: 500");
    });

    test("getAds should return public ads", async () => {
        fetch.mockResolvedValue(jsonResponse({ results: [] }));

        const result = await api.getAds();

        expect(fetch).toHaveBeenCalledWith("/api/ads", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
        expect(result).toEqual({ results: [] });
    });

    test("getHiddenAds should call ads endpoint with auth", async () => {
        fetch.mockResolvedValue(jsonResponse({ results: [{ pk: 1 }] }));

        const result = await api.getHiddenAds("user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/ads", expect.any(Object));
        expect(result).toEqual({ results: [{ pk: 1 }] });
    });

    test("getAd should call ad endpoint by id", async () => {
        fetch.mockResolvedValue(jsonResponse({ pk: 1 }));

        const result = await api.getAd(1, "user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith("/api/ads/1", expect.any(Object));
        expect(result).toEqual({ pk: 1 });
    });

    test("addAd should send form data", async () => {
        fetch.mockResolvedValue(jsonResponse({ pk: 3, title: "Ad" }));

        const image = new File(["photo"], "photo.jpg", { type: "image/jpeg" });

        const result = await api.addAd(
            {
                image,
                title: "  Ad title  ",
                price: "1000",
                description: "  Description  ",
            },
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/ads", {
            method: "POST",
            headers: {
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
            body: expect.any(FormData),
        });
        expect(result).toEqual({ pk: 3, title: "Ad" });
    });

    test("editAdd should send PATCH request", async () => {
        fetch.mockResolvedValue(jsonResponse({ pk: 1, title: "Updated ad" }));

        const result = await api.editAdd(
            1,
            { title: "Updated ad" },
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith(
            "/api/ads/1",
            expect.objectContaining({
                method: "PATCH",
                body: JSON.stringify({ title: "Updated ad" }),
            })
        );
        expect(result).toEqual({ pk: 1, title: "Updated ad" });
    });

    test("editAddPhoto should send PATCH form data", async () => {
        fetch.mockResolvedValue(jsonResponse({ image: "/ad.jpg" }));

        const image = new File(["photo"], "ad.jpg", { type: "image/jpeg" });
        const result = await api.editAddPhoto(
            1,
            image,
            "user@example.com",
            "password123"
        );

        expect(fetch).toHaveBeenCalledWith("/api/ads/1/image", {
            method: "PATCH",
            headers: {
                Authorization:
                    "Basic " + base64.encode("user@example.com:password123"),
            },
            body: expect.any(FormData),
        });
        expect(result).toEqual({ image: "/ad.jpg" });
    });

    test("deleteAdd should resolve null on success", async () => {
        fetch.mockResolvedValue({
            ok: true,
            status: 204,
        });

        const result = await api.deleteAdd(1, "user@example.com", "password123");

        expect(fetch).toHaveBeenCalledWith(
            "/api/ads/1",
            expect.objectContaining({
                method: "DELETE",
            })
        );
        expect(result).toBeNull();
    });

    test("should parse text response", async () => {
        fetch.mockResolvedValue(textResponse("plain text response"));

        const result = await api.getAds();

        expect(result).toBe("plain text response");
    });

    test("should return null for 204 json parser branch", async () => {
        fetch.mockResolvedValue({
            ok: true,
            status: 204,
            headers: {
                get: jest.fn().mockReturnValue("application/json"),
            },
            json: jest.fn(),
            text: jest.fn(),
        });

        const result = await api.getAds();

        expect(result).toBeNull();
    });

    test("should reject with json error message", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 400,
            headers: {
                get: jest.fn().mockReturnValue("application/json"),
            },
            json: jest.fn().mockResolvedValue({
                message: "Bad request message",
            }),
            text: jest.fn(),
        });

        await expect(api.getAds()).rejects.toBe("Bad request message");
    });

    test("should reject with text error message", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 500,
            headers: {
                get: jest.fn().mockReturnValue("text/plain"),
            },
            json: jest.fn(),
            text: jest.fn().mockResolvedValue("Server exploded"),
        });

        await expect(api.getAds()).rejects.toBe("Server exploded");
    });

    test("should reject with default status when error parsing fails", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 503,
            headers: {
                get: jest.fn().mockReturnValue("application/json"),
            },
            json: jest.fn().mockRejectedValue(new Error("broken json")),
            text: jest.fn(),
        });

        await expect(api.getAds()).rejects.toBe("Error: 503");
    });
});