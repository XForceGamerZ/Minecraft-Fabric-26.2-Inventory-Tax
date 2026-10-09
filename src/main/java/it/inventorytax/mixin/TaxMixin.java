package it.inventorytax.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class TaxMixin {

    @Unique
    private int tick;

    @Unique
    private int second;

    @Inject(method = "tick", at = @At("TAIL"))
    private void inventor$tick(CallbackInfo ci) {

        if (((Player) (Object) this).level().isClientSide()) { // Check If We Are Running On Server Not Client
            return;
        }

        // Count Second
        tick += 1;

        boolean atSecond;

        if (tick >= 20){
            second += 1;
            tick = 0;
            atSecond = true;
        } else {
            atSecond = false;
        }

        if (atSecond) {
            System.out.println(second);
        }
    }
}
