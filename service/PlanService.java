package mealplanner.service;

import mealplanner.dao.MealDao;
import mealplanner.model.Meal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PlanService {
	private final MealDao mealDao;
	private final Scanner scanner;

	public PlanService(MealDao mealDao, Scanner scanner) {
		this.mealDao = mealDao;
		this.scanner = scanner;
	}

	public void planMeals() {
		String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
		String[] categories = {"breakfast", "lunch", "dinner"};

		mealDao.clearPlan();

		for (String day : days) {
			System.out.println(day);

			for (String category : categories) {
				List<String> meals = mealDao.getAllMealsByCategory(category);
				Collections.sort(meals);

				for (String meal : meals) {
					System.out.println(meal);
				}

				Meal chosenMeal = chooseMeal(meals, category, day);
				mealDao.addMealToPlan(day, category, chosenMeal.getId());
			}

			System.out.println("Yeah! We planned the meals for " + day + ".\n");
		}

		printWeekPlan();
	}

	private Meal chooseMeal(List<String> meals, String category, String day) {
		while (true) {
			System.out.println("Choose the " + category + " for " + day + " from the list above:");
			String chosenMealName = scanner.nextLine().trim();
			if (meals.contains(chosenMealName)) {
				return mealDao.getMealByNameAndCategory(chosenMealName, category);
			}
			System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
		}
	}

	public void printWeekPlan() {
		List<Meal> weekPlan = mealDao.getWeekPlan();
		String currentDay = null;
		for (Meal meal : weekPlan) {
			if (!meal.getDay().equals(currentDay)) {
				if (currentDay != null) {
					System.out.println();
				}
				currentDay = meal.getDay();
				System.out.println(currentDay);
			}
			String mealType = getMealType(meal.getCategory());
			System.out.println(mealType + ": " + meal.getName());
		}
	}

	private String getMealType(String category) {
		switch (category.toLowerCase()) {
			case "breakfast":
				return "Breakfast";
			case "lunch":
				return "Lunch";
			case "dinner":
				return "Dinner";
			default:
				return "";
		}
	}

	public void saveShoppingList() {
		List<Meal> weekPlan = mealDao.getWeekPlan();
		if (weekPlan.isEmpty()) {
			System.out.println("Unable to save. Plan your meals first.");
			return;
		}

		System.out.println("Input a filename:");
		String filename = scanner.nextLine().trim();

		Map<String, Integer> ingredientCounts = new HashMap<>();
		for (Meal meal : weekPlan) {
			for (String ingredient : meal.getIngredients()) {
				ingredientCounts.put(ingredient, ingredientCounts.getOrDefault(ingredient, 0) + 1);
			}
		}

		try (FileWriter writer = new FileWriter(filename)) {
			for (Map.Entry<String, Integer> entry : ingredientCounts.entrySet()) {
				String line = entry.getKey();
				if (entry.getValue() > 1) {
					line += " x" + entry.getValue();
				}
				writer.write(line + "\n");
			}
			System.out.println("Saved!");
		} catch (IOException e) {
			System.out.println("An error occurred while saving the file: " + e.getMessage());
		}
	}
}
