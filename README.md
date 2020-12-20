# minesweeper-API
## Introduction
This is Matias R. Graziani implementation of the minesweeper-API.

## What we try to achieve
This is API where you can create your profile, and play in it. 
With your profile, you can: 
* Play your game from any UI you have that use this interface
* Leave the game and continue later, or play other in the meantime.
* Know how many games you have and how well you did it on them.
* Track the time, from the when you make the first click until you end. (Since is an API you can't stop the clock)
* Have 2 players playing at the same time on different games, while they have 2 different profiles
* Don't lost on the first click, mines are set after you click on the first set to prevent this.
* Show where mines were set in case you lost the game.
* Inteligence to set mines as flagged if you see all other sets.
* 2 auxiliary flags (doesn't affect the game) red and question flag so you can set it if you are still analyzing what to do.

## Our thoughts of how to continue this
* We should implement SpringSecurity, so we get the user from the principal and no one can modify your game, just you.
* Have a UI (This is not part of the API but will be nice to have a nice way to play it)

## How to play it
you can use Swagger UI to use and check the endpoints:
* https://minesweeper-mgraz.herokuapp.com/swagger-ui/#/

You can use API docs to generate your own client:
* https://minesweeper-mgraz.herokuapp.com/v2/api-docs

## API DOCUMENTATION:
### Create a user
* POST https://minesweeper-mgraz.herokuapp.com/player 
```
{
  "id": 1,
  "name": "Mati"
}
```
### Get the user
* GET https://minesweeper-mgraz.herokuapp.com/player/{userId} 
### Create a game
* PUT https://minesweeper-mgraz.herokuapp.com/game/{userId} 
### Create a game custom
* POST https://minesweeper-mgraz.herokuapp.com/game/{userId} 
```
{
  "horizontalSize": 0,
  "mines": 0,
  "verticalSize": 0
}
```
### Get all player games
* GET https://minesweeper-mgraz.herokuapp.com/game/{userId} 
### Get all games
* GET https://minesweeper-mgraz.herokuapp.com/game
### Switch to other player game
* PUT https://minesweeper-mgraz.herokuapp.com/game/switch/{userId}/{gameId}
### Click a cell in a game
* POST https://minesweeper-mgraz.herokuapp.com/cell/click
```
{
  "horizontal": 0,
  "userId": 0,
  "vertical": 0
}
```
### Flag a cell in a game
* POST https://minesweeper-mgraz.herokuapp.com/cell/flag
```
{
  "horizontal": 0,
  "userId": 0,
  "vertical": 0
}
```
### Question a cell in a game
* POST https://minesweeper-mgraz.herokuapp.com/cell/question
```
{
  "horizontal": 0,
  "userId": 0,
  "vertical": 0
}
```
### Red Flag a cell in a game
* POST https://minesweeper-mgraz.herokuapp.com/cell/red-flag
```
{
  "horizontal": 0,
  "userId": 0,
  "vertical": 0
}
```

## Comments
Probably if you try to create the UI will ask for a few changes, for example have a flag to know if win or lost, and not have to check on cells if is one exploited, but all that is adjustment I think API cover all that is need.

Hope you have fun with it!!!

Matias

# Original content
API test

We ask that you complete the following challenge to evaluate your development skills. Please use the programming language and framework discussed during your interview to accomplish the following task.

PLEASE DO NOT FORK THE REPOSITORY. WE NEED A PUBLIC REPOSITORY FOR THE REVIEW. 

## The Game
Develop the classic game of [Minesweeper](https://en.wikipedia.org/wiki/Minesweeper_(video_game))

## Show your work

1.  Create a Public repository ( please dont make a pull request, clone the private repository and create a new plublic one on your profile)
2.  Commit each step of your process so we can follow your thought process.

## What to build
The following is a list of items (prioritized from most important to least important) we wish to see:
* Design and implement  a documented RESTful API for the game (think of a mobile app for your API)
* Implement an API client library for the API designed above. Ideally, in a different language, of your preference, to the one used for the API
* When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat)
* Ability to 'flag' a cell with a question mark or red flag
* Detect when game is over
* Persistence
* Time tracking
* Ability to start a new game and preserve/resume the old ones
* Ability to select the game parameters: number of rows, columns, and mines
* Ability to support multiple users/accounts
 
## Deliverables we expect:
* URL where the game can be accessed and played (use any platform of your preference: heroku.com, aws.amazon.com, etc)
* Code in a public Github repo
* README file with the decisions taken and important notes

## Time Spent
You need to fully complete the challenge. We suggest not spending more than 5 days total.  Please make commits as often as possible so we can see the time you spent and please do not make one commit.  We will evaluate the code and time spent.
 
What we want to see is how well you handle yourself given the time you spend on the problem, how you think, and how you prioritize when time is sufficient to solve everything.

Please email your solution as soon as you have completed the challenge or the time is up.
