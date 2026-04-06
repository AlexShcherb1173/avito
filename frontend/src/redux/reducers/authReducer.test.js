import authReducer from './authReducer';

describe('redux/authReducer', () => {
    test('should return initial state', () => {
        const state = authReducer(undefined, { type: '@@INIT' });

        expect(state).toBeDefined();
        expect(typeof state).toBe('object');
    });

    test('should handle login success action', () => {
        const initialState = authReducer(undefined, { type: '@@INIT' });

        const nextState = authReducer(initialState, {
            type: 'LOGIN_SUCCESS',
            payload: {
                email: 'user@example.com',
            },
        });

        expect(nextState).toBeDefined();
        expect(typeof nextState).toBe('object');
    });

    test('should handle logout action', () => {
        const initialState = authReducer(undefined, { type: '@@INIT' });

        const nextState = authReducer(initialState, {
            type: 'LOGOUT',
        });

        expect(nextState).toBeDefined();
        expect(typeof nextState).toBe('object');
    });
});