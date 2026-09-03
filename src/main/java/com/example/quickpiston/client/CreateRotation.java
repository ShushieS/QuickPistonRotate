package com.example.quickpiston.client;

import com.simibubi.create.content.contraptions.wrench.RadialWrenchMenuSubmitPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Rotation 100% cote client : on reutilise le paquet natif de Create
 * {@link RadialWrenchMenuSubmitPacket} (AllPackets.RADIAL_WRENCH_MENU_SUBMIT).
 *
 * Create enregistre le handler {@code playToServer} de ce paquet sur tout serveur
 * ou Create est installe. Son handler serveur ne verifie ni la cle a molette, ni la
 * portee, ni les permissions : il verifie seulement que le bloc present a la position
 * correspond bien au bloc de l'etat envoye, puis applique l'etat via
 * {@code KineticBlockEntity.switchToBlockState} et joue le son de la cle a molette.
 *
 * Ce mod n'a donc aucun code serveur et n'a pas besoin d'etre installe cote serveur.
 */
public final class CreateRotation {

    private CreateRotation() {}

    public static void rotate(Level level, BlockPos pos, Direction target) {
        BlockState state = level.getBlockState(pos);
        BlockState rotated = withFacing(state, target);
        if (rotated == null || rotated == state) return;
        PacketDistributor.sendToServer(new RadialWrenchMenuSubmitPacket(pos, rotated));
    }

    /**
     * Applique {@code target} sur la premiere propriete d'orientation que possede le bloc.
     * Reprend l'ensemble des proprietes que le menu radial de Create sait editer.
     * Renvoie {@code null} si aucune propriete ne convient.
     */
    private static BlockState withFacing(BlockState state, Direction target) {
        Direction.Axis axis = target.getAxis();

        // FACING 6 directions (piston, observateur, distributeur, dropper, tonneau, blocs cinetiques directionnels...)
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.setValue(BlockStateProperties.FACING, target);
        }
        // FACING horizontal (four, coffre, entonnoir horizontal...)
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis.isHorizontal()) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, target);
        }
        // Entonnoir : bas + 4 horizontales, pas de haut
        if (state.hasProperty(BlockStateProperties.FACING_HOPPER) && target != Direction.UP) {
            return state.setValue(BlockStateProperties.FACING_HOPPER, target);
        }
        // AXIS (buches, arbres cinetiques, tuyaux...)
        if (state.hasProperty(BlockStateProperties.AXIS)) {
            return state.setValue(BlockStateProperties.AXIS, axis);
        }
        // AXIS horizontal
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis.isHorizontal()) {
            return state.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
        }
        return null;
    }
}
