import actions, {
    loadedUser,
    loadedUserAds,
    userAuth,
    userLogOut,
    loadDefaultAds,
} from "./actions";

describe("redux/actions", () => {
    test("should export action constants", () => {
        expect(actions.NEW_AD).toBe("NEW_AD");
        expect(actions.NEW_USER_AD).toBe("NEW_USER_AD");
        expect(actions.UPDATE_AD).toBe("UPDATE_AD");
        expect(actions.LOAD_ADS).toBe("LOAD_ADS");
        expect(actions.LOAD_USER_ADS).toBe("LOAD_USER_ADS");
        expect(actions.LOAD_USER).toBe("LOAD_USER");
        expect(actions.AUTH_USER).toBe("AUTH_USER");
        expect(actions.LOGOUT_USER).toBe("LOGOUT_USER");
        expect(actions.UPDATE_USER).toBe("UPDATE_USER");
        expect(actions.ADS_LOAD_DEFAULT).toBe("ADS_LOAD_DEFAULT");
    });

    test("loadedUser should create LOAD_USER action", () => {
        const user = {
            id: 1,
            username: "user@example.com",
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        };

        expect(loadedUser(user)).toEqual({
            type: actions.LOAD_USER,
            id: 1,
            username: "user@example.com",
            firstName: "Ivan",
            lastName: "Ivanov",
            phone: "+79990000001",
        });
    });

    test("loadedUser should work with empty object", () => {
        expect(loadedUser({})).toEqual({
            type: actions.LOAD_USER,
        });
    });

    test("loadedUserAds should create LOAD_USER_ADS action", () => {
        const ads = [
            { pk: 1, title: "Ad 1" },
            { pk: 2, title: "Ad 2" },
        ];

        const action = loadedUserAds(ads);

        expect(action).toEqual({
            type: actions.LOAD_USER_ADS,
            ads: [
                { pk: 1, title: "Ad 1" },
                { pk: 2, title: "Ad 2" },
            ],
        });
    });

    test("loadedUserAds should copy input array", () => {
        const ads = [{ pk: 1, title: "Ad 1" }];

        const action = loadedUserAds(ads);

        expect(action.ads).toEqual([{ pk: 1, title: "Ad 1" }]);
        expect(action.ads).not.toBe(ads);
    });

    test("userAuth should create AUTH_USER action", () => {
        const user = {
            username: "user@example.com",
            password: "password123",
        };

        expect(userAuth(user)).toEqual({
            type: actions.AUTH_USER,
            username: "user@example.com",
            password: "password123",
        });
    });

    test("userAuth should work with empty object", () => {
        expect(userAuth({})).toEqual({
            type: actions.AUTH_USER,
        });
    });

    test("userLogOut should create LOGOUT_USER action", () => {
        expect(userLogOut()).toEqual({
            type: actions.LOGOUT_USER,
        });
    });

    test("loadDefaultAds should create ADS_LOAD_DEFAULT action", () => {
        const ads = [
            { pk: 1, title: "Default 1" },
            { pk: 2, title: "Default 2" },
        ];

        const action = loadDefaultAds(ads);

        expect(action).toEqual({
            type: actions.ADS_LOAD_DEFAULT,
            ads: [
                { pk: 1, title: "Default 1" },
                { pk: 2, title: "Default 2" },
            ],
        });
    });

    test("loadDefaultAds should copy input array", () => {
        const ads = [{ pk: 1, title: "Default 1" }];

        const action = loadDefaultAds(ads);

        expect(action.ads).toEqual([{ pk: 1, title: "Default 1" }]);
        expect(action.ads).not.toBe(ads);
    });
});