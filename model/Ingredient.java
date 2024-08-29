package mealplanner.model;

public class Ingredient {
	private int id;
	private int mealId;
	private String name;

	public Ingredient(String name) {
		this.name = name;
	}

	public Ingredient(int id, int mealId, String name) {
		this.id = id;
		this.mealId = mealId;
		this.name = name;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMealId() {
		return mealId;
	}

	public void setMealId(int mealId) {
		this.mealId = mealId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}