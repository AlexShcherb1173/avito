import ACTIONS, { adsDefault, REDUCERS } from "./adsDefault";
import actions from "../../actions";

describe("redux/reducers/ads/adsDefault", () => {
    test("should return initial state", () => {
        expect(adsDefault(undefined, { type: "@@INIT" })).toEqual([]);
    });

    test("should ignore unknown action", () => {
        const initialState = [{ pk: 1, title: "Ad 1" }];

        const nextState = adsDefault(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toBe(initialState);
    });

    test("should load default ads", () => {
        const payload = [
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ];

        const nextState = adsDefault([], REDUCERS.LOAD(payload));

        expect(nextState).toEqual([
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ]);
        expect(nextState).not.toBe(payload);
    });

    test("should add new default ad", () => {
        const initialState = [{ pk: 1, title: "Old ad" }];

        const nextState = adsDefault(
            initialState,
            REDUCERS.ADD({
                pk: 2,
                title: "New default ad",
                price: 1500,
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "Old ad" },
            { pk: 2, title: "New default ad", price: 1500 },
        ]);
    });

    test("should delete default ad by pk", () => {
        const initialState = [
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ];

        const nextState = adsDefault(initialState, REDUCERS.DELETE(2));

        expect(nextState).toEqual([{ pk: 1, title: "Default 1" }]);
    });

    test("should not delete anything if pk not found", () => {
        const initialState = [
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ];

        const nextState = adsDefault(initialState, REDUCERS.DELETE(999));

        expect(nextState).toEqual(initialState);
        expect(nextState).not.toBe(initialState);
    });

    test("should edit default ad by pk", () => {
        const initialState = [
            { pk: 1, title: "Default 1", price: 100 },
            { pk: 2, title: "Default 2", price: 200 },
        ];

        const nextState = adsDefault(
            initialState,
            REDUCERS.EDIT(1, {
                title: "Updated Default 1",
                image: "/img.jpg",
            })
        );

        expect(nextState).toEqual([
            { pk: 1, title: "Updated Default 1", price: 100, image: "/img.jpg" },
            { pk: 2, title: "Default 2", price: 200 },
        ]);
    });

    test("should keep untouched objects by reference when editing", () => {
        const ad1 = { pk: 1, title: "Default 1" };
        const ad2 = { pk: 2, title: "Default 2" };
        const initialState = [ad1, ad2];

        const nextState = adsDefault(
            initialState,
            REDUCERS.EDIT(1, { title: "Updated Default 1" })
        );

        expect(nextState[0]).not.toBe(ad1);
        expect(nextState[1]).toBe(ad2);
    });

    test("should clear default ads on logout", () => {
        const initialState = [
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ];

        const nextState = adsDefault(initialState, { type: actions.LOGOUT_USER });

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