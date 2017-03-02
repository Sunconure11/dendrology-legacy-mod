package inc.a13xis.legacy.dendrology.events;

import com.google.common.collect.ImmutableList;
import inc.a13xis.legacy.dendrology.TheMod;
import inc.a13xis.legacy.dendrology.block.ModBlocks;
import inc.a13xis.legacy.dendrology.block.ModSapling2Block;
import inc.a13xis.legacy.dendrology.block.ModSaplingBlock;
import inc.a13xis.legacy.dendrology.config.Settings;
import inc.a13xis.legacy.dendrology.item.ModItems;
import inc.a13xis.legacy.dendrology.item.ModSaplingItem;
import inc.a13xis.legacy.dendrology.item.SaplingParcel;
import inc.a13xis.legacy.koresample.common.util.event.ForgeEventListener;
import inc.a13xis.legacy.koresample.tree.DefinesSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Random;

public class GenerationEvents {

        /*@SubscribeEvent
    public void addAllSaplingsToChests(LootTableLoadEvent e)
    {
        final int rarity = Settings.INSTANCE.chestRarity(e.getName());
        if (rarity <= 0) return;
        LootTable table = e.getTable();
        LootEntryItem[] entries = new LootEntryItem[13];
        int i=0;
        for(final DefinesSapling def:ModBlocks.taxonomyInstance().saplingDefinitions()){
            if(i>12)break;
            entries[i]=new LootEntryItem(Item.REGISTRY.getObject(def.saplingBlock().getRegistryName()),1,0,new LootFunction[]{new LootFunction(new LootCondition[0]) {
                @Override
                public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
                    stack.setItemDamage(def.saplingSubBlockVariant().ordinal());
                    return stack;
                }
            }},new LootCondition[0],"dendrology:"+def.speciesName());
            i++;
        }
        table.addPool(new LootPool(entries,new LootCondition[]{new LootCondition() {
            @Override
            public boolean testCondition(Random rand, LootContext context) {
               return rand.nextInt(2000000000)<=rarity;
            }
        }},new RandomValueRange(1),new RandomValueRange(1),"dendrology:surprise"));
    }*/

    @SubscribeEvent
    public void addParcelToChest(LootTableLoadEvent e)
    {
        final int rarity = Settings.INSTANCE.chestRarity(e.getName());
        if (rarity <= 0) return;

        LootEntry[] entries = new LootEntry[]{new LootEntryItem(ModItems.parcelInstance(),rarity,rarity,new LootFunction[]{},new LootCondition[0],ModItems.parcelInstance().getUnlocalizedName())};
        e.getTable().addPool(new LootPool(entries,new LootCondition[]{new LootCondition() {
            @Override
            public boolean testCondition(Random rand, LootContext context) {
                return rand.nextInt(2000000000)<=rarity;
            }
        }},new RandomValueRange(1),new RandomValueRange(1),"dendrology:gift"));
    }

}
