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
  
