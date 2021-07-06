package dev.toma.gunsrpg.jei;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SmithingRecipeWrapper implements IRecipeWrapper {

    private final SmithingTableRecipes.SmithingRecipe recipe;

    public SmithingRecipeWrapper(SmithingTableRecipes.SmithingRecipe recipe) {
        this.recipe = recipe;
    }

    public SmithingTableRecipes.SmithingRecipe getRecipe() {
        return recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> ingredientLists = new ArrayList<>();
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutputForDisplay());
        for (SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
            Item item = ingredient.getItems();
            List<ItemStack> stacks = new ArrayList<>();
            for (int i : ingredient.getSubtypes()) {
                stacks.add(new ItemStack(item, 1, i));
            }
            ingredientLists.add(stacks);
        }
        ingredients.setInputLists(VanillaTypes.ITEM, ingredientLists);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        SkillType<?>[] types = recipe.getRequiredTypes();
        minecraft.fontRenderer.drawString("Required skills:", -35, recipeHeight + 10, 0x454545);
        EntityPlayer player = minecraft.player;
        PlayerData data = PlayerDataFactory.get(player);
        PlayerSkills skills = data.getSkills();
        for (int i = 0; i < types.length; i++) {
            SkillType<?> type = types[i];
            drawSkill(minecraft, type, i, recipeWidth, recipeHeight, mouseX, mouseY, skills.hasSkill(type));
        }
    }

    private void drawSkill(Minecraft mc, SkillType<?> skill, int id, int width, int height, int mouseX, int mouseY, boolean hasSkill) {
        int y = height + 20 + id * 18;
        Gui.drawRect(-35, y, width + 35, y + 20, 0xff454545);
        Gui.drawRect(-33, y + 2, width + 33, y + 18, 0xffc6c6c6);
        if(skill.hasCustomRenderFactory()) {
            mc.getRenderItem().renderItemIntoGUI(skill.getRenderItem(), -33, y + 2);
        } else {
            ModUtils.renderTexture(-33, y + 2, -17, y + 18, skill.icon);
        }
        mc.fontRenderer.drawStringWithShadow(skill.getDisplayName(), -14, y + 6, hasSkill ? 0xFF80FF20 : 0xFFFF6060);
    }
}
