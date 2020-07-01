## Screenshots of the application

<table>
  <tr>
    <td>Main Screen</td>
     <td>Detail Screen</td>
     <td>Main screen with menu</td>
  </tr>
  <tr>
    <td><img src="/screenshots/Screenshot_1593541479.png" width=270 height=480></td>
    <td><img src="/screenshots/Screenshot_1593542484.png" width=270 height=480></td>
    <td><img src="/screenshots/Screenshot_1593542507.png" width=270 height=480></td>
  </tr>
 </table>



## Project Overview
This android application shows a list of movies from movieDbApi. Movies can be categorized as the highest rating movies, popular movies, and favorite movies. Selecting a movie from the grid view will display details of a movie. Users can add/remove a movie from the favorite list. Developed as part of Udacity Android nanodegree program

## Requirements
- Present the user with a grid arrangement of movie posters upon launch.
- Allow user to change sort order via a setting:
The sort order can be by most popular or by highest-rated or favorites
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
original title, movie poster image thumbnail, A plot synopsis (called overview in the api), user rating (called vote_average in the api), release date
- Allow users to view and play trailers (either in the youtube app or a web browser).
- Allow users to read reviews of a selected movie.
- Allow users to mark a movie as a favorite in the details view by tapping a button.
- Use Android Architecture Components (Room, LiveData, ViewModel and Lifecycle) to create a robust and efficient application.
- Create a database using Room to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).

## Why this Project?
To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.

By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience. 

## Learning objectives
- Fetch data from the Internet with theMovieDB API.
- Use adapters and custom list layouts to populate list views.
- Incorporate libraries to simplify the amount of code you need to write
- Build a fully-featured application that looks and feels natural on the latest Android operating system.

### Note
To run this app please create your API key following the link: https://www.themoviedb.org/documentation/api. Save the api key in gradle.properties file using key MOVIE_DB_API_KEY. Build and run the application. 


