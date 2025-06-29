# Address Book

Address book app using JavaFX. Includes contact cards as well as groups to help users organise their contacts.

Allows CRUD operations for both contacts and groups, as well as searching and filtering features. 

Database handled using *MySQL*. Assumes a user has been created in MySQL with a valid username and password. When the user logs in for the first time the database will be created with the relevant tables. This is assuming a database with the name address_book has not already been set up.

Database config is stored in the DatabaseConfig java file. 


Testing has been added. For a number of tests user credentials are required to log into the database. This should be placed in a test.properties file inside a config folder in root folder. Data should be in the below format:
- db.url = *your_database_url*
- db.user = *your_username*
- db.password = *your_password*

Unit, Integration and UI testing are performed.


*(in progress)*