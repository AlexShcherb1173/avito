import { userAds } from "./userAds";

describe("redux/reducers/ads/userAds", () => {
    test("should return initial state", () => {
        const state = userAds(undefined, { type: "@@INIT" });
        expect(state).toBeDefined();
    });

    test("should ignore unknown action", () => {
        const initialState = userAds(undefined, { type: "@@INIT" });
        const nextState = userAds(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toEqual(initialState);
    });
});