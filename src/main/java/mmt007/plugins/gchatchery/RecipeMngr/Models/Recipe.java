package mmt007.plugins.gchatchery.RecipeMngr.Models;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.*;

public class Recipe implements Serializable {
    private String Name;
    private HashMap<Integer,ItemStack> Input;
    private HashMap<Integer,ItemStack> Output;
    private double Time;

    public Recipe(String name,HashMap<Integer,ItemStack> input, HashMap<Integer,ItemStack> output, double time) {
        Name = name;
        Input = input;
        Output = output;
        Time = time;
    }


    public String getName() {return Name;}
    public void setName(String name) {Name = name;}

    public HashMap<Integer,ItemStack> getInput() {return Input;}
    public void setInput(HashMap<Integer,ItemStack> input) {Input = input;}

    public HashMap<Integer,ItemStack> getOutput() {return Output;}
    public void setOutput(HashMap<Integer,ItemStack> output) {Output = output;}

    public double getTime() {return Time;}
    public void setTime(double time) {Time = time;}

    public static Recipe getEmptyRecipe(){
        return new Recipe(
                "",
                new HashMap<>(),
                new HashMap<>(),
                0.0d);
    }

    @Override
    public boolean equals(Object o){
        Recipe recipe = (Recipe) o;
        if(!Recipe.class.equals(o.getClass())){return false;}
        return recipe.getName().equalsIgnoreCase(this.Name);
    }
}
