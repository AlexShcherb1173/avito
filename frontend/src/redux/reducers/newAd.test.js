import { newAd as newAdReducer } from "./newAd";

describe("redux/reducers/newAd", () => {
    test("should return initial state", () => {
        const state = newAdReducer(undefined, { type: "@@INIT" });

        expect(state).toEqual({});
    });

    test("should return payload fields without type", () => {
        const nextState = newAdReducer(undefined, {
            type: "CREATE_NEW_AD",
            title: "Test ad",
            price: 1000,
            description: "Test description",
        });

        expect(nextState).toEqual({
            title: "Test ad",
            price: 1000,
            description: "Test description",
        });
    });

    test("should replace previous state with new payload fields", () => {
        const initialState = {
            title: "Old title",
            price: 500,
        };

        const nextState = newAdReducer(initialState, {
            type: "UPDATE_NEW_AD",
            title: "New title",
            image: "/img.jpg",
        });

        expect(nextState).toEqual({
            title: "New title",
            image: "/img.jpg",
        });
    });

    test("should return empty object when action has only type", () => {
        const nextState = newAdReducer(
            { title: "Old title" },
            { type: "EMPTY_ACTION" }
        );

        expect(nextState).toEqual({});
    });
});