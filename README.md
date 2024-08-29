# Meal Planner

Meal Planner is a Java application that helps users plan their meals for the week and generate shopping lists. It allows users to add meals, view meal categories, plan weekly meals, and save shopping lists.

This project is part of the JetBrains Academy Backend Developer on Java course.

## Features

- Add meals with ingredients
- View meals by category
- Plan meals for the entire week
- Generate and save shopping lists
- Database integration for persistent storage

## How it works

1. **Adding meals**: Users can add meals by specifying the category (breakfast, lunch, or dinner), meal name, and ingredients.

2. **Viewing meals**: Users can view meals by category, seeing the meal names and their ingredients.

3. **Planning meals**: Users can plan meals for each day of the week, choosing from the available meals in the database.

4. **Generating shopping lists**: After planning meals for the week, users can generate a shopping list that combines all the necessary ingredients.

5. **Saving shopping lists**: Users can save the generated shopping list to a file.

## Usage

1. Run the application.
2. Choose from the following options:
   - `add`: Add a new meal
   - `show`: View meals by category
   - `plan`: Plan meals for the week
   - `save`: Save the shopping list (only available after planning meals)
   - `exit`: Exit the application

## Database

The application uses a PostgreSQL database to store meal information. Make sure you have PostgreSQL installed and create a database named `meals_db` before running the application.

## Dependencies

- Java 8 or higher
- PostgreSQL JDBC driver

## Setup

1. Clone the repository
2. Set up the PostgreSQL database
3. Update the database connection details in `DatabaseManager.java` if necessary
4. Compile and run the `Main.java` file

## Contributing

Feel free to fork the project and submit pull requests with any improvements or bug fixes.

## License

This project is open-source and available under the MIT License.

## Acknowledgements

This project was developed as part of the JetBrains Academy Backend Developer on Java course. It serves as a practical application of Java programming concepts, database integration, and software design principles.
