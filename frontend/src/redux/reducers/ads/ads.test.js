import ACTIONS, { ads, REDUCERS } from "./ads";
import actions from "../../actions";

describe("redux/reducers/ads/ads", () => {
    test("should return initial state", () => {
        expect(ads(undefined, { type: "@@INIT" })).toEqual([]);
    });

    test("should ignore unknown action", () => {
        const initialState = [{ pk: 1, title: "Ad 1" }];

        const nextState = ads(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toBe(initialState);
    });

    test("should load ads", () => {
        const payload = [
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ];

        const nextState = ads([], REDUCERS.LOAD(payload));

        expect(nextState).toEqual([
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ]);
        expect(nextState).not.toBe(payload);
    });

    test("should add new ad", () => {
        const initialState = [{ pk: 1, title: "Old ad" }];

        const nextState = ads(
            initialState,
            REDUCERS.ADD({
                pk: 2,
                title: "New ad",
                price: 1000,
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "Old ad" },
            { pk: 2, title: "New ad", price: 1000 },
        ]);
    });

    test("should delete ad by pk", () => {
        const initialState = [
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ];

        const nextState = ads(initialState, REDUCERS.DELETE(1));

        expect(nextState).toEqual([{ pk: 2, title: "Ad 2" }]);
    });

    test("should not delete anything if pk not found", () => {
        const initialState = [
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ];

        const nextState = ads(initialState, REDUCERS.DELETE(999));

        expect(nextState).toEqual(initialState);
        expect(nextState).not.toBe(initialState);
    });

    test("should edit ad by pk", () => {
        const initialState = [
            { pk: 1, title: "Ad 1", price: 100 },
            { pk: 2, title: "Ad 2", price: 200 },
        ];

        const nextState = ads(
            initialState,
            REDUCERS.EDIT(2, {
                title: "Updated Ad 2",
                price: 500,
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "Ad 1", price: 100 },
            { pk: 2, title: "Updated Ad 2", price: 500 },
        ]);
    });

    test("should keep state objects unchanged except edited ad", () => {
        const ad1 = { pk: 1, title: "Ad 1", price: 100 };
        const ad2 = { pk: 2, title: "Ad 2", price: 200 };
        const initialState = [ad1, ad2];

        const nextState = ads(
            initialState,
            REDUCERS.EDIT(2, { title: "Updated Ad 2" })
        );

        expect(nextState[0]).toBe(ad1);
        expect(nextState[1]).not.toBe(ad2);
        expect(nextState[1]).toEqual({
            pk: 2,
            title: "Updated Ad 2",
            price: 200,
        });
    });

    test("should return mapped copy when edited ad id not found", () => {
        const ad1 = { pk: 1, title: "Ad 1" };
        const ad2 = { pk: 2, title: "Ad 2" };
        const initialState = [ad1, ad2];

        const nextState = ads(initialState, REDUCERS.EDIT(999, { title: "X" }));

        expect(nextState).toEqual(initialState);
        expect(nextState).not.toBe(initialState);
        expect(nextState[0]).toBe(ad1);
        expect(nextState[1]).toBe(ad2);
    });

    test("should clear state on logout", () => {
        const initialState = [
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ];

        const nextState = ads(initialState, { type: actions.LOGOUT_USER });

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