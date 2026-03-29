import storeFactory from "./store";

describe("redux/store", () => {
    beforeEach(() => {
        localStorage.clear();
        jest.spyOn(Storage.prototype, "setItem");
        jest.spyOn(console, "groupCollapsed").mockImplementation(() => {});
        jest.spyOn(console, "log").mockImplementation(() => {});
        jest.spyOn(console, "groupEnd").mockImplementation(() => {});
    });

    afterEach(() => {
        jest.restoreAllMocks();
        localStorage.clear();
    });

    test("should create store instance", () => {
        const store = storeFactory();

        expect(store).toBeDefined();
        expect(typeof store.getState).toBe("function");
        expect(typeof store.dispatch).toBe("function");
        expect(typeof store.subscribe).toBe("function");
    });

    test("should create store from provided initial state", () => {
        const initialState = {
            ads: [{ pk: 1, title: "Test ad" }],
            adsDefault: [],
            userAds: [],
            userInfo: {
                username: "user@example.com",
                password: "password123",
            },
        };

        const store = storeFactory(initialState);

        expect(store.getState()).toEqual(initialState);
    });

    test("should prefer persisted localStorage state over passed initial state", () => {
        const persistedState = {
            ads: [{ pk: 2, title: "Persisted ad" }],
            adsDefault: [],
            userAds: [],
            userInfo: {
                username: "persisted@example.com",
                password: "persisted-password",
            },
        };

        localStorage.setItem("appData", JSON.stringify(persistedState));

        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        expect(store.getState()).toEqual(persistedState);
    });

    test("should save state to localStorage after dispatch", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch({ type: "UNKNOWN_ACTION_FOR_SAVER_TEST" });

        expect(localStorage.setItem).toHaveBeenCalled();
        expect(localStorage.setItem).toHaveBeenCalledWith(
            "appData",
            JSON.stringify(store.getState())
        );
    });
});