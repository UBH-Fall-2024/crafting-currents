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

public class RedstoneConverter extends Block {

    // Direction Property for horizontal facing (only North, South, East, West)
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // Define distinct properties for the left and right signals
    public static final BooleanProperty LEFT_SIGNAL = BooleanProperty.create("left_signal");
    public static final BooleanProperty RIGHT_SIGNAL = BooleanProperty.create("right_signal");

    public RedstoneConverter(BlockBehaviour.Properties properties) {
        super(properties);
        // Set default state for both signals to false (no signal), default facing to NORTH
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)   // Default facing direction is NORTH
                .setValue(LEFT_SIGNAL, false)
                .setValue(RIGHT_SIGNAL, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // Get the direction the player is looking (it will be restricted to horizontal directions)
        Direction facingDirection = context.getHorizontalDirection(); // Only gives north, south, east, or west

        // Swap left and right directions
        Direction rightDirection = facingDirection.getClockWise();  // Right is clockwise from facing
        Direction leftDirection = facingDirection.getCounterClockWise();  // Left is counter-clockwise from facing

        boolean leftSignal = level.hasSignal(pos.relative(leftDirection), leftDirection);
        boolean rightSignal = level.hasSignal(pos.relative(rightDirection), rightDirection);

        // Initialize block state based on these signals and facing direction
        return this.defaultBlockState()
                .setValue(FACING, facingDirection)  // Set the direction the block is facing
                .setValue(LEFT_SIGNAL, leftSignal)
                .setValue(RIGHT_SIGNAL, rightSignal);
    }

    // Add a neighborChanged method to update signals dynamically
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) { // Only run this logic on the server side
            Direction facingDirection = state.getValue(FACING);

            // Swap left and right directions
            Direction rightDirection = facingDirection.getClockWise();
            Direction leftDirection = facingDirection.getCounterClockWise();

            boolean leftSignal = level.hasSignal(pos.relative(leftDirection), leftDirection);
            boolean rightSignal = level.hasSignal(pos.relative(rightDirection), rightDirection);

            // Update block state if the signal has changed on either side
            level.setBlock(pos, state.setValue(LEFT_SIGNAL, leftSignal).setValue(RIGHT_SIGNAL, rightSignal), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Register the LEFT_SIGNAL, RIGHT_SIGNAL, and FACING properties
        builder.add(FACING, LEFT_SIGNAL, RIGHT_SIGNAL);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide) {
            Direction facingDirection = state.getValue(FACING);

            // Swap left and right directions
            Direction rightDirection = facingDirection.getClockWise();
            Direction leftDirection = facingDirection.getCounterClockWise();

            boolean leftSignal = level.hasSignal(pos.relative(leftDirection), leftDirection);
            boolean rightSignal = level.hasSignal(pos.relative(rightDirection), rightDirection);

            // Update the block state based on initial redstone signals
            level.setBlock(pos, state.setValue(LEFT_SIGNAL, leftSignal).setValue(RIGHT_SIGNAL, rightSignal), 2);
        }
    }

    
    public void updateSignal(Level level, BlockPos pos, int newPower) {
        BlockState state = level.getBlockState(pos); // Get the current state of the block
        if (state.getValue(POWER) != newPower) { // Check if the current power level differs from the new one
            level.setBlock(pos, state.setValue(POWER, newPower), 3); // Update the block state and notify neighbors
        }
    }

}
