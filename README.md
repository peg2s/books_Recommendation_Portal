# About project

### This is a book recommendation service
Current functionality:

* register/login with activation via email
* adding new books
* books list view, search by author/title/everywhere, filtering by genre
* when books are displayed in card-columns, the annotation is truncated
  to the nearest space character 150 and the link "... Read more" is added
* random book from DB for each click on menu link "Random book"
* administrative functions for users with admin rules (premod of books, editing users, etc.)
* possibility to rate book and view all rated books for each authorized user

### Stack

* Spring Boot, Spring Security
* Lombok
* PostgreSQL
* Bootstrap
* JS
* Thymeleaf

### Backlog
Coming soon:

* password recovery via email by unique link
* extended account profile for user role
* RabbitMQ/ ActiveMq/ Kafka?
* database caching
* integration with ML model written on python
* minor fixes and improvements

### View and test

https://lit-hamlet-12359.herokuapp.com/
Autodeploy when git push detected. Test admin credentionals is admin/password.
