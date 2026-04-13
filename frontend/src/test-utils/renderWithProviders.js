import React from "react";
import { Provider } from "react-redux";
import { MemoryRouter } from "react-router-dom";
import { render } from "@testing-library/react";
import storeFactory from "../redux/store";

export function renderWithProviders(
    ui,
    {
        route = "/",
        preloadedState,
        store = storeFactory(preloadedState),
    } = {}
) {
    return {
        store,
        ...render(
            <Provider store={store}>
                <MemoryRouter initialEntries={[route]}>{ui}</MemoryRouter>
            </Provider>
        ),
    };
}