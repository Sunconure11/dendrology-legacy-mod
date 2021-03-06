package inc.a13xis.legacy.dendrology.block;

import com.google.common.collect.ImmutableList;
import inc.a13xis.legacy.dendrology.TheMod;
import inc.a13xis.legacy.dendrology.content.ProvidesPotionEffect;
import inc.a13xis.legacy.koresample.tree.DefinesSapling;
import inc.a13xis.legacy.koresample.tree.block.SaplingBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class ModSapling2Block extends SaplingBlock
{
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", ModSlab2Block.EnumType.class);
    public ModSapling2Block(Iterable<? extends DefinesSapling> subBlocks)
    {
        super(ImmutableList.copyOf(subBlocks));
        setCreativeTab(TheMod.INSTANCE.creativeTab());
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, ModSlab2Block.EnumType.LATA).withProperty(STAGE, 0) );
    }

    @Override
    protected String resourcePrefix() { return TheMod.getResourcePrefix(); }

    @Override
    public boolean isTypeAt(World worldIn, BlockPos pos, Enum type) {
        return type instanceof ModSlab2Block.EnumType && type.equals(worldIn.getBlockState(pos).getValue(VARIANT));
    }

    @SuppressWarnings("ReturnOfNull")
    public String getPotionEffect(ItemStack itemStack)
    {
        final List<DefinesSapling> subBlocks = subBlocks();
        final int itemDamage = itemStack.getItemDamage();
        if (itemDamage < 0 || itemDamage >= subBlocks.size()) return null;

        final DefinesSapling subBlock = subBlocks.get(itemDamage);
        return subBlock instanceof ProvidesPotionEffect ? ((ProvidesPotionEffect) subBlock).potionEffect() : null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        ModSlabBlock.EnumType id=ModSlabBlock.EnumType.fromId(meta);
        return meta<5?getDefaultState().withProperty(VARIANT,ModSlab2Block.EnumType.fromId(meta)).withProperty(STAGE,0):getDefaultState().withProperty(VARIANT,ModSlab2Block.EnumType.fromId(meta)).withProperty(STAGE,1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        ModSlab2Block.EnumType type = (ModSlab2Block.EnumType) state.getValue(VARIANT);
        int stage = (Integer)state.getValue(STAGE);
        int id = type.ordinal();
        return stage*5+type.ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {VARIANT, STAGE});
    }
}
