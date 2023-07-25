package ru.bastard.culinary.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Culinary.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.WHEAT_GRAIN);
        simpleItem(ModItems.OAT_GRAIN);
        simpleItem(ModItems.RYE_GRAIN);
        simpleItem(ModItems.MAIZE_GRAIN);
        simpleItem(ModItems.RICE_GRAIN);
        //simpleItem(ModItems.SORGHUM_GRAIN);
        simpleItem(ModItems.BARLEY_GRAIN);
        //simpleItem(ModItems.MILLET_GRAIN);
        //simpleItem(ModItems.QUINOA_GRAIN);
        //simpleItem(ModItems.BUCKWHEAT_GRAIN);

        simpleItem(ModItems.OAT);
        simpleItem(ModItems.RYE);
        simpleItem(ModItems.MAIZE);
        simpleItem(ModItems.RICE);
        //simpleItem(ModItems.SORGHUM);
        simpleItem(ModItems.BARLEY);
        //simpleItem(ModItems.MILLET);
        //simpleItem(ModItems.QUINOA);
        //simpleItem(ModItems.BUCKWHEAT);

        simpleItem(ModItems.WHEAT_FLOUR);
        simpleItem(ModItems.OAT_FLOUR);
        simpleItem(ModItems.RYE_FLOUR);
        simpleItem(ModItems.MAIZE_FLOUR);
        simpleItem(ModItems.RICE_FLOUR);
        //simpleItem(ModItems.SORGHUM_FLOUR);
        simpleItem(ModItems.BARLEY_FLOUR);
        //simpleItem(ModItems.MILLET_FLOUR);
        //simpleItem(ModItems.QUINOA_FLOUR);
        //simpleItem(ModItems.BUCKWHEAT_FLOUR);

        simpleItem(ModItems.WHEAT_DOUGH);
        simpleItem(ModItems.OAT_DOUGH);
        simpleItem(ModItems.RYE_DOUGH);
        simpleItem(ModItems.MAIZE_DOUGH);
        simpleItem(ModItems.RICE_DOUGH);
       // simpleItem(ModItems.SORGHUM_DOUGH);
        simpleItem(ModItems.BARLEY_DOUGH);
        //simpleItem(ModItems.MILLET_DOUGH);
        //simpleItem(ModItems.QUINOA_DOUGH);
        //simpleItem(ModItems.BUCKWHEAT_DOUGH);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Culinary.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Culinary.MOD_ID, "item/" + item.getId().getPath()));
    }

}
