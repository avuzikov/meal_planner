package mealplanner.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputValidator {
	public static String getValidCategory(Scanner scanner) {
		while (true) {
			System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
			String category = scanner.nextLine().trim().toLowerCase();
			if (category.equals("breakfast") || category.equals("lunch") || category.equals("dinner")) {
				return category;
			}
			System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
		}
	}

	public static String getValidName(Scanner scanner, String type) {
		while (true) {
			System.out.println("Input the " + type + " name:");
			String name = scanner.nextLine().trim();
			if (isValidName(name)) {
				return name;
			}
			System.out.println("Wrong format. Use letters only!");
		}
	}

	public static List<String> getValidIngredients(Scanner scanner) {
		while (true) {
			System.out.println("Input the ingredients:");
			String input = scanner.nextLine().trim();
			String[] ingredientArray = input.split(",");
			List<String> ingredients = new ArrayList<>();
			boolean valid = true;
			for (String ingredient : ingredientArray) {
				String trimmed = ingredient.trim();
				if (!isValidName(trimmed)) {
					valid = false;
					break;
				}
				ingredients.add(trimmed);
			}
			if (valid && !ingredients.isEmpty()) {
				return ingredients;
			}
			System.out.println("Wrong format. Use letters only!");
		}
	}

	private static boolean isValidName(String name) {
		return !name.isEmpty() && name.matches("[a-zA-Z ]+");
	}
}
