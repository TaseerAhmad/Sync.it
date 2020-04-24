# Sync.it
Cloud based notes app

This project is postponed because I started it primarly for learning how to work with Firebase. However, I do not see any reason to invest my time in it or get it ready for Playstore. There are many note apps on Playstore providing many features for free, however, even if I implement those features, I can not afford to pay for renting charges or advertisements.



Currently, the app does not include a login screen and the icons need to be replaced. It's a small task but again, there is barely any learning for me in it.

I wrote my own syncing logic but I do not recommend it because it is a simply re-invented wheel. Firestore provides an offline caching feature which should be preferred. Why I did it? For learning, ofcourse.


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
Apparently, if you set it up right, it is possible to decrease the probability of increased reads/writes. Can it scale? Again, it depends on how you set things up. Firebase documentation explains different ways to modeling your database.
