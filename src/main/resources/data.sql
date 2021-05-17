/* SPORTS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1, 'Play Football', 'SPORTS', '???', '???', '???'),
       (2, 'Hiking on the Uetliberg', 'SPORTS', '???', '???', '???'),
       (3, 'Enjoy a Bike Adventure', 'SPORTS', 'point_of_interest', 'Bikepark%20Zürich', 'https://source.unsplash.com/n13ABwV-Pic/1600x900'),
       (4, 'Go Swimming', 'SPORTS', 'point_of_interest', 'Freibad Letzigraben', 'https://source.unsplash.com/TVOAbbLL050/1600x900'),
       (5, 'Play Squash', 'SPORTS', 'point_of_interest', 'squash', 'https://source.unsplash.com/P9y0G14Z7wA/1600x900'),
       (6, 'Go Bowling', 'SPORTS', 'bowling_alley', 'bowling', 'https://source.unsplash.com/IoBCIosXkH8/1600x900'),
       (100, 'Play Tennis', 'SPORTS', '???', '???', '???');

/* MUSIC */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (101, 'Listen Concert at Tonhalle Zurich', 'MUSIC', '???', '???', '???'),
       (102, 'Listen to an Opera', 'MUSIC', 'point_of_interest', 'Opernhaus', 'https://source.unsplash.com/XSYr1XOxCBA/1600x900'),
       (103, 'Make a Jam Session', 'MUSIC', 'park', 'park', 'https://source.unsplash.com/L3sBSDZKnws/1600x900'),
       (200, 'Music Activity', 'MUSIC', '???', '???', '???');

/* THEATRE */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (201, 'See a play', 'THEATRE', '???', '???', '???'),
       (202, 'See a musical', 'THEATRE', 'point_of_interest', 'musicalplay', 'https://source.unsplash.com/p6rNTdAPbuk/1600x900'),
       (203, 'Take a improv class', 'THEATRE', 'point_of_interest', 'improv', 'https://source.unsplash.com/mGeSmM_EOqc/1600x900'),
       (300, 'Theater Activity', 'THEATRE', '???', '???', '???');

/* EATING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (301, 'Eat sushi', 'EATING', 'restaurant', 'sushi', 'https://source.unsplash.com/iy_MT2ifklc/1600x900'),
       (302, 'Eat pizza', 'EATING', 'restaurant', 'pizza', 'https://source.unsplash.com/gJW-pfaqihA/1600x900'),
       (303, 'Eat hamburger', 'EATING', 'restaurant', 'hamburger', 'https://source.unsplash.com/FlmXvqlD-nI/1600x900'),
       (304, 'Eat pasta', 'EATING', 'restaurant', 'pasta', 'https://source.unsplash.com/b29Qdj7zc5g/1600x900'),
       (305, 'Eat ice cream', 'EATING', 'restaurant', 'icecream', 'https://source.unsplash.com/jnWGWSWTVqU/1600x900'),
       (306, 'Make a picnic', 'EATING', 'park', 'park', 'https://source.unsplash.com/3m5P_kEykqc/1600x900'),
       (400, 'Eating Activity', 'EATING', '???', '???', '???');

/* COOKING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (401, 'Make a pizza', 'COOKING', '???', '???', '???'),
       (402, 'Take a cooking class', 'COOKING', 'point_of_interest', 'cooking class', 'https://source.unsplash.com/bG5rhvRH0JM/1600x900'),
       (403, 'Cook a vegan meal', 'COOKING', '', '', 'https://source.unsplash.com/IGfIGP5ONV0/1600x900'),
       (403, 'Do an outdoor BBQ', 'COOKING', 'point_of_interest', 'barbecue area', 'https://source.unsplash.com/HH8D03HHqDI/1600x900'),
       (500, 'Cooking Activity', 'COOKING', '???', '???', '???');

/* SIGHTSEEING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (501, 'Explore the city', 'SIGHTSEEING', '???', '???', '???'),
       (502, 'Visit the Grossmünster', 'SIGHTSEEING', 'tourist_attraction', 'Grossmünster', 'https://source.unsplash.com/MSkwgjm9UEw/1600x900'),
       (503, 'Visit the Lindenhof', 'SIGHTSEEING', '', 'Lindenhof', 'https://source.unsplash.com/AGTiLHWv0FM/1600x900'),
       (600, 'Sightseeing Activity', 'SIGHTSEEING', '???', '???', '???');

/* OUTDOOR_ACTIVITY */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (601, 'Go on a bike tour', 'OUTDOOR_ACTIVITY', '???', '???', '???'),
       (602, 'Go for a walk', 'OUTDOOR_ACTIVITY', 'park', 'park', 'https://source.unsplash.com/t4aa3L7QLcI/1600x900'),
       (603, 'Do a boat tour', 'OUTDOOR_ACTIVITY', 'point_of_interest', 'boat', 'https://source.unsplash.com/g--YmYxD1a4/1600x900'),
       (604, 'Help at the animal shelter', 'OUTDOOR_ACTIVITY', 'point_of_interest', 'animal shelter', 'https://source.unsplash.com/uNNCs5kL70Q/1600x900'),
       (700, 'Outdoor Activity', 'OUTDOOR_ACTIVITY', '???', '???', '???');

/* MOVIES */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (701, 'Watch a horror movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/PbspBt8DOyY/1600x900'),
       (702, 'Watch a thriller movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/p3m7uRHDFac/1600x900'),
       (703, 'Watch a action movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/cqtw4QCfbQg'),
       (704, 'Watch a drama movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/E5bGCHzjpr8/1600x900'),
       (705, 'Watch a comedy movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/CiUR8zISX60/1600x900'),
       (706, 'Watch a love movie', 'MOVIES', 'movie_theater', 'Arena%20Cinemas', 'https://source.unsplash.com/Eg5KA2-0lg4/1600x900'),
       (800, 'Movies Activity', 'MOVIES', '???', '???', '???');

/* MUSEUMS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (801, 'Visit the Kunsthaus Zurich', 'MUSEUMS', 'museum', 'kunsthaus', '???'),
       (802, 'Visit the FIFA World Football Museum', 'MUSEUMS', 'museum', 'FIFA%20World%20Football%20Museum', 'https://source.unsplash.com/Y_upPEyxXN8/1600x900'),
       (803, 'Visit the zoological Museum of Zurich', 'MUSEUMS', 'museum', 'Zoologisches%20Museum%20Zürich', 'https://source.unsplash.com/BuQ1RZckYW4/1600x900'),
       (804, 'Visit the Museum Rietberg', 'MUSEUMS', 'museum', 'Museum%20Rietberg%20Museum%20Zürich', 'https://source.unsplash.com/D8NXYiz2uTQ/1600x900'),
       (805, 'Visit the Museum of Design', 'MUSEUMS', 'museum', 'Museum%20für%20Gestaltung%20Zürich', 'https://source.unsplash.com/56Sbpi_hrqU/1600x900'),
       (806, 'Visit the Kulturama / Museum of Man', 'MUSEUMS', 'museum', 'Kulturama', 'https://source.unsplash.com/keIjMFXUu0Y/1600x900'),
       (900, 'Museum Activity', 'MUSEUMS', 'museum', '???', '???');

/* WELLNESS */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (901, 'Wellness Activity', 'WELLNESS', '???', '???', '???'),
       (902, 'Get a massage', 'WELLNESS', 'spa', 'massage', 'https://source.unsplash.com/SMwCQZWayj0/1600x900'),
       (903, 'Go to the sauna', 'WELLNESS', 'spa', 'sauna', 'https://source.unsplash.com/_aWzq1pDoEU/1600x900'),
       (1000, 'Wellness Activity 2', 'WELLNESS', '???', '???', '???');

/* SHOPPING */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1001, 'Shopping Activity', 'SHOPPING', '???', '???', '???'),
       (1002, 'Go shoe shopping', 'SHOPPING', 'shoe_store', 'shoe', 'https://source.unsplash.com/OX_en7CXMj4/1600x900'),
       (1003, 'Do a makeover', 'SHOPPING', 'shopping_mall', 'shopping mall', 'https://source.unsplash.com/_3Q3tsJ01nc/1600x900'),
       (1100, 'Shopping Activity 2', 'SHOPPING', '???', '???', '???');

/* GAMES */
INSERT INTO ACTIVITY_PRESET (ID, ACTIVITY_NAME, ACTIVITY_CATEGORY, GOOGLEPOICATEGORY, GOOGLEPOIKEYWORD, IMAGEURL)
VALUES (1101, 'Gaming Activity', 'GAMES', '???', '???', '???'),
       (1102, 'Do an escape room', 'GAMES', 'point_of_interest', 'escape room', 'https://source.unsplash.com/RfdK3RQwuMk/1600x900'),
       (1103, 'Play Mario Kart', 'GAMES', '', '', 'https://source.unsplash.com/2X6rLd40nF0/1600x900'),
       (1104, 'Play FIFA', 'GAMES', '', '', 'https://source.unsplash.com/eCktzGjC-iU/1600x900'),
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