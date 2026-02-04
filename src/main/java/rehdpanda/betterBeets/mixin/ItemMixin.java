package rehdpanda.betterBeets.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    private ComponentMap betterBeets$cachedComponents;

    @Inject(method = "getComponents", at = @At("RETURN"), cancellable = true)
    private void betterBeets$modifyComponents(CallbackInfoReturnable<ComponentMap> cir) {
        Item item = (Item) (Object) this;
        if (item == Items.BEETROOT) {
            if (betterBeets$cachedComponents == null) {
                FoodComponent beetrootFood = new FoodComponent.Builder()
                        .nutrition(1)
                        .saturationModifier(0.6f)
                        .build();
                betterBeets$cachedComponents = ComponentMap.builder()
                        .addAll(cir.getReturnValue())
                        .add(DataComponentTypes.FOOD, beetrootFood)
                        .build();
            }
            cir.setReturnValue(betterBeets$cachedComponents);
        } else if (item == Items.BEETROOT_SOUP) {
            if (betterBeets$cachedComponents == null) {
                FoodComponent beetrootSoupFood = new FoodComponent.Builder()
                        .nutrition(10)
                        .saturationModifier(0.6f)
                        .build();
                betterBeets$cachedComponents = ComponentMap.builder()
                        .addAll(cir.getReturnValue())
                        .add(DataComponentTypes.FOOD, beetrootSoupFood)
                        .build();
            }
            cir.setReturnValue(betterBeets$cachedComponents);
        }
    }
}
