import { userInfo } from "./userInfo";

describe("redux/reducers/userInfo", () => {
    test("should return initial state", () => {
        const state = userInfo(undefined, { type: "@@INIT" });
        expect(state).toBeDefined();
    });

    test("should ignore unknown action", () => {
        const initialState = userInfo(undefined, { type: "@@INIT" });
        const nextState = userInfo(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toEqual(initialState);
    });
});