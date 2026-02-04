package rehdpanda.betterBeets.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity {
    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @org.spongepowered.asm.mixin.Shadow public abstract boolean isTame();
    @org.spongepowered.asm.mixin.Shadow protected abstract boolean receiveFood(net.minecraft.entity.player.PlayerEntity player, ItemStack itemStack);

    @Inject(method = "receiveFood", at = @At("HEAD"), cancellable = true)
    private void betterBeets$receiveFood(net.minecraft.entity.player.PlayerEntity player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(Items.BEETROOT)) {
            boolean healed = false;
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(1.0F);
                healed = true;
            }

            if (this.isBaby() && this.getBreedingAge() < 0) {
                this.growUp(AnimalEntity.toGrowUpAge(-this.getBreedingAge() / 20), true);
                healed = true;
            }

            if (healed) {
                if (this.getEntityWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), 7, 0.0, 0.0, 0.0, 0.5);
                }
                this.emitGameEvent(net.minecraft.world.event.GameEvent.ENTITY_INTERACT);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void betterBeets$interactMob(net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, CallbackInfoReturnable<net.minecraft.util.ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BEETROOT)) {
            if (this.receiveFood(player, itemStack)) {
                cir.setReturnValue(net.minecraft.util.ActionResult.SUCCESS);
            }
        }
    }
}
