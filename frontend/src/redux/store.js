import {
	legacy_createStore as createStore,
	combineReducers,
	applyMiddleware
} from "redux";
import startState from "./initialState";
import { userInfo } from "./reducers/userInfo";

import { ads } from "./reducers/ads/ads";
import { userAds } from "./reducers/ads/userAds";
import { adsDefault } from "./reducers/ads/adsDefault";

const APP_STORAGE_NAME = "appData";

const logger = (store) => (next) => (action) => {
	console.groupCollapsed("dispatching", action.type);
	console.log("prev state", store.getState());
	console.log("action", action);
	const result = next(action);
	console.log("next state", store.getState());
	console.groupEnd();
	return result;
};

const saver = (store) => (next) => (action) => {
	const result = next(action);
	localStorage.setItem(APP_STORAGE_NAME, JSON.stringify(store.getState()));
	return result;
};

const middlewares =
	process.env.NODE_ENV === "test" ? [saver] : [logger, saver];

const storeFactory = (initialState = startState) =>
	createStore(
		combineReducers({ ads, adsDefault, userAds, userInfo }),
		localStorage.getItem(APP_STORAGE_NAME)
			? JSON.parse(localStorage.getItem(APP_STORAGE_NAME))
			: initialState,
		applyMiddleware(...middlewares)
	);

export default storeFactory;