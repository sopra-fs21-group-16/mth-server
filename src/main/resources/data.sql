INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1, 'Play Football', 'SPORTS', '???', '???', '???'),
       (2, 'Play Tennis', 'SPORTS', '???', '???', '???'),
       (3, 'Listen Concert at Tonhalle Zurich', 'MUSIC', '???', '???', '???'),
       (4, 'See a play', 'THEATRE', '???', '???', '???'),
       (5, 'Eat sushi', 'EATING', '???', '???', '???'),
       (6, 'Explore the city', 'SIGHTSEEING', '???', '???', '???'),
       (7, 'Make a pizza', 'COOKING', '???', '???', '???'),
       (8, 'Go on a bike tour', 'OUTDOOR_ACTIVITY', '???', '???', '???'),
       (9, 'Watch a horror movie', 'MOVIES', '???', '???', '???'),
       (10, 'Hiking on the Uetliberg', 'SPORTS', '???', '???', '???');

INSERT INTO USER (ID, EMAIL, PASSWORD, EMAIL_VERIFIED, BIO, PHONE, TOKEN, NAME, DATE_OF_BIRTH, GENDER, PROFILE_PICTURE, LAST_SEEN,
                  USER_INTERESTS_ID)
VALUES (101, 'database.user@uzh.ch', 'databasePassword','true', 'I like spending time in nature and watching movies. I am studying Informatics and my favorite subject is Software Engineering!', '+41791231111', 'databaseToken', 'databaseTester', '2000-01-01', 'MALE', 'TestLink', '2021-04-25', NULL),
       (102, 'database.user2@uzh.ch', 'databasePassword2','true', 'My interests range from outdoor activities to good food. My favorite number is 43.', '+41791232222', 'databaseToken2','databaseTester2', '2000-01-01', 'FEMALE', 'TestLink2', '2021-04-26', NULL);

INSERT INTO USER_INTERESTS (ID, AGE_RANGE_MIN, AGE_RANGE_MAX, GENDER_PREFERENCE, USER_ID)
VALUES (1001, 18, 30, 'FEMALE', 101),
       (1002, 19, 26, 'MALE', 102);

INSERT INTO USER_INTERESTS_ACTIVITY (USER_INTERESTS_ID, ACTIVITY_INTERESTS)
VALUES (1001, 'EATING'),
       (1001, 'COOKING'),
       (1001, 'MOVIES'),
       (1001, 'OUTDOOR_ACTIVITY'),
       (1001, 'SPORTS'),
       (1001, 'SHOPPING'),
       (1002, 'COOKING'),
       (1002, 'MOVIES'),
       (1002, 'GAMES'),
       (1002, 'WELLNESS'),
       (1002, 'SHOPPING');

UPDATE USER
SET USER_INTERESTS_ID=1001
WHERE ID = 101;

UPDATE USER
SET USER_INTERESTS_ID=1002
WHERE ID = 102;