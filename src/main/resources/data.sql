/* SPORTS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1, 'Play Football', 'SPORTS', '???', '???', '???'),
       (2, 'Hiking on the Uetliberg', 'SPORTS', '???', '???', '???'),
       (100, 'Play Tennis', 'SPORTS', '???', '???', '???');

/* MUSIC */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (101, 'Listen Concert at Tonhalle Zurich', 'MUSIC', '???', '???', '???'),
       (200, 'Music Activity', 'MUSIC', '???', '???', '???');

/* THEATRE */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (201, 'See a play', 'THEATRE', '???', '???', '???'),
       (300, 'Theater Activity', 'THEATRE', '???', '???', '???');

/* EATING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (301, 'Eat sushi', 'EATING', 'restaurant', 'sushi', 'https://source.unsplash.com/iy_MT2ifklc/1600x900'),
       (302, 'Eat pizza', 'EATING', 'restaurant', 'pizza', 'https://source.unsplash.com/gJW-pfaqihA/1600x900'),
       (303, 'Eat hamburger', 'EATING', 'restaurant', 'hamburger', 'https://source.unsplash.com/FlmXvqlD-nI/1600x900'),
       (304, 'Eat pasta', 'EATING', 'restaurant', 'pasta', 'https://source.unsplash.com/b29Qdj7zc5g/1600x900'),
       (305, 'Eat ice cream', 'EATING', 'restaurant', 'icecream', 'https://source.unsplash.com/jnWGWSWTVqU/1600x900'),
       (400, 'Eating Activity', 'EATING', '???', '???', '???');

/* COOKING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (401, 'Make a pizza', 'COOKING', '???', '???', '???'),
       (500, 'Cooking Activity', 'COOKING', '???', '???', '???');

/* SIGHTSEEING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (501, 'Explore the city', 'SIGHTSEEING', '???', '???', '???'),
       (600, 'Sightseeing Activity', 'SIGHTSEEING', '???', '???', '???');

/* OUTDOOR_ACTIVITY */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (601, 'Go on a bike tour', 'OUTDOOR_ACTIVITY', '???', '???', '???'),
       (700, 'Outdoor Activityr', 'OUTDOOR_ACTIVITY', '???', '???', '???');

/* MOVIES */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (701, 'Watch a horror movie', 'MOVIES', '???', '???', '???'),
       (800, 'Movies Activity', 'MOVIES', '???', '???', '???');

/* MUSEUMS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (801, 'Visit the Kunsthaus Zurich', 'MUSEUMS', 'museum', 'kunsthaus', '???'),
       (900, 'Museum Activity', 'MUSEUMS', 'museum', '???', '???');

/* WELLNESS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (901, 'Wellness Activity', 'WELLNESS', '???', '???', '???'),
       (1000, 'Wellness Activity 2', 'WELLNESS', '???', '???', '???');

/* SHOPPING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1001, 'Shopping Activity', 'SHOPPING', '???', '???', '???'),
       (1100, 'Shopping Activity 2', 'SHOPPING', '???', '???', '???');

/* GAMES */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1101, 'Gaming Activity', 'GAMES', '???', '???', '???'),
       (1200, 'Gaming Activity 2', 'GAMES', '???', '???', '???');

/* WINTERSPORTS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1201, 'Winter Sports Activity', 'WINTERSPORTS', '???', '???', '???'),
       (1300, 'Winter Sports Activity 2', 'WINTERSPORTS', '???', '???', '???');


/* TRAVEL (maybe a future category since we're only allowing zurich as a location
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1301, 'TRAVEL', 'TRAVEL', '???', '???', '???'),
       (1400, 'TRAVEL', 'TRAVEL', '???', '???', '???');
*/

INSERT INTO USER (ID, EMAIL, PASSWORD, BIO, PHONE, TOKEN, NAME, DATE_OF_BIRTH, GENDER, PROFILE_PICTURE, LAST_SEEN,
                  USER_INTERESTS_ID)
VALUES (101, 'database.user@uzh.ch', 'databasePassword', 'I like spending time in nature and watching movies. I am studying Informatics and my favorite subject is Software Engineering!', '+41791231111', 'databaseToken', 'databaseTester', '2000-01-01', 'MALE', 'TestLink', '2021-04-25', NULL),
       (102, 'database.user2@uzh.ch', 'databasePassword2', 'My interests range from outdoor activities to good food. My favorite number is 43.', '+41791232222', 'databaseToken2','databaseTester2', '2000-01-01', 'FEMALE', 'TestLink2', '2021-04-26', NULL);

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