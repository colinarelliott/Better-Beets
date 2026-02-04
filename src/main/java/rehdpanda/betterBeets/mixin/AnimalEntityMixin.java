package rehdpanda.betterBeets.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rehdpanda.betterBeets.BetterBeets;

@Mixin(value = AnimalEntity.class)
public abstract class AnimalEntityMixin extends net.minecraft.entity.passive.PassiveEntity {
    protected AnimalEntityMixin(net.minecraft.entity.EntityType<? extends net.minecraft.entity.passive.PassiveEntity> entityType, net.minecraft.world.World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean canEat();
    @Shadow public abstract void setLoveTicks(int loveTicks);

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void betterBeets$interactMob(net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, CallbackInfoReturnable<net.minecraft.util.ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BEETROOT)) {
            AnimalEntity animal = (AnimalEntity) (Object) this;
            if (this.getBreedingAge() == 0 && (animal.getLoveTicks() <= 0 || !animal.isInLove()) && this.canEat()) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                this.setLoveTicks(600);
                if (animal.getEntityWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, animal.getParticleX(1.0), animal.getRandomBodyY() + 0.5, animal.getParticleZ(1.0), 7, 0.0, 0.0, 0.0, 0.5);
                }
                animal.emitGameEvent(net.minecraft.world.event.GameEvent.ENTITY_INTERACT);
                cir.setReturnValue(net.minecraft.util.ActionResult.SUCCESS);
            }
        }
    }
}
