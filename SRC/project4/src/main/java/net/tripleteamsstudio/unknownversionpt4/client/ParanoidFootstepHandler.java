package net.tripleteamsstudio.unknownversionpt4.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "unknownversionpt4", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ParanoidFootstepHandler {

    private static final Random RANDOM = new Random();

    // Tune these to change how often the effect triggers
    private static final int MIN_COOLDOWN_TICKS = 20 * 20;
    private static final int MAX_COOLDOWN_TICKS = 20 * 120;

    private static final int STEPS_PER_SEQUENCE = 6;
    private static final int TICKS_BETWEEN_STEPS = 7; // ~0.35s per step

    private static final double SIDE_DISTANCE = 4.0; // how far left/right

    private static int cooldown = randomCooldown();

    private static int stepsRemaining = 0;
    private static int nextStepTick = 0;
    private static Vec3 sequenceOrigin = null;

    private static int randomCooldown() {
        return MIN_COOLDOWN_TICKS + RANDOM.nextInt(MAX_COOLDOWN_TICKS - MIN_COOLDOWN_TICKS);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (stepsRemaining > 0) {
            nextStepTick--;
            if (nextStepTick <= 0) {
                playPhantomStep(mc);
                stepsRemaining--;
                nextStepTick = TICKS_BETWEEN_STEPS;
            }
            return;
        }

        cooldown--;
        if (cooldown <= 0) {
            startSequence(mc.player);
            cooldown = randomCooldown();
        }
    }

    private static void startSequence(LocalPlayer player) {
        boolean rightSide = RANDOM.nextBoolean();

        Vec3 look = player.getLookAngle();
        Vec3 right = new Vec3(-look.z, 0, look.x).normalize();
        Vec3 offset = right.scale(rightSide ? SIDE_DISTANCE : -SIDE_DISTANCE);

        sequenceOrigin = player.position().add(offset);
        stepsRemaining = STEPS_PER_SEQUENCE;
        nextStepTick = 0;
    }

    private static void playPhantomStep(Minecraft mc) {
        if (sequenceOrigin == null || mc.level == null) return;

        double jitterX = (RANDOM.nextDouble() - 0.5) * 0.4;
        double jitterZ = (RANDOM.nextDouble() - 0.5) * 0.4;

        BlockPos groundPos = BlockPos.containing(
                sequenceOrigin.x + jitterX,
                sequenceOrigin.y - 1,
                sequenceOrigin.z + jitterZ
        );

        BlockState state = mc.level.getBlockState(groundPos);
        if (state.isAir()) return;

        SoundType soundType = state.getSoundType();
        float volume = 0.6F + RANDOM.nextFloat() * 0.3F;
        float pitch = 0.85F + RANDOM.nextFloat() * 0.3F;

        mc.level.playLocalSound(
                sequenceOrigin.x + jitterX,
                sequenceOrigin.y,
                sequenceOrigin.z + jitterZ,
                soundType.getStepSound(),
                SoundSource.AMBIENT,
                volume,
                pitch,
                false
        );
    }
}