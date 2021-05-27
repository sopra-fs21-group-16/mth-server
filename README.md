# SoPra FS21 - Group 16

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs21-group-16_mth-server&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=sopra-fs21-group-16_mth-server)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs21-group-16_mth-server&metric=security_rating)](https://sonarcloud.io/dashboard?id=sopra-fs21-group-16_mth-server)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs21-group-16_mth-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=sopra-fs21-group-16_mth-server)

## MoreThanHey (Backend)

### Introduction
**MoreThanHey** is an activity-based matchmaking mobile web app with the feature-twist that you are not swiping other people, but fun free-time activities. Set up your profile and choose your interests, so you'll be ready to be matched! You will be proposed activities, where you can swipe right for approving or left for rejecting the activity. Based on these decisions, you get a potential match list with other persons preferring the same activities and matching with the your preferences (age, gender, location). If you and your match are online, you can go through a step-by-step real-time selection, where you can vote on a concrete activity, a time and the location where you want to meet. After successfully completing these steps and coming to an agreement the event gets added to a list for the user to keep track of upcoming activities.

### Technologies
We used Java and Spring Boot for the backend of the MoreThanHey application. To test our application, we use JUnit and Mockito. 

### Main Components
Our Backened uses Models, Controllers, Services and REST DTO Mappers. Our main maincomponents:
- User ([UserController](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/UserController.java), [UserService](https://github.com/sopra-fs21-group-16/mth-server/tree/master/src/main/java/ch/uzh/ifi/hase/soprafs21/service))
    - All major components are based on or at least use the `User` class and its `UserService`. The `ActivityService` needs the user object to generate fitting activities based on the users interests, as well as machting users with a potential user based on their interests and preferences (such as age or gender).
- Activity ([ActivityController](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/ActivityController.java), [ActivityService](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/service/ActivityService.java))
    - An Activity object is based on an [ActivityPreset](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entities/ActivityPreset.java) (containing the information about an activity, i.e. type & name of an activity, an image and Google POI API keywords) and a list of [UserSwipeStatus](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entities/UserSwipeStatus.java) (containing the users to be matched with the activity, as well as their current swipe status). Therefore, generating an activity encompasses the matchmaking process: selecting users that are a potential match for each other, selecting potentially interesting activities for them, generating these activities and sending these Activities to the front-end for the users to be swiped.
- Date-Scheduling ([SchedulesController](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/SchedulesController.java), [SchedulingService](https://github.com/sopra-fs21-group-16/mth-server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/service/SchedulingService.java))
    - If both users of an activity have swiped that activity to the right, they are matched. As soon as both users are online, they can enter the real-time Scheduling process to specifiy their date details. Based on the `ActivityPreset` information, locations are proposed if appropriate (e.g. a visit to an art museum will provide concrete art museums to be selected by the users - the data is fetched from Google POI API).

### Launch & Deployment
This repository can be downloaded and run via Gradle. Gradle will install all needed dependencies and automatically run tests if the project is being built. Implementing new features will trigger our JUnit tests automatically when rebuilding the project.

#### Build
```
./gradlew build
```
#### Run
```
./gradlew bootRun
```
#### Test
```
./gradlew test
```
#### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

### Roadmap

### Authors and acknowledgment
#### Backend 
- Aleksandar Ristic
- Samuel Brügger
- Tarek Alakmeh // Backend Coordinator

#### Frontend
- Amos Calamida // Frontend Coordinator
- Micha Eschmann

#### Further Acknowledgment
- Marion Dübendorfer // TA
- Tomas Fritz, Prof. Dr.

### License
- Under our own Copyright &copy; 
