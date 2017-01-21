package inc.a13xis.legacy.dendrology.world.gen.feature.cerasu;

import inc.a13xis.legacy.dendrology.world.gen.feature.vanilla.AbstractLargeVanillaTree;
import inc.a13xis.legacy.dendrology.content.overworld.OverworldTreeSpecies;

public class LargeCerasuTree extends AbstractLargeVanillaTree
{
    public LargeCerasuTree(boolean fromSapling) { super(fromSapling); }

    @Override
    protected int getUnmaskedLogMeta() { return OverworldTreeSpecies.CERASU.logSubBlockIndex(); }
}