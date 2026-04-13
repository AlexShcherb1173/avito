import ACTIONS, { userAds, REDUCERS } from "./userAds";
import actions from "../../actions";

describe("redux/reducers/ads/userAds", () => {
    test("should return initial state", () => {
        expect(userAds(undefined, { type: "@@INIT" })).toEqual([]);
    });

    test("should ignore unknown action", () => {
        const initialState = [{ pk: 1, title: "User ad 1" }];

        const nextState = userAds(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toBe(initialState);
    });

    test("should load user ads", () => {
        const payload = [
            { pk: 1, title: "User Ad 1" },
            { pk: 2, title: "User Ad 2" },
        ];

        const nextState = userAds([], REDUCERS.LOAD(payload));

        expect(nextState).toEqual([
            { pk: 1, title: "User Ad 1" },
            { pk: 2, title: "User Ad 2" },
        ]);
        expect(nextState).not.toBe(payload);
    });

    test("should add new user ad", () => {
        const initialState = [{ pk: 1, title: "Old user ad" }];

        const nextState = userAds(
            initialState,
            REDUCERS.ADD({
                pk: 2,
                title: "New user ad",
                description: "desc",
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "Old user ad" },
            { pk: 2, title: "New user ad", description: "desc" },
        ]);
    });

    test("should delete user ad by pk", () => {
        const initialState = [
            { pk: 1, title: "User Ad 1" },
            { pk: 2, title: "User Ad 2" },
        ];

        const nextState = userAds(initialState, REDUCERS.DELETE(1));

        expect(nextState).toEqual([{ pk: 2, title: "User Ad 2" }]);
    });

    test("should not delete anything if pk not found", () => {
        const initialState = [
            { pk: 1, title: "User Ad 1" },
            { pk: 2, title: "User Ad 2" },
        ];

        const nextState = userAds(initialState, REDUCERS.DELETE(999));

        expect(nextState).toEqual(initialState);
        expect(nextState).not.toBe(initialState);
    });

    test("should edit user ad by pk", () => {
        const initialState = [
            { pk: 1, title: "User Ad 1", price: 100 },
            { pk: 2, title: "User Ad 2", price: 200 },
        ];

        const nextState = userAds(
            initialState,
            REDUCERS.EDIT(2, {
                title: "Updated User Ad 2",
                price: 999,
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "User Ad 1", price: 100 },
            { pk: 2, title: "Updated User Ad 2", price: 999 },
        ]);
    });

    test("should preserve untouched references when editing", () => {
        const ad1 = { pk: 1, title: "User Ad 1" };
        const ad2 = { pk: 2, title: "User Ad 2" };
        const initialState = [ad1, ad2];

        const nextState = userAds(
            initialState,
            REDUCERS.EDIT(2, { title: "Updated User Ad 2" })
        );

        expect(nextState[0]).toBe(ad1);
        expect(nextState[1]).not.toBe(ad2);
    });

    test("should clear user ads on logout", () => {
        const initialState = [
            { pk: 1, title: "User Ad 1" },
            { pk: 2, title: "User Ad 2" },
        ];

        const nextState = userAds(initialState, { type: actions.LOGOUT_USER });

        expect(nextState).toEqual([]);
    });

    test("should export reducer action creators", () => {
        expect(REDUCERS.LOAD([{ pk: 1 }])).toEqual({
            type: ACTIONS.LOAD_ADS,
            ads: [{ pk: 1 }],
        });

        expect(REDUCERS.ADD({ pk: 2 })).toEqual({
            type: ACTIONS.NEW_AD,
            newAd: { pk: 2 },
        });

        expect(REDUCERS.DELETE(10)).toEqual({
            type: ACTIONS.DELETE_AD,
            deleteId: 10,
        });

        expect(REDUCERS.EDIT(5, { title: "Updated" })).toEqual({
            type: ACTIONS.EDIT_AD,
            adId: 5,
            newFields: { title: "Updated" },
        });
    });
});