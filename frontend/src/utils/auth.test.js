import auth from "./auth";

describe("utils/auth", () => {
    beforeEach(() => {
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("should send registration request", async () => {
        const requestData = {
            username: "user@example.com",
            password: "password123",
            role: "USER",
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        };

        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                success: true,
                message: "User registered successfully",
            }),
        });

        const result = await auth.registration(requestData);

        expect(fetch).toHaveBeenCalledWith("/api/register", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        });

        expect(result).toEqual({
            success: true,
            message: "User registered successfully",
        });
    });

    test("should send authentication request", async () => {
        const requestData = {
            username: "user@example.com",
            password: "password123",
        };

        fetch.mockResolvedValueOnce({
            ok: true,
            status: 200,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                success: true,
                message: "Login successful",
            }),
        });

        const result = await auth.authentication(requestData);

        expect(fetch).toHaveBeenCalledWith("/api/login", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        });

        expect(result).toEqual({
            success: true,
            message: "Login successful",
        });
    });

    test("should reject with backend message on auth error", async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            status: 401,
            headers: {
                get: () => "application/json",
            },
            json: async () => ({
                message: "Invalid credentials",
            }),
        });

        await expect(
            auth.authentication({
                username: "user@example.com",
                password: "wrong-password",
            })
        ).rejects.toBe("Invalid credentials");
    });

    test("should reject with text body if response is not json", async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
            status: 500,
            headers: {
                get: () => "text/plain",
            },
            text: async () => "Internal server error",
        });

        await expect(
            auth.registration({
                username: "user@example.com",
                password: "password123",
            })
        ).rejects.toBe("Internal server error");
    });

    test("should return empty object for 204 response", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            status: 204,
            headers: {
                get: () => "",
            },
        });

        const result = await auth.authentication({
            username: "user@example.com",
            password: "password123",
        });

        expect(result).toEqual({});
    });
});