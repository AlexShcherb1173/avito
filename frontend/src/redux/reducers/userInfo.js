import A from "../actions";

export const userInfo = (state = {}, action) => {
	const { type, ...result } = action;

	if (A.LOAD_USER === type || A.AUTH_USER === type) {
		const isSameUser =
			state.id === result.id &&
			state.username === result.username &&
			state.password === result.password &&
			state.firstName === result.firstName &&
			state.lastName === result.lastName &&
			state.phone === result.phone;

		if (isSameUser) {
			return state;
		}

		return {
			...state,
			...result
		};
	}

	if (A.LOGOUT_USER === type) {
		return {};
	}

	return state;
};