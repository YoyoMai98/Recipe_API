package com.bnta.recipe_API.controllers;

import com.bnta.recipe_API.models.Ingredient;
import com.bnta.recipe_API.models.Recipe;
import com.bnta.recipe_API.models.User;
import com.bnta.recipe_API.repositories.RecipeRepository;
import com.bnta.recipe_API.services.RecipeService;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@RestController
//@RequestMapping = endpoint
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

//GET METHODS (Show)


    //Method to get all recipes:
    //client will make a request i.e. type in: localhost:8080/recipes
    // getAllRecipes() will return a list of Recipes objects
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        // the controller will call the getAllRecipes method from the recipesService
        List<Recipe> recipes = recipeService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
        // - this method will then grab all the Recipes from recipeRepository (our database)
    }


    //Method to get a recipe by id:

    @GetMapping(value = "/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }


    //POST METHODS

    // Create a new recipe method  - localhost:8080/recipe/create
    @PostMapping("/create")
    public ResponseEntity<Recipe> addNewRecipe(@RequestBody String name,
                                               @RequestBody float averageRating,
                                               @RequestBody  int time,
                                               @RequestBody  int calories,
                                               @RequestBody  int servings,
                                               @RequestBody  List<Ingredient> ingredients) {

        Recipe newRecipe = new Recipe(name,averageRating,time,calories,servings, ingredients);
        recipeService.saveRecipe(newRecipe);
        System.out.println("Recipe" + name + " has been added!");

//        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        return ResponseEntity.ok().body(newRecipe);

    }

    // DELETE MAPPING - delete recipe

    @DeleteMapping("/deleteRecipe{id}")
    public void deleteRecipe(@PathVariable Long id) {

        recipeRepository.deleteById(id);


    }

    @GetMapping("/ingredient")
    public ResponseEntity<List<Recipe>> getAllRecipesByIngredient(@RequestParam Optional<Long> ingredientId){
        List<Recipe> recipes;
        recipes = recipeService.getRecipeByIngredientId(ingredientId.get());

        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }

    @GetMapping("ingredient/name")
    public ResponseEntity<List<Recipe>> getAllRecipesByName(
            @RequestParam Optional<String> ingredient1,
            @RequestParam Optional<String> ingredient2,
            @RequestParam Optional<String> ingredient3
    ){
        if (ingredient1.isPresent() && ingredient2.isPresent() && ingredient3.isPresent()){
            List<Recipe> recipes = recipeService.getRecipeByIngredientName(ingredient1.get());
            List<Recipe> second_recipes = recipeService.getRecipeByIngredientName(ingredient2.get());
            List<Recipe> third_recipes = recipeService.getRecipeByIngredientName(ingredient3.get());
            second_recipes.retainAll(third_recipes);
            recipes.retainAll(second_recipes);
            return new ResponseEntity<>(recipes,HttpStatus.OK);

        } else if (ingredient1.isPresent() && ingredient2.isPresent()){
            List<Recipe> recipes = recipeService.getRecipeByIngredientName(ingredient1.get());
            List<Recipe> second_recipes = recipeService.getRecipeByIngredientName(ingredient2.get());
            recipes.retainAll(second_recipes); // replaces list recipes with only values present in both lists
            return new ResponseEntity<>(recipes,HttpStatus.OK);
        } else if (ingredient1.isPresent()){
            List<Recipe> recipes = recipeService.getRecipeByIngredientName(ingredient1.get());
            return new ResponseEntity<>(recipes,HttpStatus.OK);
        }

        return null;
    }














//    //getRecipes(String): ResponseEntity <Recipe> :
//
////    @GetMapping(value = "/{string}")
////    public ResponseEntity<Recipe> getRecipe(@PathVariable String string) {
////        Optional<Recipe> recipe = recipeService.getRecipe(string);
////        if(recipe.isPresent()) {
////            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
////
////        }else {
////            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
////        }
////
////    }
//





}
