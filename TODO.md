### Add exception handlers
Create exception handler class or few classes like here [here](https://github.com/TarasLavrenyuk/MovieReview/blob/master/src/main/java/com/cursor/moviereview/exceptions/handlers/MovieResponseEntityExceptionHandler.java). Use @ControllerAdvice annotation.

### Improve authentication mechanisms
1. Add userId to token
2. Create AuthenticationUtil class with the following methods: 
- getUsername()
- getUserId()
- getUserPermissions() - returns list of permissions
- hasRole(UserPermission permission) - checks whether user has some permission or not

### Use ResponseEntity.ok() instead new ResponseEntity(....)

### Add `limit`, `offset` and `sort` params to getAll() endpoints

### Add api documentation
Use Swagger or something else to create API documentation

### Add logs
User slf4j to log some important actions like createUser, login attempts, errors/exceptions and so on...

### Dockerize the app

### Deploy the app to heroku 
