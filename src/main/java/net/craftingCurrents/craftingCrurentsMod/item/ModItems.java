package net.craftingCurrents.craftingCrurentsMod.item;

import net.craftingCurrents.craftingCrurentsMod.CraftingCurentsMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CraftingCurentsMod.MOD_ID);

    //EXAMPLE ITEM
    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite",
            () -> new Item(new Item.Properties()));
    //EXAMPLE ITEM
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CC_LOGO = ITEMS.register("cc_logo",
            () -> new Item(new Item.Properties()));


//DO NO DELETE _ METHOD IS USED TO registor items
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}