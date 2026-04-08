export const newAd = (state = {}, action) => {
	const { type, ...ad } = action;

	return {
		...ad,
	};
};