import { ads } from "./ads";

describe("redux/reducers/ads/ads", () => {
    test("should return initial state", () => {
        const state = ads(undefined, { type: "@@INIT" });
        expect(state).toBeDefined();
    });

    test("should ignore unknown action", () => {
        const initialState = ads(undefined, { type: "@@INIT" });
        const nextState = ads(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toEqual(initialState);
    });
});