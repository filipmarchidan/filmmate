# Filmmate
This README contains information about Filmmate. Filmmate is a **film-taste based dating app**  we are developing for our course, CSE2000. You can see our detailed RoadMap [here](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2020-2021-q4/cluster-00/filmmate/filmmate-front-end/-/issues?scope=all&sort=created_asc&state=all&utf8=%E2%9C%93).


# ![logo](https://images-ext-1.discordapp.net/external/SpGYl0z5BDfXm5ksGLsm3q15VVYL8laitCyhq6z93xI/https/media.discordapp.net/attachments/729354405958385795/813461460696039444/logo.png?width=333&height=300)

> Filmmate - match with people based on your film taste

## Table of Contents

- How to run
- Features
- Tools Used
- Authors


## How to Run
After downloading the 'zip' file of the `master` branch, open the extracted folder in [Android Studio](https://developer.android.com/studio) 
Following this, `./gradlew build` should be ran. As a consequence, 'gradle' will get the project built, taking care of all dependencies.
This build can be invoked manually at any point as seen in the GIF below
![Gradle build](https://i.gyazo.com/5e1ae0942e432471d9bfdb82f2e04375.gif)

Once this is done, our project is ready to be run. In order to achieve these, you have two different possibilities:
1) Connect your phone to your computer via USB and installing and running the app locally on your phone.
2) Use  [Android Virtual Device Manager](https://developer.android.com/studio/run/managing-avds) and create an emulator to run the app on your computer just like you would on your phone. For this you'd just need to first create a virtual device and then just run it as seen in the GIF below.

[![virtual device](https://i.gyazo.com/84043d3e020794f2299a04e9ecfc369b.gif)](https://gyazo.com/84043d3e020794f2299a04e9ecfc369b)

## Features
### - Discover Cinema
As a user, you'll be able to view the popular and best movies and tv shows, get tailored recommendations and search for the movies you'd like to find.
<br/>
[![RECOMMENDED](https://i.gyazo.com/293e99b0b3dd887a0f170ff521d4b594.gif)](https://gyazo.com/293e99b0b3dd887a0f170ff521d4b594)
<br/>

### - Rate and review movies
After searching for a movie, or even finding one in your recommendations, popular lists, or anywhere else, if you click on it, you'll be able to rate it and leave a review, which will be stored and taken into account for recommendations. Both can of course be modified at any point in time. 
<br/>

[![zhivago](https://i.gyazo.com/52e6209e18228ac3954c262b5c7982a5.gif)](https://gyazo.com/52e6209e18228ac3954c262b5c7982a5)

### - Match with recommended users
On you'll main screen you'll find suggested matches. You can swipe right or left on them. If both you and the other user swipe right on each other, you'll be matched with them and you'll be able to talk to them.
<br/>

[![matching](https://i.gyazo.com/f61ca5ff0fb393d673eec253cbdbc18a.gif)](https://gyazo.com/f61ca5ff0fb393d673eec253cbdbc18a)

### - Chat with your matches
Once you've matched with another user, as we mentioned before, you can talk to them. A representation of this can be seen in the GIF below.
<br/>

[![cutie](https://i.gyazo.com/3005a353148440a8fcbc627a0975c26e.gif)](https://gyazo.com/3005a353148440a8fcbc627a0975c26e)

### - Edit your profile
Your profile is important as it displays information that will let other users know what kind of film lover you are. For instance your favorite movies, actors, quote, bio... Of course, all these can be edited.
<br/>

[![bette](https://i.gyazo.com/9de1ceace0736fc45ffae91f7216e428.gif)](https://gyazo.com/9de1ceace0736fc45ffae91f7216e428)
## Tools
### Static analysis tools
| Name | Description |
| --- | --- |
| [`pmd`](https://pmd.github.io/) | Analyzes source code for flaws |
| [`checkstyle`](https://checkstyle.sourceforge.io/) | Ensures coding standards |
| [`lint`](https://developer.android.com/studio/write/lint) | Improves UI code |
### Libraries
| Name | Description |
| --- | --- |
| [`Retrofit 2`](https://square.github.io/retrofit/) | Http client for java |
| [`RxJava 2`](https://www.baeldung.com/rxjava-2-flowable) | Consume sequences of data |
| [`Glide`](https://github.com/bumptech/glide) | Image loading framework |
| [`JUnit 5`](https://junit.org/junit5/) | Automated unit testing |
| [`Mockito`](https://site.mockito.org/) | Mocking testing framework |
### Other tools
| Name | Description |
| --- | --- |
| [`Firebase`](https://firebase.google.com/) | Database solution |
| [`Tmdb API`](https://www.themoviedb.org/documentation/api) | For getting movie data |

### Important note for developers
If one wishes to test the social authentication while the application is in development mode, then **SHA1** and **SHA256** emulator certificates are required to be added in the Firebase console. To add your certificates go to _Project Settings -> SDK setup and Configuration -> SHA certificates fingerprints_. Those fingerprints allow you to communicate with the required Google modules that are used for **Phone**, **Facebook** and **Google** authentication. Once the application has been deployed there will be one unique fingerprint for the entire application. 
A TMDB API key needs also to be added to the gradle.properties file in the variable TMDB_API_KEY before deploying the application. The key can be created at the TMDB website.

## Authors

* Rahul Crunal Kalaria
* Filip Marchidan
* Nada Mouman
* Pablo Biedma
* Grigory Prikazchikov

