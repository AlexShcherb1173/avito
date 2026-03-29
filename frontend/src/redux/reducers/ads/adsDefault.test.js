import { adsDefault } from "./adsDefault";

describe("redux/reducers/ads/adsDefault", () => {
    test("should return initial state", () => {
        const state = adsDefault(undefined, { type: "@@INIT" });
        expect(state).toBeDefined();
    });

    test("should ignore unknown action", () => {
        const initialState = adsDefault(undefined, { type: "@@INIT" });
        const nextState = adsDefault(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toEqual(initialState);
    });
});