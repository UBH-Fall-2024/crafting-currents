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

import org.apache.commons.lang3.ObjectUtils.Null;


public class bi_signal_bus extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LEFT_SIGNAL = BooleanProperty.create("left_signal");
    public static final BooleanProperty RIGHT_SIGNAL = BooleanProperty.create("right_signal");

    public bi_signal_bus(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this
            .defaultBlockState()
            .setValue(FACING, Direction.NORTH) // Default facing to NORTH
            .setValue(LEFT_SIGNAL, false)
            .setValue(RIGHT_SIGNAL, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facingDirection = context.getHorizontalDirection();
        Direction backside = facingDirection.getOpposite();
        Boolean[] signals = checkSignalsFromAdjacentBlock(level, pos, backside);

        // Initialize block state based on these signals
        return this.defaultBlockState()
            .setValue(FACING, facingDirection)
            .setValue(LEFT_SIGNAL, signals[0])
            .setValue(RIGHT_SIGNAL, signals[1]);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facingDirection = state.getValue(FACING);
            Direction backside = facingDirection.getOpposite();
            Boolean[] signals = checkSignalsFromAdjacentBlock(level, pos, backside);
            Boolean newLeftSignal = signals[0];
            Boolean newRightSignal = signals[1];

            
            checkSignalsFromAdjacentBlock(level, pos, facingDirection);
            // Only update if the signals have changed
            if (newLeftSignal != state.getValue(LEFT_SIGNAL) || newRightSignal != state.getValue(RIGHT_SIGNAL)) {
                level.setBlockAndUpdate(pos, state.setValue(LEFT_SIGNAL, newLeftSignal).setValue(RIGHT_SIGNAL, newRightSignal));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT_SIGNAL, RIGHT_SIGNAL);
    }

    public Boolean[] checkSignalsFromAdjacentBlock(Level level, BlockPos adjacentBlockPos, Direction directionToRedstoneConverter) {
        BlockPos redstoneConverterPos = adjacentBlockPos.relative(directionToRedstoneConverter);
        BlockState blockState = level.getBlockState(redstoneConverterPos);

        if (blockState.getBlock() instanceof RedstoneConverter) {
            Boolean leftSignal = blockState.getValue(RedstoneConverter.LEFT_SIGNAL);
            Boolean rightSignal = blockState.getValue(RedstoneConverter.RIGHT_SIGNAL);
            return new Boolean[] {leftSignal, rightSignal};
        } else {
            return new Boolean[] {false, false};
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            Direction facingDirection = state.getValue(FACING);
            Direction backside = facingDirection.getOpposite();
            Boolean[] signals = checkSignalsFromAdjacentBlock(level, pos, backside);
            level.setBlockAndUpdate(pos, state.setValue(LEFT_SIGNAL, signals[0]).setValue(RIGHT_SIGNAL, signals[1]));
        }
    }
}
