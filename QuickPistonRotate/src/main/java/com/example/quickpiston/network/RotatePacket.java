package com.example.quickpiston.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RotatePacket(BlockPos pos, Direction targetDirection) implements CustomPacketPayload {
    public static final Type<RotatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("quickpiston", "rotate"));

    public static final StreamCodec<FriendlyByteBuf, RotatePacket> STREAM_CODEC = CustomPacketPayload.codec(
            (val, buf) -> {
                buf.writeBlockPos(val.pos);
                buf.writeEnum(val.targetDirection);
            },
            buf -> new RotatePacket(buf.readBlockPos(), buf.readEnum(Direction.class))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RotatePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                var level = player.serverLevel();
                BlockPos pos = payload.pos();
                BlockState state = level.getBlockState(pos);

                if (state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON)) {
                    level.setBlock(pos, state.setValue(BlockStateProperties.FACING, payload.targetDirection()), 3);
                }
            }
        });
    }
}