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
       (10, 'Hiking on the Uetliberg', 'SPORTS', '???', '???');



INSERT INTO USER (ID, EMAIL, PASSWORD, BIO, PHONE, TOKEN, NAME, DATE_OF_BIRTH, GENDER, PROFILE_PICTURE, LAST_SEEN,
                  USER_INTERESTS_ID)
VALUES (10, 'database.user@uzh.ch', 'databasePassword', 'databaseBio', '+41791231111', 'databaseToken', 'databaseTester', NULL, 'MALE', 'TestLink', '2021-04-25', NULL),
       (20, 'database.user2@uzh.ch', 'databasePassword2', 'databaseBio2', '+41791232222', 'databaseToken2','databaseTester2', NULL, 'FEMALE', 'TestLink2', '2021-04-26', NULL);

INSERT INTO USER_INTERESTS (ID, AGE_RANGE_MIN, AGE_RANGE_MAX, GENDER_PREFERENCE, USER_ID)
VALUES (1, 18, 30, 'FEMALE', 10),
       (2, 21, 26, 'MALE', 20);

INSERT INTO USER_INTERESTS_ACTIVITY (USER_INTERESTS_ID, ACTIVITY_INTERESTS)
VALUES (1, 'EATING'),
       (1, 'COOKING'),
       (1, 'MOVIES'),
       (1, 'OUTDOOR_ACTIVITY'),
       (1, 'SPORTS'),
       (1, 'SHOPPING'),
       (2, 'COOKING'),
       (2, 'MOVIES'),
       (2, 'GAMES'),
       (2, 'WELLNESS'),
       (2, 'SHOPPING');

UPDATE USER
SET USER_INTERESTS_ID=1
WHERE ID = 10;

UPDATE USER
SET USER_INTERESTS_ID=2
WHERE ID = 20;