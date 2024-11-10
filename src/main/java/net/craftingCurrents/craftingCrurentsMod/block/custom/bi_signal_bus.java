package net.craftingCurrents.craftingCrurentsMod.block.custom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;


public class bi_signal_bus extends Block {
    
    // Define distinct properties for the left and right signals
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LEFT_SIGNAL = BooleanProperty.create("left_signal");
    public static final BooleanProperty RIGHT_SIGNAL = BooleanProperty.create("right_signal");

    public bi_signal_bus(BlockBehaviour.Properties properties) {
        super(properties);
        // Set default state for both signals to false (no signal)
        this.registerDefaultState(this
            .defaultBlockState()
            .setValue(LEFT_SIGNAL, false)
            .setValue(RIGHT_SIGNAL, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // Get the direction the player is looking (it will be restricted to horizontal directions)

        // Check for redstone signals on the north and south sides when placed, left is tide to north(not up, and may need to fix relative north)
        
        Direction facingDirection = context.getHorizontalDirection();// Only gives north, south, east, or west
        Direction leftSide = facingDirection.getClockWise();
        Direction rightSide = facingDirection.getCounterClockWise();
        
        boolean leftSignal = level.hasSignal(pos.relative(leftSide), leftSide);
        boolean rightSignal = level.hasSignal(pos.relative(rightSide), rightSide);

        // Initialize block state based on these signals
        return this.defaultBlockState()
            .setValue(FACING, facingDirection)
            .setValue(LEFT_SIGNAL, leftSignal)
            .setValue(RIGHT_SIGNAL, rightSignal);
            
    }

    // Add a neighborChanged method to update signals dynamically
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) { // Only run this logic on the server side
    
            // Get the block’s facing direction
            Direction facingDirection = state.getValue(FACING);
            Direction leftSide = facingDirection.getClockWise();
            Direction rightSide = facingDirection.getCounterClockWise();
    
            // Check signals on left and right sides relative to the block's facing direction
            boolean newLeftSignal = level.hasSignal(pos.relative(leftSide), leftSide);
            boolean newRightSignal = level.hasSignal(pos.relative(rightSide), rightSide);
    
            // Only update if the signals have changed
            boolean currentLeftSignal = state.getValue(LEFT_SIGNAL);
            boolean currentRightSignal = state.getValue(RIGHT_SIGNAL);
    
            // Update the block state without causing recursive neighbor updates
            if (newLeftSignal != currentLeftSignal) {
                level.setBlockAndUpdate(pos, state.setValue(LEFT_SIGNAL, newLeftSignal));
            }
            if (newRightSignal != currentRightSignal) {
                level.setBlockAndUpdate(pos, state.setValue(RIGHT_SIGNAL, newRightSignal));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Register the LEFT_SIGNAL and RIGHT_SIGNAL properties
        builder.add(LEFT_SIGNAL, RIGHT_SIGNAL);
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide) {
            boolean leftSignal = level.hasSignal(pos.relative(Direction.NORTH), Direction.NORTH);
            boolean rightSignal = level.hasSignal(pos.relative(Direction.SOUTH), Direction.SOUTH);

            // Update the block state based on initial redstone signals
            level.setBlock(pos, state.setValue(LEFT_SIGNAL, leftSignal).setValue(RIGHT_SIGNAL, rightSignal), 2);
        }
    }
}