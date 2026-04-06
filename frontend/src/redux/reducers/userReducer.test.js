import userReducer from './userReducer';

describe('redux/userReducer', () => {
    test('should return initial state', () => {
        const state = userReducer(undefined, { type: '@@INIT' });

        expect(state).toBeDefined();
        expect(typeof state).toBe('object');
    });

    test('should handle get current user success action', () => {
        const initialState = userReducer(undefined, { type: '@@INIT' });

        const nextState = userReducer(initialState, {
            type: 'GET_CURRENT_USER_SUCCESS',
            payload: {
                id: 1,
                email: 'user@example.com',
                firstName: 'Ivan',
                lastName: 'Ivanov',
            },
        });

        expect(nextState).toBeDefined();
        expect(typeof nextState).toBe('object');
    });
});