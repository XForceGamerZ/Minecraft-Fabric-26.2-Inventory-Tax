package it.inventorytax.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        if (tick >= 20){ // Reach 1 Second
            second += 1; // Add 1 To Second
            tick = 0; // Reset Tick
            atSecond = true; // At Second
        } else {
            atSecond = false; // Not At Second
        }




        // Change Setting Here

        int taxIntervalSeconds = 10; // Delay For Another Tax Season

        int removeItemCount = 1; // How Much Item To Remove Per Tax Interval Second

        int itemLimit = 64 * 6; // When Inventory Has Exceeded This Amount Of Item. e.g. 64 * 6 = 6 stack (1 stack = 64 item)






        if (atSecond && second >= taxIntervalSeconds) { // At Second And Reach Reset Second

            second = 0; // Reset Second

            List<Integer> occupiedSlots = new ArrayList<>(); // Make An Array List

            Player player = (Player) (Object) this; // Get Player

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) { // Run For Loop For Getting Every Available Slot In Inventory
                if (!player.getInventory().getItem(i).isEmpty()) {
                    occupiedSlots.add(i);
                }
            }

            int totalItem = 0;

            for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
                totalItem += player.getInventory().getItem(i).getCount();
            }

            if (occupiedSlots.isEmpty()) {
                return; // If Inventory Is Empty Do Nothing
            }

            Random random = new Random();

            int randomIndex = random.nextInt(occupiedSlots.size()); // Get Random Number From Inventory
            int slot = occupiedSlots.get(randomIndex); // Get Slot

            System.out.println(slot); // Print Slot

            if(totalItem > itemLimit)
            {
                int toRemove = player.getInventory().getItem(slot).getCount();

                player.getInventory().getItem(slot).shrink(toRemove);

            } else {
                player.getInventory().getItem(slot).shrink(removeItemCount); // Remove Item From Slot
            }

        }

    }
}
