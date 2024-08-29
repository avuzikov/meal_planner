package mealplanner.service;

import mealplanner.dao.MealDao;
import mealplanner.util.DatabaseManager;

import java.util.Scanner;

public class MealPlanner {
	private final Scanner scanner;
	private final MealService mealService;
	private final PlanService planService;

	public MealPlanner() {
		this.scanner = new Scanner(System.in);
		DatabaseManager dbManager = new DatabaseManager();
		MealDao mealDao = new MealDao(dbManager);
		this.mealService = new MealService(mealDao, scanner);
		this.planService = new PlanService(mealDao, scanner);
	}

	public void start() {
		while (true) {
			System.out.println("What would you like to do (add, show, plan, save, exit)?");
			String command = scanner.nextLine().trim().toLowerCase();
			switch (command) {
				case "add":
					mealService.addMeal();
					break;
				case "show":
					mealService.showMeals();
					break;
				case "plan":
					planService.planMeals();
					break;
				case "save":
					planService.saveShoppingList();
					break;
				case "exit":
					System.out.println("Bye!");
					return;
				default:
			}
		}
	}
}