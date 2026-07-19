package net.tripleteamsstudio.unknownversionpt4.server;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tripleteamsstudio.unknownversionpt4.server.goal.StareAtPlayerGoal;

@Mod.EventBusSubscriber(modid = "unknownversionpt4", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StareAtPlayerHandler {

    // Tune this
    private static final double STARE_RANGE = 21;

    private static final String TAG_KEY = "unknownversionpt4_stare_added";

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof Chicken
                || event.getEntity() instanceof Cow
                || event.getEntity() instanceof Pig
                || event.getEntity() instanceof Sheep)) {
            return;
        }

        Mob mob = (Mob) event.getEntity();

        if (mob.getPersistentData().getBoolean(TAG_KEY)) return;
        mob.getPersistentData().putBoolean(TAG_KEY, true);

        mob.goalSelector.addGoal(0, new StareAtPlayerGoal(mob, STARE_RANGE));
    }
}