package net.tripleteamsstudio.unknownversionpt1.mixin;

import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Button.class)
public class ButtonMixin {

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void unknownversion$blockQuit(CallbackInfo ci) {
        Button self = (Button) (Object) this;
        String msg = self.getMessage().getString();

        if (msg.contains("Save and Quit") || msg.contains("S̸̢a̷v̶e̴")) {
            System.out.println("[UnknownVersion] Blocked quit button press!");
            ci.cancel();
        }
    }
}