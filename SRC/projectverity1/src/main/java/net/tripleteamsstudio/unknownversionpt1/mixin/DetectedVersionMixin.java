package net.tripleteamsstudio.unknownversionpt1.mixin;

import net.minecraft.DetectedVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DetectedVersion.class)
public class DetectedVersionMixin {

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void changeVersionName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Infdev");
    }

    @Inject(method = "getId", at = @At("HEAD"), cancellable = true)
    private void changeVersionId(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Infdev");
    }
}