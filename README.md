# Address Book

Address book app using JavaFX. Includes contact cards as well as groups to help users organise their contacts.

Allows CRUD operations for both contacts and groups, as well as searching and filtering features. 

Database handled using *MySQL*. Currently only allows the user that owns the database to login and interact with the app.
Assumes database structure has already been set up as it stands. This will be changed.

Testing has been added. For a number of tests user credentials are required to log into the database. This should be placed in a test.properties file inside the config folder in the below format:
- db.url = *your_database_url*
- db.user = *your_username*
- db.password = *your_password*

Unit, Integration and UI testing are performed.


*(in progress)*