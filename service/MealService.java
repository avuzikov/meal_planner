package mealplanner.service;

import mealplanner.dao.MealDao;
import mealplanner.model.Meal;
import mealplanner.util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class MealService {
	private final MealDao mealDao;
	private final Scanner scanner;

	public MealService(MealDao mealDao, Scanner scanner) {
		this.mealDao = mealDao;
		this.scanner = scanner;
	}

	public void addMeal() {
		String category = InputValidator.getValidCategory(scanner);
		String name = InputValidator.getValidName(scanner, "meal's");
		List<String> ingredients = InputValidator.getValidIngredients(scanner);

		Meal meal = new Meal(category, name, ingredients);
		mealDao.addMeal(meal);
		System.out.println("The meal has been added!");
	}

	public void showMeals() {
		System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
		String category = InputValidator.getValidCategory(scanner);
		List<Meal> meals = mealDao.getMealsByCategory(category);

		if (meals.isEmpty()) {
			System.out.println("No meals found.");
		} else {
			System.out.println("Category: " + category);
			for (Meal meal : meals) {
				System.out.println("Name: " + meal.getName());
				System.out.println("Ingredients:");
				meal.getIngredients().forEach(System.out::println);
				System.out.println();
			}
		}
	}
}
