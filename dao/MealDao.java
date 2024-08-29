package mealplanner.dao;

import mealplanner.model.Meal;
import mealplanner.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDao {
	private final DatabaseManager dbManager;

	public MealDao(DatabaseManager dbManager) {
		this.dbManager = dbManager;
	}

	public void addMeal(Meal meal) {
		String insertMealSQL = "INSERT INTO meals (meal_id, category, meal) VALUES (?, ?, ?)";
		String insertIngredientSQL = "INSERT INTO ingredients (ingredient_id, meal_id, ingredient) VALUES (?, ?, ?)";

		try (Connection conn = dbManager.getConnection();
		     PreparedStatement mealStmt = conn.prepareStatement(insertMealSQL);
		     PreparedStatement ingredientStmt = conn.prepareStatement(insertIngredientSQL)) {

			int mealId = getNextId(conn, "meals");
			mealStmt.setInt(1, mealId);
			mealStmt.setString(2, meal.getCategory());
			mealStmt.setString(3, meal.getName());
			mealStmt.executeUpdate();

			for (String ingredient : meal.getIngredients()) {
				int ingredientId = getNextId(conn, "ingredients");
				ingredientStmt.setInt(1, ingredientId);
				ingredientStmt.setInt(2, mealId);
				ingredientStmt.setString(3, ingredient);
				ingredientStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getNextId(Connection conn, String tableName) throws SQLException {
		String sql = "SELECT COALESCE(MAX(meal_id), 0) + 1 AS next_id FROM " + tableName;
		if (tableName.equals("ingredients")) {
			sql = "SELECT COALESCE(MAX(ingredient_id), 0) + 1 AS next_id FROM " + tableName;
		}
		try (Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				return rs.getInt("next_id");
			}
		}
		return 1;
	}

	public List<Meal> getMealsByCategory(String category) {
		List<Meal> meals = new ArrayList<>();
		String sql = "SELECT m.meal_id, m.meal, i.ingredient FROM meals m " +
				"LEFT JOIN ingredients i ON m.meal_id = i.meal_id " +
				"WHERE m.category = ? ORDER BY m.meal_id";

		try (Connection conn = dbManager.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category);
			ResultSet rs = stmt.executeQuery();

			int currentMealId = -1;
			Meal currentMeal = null;
			List<String> ingredients = new ArrayList<>();

			while (rs.next()) {
				int mealId = rs.getInt("meal_id");
				if (mealId != currentMealId) {
					if (currentMeal != null) {
						currentMeal.getIngredients().addAll(ingredients);
						meals.add(currentMeal);
					}
					currentMealId = mealId;
					currentMeal = new Meal(mealId, category, rs.getString("meal"), new ArrayList<>());
					ingredients = new ArrayList<>();
				}
				String ingredient = rs.getString("ingredient");
				if (ingredient != null) {
					ingredients.add(ingredient);
				}
			}
			if (currentMeal != null) {
				currentMeal.getIngredients().addAll(ingredients);
				meals.add(currentMeal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return meals;
	}

	public List<String> getAllMealsByCategory(String category) {
		List<String> meals = new ArrayList<>();
		String sql = "SELECT meal FROM meals WHERE category = ? ORDER BY meal";

		try (Connection conn = dbManager.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				meals.add(rs.getString("meal"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return meals;
	}

	public Meal getMealByNameAndCategory(String name, String category) {
		String sql = "SELECT m.meal_id, m.meal, i.ingredient FROM meals m " +
				"LEFT JOIN ingredients i ON m.meal_id = i.meal_id " +
				"WHERE m.category = ? AND m.meal = ?";

		try (Connection conn = dbManager.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category);
			stmt.setString(2, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int mealId = rs.getInt("meal_id");
				Meal meal = new Meal(mealId, category, name, new ArrayList<>());
				do {
					String ingredient = rs.getString("ingredient");
					if (ingredient != null) {
						meal.getIngredients().add(ingredient);
					}
				} while (rs.next());
				return meal;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addMealToPlan(String day, String category, int mealId) {
		String sql = "INSERT INTO plan (day, category, meal_id) VALUES (?, ?, ?)";
		try (Connection conn = dbManager.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, day);
			stmt.setString(2, category);
			stmt.setInt(3, mealId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void clearPlan() {
		String sql = "DELETE FROM plan";
		try (Connection conn = dbManager.getConnection();
		     Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Meal> getWeekPlan() {
		List<Meal> weekPlan = new ArrayList<>();
		String sql = "SELECT p.day, p.category, m.meal_id, m.meal, i.ingredient " +
				"FROM plan p " +
				"JOIN meals m ON p.meal_id = m.meal_id " +
				"LEFT JOIN ingredients i ON m.meal_id = i.meal_id " +
				"ORDER BY CASE p.day " +
				"    WHEN 'Monday' THEN 1 " +
				"    WHEN 'Tuesday' THEN 2 " +
				"    WHEN 'Wednesday' THEN 3 " +
				"    WHEN 'Thursday' THEN 4 " +
				"    WHEN 'Friday' THEN 5 " +
				"    WHEN 'Saturday' THEN 6 " +
				"    WHEN 'Sunday' THEN 7 " +
				"END, " +
				"CASE p.category " +
				"    WHEN 'breakfast' THEN 1 " +
				"    WHEN 'lunch' THEN 2 " +
				"    WHEN 'dinner' THEN 3 " +
				"END";

		try (Connection conn = dbManager.getConnection();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			String currentDay = null;
			String currentCategory = null;
			int currentMealId = -1;
			Meal currentMeal = null;
			List<String> ingredients = new ArrayList<>();

			while (rs.next()) {
				String day = rs.getString("day");
				String category = rs.getString("category");
				int mealId = rs.getInt("meal_id");
				String mealName = rs.getString("meal");
				String ingredient = rs.getString("ingredient");

				if (!day.equals(currentDay) || !category.equals(currentCategory) || mealId != currentMealId) {
					if (currentMeal != null) {
						currentMeal.getIngredients().addAll(ingredients);
						weekPlan.add(currentMeal);
					}
					currentDay = day;
					currentCategory = category;
					currentMealId = mealId;
					currentMeal = new Meal(mealId, category, mealName, new ArrayList<>());
					currentMeal.setDay(day);
					ingredients = new ArrayList<>();
				}

				if (ingredient != null) {
					ingredients.add(ingredient);
				}
			}

			if (currentMeal != null) {
				currentMeal.getIngredients().addAll(ingredients);
				weekPlan.add(currentMeal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return weekPlan;
	}
}
