# Sync.it
Cloud-based notes app

This project is postponed because I started it primarily for learning how to work with Firebase. However, I do not see any reason to invest my time in it or get it ready for Playstore. There are many note apps on Playstore providing many features for free, however, even if I implement those features, I can not afford to pay for renting charges or advertisements. Also, my goal was to give the application a material theme by using Android Material Designs library. Honestly, It was so frusturating to work it and I was spending significant amount of time on understanding and fixing UI problems than to work on the actual business logic of the app.



Currently, the app needs an authentication view, a handle user session, and other minor things. It's a small task but again, there is barely any learning for me in it.

What can the app do currently?
- Track any changes made in the notes stored in the local database
- Log all tracked changes to a local database
- Initiate cloud syncing based on certain conditions set via WorkManager

What critical features need to develop?
- Download all the notes from a cloud database to a local database
- Handle user session
- Implement proper navigation



I wrote my own syncing logic as opposed to using the local database and change tracker offered by Firebase Database, but I do not recommend it because it is a simply re-invented wheel. Firestore provides an offline caching feature which should be preferred. Why I did it? For learning, of course.


The app is based on:
- MVVM
- WorkManager
- Coroutines
- Firestore
- FirebaseAuth
- Room DB
- SQLCipher
- EncryptedSharedPreference


## F.A.Q

### Would you ever complete the project?
I don't see myself completing it anytime in the future. But that does not mean that it is abandoned. 

### Do you think that Firestore is a good option for a project like this?
Apparently, if you set it upright, it is possible to decrease the probability of increased reads/writes. Can it scale? Again, it depends on how you set things up. Firebase documentation explains different ways to modeling your database.
