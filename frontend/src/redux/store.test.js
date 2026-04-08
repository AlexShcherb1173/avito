import storeFactory from "./store";
import { loadedUser, userAuth, userLogOut } from "./actions";
import { REDUCERS as adsReducers } from "./reducers/ads/ads";
import { REDUCERS as userAdsReducers } from "./reducers/ads/userAds";
import { REDUCERS as adsDefaultReducers } from "./reducers/ads/adsDefault";
import startState from "./initialState";

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

    test("should use initialState by default", () => {
        const store = storeFactory();

        expect(store.getState()).toEqual(startState);
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

    test("should save state to localStorage after unknown dispatch", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch({ type: "UNKNOWN_ACTION_FOR_SAVER_TEST" });

        expect(localStorage.setItem).toHaveBeenCalledWith(
            "appData",
            JSON.stringify(store.getState())
        );
    });

    test("should update state through LOAD_USER reducer and save to localStorage", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            loadedUser({
                id: 1,
                username: "user@example.com",
                password: "password123",
                firstName: "Ivan",
            })
        );

        expect(store.getState().userInfo).toEqual({
            id: 1,
            username: "user@example.com",
            password: "password123",
            firstName: "Ivan",
        });

        expect(localStorage.setItem).toHaveBeenLastCalledWith(
            "appData",
            JSON.stringify(store.getState())
        );
    });

    test("should update state through AUTH_USER reducer", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            userAuth({
                username: "auth@example.com",
                password: "secret123",
            })
        );

        expect(store.getState().userInfo).toEqual({
            username: "auth@example.com",
            password: "secret123",
        });
    });

    test("should clear userInfo on logout action", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {
                id: 1,
                username: "user@example.com",
                password: "password123",
            },
        });

        store.dispatch(userLogOut());

        expect(store.getState().userInfo).toEqual({});
        expect(localStorage.setItem).toHaveBeenLastCalledWith(
            "appData",
            JSON.stringify(store.getState())
        );
    });

    test("should load ads into ads reducer", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            adsReducers.LOAD([
                { pk: 1, title: "Ad 1" },
                { pk: 2, title: "Ad 2" },
            ])
        );

        expect(store.getState().ads).toEqual([
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ]);
    });

    test("should add ad into ads reducer", () => {
        const store = storeFactory({
            ads: [{ pk: 1, title: "Old ad" }],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            adsReducers.ADD({
                pk: 2,
                title: "New ad",
                price: 1000,
            })
        );

        expect(store.getState().ads).toEqual([
            { pk: 1, title: "Old ad" },
            { pk: 2, title: "New ad", price: 1000 },
        ]);
    });

    test("should edit ad inside ads reducer", () => {
        const store = storeFactory({
            ads: [
                { pk: 1, title: "Ad 1", price: 100 },
                { pk: 2, title: "Ad 2", price: 200 },
            ],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            adsReducers.EDIT(2, {
                title: "Updated ad",
                price: 999,
            })
        );

        expect(store.getState().ads).toEqual([
            { pk: 1, title: "Ad 1", price: 100 },
            { pk: 2, title: "Updated ad", price: 999 },
        ]);
    });

    test("should delete ad inside ads reducer", () => {
        const store = storeFactory({
            ads: [
                { pk: 1, title: "Ad 1" },
                { pk: 2, title: "Ad 2" },
            ],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(adsReducers.DELETE(1));

        expect(store.getState().ads).toEqual([{ pk: 2, title: "Ad 2" }]);
    });

    test("should load user ads into userAds reducer", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            userAdsReducers.LOAD([
                { pk: 10, title: "User ad 1" },
                { pk: 11, title: "User ad 2" },
            ])
        );

        expect(store.getState().userAds).toEqual([
            { pk: 10, title: "User ad 1" },
            { pk: 11, title: "User ad 2" },
        ]);
    });

    test("should load default ads into adsDefault reducer", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            adsDefaultReducers.LOAD([
                { pk: 101, title: "Default ad 1" },
                { pk: 102, title: "Default ad 2" },
            ])
        );

        expect(store.getState().adsDefault).toEqual([
            { pk: 101, title: "Default ad 1" },
            { pk: 102, title: "Default ad 2" },
        ]);
    });

    test("should notify subscribers on dispatch", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        const listener = jest.fn();
        const unsubscribe = store.subscribe(listener);

        store.dispatch(
            loadedUser({
                id: 1,
                username: "user@example.com",
            })
        );

        expect(listener).toHaveBeenCalledTimes(1);

        unsubscribe();

        store.dispatch(
            loadedUser({
                id: 2,
                username: "another@example.com",
            })
        );

        expect(listener).toHaveBeenCalledTimes(1);
    });

    test("should save latest state after multiple dispatches", () => {
        const store = storeFactory({
            ads: [],
            adsDefault: [],
            userAds: [],
            userInfo: {},
        });

        store.dispatch(
            loadedUser({
                id: 1,
                username: "user@example.com",
            })
        );

        store.dispatch(
            adsReducers.ADD({
                pk: 77,
                title: "Saved ad",
            })
        );

        expect(localStorage.setItem).toHaveBeenLastCalledWith(
            "appData",
            JSON.stringify(store.getState())
        );

        expect(store.getState()).toEqual({
            ads: [{ pk: 77, title: "Saved ad" }],
            adsDefault: [],
            userAds: [],
            userInfo: {
                id: 1,
                username: "user@example.com",
            },
        });
    });
});