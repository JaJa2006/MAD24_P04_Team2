# MAD24_P04_Team2
# Introduction
The goal of the project is to create a flashcard app that would allow users to add text, images, video, and sound to the app to be able to make a flashcard. The flash card would have a question and an answer section that the user can fill up. After making the flashcard, the users would be able to review them. The users would also be able to group th flashcard together to organise them.

# Motivation/Objective
This app is to enable students to be able to easily create flashcards and be able to rewiew them practicing their different subject. The app is also trying to reduce the amount of paper being used when students make flashcard and allow the students to take them wherever they go as it is all in their phones.

# App category 
The category of the app is Educational

# Stage 1 Task
(All pages will use responsive layout to fit the size of the screen)

Login page – allow users to login to their account

Main Deck page – This page contains all the decks of cards; users can select the deck they want to edit or change or make a new deck. 
•	(use recycler view, users will be able to scroll to the deck they want)

Create deck page – in this page the users can set the title for the decks. Then it would go to the create card Page.

Add Card Page – in this page, the user will be able to create the cards, entering the questions and the answer. There would be a button to exit after all the cards are finished. 
•	(Use persistent memory to save the data of the cards.) 
•	(Also use Multimedia as users will be able to add image, video or audio for the question and answer.)

View Deck Page - when users click on a deck that has been made, they will go to a new activity and be able to change deck name, add cards, revise cards and delete cards. (change deck name and delete cards will be on the same activity)

Revise card deck page - On the revise deck page, users will be able view the question and there will be a show answer button. On clicking this button, the activity would show the answer and a correct or wrong button will appear for the user to click if they got the question right. The user will also be able to switch to the previous or next card.

# Stage 2 Task
•	Speech to text for answering questions (Using API from google)

•	Allow user to share decks with others globally and allow for rating (using firebase cloud messaging)

•	Analytical report on the number of cards answered correctly and cards reviewed and daily login.

•	Setting for the decks to change the looks of it such as colours. (can use recycler views in a recycler view to get this.)

•	Fingerprint login to increase security. (use biometric sensor)

•	Export and import page for back up of the 