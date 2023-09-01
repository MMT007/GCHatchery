package mmt007.plugins.gchatchery.RecipeMngr;

import mmt007.plugins.gchatchery.GCHatchery;
import mmt007.plugins.gchatchery.RecipeMngr.Models.Recipe;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class RecipeMngr {
    private static final Plugin main = GCHatchery.getPlugin();
    private static ArrayList<Recipe> recipes = new ArrayList<>();

    //Adds Recipe To {recipes} Variable and Saves Changes To File
    public static boolean addRecipe(Recipe recipe){
        boolean wasAdded = recipes.add(recipe);

        if(wasAdded) {
            saveRecipes();
            return true;
        }else{return false;}
    }

    //Returns Recipe From {recipes} Variable Not Base On Item Amount
    //(I Should Make This Ignore Item Position On GUI)
    public static Recipe getRecipe(Map<Integer,ItemStack> input) {
        Recipe finalRecipe = Recipe.getEmptyRecipe();

        ArrayList<ItemStack> input1 = new ArrayList<>(List.copyOf(input.values()));
        ArrayList<ItemStack> input2 = new ArrayList<>();

        //Resets Item Amount
        for(ItemStack item : input1){item.setAmount(0);}

        for(Recipe recipe : recipes){
            //Loops Through Recipes And Gets It Inputs
            input2.addAll(List.copyOf(recipe.getInput().values()));
            for(ItemStack item : input2){item.setAmount(0);}

            //If They Are The Same, Return Recipe
            if(input1.hashCode() == input2.hashCode()){
                return recipe;
            }else{
                input2 = new ArrayList<>();
            }
        }

        return finalRecipe;
    }

    //Gets Recipe By Name from {recipe} Variable
    public static Recipe getRecipeByName(String name){
        AtomicReference<Recipe> finalRecipe = new AtomicReference<>(Recipe.getEmptyRecipe());

        recipes.forEach((recipe) -> {
            if(recipe.getName().equalsIgnoreCase(name))
            {finalRecipe.set(recipe);}
        });

        return finalRecipe.get();
    }

    //Removes Recipe From {recipes} Variable, and Saves Changes
    public static boolean removeRecipe(String name){
        Recipe recipe = getRecipeByName(name);
        if(!recipes.contains(recipe)){return false;}

        boolean isRemoved = recipes.remove(recipe);

        saveRecipes();
        return isRemoved;
    }

    //Loads Recipes From File
    public static void loadRecipes() {
        File file = loadFile();
        if(file == null){return;}

        FileConfiguration config = loadConfigFile(file);

        recipes = decode(config);
    }

    //Saves Recipes To File
    public static void saveRecipes(){
        File file = loadFile();
        if(file == null){return;}

        FileConfiguration config = loadConfigFile(file);


        encode(config);

        try {config.save(file);}catch (IOException e){e.printStackTrace();}
    }

    //Encodes The {recipes} Variable To The YAML Format
    private static void encode(FileConfiguration file){
        for (Recipe recipe : recipes) {
            ConfigurationSection RecpieSection =
                    file.createSection("Recipes." + recipe.getName());

            //Creates Sections For Input And Output
            ConfigurationSection input = RecpieSection.createSection("input");
            ConfigurationSection output = RecpieSection.createSection("output");

            //Sets Time Variable
            RecpieSection.set("timeToCraft", recipe.getTime());

            //Adds Items To Sections
            addItemStackList(input, recipe.getInput());
            addItemStackList(output, recipe.getOutput());
        }
    }

    //Decodes The YAML Format To The {recipes} Variable
    private static ArrayList<Recipe> decode(FileConfiguration file){
        ArrayList<Recipe> result = new ArrayList<>();

        //Gets Main Section
        ConfigurationSection mainSection = file.getConfigurationSection("Recipes");
        if(mainSection == null){return result;}

        Set<String> recipiesSet = mainSection.getKeys(false);

        for(String recipeName : recipiesSet){
            //Loops Through Each Recipe Section
            ConfigurationSection recipeSection = mainSection.getConfigurationSection(recipeName);
            if(recipeSection == null){return result;}

            Recipe recipe = Recipe.getEmptyRecipe();

            //Sets String And Double Variables
            recipe.setName(recipeName);
            recipe.setTime(recipeSection.getDouble("timeToCraft"));

            //Gets Input And Output Sectiosn
            ConfigurationSection input = recipeSection.getConfigurationSection("input");
            ConfigurationSection output = recipeSection.getConfigurationSection("output");

            if((input == null) || (output == null)){return result;}

            //Sets Recipe Input And Output
            recipe.setInput(getItemStackList(input));
            recipe.setOutput(getItemStackList(output));

            //Adds Recipe To Final Result
            result.add(recipe);
        }

        return result;
    }

    //Creates YAML Section For Each Item
    private static void addItemStackList(ConfigurationSection section, HashMap<Integer,ItemStack> items){
        items.forEach((slotValue,item) -> section.set(slotValue.toString(),item));
    }

    //Returns Items From A YAML Section
    private static HashMap<Integer,ItemStack> getItemStackList(ConfigurationSection section){
        HashMap<Integer,ItemStack> result = new HashMap<>();
        Set<String> items = section.getKeys(false);

        //Loops Through Each Key Index And Adds Items To Result
        for (String slotValueString : items) {
            Integer slotValue = Integer.valueOf(slotValueString);
            ItemStack item = section.getItemStack(slotValueString);

            result.put(slotValue,item);
        }

        return result;
    }


    //Loads File And Returns It
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File loadFile(){
        File file = new File(main.getDataFolder(),"Recipes.yml");

        if(!file.exists()) {
            try{
                if(!file.getParentFile().exists()){file.getParentFile().mkdir();}

                file.createNewFile();
            }catch(IOException e){e.printStackTrace();return null;}
        }

        return file;
    }
    //Loads FileConfiguration And Returns It
    private static FileConfiguration loadConfigFile(File file){
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(Objects.equals(config.get("Recipes"),null)){
            config.createSection("Recipes");
        }

        return config;
    }
}

