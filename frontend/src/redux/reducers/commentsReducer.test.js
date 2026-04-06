import commentsReducer from './commentsReducer';

describe('redux/commentsReducer', () => {
    test('should return initial state', () => {
        const state = commentsReducer(undefined, { type: '@@INIT' });

        expect(state).toBeDefined();
        expect(typeof state).toBe('object');
    });

    test('should handle comments fetch success action', () => {
        const initialState = commentsReducer(undefined, { type: '@@INIT' });

        const nextState = commentsReducer(initialState, {
            type: 'GET_COMMENTS_SUCCESS',
            payload: {
                count: 1,
                results: [
                    {
                        pk: 1,
                        text: 'Test comment',
                    },
                ],
            },
        });

        expect(nextState).toBeDefined();
        expect(typeof nextState).toBe('object');
    });
});