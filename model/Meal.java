package mealplanner.model;

import java.util.List;

public class Meal {
	private int id;
	private String category;
	private String name;
	private List<String> ingredients;
	private String day;  // New field for the day of the week

	public Meal(String category, String name, List<String> ingredients) {
		this.category = category;
		this.name = name;
		this.ingredients = ingredients;
	}

	public Meal(int id, String category, String name, List<String> ingredients) {
		this(category, name, ingredients);
		this.id = id;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}
