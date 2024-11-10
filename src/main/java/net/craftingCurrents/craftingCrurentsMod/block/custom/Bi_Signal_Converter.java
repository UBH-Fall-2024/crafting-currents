package net.craftingCurrents.craftingCrurentsMod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

public class Bi_Signal_Converter extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LEFT_SIGNAL = BooleanProperty.create("left_signal");
    public static final BooleanProperty RIGHT_SIGNAL = BooleanProperty.create("right_signal");

    public Bi_Signal_Converter(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(LEFT_SIGNAL, false)
                .setValue(RIGHT_SIGNAL, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facingDirection = context.getHorizontalDirection();

        return this.defaultBlockState()
                .setValue(FACING, facingDirection)
                .setValue(LEFT_SIGNAL, false)
                .setValue(RIGHT_SIGNAL, false);
    }

    // Updates the state and handles redstone output when neighboring blocks change
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facingDirection = state.getValue(FACING);
            Direction southDirection = facingDirection.getOpposite(); // South direction relative to facing

            // Get the block state to the south
            BlockPos southPos = pos.relative(southDirection);
            BlockState southState = level.getBlockState(southPos);

            // Check if the block to the south has LEFT_SIGNAL and RIGHT_SIGNAL properties
            if (southState.hasProperty(LEFT_SIGNAL) && southState.hasProperty(RIGHT_SIGNAL)) {
                boolean leftSignal = southState.getValue(LEFT_SIGNAL);
                boolean rightSignal = southState.getValue(RIGHT_SIGNAL);

                // Update this block's state for internal reference
                level.setBlock(pos, state.setValue(LEFT_SIGNAL, leftSignal).setValue(RIGHT_SIGNAL, rightSignal), 2);

                // Output redstone power based on these signals
                outputRedstoneSignal(level, pos, facingDirection.getClockWise(), leftSignal);       // Left side output
                outputRedstoneSignal(level, pos, facingDirection.getCounterClockWise(), rightSignal); // Right side output
            }
        }
    }

    // Helper method to output redstone signal on a specified side based on a boolean input
    private void outputRedstoneSignal(Level level, BlockPos pos, Direction outputDirection, boolean signal) {
        BlockPos outputPos = pos.relative(outputDirection);
        int power = signal ? 15 : 0; // Max power if signal is true, otherwise no power

        // If you have a method or mechanism to set redstone power, it would go here
        level.updateNeighborsAt(outputPos, this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT_SIGNAL, RIGHT_SIGNAL);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // Enables redstone signal output
    }

    // Override getSignal to provide power based on direction

    public int getSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        Direction facingDirection = state.getValue(FACING);
        if (direction == facingDirection.getClockWise()) {  // Left side
            return state.getValue(LEFT_SIGNAL) ? 15 : 0;
        } else if (direction == facingDirection.getCounterClockWise()) {  // Right side
            return state.getValue(RIGHT_SIGNAL) ? 15 : 0;
        }
        return 0; // No power in other directions
    }
}
