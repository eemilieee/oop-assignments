Proiect etapa 1 - POO TV

Name: Arpasanu Emilia-Oana
Group: 321 CA

The idea

The program is designed to implement an entertainment platform (movie streaming)
that receives commands as an input and executes them according to the specified
rules. It consists of a main database which is manipulated by the users' actions
that take place on the pages they may access.

The structure

The built application follows an extensive class hierarchy that may allow further
development, highlighting encapsulation, abstractization, polymorphism and
inheritance. One of the main features is represented by the page hierarchy:
it is the base of the Visitor pattern that becomes the support of their
functionality. The user is able to navigate from one page to another according
to the platform's specifications by memorizing the current page he has accessed.
Once he has reached the desired page, specific actions may take place if
the command is valid; these commands manipulate the database used to store the lists
of users and movies with all their details (consisting of a Singleton pattern).
The page's utility is obtained by implementing the navigateTo() and use() methods
specifically for each existing type of page. All pages that construct the platform
are stored within a Singleton instance of the Application class, in order to make
the access easier through the program. The methods that handle all the exceptions
that may occur during the program's execution are found within the "validators"
package, divided in distinct utility classes for each page type.

The logic of the program

The entry point of the implementation begins with the retrieval of the JSON input
data, with the help of the Input class and the main objects' instantiations and
initializations: the database and the application. Then comes an iteration through
the actions that take place. There are two possible command types, "change page"
and "on page", which are applied to the logged-in user's current page by calling
its methods: access(), that executes the page switch and action(), responsible for
fulfilling the user's requests. If the actions are executed without any issues,
the JSON-formatted output shows the current state of the user, along with the
available movies and the ones that have been purchased, watched, liked and rated;
otherwise, errors are printed.

Project structure

* src/
  * checker/ - checker files
  * databases/ - Database and Application classes
  * entities/ - Movie and User classes
  * input/ - contains classes used to read data from the JSON files
  * pages/ - the pages' hierarchy: AuthPage, Login, Logout, Movies, Page (abstract),
		 Register, SeeDeatils, UnAuthPage, Upgrades 
		 + PageAccessor (interface -> Visitor pattern)
  * validators/ - the utility classes that contain the helper methods
		      for the implementation: checking conditions, actions, updates
			(for the current page, user and database)
  * Main - the entry-point of the program
  * Test - runs the checker
* checker/
	* input/ - contains the tests in JSON format
	* ref/ - contains all reference output for the tests in JSON format
