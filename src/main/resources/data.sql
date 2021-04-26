INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD)
VALUES (1, 'Play Football', 'SPORTS', '???', '???'),
       (2, 'Play Tennis', 'SPORTS', '???', '???'),
       (3, 'Listen Concert at Tonhalle Zurich', 'MUSIC', '???', '???'),
       (4, 'See a play', 'THEATRE', '???', '???'),
       (5, 'Eat sushi', 'EATING', '???', '???'),
       (6, 'Explore the city', 'SIGHTSEEING', '???', '???'),
       (7, 'Make a pizza', 'COOKING', '???', '???'),
       (8, 'Go on a bike tour', 'OUTDOOR_ACTIVITY', '???', '???'),
       (9, 'Watch a horror movie', 'MOVIES', '???', '???'),
       (10, 'Hiking on the Uetliberg', 'SPORTS', '???', '???')
;

INSERT INTO USER (ID, EMAIL, PASSWORD, BIO, PHONE, TOKEN, NAME, AGE, DATE_OF_BIRTH, GENDER, PROFILE_PICTURE, LAST_SEEN,
                  USER_INTERESTS_ID)
VALUES (1, 'database.user@uzh.ch', 'databasePassword', 'databaseBio', '+41791231111', 'databaseToken', 'databaseTester',
        0, NULL, 'MALE', 'TestLink', '2021-04-25', NULL),
       (2, 'database.user2@uzh.ch', 'databasePassword2', 'databaseBio2', '+41791232222', 'databaseToken2',
        'databaseTester2', 0, NULL, 'FEMALE', 'TestLink2', '2021-04-26', NULL);

