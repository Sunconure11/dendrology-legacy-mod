
package inc.a13xis.legacy.dendrology.block;

import com.google.common.collect.ImmutableList;
import inc.a13xis.legacy.dendrology.TheMod;
import inc.a13xis.legacy.dendrology.config.Settings;
import inc.a13xis.legacy.koresample.tree.DefinesLeaves;
import inc.a13xis.legacy.koresample.tree.block.LeavesBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ModLeaves3Block extends LeavesBlock
{
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", ModLog3Block.EnumType.class);
    public ModLeaves3Block(Iterable<? extends DefinesLeaves> subBlocks)
    {
        super(ImmutableList.copyOf(subBlocks));
        setCreativeTab(TheMod.INSTANCE.creativeTab());
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, ModLog3Block.EnumType.LATA).withProperty(CHECK_DECAY, Boolean.TRUE).withProperty(DECAYABLE, Boolean.TRUE));
    }

    @Override
    public int quantityDropped(Random random)
    {
        final int rarity = Settings.INSTANCE.saplingDropRarity();
        return rarity == 0 || random.nextInt(rarity) != 0 ? 0 : 1;
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[]{VARIANT,CHECK_DECAY,DECAYABLE});
    }

    @Override
    public ModLog3Block.EnumType getWoodType(int meta) {
       return ModLog3Block.EnumType.fromId(meta);
    }

    @Override
    protected String resourcePrefix() { return TheMod.getResourcePrefix(); }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        ArrayList<ItemStack> list=new ArrayList<ItemStack>();
        list.add(new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos))));
        return list;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        switch(meta/4){
            case 0:
                state = state.withProperty(CHECK_DECAY,true).withProperty(DECAYABLE,true);
            break;
            case 1:
                state = state.withProperty(CHECK_DECAY,true).withProperty(DECAYABLE,false);
            break;
            case 2:
                state = state.withProperty(CHECK_DECAY,false).withProperty(DECAYABLE,true);
            break;
            case 3:
                state = state.withProperty(CHECK_DECAY,false).withProperty(DECAYABLE,false);
            break;
        }
        state = state.withProperty(VARIANT,ModLog3Block.EnumType.fromId(meta%4));
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        ModLog3Block.EnumType type = (ModLog3Block.EnumType) state.getValue(ModLog3Block.VARIANT);
        boolean check = (Boolean) state.getValue(CHECK_DECAY);
        boolean dcable = (Boolean) state.getValue(CHECK_DECAY);
        int par = check?dcable?0:1:dcable?2:3;
        return par*4+type.ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state.withProperty(CHECK_DECAY,true).withProperty(DECAYABLE,true));
    }

}
