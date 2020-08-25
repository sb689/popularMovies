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
The android application shows a list of movies from movieDbApi. Users can sort movie lists based on rating, popularity, and favorite movies. When users select a movie from the list, a detailed page is displayed. Users can add/remove a movie from the favorite list. 

## Requirements:
- Present the user with a grid arrangement of movie posters upon launch.
- Allow the user to change sort order via a setting:
Sort orders are: popularity, highest-rated, favorites
- Allow the user to tap on a movie poster and transition to a detailed screen with additional information such as:
original title, movie poster image thumbnail, A plot synopsis, user rating, release date
- Allow users to view and play trailers (either in the youtube app or a web browser).
- Allow users to read reviews of a selected movie.
- Allow users to mark a movie as a favorite in the detailed view by tapping a button.
- Use Android Architecture Components (Room, LiveData, ViewModel, and Lifecycle) to create a robust and efficient application.
- Create a database using Room to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
- Clean and responsive UI

### Note
To run this app please create your API key following the link: https://www.themoviedb.org/documentation/api. Save the api key in values/secrets.xml file using 

 ```<string name="MOVIE_DB_API_KEY">Your API key</string>```
 
 Build and run the application. 


