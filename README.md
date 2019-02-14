# EyyAuto   [![CircleCI](https://circleci.com/gh/EyyAuto/EyyAuto/tree/master.svg?style=svg)](https://circleci.com/gh/EyyAuto/EyyAuto/tree/master)

An app to get an auto conveniently within the campus of National Institute of Technology Calicut

# Developer setup

Overview: Android Studio setup, Firebase setup

1. Download and install Android Studio 3.3
2. Use gradle 4.10 (Android Studio should ask you to do this automatically)
3. If you are a member of the EyyAuto Organization or an external collaborator, checkout this repository using built-in github support. If not, fork this repository into your account and checkout the forked copy.
4. Sync gradle and make sure to install any additional SDK's required (Studio should show you which ones are missing)
5. Now if you are an EyyAuto member, go to your firebase console and make sure you have editor access to the eyyauto project. If not contact the owner of the project and request editor access.
6. Once you have access, go to project settings and download the google-services.json file.
7. place this file in the directory EyyAuto/app/src/debug/google-services.json where EyyAuto is the root of the project.
8. Build and Run on an emulator or your device to make sure everything works as expected.

If you are not a member however, things get a little more difficult. Follow these steps instead of steps 5-6 above:

5. Create a new a firebase project from your firebase console.
6. Click on add android app and enter the package name in.ac.nitc.eyauto.
7. Enter the SHA-1 key of the debug.keystore file present in app/ directory. You should be able to do this via the gradle side pane using signingReport.
8. Download the google-services.json file now.
9. Make sure to enable at least google signin and phone verification in firebase authentication providers.
Continue from step 7 in the previous setup instructions.

Note:
  You can use any IDE you wish. However the above mentioned steps are recommended to maintain uniformity.
  You can find steps for setting up Native IntelliJ:
  
  https://www.jetbrains.com/help/idea/2017.1/getting-started-with-android-development.html
  
  Eclipse is discouraged due to the following post:
  
  https://android-developers.googleblog.com/2015/06/an-update-on-eclipse-android-developer.html
  
# Reference

## Database Structure

This app uses Firebase Real-Time Database and has the following structure:

![](https://github.com/EyyAuto/EyyAuto/blob/master/database.png)

### Description:

The database has 4 distinct parts namely, _User Info_, _Driver Info_, _Real Time Driver Location_ and _Requests_.

The _User Info_ Section contains basic user information such as name and phone number (at least) and other
shareable information needed for proper functionality.
This section is expected to be static i.e., data changes very rarely, if ever.

The _Driver Info_ section is analogous to the _User Info_ section for drivers and has the same features.

The _Real Time Driver Location_ section contains the real time location data of the driver stored in a format
consistent with the GeoFire API.
This section is expected to be very volatile.

And finally we have the _Requests_ section. It contains information about pending requests sent by the users.
Drivers will be automatically assigned to a pending request once they choose to accept requests.
The information stored here includes driver id, user id, pick up location and drop off location.
This section is expected to be moderately volatile.

## Workflow

The workflow to be followed will be as described in the following reference:
https://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project

**TL;DR**:
1. Create a new topic branch and push commits.
2. Create a Pull Request to Propose changes.
3. Changes will be reviewed by peers.
4. Modifications made to the PR if necessary.
5. Resolve merge conflicts by merging changes from master onto the topic branch
6. Merge branch once A-OK.

Specifically for this project, we have two protected branches: master and testing.
_master_ is intended to contain the latest stable code and needs no explanation.

_testing_ is used for the following:
1. As a base branch to any new topic branches.
2. To contain the latest working code, but not UI tested in firebase test lab nor real world scenarios.

As firebase test lab is a resource that needs to be managed, it is uneconomical as well as unnecessary
for every topic branch to be tested on test lab. It is a much better practice to find problems at the
source level first.

Naturally, Pull Requests from topic branches must be created against _testing_ branch only.
Only on futher testing will _testing_ then be merged onto _master_.