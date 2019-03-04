### This is a Java server designed to support this react client: https://github.com/tmandel2/project3_bookMatching. The original was created by Jacob Mink, original hosted at https://github.com/jacobmink/project3_bookMatching.

### The deployed server integrates with PostgreSQL, but it is able to work with MySQL as well.

#### The database contains three tables, one for Users, one for Books, and a through table to connect them.

Routes:
	/books/{id} : Get and delete routes.
	/books/ : Get index route.
	/books/{id} : Put route (unused in this app).
	/users/books/{id} : Delete route to remove from a user's favorites.
	/books/ : Posting a new book from the google search.
	/users/ : Get all users (unused in this app).
	/users/{id} : Delete and get specific user.
	/users/{id} : Put route for editing a specific user.
	/auth/registration : Posting a new user.

## Technologies Used
### Java
#### IntelliJ IDE, MySQL, PostgreSQL, React, Google Books API