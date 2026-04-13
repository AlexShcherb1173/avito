import state from "./initialState";

describe("redux/initialState", () => {
    test("should export expected initial state shape", () => {
        expect(state).toEqual({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });
    });

    test("should have arrays for ads collections", () => {
        expect(Array.isArray(state.ads)).toBe(true);
        expect(Array.isArray(state.adsDefault)).toBe(true);
        expect(Array.isArray(state.userAds)).toBe(true);
    });

    test("should have object for userInfo", () => {
        expect(typeof state.userInfo).toBe("object");
        expect(state.userInfo).not.toBeNull();
    });
});