import adsReducer from './adsReducer';

describe('redux/adsReducer', () => {
    test('should return initial state', () => {
        const state = adsReducer(undefined, { type: '@@INIT' });

        expect(state).toBeDefined();
        expect(typeof state).toBe('object');
    });

    test('should handle ads fetch success action', () => {
        const initialState = adsReducer(undefined, { type: '@@INIT' });

        const nextState = adsReducer(initialState, {
            type: 'GET_ADS_SUCCESS',
            payload: {
                count: 1,
                results: [
                    {
                        pk: 1,
                        title: 'Test ad',
                        price: 1000,
                    },
                ],
            },
        });

        expect(nextState).toBeDefined();
        expect(typeof nextState).toBe('object');
    });
});