package net.tripleteamsstudio.unknownversionpt4.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = "unknownversionpt4", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GhostBlockBreakHandler {

    private static final Random RANDOM = new Random();

    // Tune these
    private static final int INTERVAL_TICKS = 20 * 60 * 3;
    private static final int HORIZONTAL_RADIUS = 10;
    private static final int VERTICAL_RADIUS = 4;
    private static final int MAX_ATTEMPTS = 20; // tries to find a valid block before giving up this cycle

    private static int timer = INTERVAL_TICKS;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        timer--;
        if (timer > 0) return;
        timer = INTERVAL_TICKS;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        ServerPlayer player = players.get(RANDOM.nextInt(players.size()));
        ServerLevel level = player.serverLevel(); 
        BlockPos center = player.blockPosition();

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int dx = RANDOM.nextInt(HORIZONTAL_RADIUS * 2 + 1) - HORIZONTAL_RADIUS;
            int dy = RANDOM.nextInt(VERTICAL_RADIUS * 2 + 1) - VERTICAL_RADIUS;
            int dz = RANDOM.nextInt(HORIZONTAL_RADIUS * 2 + 1) - HORIZONTAL_RADIUS;

            if (dx == 0 && dz == 0) continue; // skip player's own column

            BlockPos pos = center.offset(dx, dy, dz);
            BlockState state = level.getBlockState(pos);

            if (state.isAir()) continue;
            if (state.getDestroySpeed(level, pos) < 0) continue; // unbreakable (bedrock etc.)

            level.destroyBlock(pos, false); // false = no item drop; still plays vanilla break sound + particles
            break;

        }
    }
}