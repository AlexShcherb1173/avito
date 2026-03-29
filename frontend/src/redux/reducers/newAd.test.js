import newAdReducer from './newAd';

describe('redux/reducers/newAd', () => {
    test('should return initial state', () => {
        const state = newAdReducer(undefined, { type: '@@INIT' });

        expect(state).toBeDefined();
    });

    test('should ignore unknown action', () => {
        const initialState = newAdReducer(undefined, { type: '@@INIT' });
        const nextState = newAdReducer(initialState, { type: 'UNKNOWN_ACTION' });

        expect(nextState).toEqual(initialState);
    });
});