package net.craftingCurrents.craftingCrurentsMod.block;

import net.craftingCurrents.craftingCrurentsMod.CraftingCurentsMod;
import net.craftingCurrents.craftingCrurentsMod.block.custom.RedstoneConverter;
import net.craftingCurrents.craftingCrurentsMod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
   //DO NOT DELETE - THIS ADDS BLOCKS TO THE GAME
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingCurentsMod.MOD_ID);

    //EXAMPLE BLOCK CAN DELETE USE AS REF
    public static final RegistryObject<Block> ALEXANDRITE_BLOCK  = registerBlock("alexandrite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    //EXAMPLE BLOCK CAN DELETE USE AS REF
    public static final RegistryObject<Block> RAW_ALEXANDRITE_BLOCK = registerBlock("raw_alexandrite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f).requiresCorrectToolForDrops()));

    // This Is the bi signal bus
    /*---------------------------*/
    public static final RegistryObject<Block> BI_SIGNAL_BUS = registerBlock("raw_alexandrite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .instabreak()));
    /*---------------------------*/
    //end

    // 2 redstone input 2 blue output block
    /*---------------------------*/
    public static final RegistryObject<Block> REDSTONE_CONVERTER= registerBlock("redstone_converter",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops().instabreak()));
    /*---------------------------*/
    //end

    //CALL THIS METHOD WHEN CREATING NEW BLOCKS
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    //DO NOT DELETE -THIS METHOD IS USED BY REGISTERBLOCK _ CALL THAT METHOD TO REIGISTOR YOUR BLOCK
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

  //DO NOT DELETE - THIS REGISTORS BLOCKS INTO THE GAME
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}