# FamilyAlbum #
## :camera: Overview :family: ##
FamilyAlbum is an Android app which creates and manages photo albums for families, allowing them to upload photos from family events, share and comment. Although intended for families, FamilyAlbum can also be used by other user-specific groups such as co-workers, classmates etc. The app allows a user to create a family album and let others join, view, edit and add additional users. The app features a simple design, suitable for users of all ages, and resembles a conventional photo gallery. Photos uploaded to the app can be sorted into separate albums with details about the time and place of the event in which the photos were taken. A built in chat allows the users to discuss about the events and comment about the photos.

## :iphone: Usage :video_game: ##
The app features a user authentication step, which prompts the user to create a new FamilyAlbum account, or login using an existing account.

<img src="https://i.imgur.com/kuV0wUi.png" width="250">

Once the user has logged in, if he is not already connected to a family, two options are available. He can either create a new family and invite others to join, or he can join an existing family. 

<img src="https://i.imgur.com/N3PCvMM.png" width="250">

If the user decided to create a new family, or alternatively if he is connected to an existing family, he can access the family serial number which allows others to connect by using the "join" option.

<img src="https://i.imgur.com/RjYxAQw.png" width="250">

Clicking the "Get Serial" allows the user to view the serial number in order to share with his family.

<img src="https://i.imgur.com/1ZT8qhr.png" width="250">

Once connected to a family, the user can create new albums. Details about the event that is connected to the album are delivered at the time of creating the album.

<img src="https://i.imgur.com/sj8SrgH.png" width="250">

Albums can be viewed at the homepage of the app.

<img src="https://i.imgur.com/Pplqif0.png" width="250">

Inside an album, by pressing the camera symbol, every user can add photos, either from the phone gallery, or directly using the camera in real-time.

<img src="https://i.imgur.com/IvYfFUz.png" width="250">

The albums update when photos are added and can be viewed by all users.

<img src="https://i.imgur.com/1hdJjF9.png" width="250">

Clicking the chat button, which appears in each album, opens a chat that allows the users to comment about photos in the album, discuss, share their experience etc. 

<img src="https://i.imgur.com/wrQwoBv.png" width="250">

## :hammer: App Architecture :construction_worker: ##
FamilyAlbum is used for album management which includes both keeping the photos themselves and the chats about each album. As such, it has to store textual data as well as images.
We decided to use Firebase as a backend, we stored the images in Firebase Storage and the data in their Realtime Database service. Using Firebase allowed us to implement an instant notification to the app immediately as the data updates.
In order to give our users the best experience, we used the relational database - SQLite as Android comes with a built in SQLite database implementation.
All the data is stored on the device and only new data is added when needed.

<img src="https://i.imgur.com/bj9ZoGu.jpg">

**_Developed by Edan Leibovitz :man: & Adi Mashiah :woman:_**

**_edan.leibo@gmail.com_**

**:smile: Feel free to use it at will with attribution of source. :bowtie:**

**:astonished: If you find any typos, errors, inconsistencies etc. please do not hesitate to contact me for any issue. :mailbox:**
