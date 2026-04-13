import auth from "./auth";

const API_URL = "http://localhost:8081";

describe("utils/auth", () => {
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
    });

    const textResponse = (text, ok = true, status = 200) => ({
        ok,
        status,
        headers: {
            get: jest.fn().mockReturnValue("text/plain"),
        },
        json: jest.fn(),
        text: jest.fn().mockResolvedValue(text),
    });

    test("registration should send POST request", async () => {
        fetch.mockResolvedValue(jsonResponse({ id: 1 }));

        const payload = {
            username: "user@example.com",
            password: "password123",
        };

        const result = await auth.registration(payload);

        expect(fetch).toHaveBeenCalledWith(`${API_URL}/register`, {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        expect(result).toEqual({ id: 1 });
    });

    test("authentication should send POST request", async () => {
        fetch.mockResolvedValue(jsonResponse({ token: "abc" }));

        const payload = {
            username: "user@example.com",
            password: "password123",
        };

        const result = await auth.authentication(payload);

        expect(fetch).toHaveBeenCalledWith(`${API_URL}/login`, {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        expect(result).toEqual({ token: "abc" });
    });

    test("should reject with json message on failed response", async () => {
        fetch.mockResolvedValue(
            jsonResponse({ message: "Invalid credentials" }, false, 401)
        );

        await expect(
            auth.authentication({
                username: "user@example.com",
                password: "wrongpass",
            })
        ).rejects.toBe("Invalid credentials");
    });

    test("should reject with json error field on failed response", async () => {
        fetch.mockResolvedValue(
            jsonResponse({ error: "User exists" }, false, 400)
        );

        await expect(
            auth.registration({
                username: "user@example.com",
                password: "password123",
            })
        ).rejects.toBe("User exists");
    });

    test("should reject with default status text when no json message exists", async () => {
        fetch.mockResolvedValue(jsonResponse({}, false, 500));

        await expect(
            auth.authentication({
                username: "user@example.com",
                password: "password123",
            })
        ).rejects.toBe("Error: 500");
    });

    test("should parse text response", async () => {
        fetch.mockResolvedValue(textResponse("plain text"));

        const result = await auth.authentication({
            username: "user@example.com",
            password: "password123",
        });

        expect(result).toEqual({ message: "plain text" });
    });

    test("should return empty object on 204", async () => {
        fetch.mockResolvedValue({
            ok: true,
            status: 204,
            headers: {
                get: jest.fn().mockReturnValue("application/json"),
            },
            json: jest.fn(),
            text: jest.fn(),
        });

        const result = await auth.authentication({
            username: "user@example.com",
            password: "password123",
        });

        expect(result).toEqual({});
    });

    test("should handle broken response parsing", async () => {
        fetch.mockResolvedValue({
            ok: false,
            status: 503,
            headers: {
                get: jest.fn().mockReturnValue("application/json"),
            },
            json: jest.fn().mockRejectedValue(new Error("broken json")),
            text: jest.fn(),
        });

        await expect(
            auth.authentication({
                username: "user@example.com",
                password: "password123",
            })
        ).rejects.toBe("Error: 503");
    });
});