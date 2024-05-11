# MAD24_P04_Team2
# Introduction
The goal of the project is to create a flashcard app that would allow users to add text, images, video, and sound to the app to be able to make a flashcard. The flash card would have a question and an answer section that the user can fill up. After making the flashcard, the users would be able to review them. The users would also be able to group th flashcard together to organise them.

# Motivation/Objective
This app is to enable students to be able to easily create flashcards and be able to rewiew them practicing their different subject. The app is also trying to reduce the amount of paper being used when students make flashcard and allow the students to take them wherever they go as it is all in their phones.

# App category 
The category of the app is Educational

# Stage 1 Task
**(All pages use responsive layout to scale to the correct size of the phone)**

Feature 1- logging in (User Identity from core android developer guide):<br>
Android developer guide for User identity - https://developer.android.com/training/sign-in<br>
•	Create a login page that allow users to enter their username and password<br>
•	Move to an authentication page where the user needs to key the OTP shown in a toaster<br>
![alt text](Feature1.png)
 
Feature 2 – Flashcards (Persistent memory + recycler view)<br>
•	There will be a button on the main screen go to “flash card decks page”<br>
•	In the “flash card decks page” user can either review deck or create new deck<br>
•	When user create new deck, they must first enter a name<br>
•	After entering a name, they will be directed to make new cards page where they can enter words in the front and back of the card. <br>
•	After making the card they can either make a new card or finish to stop making cards.<br>
•	If the user clicks to the review deck, they would be able to view the front of the deck and there will be a view answer button.<br>
•	They can then click if they got the question right and go to the next card.<br>
![alt text](Feature2.png)
 
Feature 3 – Memo (Persistent memory + Multimedia)<br>
•	In the main page, there will be a button to go to the memo page.<br>
•	The memo page is for short notes for the user know<br>
•	They can add a memo or view the memo<br>
•	In the add memo, user can add text, video, image, and audio.<br>
•	The user will be limited to a certain number of characters or one video or one image or one audio<br>
![alt text](Feature3.png)
 
Feature 4 - Study sessions (alarm from core android developer guide) <br>
Android developer guide for alarm - https://developer.android.com/develop/background-work/services/alarms <br>
•	In the main page, there will be a button to set study sessions.<br>
•	In the study sessions page, users will be able to set an alarm that will go off in 1-60 mins.<br>
•	This alarm will still work in the background if the user is still in the app.<br>
 ![alt text](Feature4.png)


Feature 5 – Notifications (Services from core android developer guide)<br>
Android developer guide for service -  https://developer.android.com/develop/background-work/services<br>
•	There will be a settings page to enable the notification<br>
•	The user will be able to set the time interval of the notification<br>
•	The notification will then play in the app after the user did not use the app for the specified time interval.<br>
•	This will still work if the app is closed.<br>
 ![alt text](Feature5.png)



# Stage 2 Task
•	Speech to text for answering questions (Using API from google)<br>

•	Allow user to share decks with others globally and allow for rating (using firebase cloud messaging)<br>

•	Analytical report on the number of cards answered correctly and cards reviewed and daily login.<br>

•	Setting for the decks to change the looks of it such as colours. (can use recycler views in a recycler view to get this.)<br>

•	Fingerprint login to increase security. (use biometric sensor)<br>

•	Export and import page for back up of the app<br>