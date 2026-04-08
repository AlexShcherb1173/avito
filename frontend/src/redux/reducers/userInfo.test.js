import { userInfo } from "./userInfo";
import actions from "../actions";

describe("redux/reducers/userInfo", () => {
    test("should return initial state", () => {
        expect(userInfo(undefined, { type: "@@INIT" })).toEqual({});
    });

    test("should ignore unknown action", () => {
        const initialState = {
            username: "user@example.com",
        };

        const nextState = userInfo(initialState, { type: "UNKNOWN_ACTION" });

        expect(nextState).toBe(initialState);
    });

    test("should load user on LOAD_USER", () => {
        const nextState = userInfo({}, {
            type: actions.LOAD_USER,
            id: 1,
            username: "user@example.com",
            firstName: "Ivan",
        });

        expect(nextState).toEqual({
            id: 1,
            username: "user@example.com",
            firstName: "Ivan",
        });
    });

    test("should auth user on AUTH_USER", () => {
        const nextState = userInfo({}, {
            type: actions.AUTH_USER,
            username: "user@example.com",
            password: "password123",
        });

        expect(nextState).toEqual({
            username: "user@example.com",
            password: "password123",
        });
    });

    test("should merge state with LOAD_USER payload", () => {
        const initialState = {
            username: "user@example.com",
            password: "password123",
        };

        const nextState = userInfo(initialState, {
            type: actions.LOAD_USER,
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
        });

        expect(nextState).toEqual({
            username: "user@example.com",
            password: "password123",
            id: 1,
            firstName: "Ivan",
            lastName: "Ivanov",
        });
    });

    test("should clear user info on LOGOUT_USER", () => {
        const initialState = {
            id: 1,
            username: "user@example.com",
            password: "password123",
        };

        const nextState = userInfo(initialState, {
            type: actions.LOGOUT_USER,
        });

        expect(nextState).toEqual({});
    });
});