package inc.a13xis.legacy.dendrology;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import inc.a13xis.legacy.dendrology.block.ModBlocks;
import inc.a13xis.legacy.dendrology.config.Settings;
import inc.a13xis.legacy.dendrology.content.ParcelManager;
import inc.a13xis.legacy.dendrology.content.crafting.Crafter;
import inc.a13xis.legacy.dendrology.content.crafting.OreDictHandler;
import inc.a13xis.legacy.dendrology.content.crafting.Smelter;
import inc.a13xis.legacy.dendrology.content.fuel.FuelHandler;
import inc.a13xis.legacy.dendrology.content.overworld.OverworldTreeGenerator;
import inc.a13xis.legacy.dendrology.content.overworld.OverworldTreeSpecies;
import inc.a13xis.legacy.dendrology.item.ModItems;
import inc.a13xis.legacy.dendrology.proxy.Proxy;
import inc.a13xis.legacy.koresample.common.util.log.Logger;
import inc.a13xis.legacy.koresample.compat.Integrates;
import inc.a13xis.legacy.koresample.config.ConfigEventHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.LoaderState.ModState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SuppressWarnings({
        "AnonymousInnerClass",
        "StaticNonFinalField",
        "WeakerAccess",
        "StaticVariableMayNotBeInitialized",
        "NonConstantFieldWithUpperCaseName"
})
@Mod(modid = TheMod.MOD_ID, name = TheMod.MOD_NAME, version = TheMod.MOD_VERSION, useMetadata = true, guiFactory = TheMod.MOD_GUI_FACTORY)
public final class TheMod
{
    public static final String MOD_ID = "dendrology";
    static final String MOD_NAME = "Ancient Trees";
    static final String MOD_VERSION = "1.8.9-L1";
    static final String MOD_GUI_FACTORY = "inc.a13xis.legacy.dendrology.config.client.ModGuiFactory";
    private static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ':';
    @SuppressWarnings("PublicField")
    @Instance(MOD_ID)
    public static TheMod INSTANCE;
    private final CreativeTabs creativeTab = new CreativeTabs(MOD_ID.toLowerCase())
    {
        private final OverworldTreeSpecies ICON = OverworldTreeSpecies.PORFFOR;

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIconItemStack()
        {
            return new ItemStack(ICON.saplingBlock(), 1, ICON.saplingSubBlockVariant().ordinal());
        }


        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem() { return null; }
    };
    private final List<Integrates> integrators = Lists.newArrayList();
    private Optional<ConfigEventHandler> configEventHandler = Optional.absent();

    public static String getResourcePrefix() { return RESOURCE_PREFIX; }

    public static Logger logger() { return Logger.forMod(MOD_ID); }

//    private void initIntegrators()
//    {
//        Logger.forMod(MOD_ID).info("Preparing integration with other mods.");
//        integrators.add(new MinechemMod());
//        integrators.add(new ForestryMod());
//        integrators.add(new GardenCoreMod());
//        integrators.add(new GardenTreesMod());
//        integrators.add(new ChiselMod());
//        integrators.add(new MineFactoryReloadedMod());
//        integrators.add(new StorageDrawersMod());
//   }

    public Configuration configuration()
    {
        if (configEventHandler.isPresent()) return configEventHandler.get().configuration();
        return new Configuration();
    }

    public CreativeTabs creativeTab()
    {
        return creativeTab;
    }

    private void integrateMods(ModState modState)
    {
        for (final Integrates integrator : integrators)
            integrator.integrate(modState);
    }

    @EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event)
    {
        configEventHandler = Optional.of(new ConfigEventHandler(MOD_ID, event.getSuggestedConfigurationFile(), Settings.INSTANCE, Settings.CONFIG_VERSION));
        configEventHandler.get().activate();

        new ModBlocks().loadContent();
        ModItems mi = new ModItems();
        mi.loadContent();
        Proxy.common.registerRenders();

        //Proxy.common.initSubRenders();
        //initIntegrators();
        integrateMods(event.getModState());
    }

    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event)
    {
        Logger.forMod(MOD_ID).info("Adding recipes.");
        new OreDictHandler().registerBlocksWithOreDictinary();
        new Crafter().writeRecipes();
        new Smelter().registerSmeltings();
        integrateMods(event.getModState());
    }

    @EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event)
    {
        Proxy.render.postInit();
        FuelHandler.postInit();
        integrateMods(event.getModState());
        integrators.clear();
        ParcelManager.INSTANCE.init();

        new OverworldTreeGenerator().install();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this).add("creativeTab", creativeTab).add("integrators", integrators)
                .add("configEventHandler", configEventHandler).toString();
    }
}
