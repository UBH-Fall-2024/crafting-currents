package net.craftingCurrents.craftingCrurentsMod.item;

import net.craftingCurrents.craftingCrurentsMod.CraftingCurentsMod;
import net.craftingCurrents.craftingCrurentsMod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CraftingCurentsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab>  CRAFTING_CURRENTS_ITEMS_TAB = CREATIVE_MODE_TABS.register("crafting_currents_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ALEXANDRITE.get()))
                    .title(Component.translatable("creativetab.craftingcurrentsmod.craftingcurrents_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                      output.accept(ModItems.ALEXANDRITE.get());
                      output.accept(ModItems.RAW_ALEXANDRITE.get());

                    }).build());

    public static final RegistryObject<CreativeModeTab>  CRAFTING_CURRENTS_BLOCKS_TAB = CREATIVE_MODE_TABS.register("crafting_currents_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ALEXANDRITE_BLOCK.get()))
                    .withTabsBefore(CRAFTING_CURRENTS_ITEMS_TAB.getId())
                    .title(Component.translatable("creativetab.craftingcurrentsmod.craftingcurrents_block"))
                    .displayItems((itemDisplayParameters, output) -> {
                      output.accept(ModBlocks.ALEXANDRITE_BLOCK.get());
                      output.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
