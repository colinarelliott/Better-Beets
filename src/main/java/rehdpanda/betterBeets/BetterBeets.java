package rehdpanda.betterBeets;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class BetterBeets implements ModInitializer {

    public static final TagKey<Item> BEETROOT_FOOD = TagKey.of(RegistryKeys.ITEM, Identifier.of("better-beets", "beetroot_food"));

    @Override
    public void onInitialize() {
    }
}
