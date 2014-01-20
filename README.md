AppStudio - Hangman
=========

AppStudio minor programmeren 2013-2014 course


Description
========

Immediately upon launch, gameplay starts (or resumes).

The app displays hyphens for unguessed letters.

The app informs the user numerically how many incorrect guesses he or she can still make before losing.

The app displays the unguessed letters.

Input is given through the on-screen keyboard.

Invalid input (e.g., multiple characters, no characters, characters already inputted, punctuation, etc.) are ignored but not penalized. However guessing the correct word ends the game.

The app has the title Hangman and one button that starts a new game.

After the user has guessed every letter in some word before running out of chances, the user is congratulated, and the highscores are shown. If the user has failed to guess every letter in some word before running out of chances, the user is shown a full hangman and given the option to start a new game or display the highscores. 

The user can configure two settings: the length of words to be guessed (the allowed range for which must be [1,n], where n is the length of the longest word in words.plist/xml); and the maximum number of incorrect guesses allowed (the allowed range for which must be [1,26]).

The app maintains the history of high scores of 10 games. The highest scoring word is the word that has been guessed with the least incorrect guesses.
