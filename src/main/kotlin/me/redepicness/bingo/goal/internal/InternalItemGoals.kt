package me.redepicness.bingo.goal.internal

import com.destroystokyo.paper.MaterialSetTag
import com.destroystokyo.paper.MaterialTags
import me.redepicness.bingo.goal.GoalDifficulty
import me.redepicness.bingo.goal.GoalManager
import me.redepicness.bingo.goal.GoalTag
import me.redepicness.bingo.goal.info.ItemGoalInfo
import me.redepicness.bingo.goal.type.ItemGoal
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object InternalItemGoals {

    private val LOGGER: Logger = LoggerFactory.getLogger("internal-item-goals")

    private val END_DIFFICULTY_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("dimension", "end"))
        .add(Tag.SHULKER_BOXES)
        .add(
            Material.CHORUS_FLOWER,
            Material.CHORUS_FRUIT,
            Material.POPPED_CHORUS_FRUIT,
            Material.END_STONE,
            Material.END_STONE_BRICKS,
            Material.END_STONE_BRICK_SLAB,
            Material.END_STONE_BRICK_STAIRS,
            Material.END_STONE_BRICK_WALL,
            Material.PURPUR_BLOCK,
            Material.PURPUR_PILLAR,
            Material.PURPUR_SLAB,
            Material.PURPUR_STAIRS,
            Material.SHULKER_SHELL,
            Material.ELYTRA,
            Material.DRAGON_BREATH,
            Material.DRAGON_EGG,
            Material.DRAGON_HEAD
        )
        .lock()
    private val NETHER_DIFFICULTY_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("dimension", "nether"))
        .contains("NETHER")
        .contains("WART")
        .contains("QUARTZ")
        .contains("GLOWSTONE")
        .contains("BLAZE")
        .contains("CRIMSON")
        .contains("WARPED")
        .contains("GHAST")
        .contains("BLACKSTONE")
        .lock()
    private val SILK_TOUCH_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("enchant", "silk-touch"))
        .add(Tag.COAL_ORES)
        .add(Tag.COPPER_ORES)
        .add(Tag.DIAMOND_ORES)
        .add(Tag.IRON_ORES)
        .add(Tag.GOLD_ORES)
        .add(Tag.EMERALD_ORES)
        .add(Tag.LAPIS_ORES)
        .add(Tag.DIAMOND_ORES)
        .add(Tag.REDSTONE_ORES)
        .add(Material.NETHER_QUARTZ_ORE)
        .lock()

    //<editor-fold desc="Woods" defaultstate="collapsed">
    private val OAK_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "oak"))
        .startsWith("OAK")
        .notContains("DARK_OAK")
        .lock()
    private val BIRCH_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "birch"))
        .startsWith("BIRCH")
        .lock()
    private val JUNGLE_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "jungle"))
        .startsWith("JUNGLE")
        .lock()
    private val ACACIA_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "acacia"))
        .startsWith("ACACIA")
        .lock()
    private val DARK_OAK_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "dark-oak"))
        .startsWith("DARK_OAK")
        .lock()
    private val SPRUCE_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "spruce"))
        .startsWith("SPRUCE")
        .lock()
    private val MANGROVE_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "mangrove"))
        .startsWith("MANGROVE")
        .lock()
    private val AZALEA_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "azalea"))
        .startsWith("AZALEA")
        .lock()
    private val CHERRY_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "azalea"))
        .startsWith("CHERRY")
        .lock()
    private val CRIMSON_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "crimson"))
        .startsWith("CRIMSON")
        .notEndsWith("NYLIUM")
        .notEndsWith("ROOTS")
        .notEndsWith("FUNGUS")
        .lock()
    private val WARPED_WOODS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("wood", "warped"))
        .add { m: Material -> m.name.contains("WARPED") }
        .lock()

    //</editor-fold>
    //<editor-fold desc="Dyed stuff" defaultstate="collapsed">
    private val WHITE_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "white"))
        .startsWith("WHITE_")
        .lock()
    private val ORANGE_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "orange"))
        .startsWith("ORANGE_")
        .lock()
    private val MAGENTA_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "magenta"))
        .startsWith("MAGENTA_")
        .lock()
    private val LIGHT_BLUE_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "light-blue"))
        .startsWith("LIGHT_BLUE_")
        .lock()
    private val YELLOW_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "yellow"))
        .startsWith("YELLOW_")
        .lock()
    private val PINK_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "pink"))
        .startsWith("PINK_")
        .lock()
    private val GRAY_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "gray"))
        .startsWith("GRAY_")
        .lock()
    private val LIGHT_GRAY_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "light-gray"))
        .startsWith("LIGHT_GRAY_")
        .lock()
    private val CYAN_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "cyan"))
        .startsWith("CYAN_")
        .lock()
    private val PURPLE_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "purple"))
        .startsWith("PURPLE_")
        .lock()
    private val BLUE_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "blue"))
        .startsWith("BLUE_")
        .lock()
    private val BROWN_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "brown"))
        .startsWith("BROWN_")
        .lock()
    private val RED_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "red"))
        .startsWith("RED_")
        .lock()
    private val BLACK_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "black"))
        .startsWith("BLACK_")
        .lock()
    private val GREEN_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "green"))
        .startsWith("GREEN_")
        .lock()
    private val LIME_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("color", "lime"))
        .startsWith("LIME_")
        .lock()

    //</editor-fold>
    private val BEE_ITEMS_TAG: Tag<Material> = MaterialSetTag(NamespacedKey("entity", "bee"))
        .contains("WAX")
        .contains("HONEY")
        .contains("BEE")

    private val invalidTags = setOf(
        MaterialTags.SPAWN_EGGS,
        MaterialTags.MUSIC_DISCS,
    )

    private val woodTag = GoalTag["wood", "all"]
    private val colorTag = GoalTag["color", "all"]

    private val tagAndContainerList = listOf(
        MaterialTagAndContainer(END_DIFFICULTY_TAG, GoalDifficulty.INSANE),
        MaterialTagAndContainer(NETHER_DIFFICULTY_TAG, GoalDifficulty.HARD),
        MaterialTagAndContainer(OAK_WOODS_TAG, GoalDifficulty.FREE, woodTag),
        MaterialTagAndContainer(BIRCH_WOODS_TAG, GoalDifficulty.FREE, woodTag),
        MaterialTagAndContainer(JUNGLE_WOODS_TAG, GoalDifficulty.EASY, woodTag),
        MaterialTagAndContainer(ACACIA_WOODS_TAG, GoalDifficulty.EASY, woodTag),
        MaterialTagAndContainer(DARK_OAK_WOODS_TAG, GoalDifficulty.EASY, woodTag),
        MaterialTagAndContainer(SPRUCE_WOODS_TAG, GoalDifficulty.MEDIUM, woodTag),
        MaterialTagAndContainer(MANGROVE_WOODS_TAG, GoalDifficulty.HARD, woodTag),
        MaterialTagAndContainer(AZALEA_WOODS_TAG, GoalDifficulty.HARD, woodTag),
        MaterialTagAndContainer(CHERRY_WOODS_TAG, GoalDifficulty.HARD, woodTag),
        MaterialTagAndContainer(CRIMSON_WOODS_TAG, woodTag),  //Also in Nether
        MaterialTagAndContainer(WARPED_WOODS_TAG, woodTag),  //Also in nether
        MaterialTagAndContainer(WHITE_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(ORANGE_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(MAGENTA_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(LIGHT_BLUE_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(YELLOW_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(PINK_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(GRAY_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(LIGHT_GRAY_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(CYAN_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(PURPLE_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(BLUE_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(BROWN_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(RED_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(BLACK_TAG, GoalDifficulty.EASY, colorTag),
        MaterialTagAndContainer(GREEN_TAG, GoalDifficulty.HARD, colorTag),
        MaterialTagAndContainer(LIME_TAG, GoalDifficulty.HARD, colorTag),
        MaterialTagAndContainer(MaterialTags.STAINED_GLASS, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialTags.STAINED_GLASS_PANES, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialTags.TERRACOTTA, GoalDifficulty.MEDIUM),
        MaterialTagAndContainer(MaterialTags.STAINED_TERRACOTTA, GoalDifficulty.MEDIUM),
        MaterialTagAndContainer(MaterialTags.GLAZED_TERRACOTTA, GoalDifficulty.MEDIUM),
        MaterialTagAndContainer(MaterialSetTag.WOOL, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialSetTag.WOOL_CARPETS, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialTags.CONCRETES, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialTags.CONCRETE_POWDER, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialTags.BEDS, GoalDifficulty.EASY),
        MaterialTagAndContainer(MaterialSetTag.ITEMS_BANNERS, GoalDifficulty.EASY),
        MaterialTagAndContainer(SILK_TOUCH_TAG, GoalDifficulty.INSANE),
        MaterialTagAndContainer(BEE_ITEMS_TAG, GoalDifficulty.HARD),
        // todo add all the remaining tags for items
        //MaterialTagAndContainer(MaterialSetTag(NamespacedKey("", "")))
    )

    private fun item(material: Material, container: GoalDifficultyAndTagContainer): ItemGoal {
        return ItemGoal(
            material.key().value(),
            ItemGoalInfo(
                material,
                container
            )
        )
    }

    fun registerInternalGoals() {
        var unregisteredCount = 0
        out@ for (mat in Material.entries) {
            if (!mat.isItem || mat.isLegacy) {
                continue
            }
            for (tag in invalidTags) {
                if (tag.isTagged(mat)) {
                    continue@out
                }
            }
            var container = GoalDifficultyAndTagContainer.EMPTY
            for ((tag, container1) in tagAndContainerList) {
                if (tag.isTagged(mat)) {
                    container = container.combine(container1).addTags(GoalTag[tag.key()])
                }
            }
            if (container.difficulties.isNotEmpty()) {
                try {
                    GoalManager.registerGoal(item(mat, container))
                }
                catch (e: Exception){
                    LOGGER.warn("Error registering {}!", mat)
                    LOGGER.warn("Error: ", e)
                }
            } else {
                unregisteredCount++
                LOGGER.info("Material {} not registered internally?", mat)
            }
        }
        LOGGER.info("{}/{} materials not registered!", unregisteredCount, Material.entries.size)
    }
    /*private static @Nullable GoalDifficultyAndTagContainer getGoalContainer(@NotNull Material mat){
        return switch (mat){
            case STONE,
                    GRANITE,
                    DIORITE,
                    ANDESITE -> item(mat, FREE, STONES);
            case POLISHED_GRANITE,
                    POLISHED_DIORITE,
                    POLISHED_ANDESITE,
                    DEEPSLATE,
                    COBBLED_DEEPSLATE,
                    POLISHED_DEEPSLATE -> item(mat, EASY, STONES);
            case CALCITE -> null;
            case TUFF -> null;
            case DRIPSTONE_BLOCK -> null;
            case GRASS_BLOCK -> null;
            case DIRT -> null;
            case COARSE_DIRT -> null;
            case PODZOL -> null;
            case ROOTED_DIRT -> null;
            case MUD -> null;
            case CRIMSON_NYLIUM -> null;
            case WARPED_NYLIUM -> null;
            case COBBLESTONE -> null;
            case BEDROCK -> null;
            case SAND -> null;
            case RED_SAND -> null;
            case GRAVEL -> null;
            case ANCIENT_DEBRIS -> null;
            case COAL_BLOCK -> null;
            case RAW_IRON_BLOCK -> null;
            case RAW_COPPER_BLOCK -> null;
            case RAW_GOLD_BLOCK -> null;
            case AMETHYST_BLOCK -> null;
            case BUDDING_AMETHYST -> null;
            case IRON_BLOCK -> null;
            case COPPER_BLOCK -> null;
            case GOLD_BLOCK -> null;
            case DIAMOND_BLOCK -> null;
            case NETHERITE_BLOCK -> null;
            case EXPOSED_COPPER -> null;
            case WEATHERED_COPPER -> null;
            case OXIDIZED_COPPER -> null;
            case CUT_COPPER -> null;
            case EXPOSED_CUT_COPPER -> null;
            case WEATHERED_CUT_COPPER -> null;
            case OXIDIZED_CUT_COPPER -> null;
            case CUT_COPPER_STAIRS -> null;
            case EXPOSED_CUT_COPPER_STAIRS -> null;
            case WEATHERED_CUT_COPPER_STAIRS -> null;
            case OXIDIZED_CUT_COPPER_STAIRS -> null;
            case CUT_COPPER_SLAB -> null;
            case EXPOSED_CUT_COPPER_SLAB -> null;
            case WEATHERED_CUT_COPPER_SLAB -> null;
            case OXIDIZED_CUT_COPPER_SLAB -> null;
            case SPONGE -> null;
            case WET_SPONGE -> null;
            case GLASS -> null;
            case TINTED_GLASS -> null;
            case LAPIS_BLOCK -> null;
            case SANDSTONE -> null;
            case CHISELED_SANDSTONE -> null;
            case CUT_SANDSTONE -> null;
            case COBWEB -> null;
            case GRASS -> null;
            case FERN -> null;
            case DEAD_BUSH -> null;
            case SEAGRASS -> null;
            case SEA_PICKLE -> null;
            case DANDELION -> null;
            case POPPY -> null;
            case BLUE_ORCHID -> null;
            case ALLIUM -> null;
            case AZURE_BLUET -> null;
            case RED_TULIP -> null;
            case ORANGE_TULIP -> null;
            case WHITE_TULIP -> null;
            case PINK_TULIP -> null;
            case OXEYE_DAISY -> null;
            case CORNFLOWER -> null;
            case LILY_OF_THE_VALLEY -> null;
            case WITHER_ROSE -> null;
            case SPORE_BLOSSOM -> null;
            case BROWN_MUSHROOM -> null;
            case RED_MUSHROOM -> null;
            case CRIMSON_FUNGUS -> null;
            case WARPED_FUNGUS -> null;
            case CRIMSON_ROOTS -> null;
            case WARPED_ROOTS -> null;
            case NETHER_SPROUTS -> null;
            case WEEPING_VINES -> null;
            case TWISTING_VINES -> null;
            case SUGAR_CANE -> null;
            case KELP -> null;
            case MOSS_CARPET -> null;
            case MOSS_BLOCK -> null;
            case HANGING_ROOTS -> null;
            case BIG_DRIPLEAF -> null;
            case SMALL_DRIPLEAF -> null;
            case BAMBOO -> null;
            case STONE_SLAB -> null;
            case SMOOTH_STONE_SLAB -> null;
            case SANDSTONE_SLAB -> null;
            case CUT_SANDSTONE_SLAB -> null;
            case COBBLESTONE_SLAB -> null;
            case BRICK_SLAB -> null;
            case STONE_BRICK_SLAB -> null;
            case MUD_BRICK_SLAB -> null;
            case NETHER_BRICK_SLAB -> null;
            case QUARTZ_SLAB -> null;
            case RED_SANDSTONE_SLAB -> null;
            case CUT_RED_SANDSTONE_SLAB -> null;
            case PURPUR_SLAB -> null;
            case PRISMARINE_SLAB -> null;
            case PRISMARINE_BRICK_SLAB -> null;
            case DARK_PRISMARINE_SLAB -> null;
            case SMOOTH_QUARTZ -> null;
            case SMOOTH_RED_SANDSTONE -> null;
            case SMOOTH_SANDSTONE -> null;
            case SMOOTH_STONE -> null;
            case BRICKS -> null;
            case BOOKSHELF -> null;
            case MOSSY_COBBLESTONE -> null;
            case OBSIDIAN -> null;
            case TORCH -> null;
            case END_ROD -> null;
            case CHORUS_PLANT -> null;
            case CHORUS_FLOWER -> null;
            case PURPUR_BLOCK -> null;
            case PURPUR_PILLAR -> null;
            case PURPUR_STAIRS -> null;
            case SPAWNER -> null;
            case CHEST -> null;
            case CRAFTING_TABLE -> null;
            case FARMLAND -> null;
            case FURNACE -> null;
            case LADDER -> null;
            case COBBLESTONE_STAIRS -> null;
            case SNOW -> null;
            case ICE -> null;
            case SNOW_BLOCK -> null;
            case CACTUS -> null;
            case CLAY -> null;
            case JUKEBOX -> null;
            case PUMPKIN -> null;
            case CARVED_PUMPKIN -> null;
            case JACK_O_LANTERN -> null;
            case NETHERRACK -> null;
            case SOUL_SAND -> null;
            case SOUL_SOIL -> null;
            case BASALT -> null;
            case POLISHED_BASALT -> null;
            case SMOOTH_BASALT -> null;
            case SOUL_TORCH -> null;
            case GLOWSTONE -> null;
            case STONE_BRICKS -> null;
            case MOSSY_STONE_BRICKS -> null;
            case CRACKED_STONE_BRICKS -> null;
            case CHISELED_STONE_BRICKS -> null;
            case PACKED_MUD -> null;
            case MUD_BRICKS -> null;
            case DEEPSLATE_BRICKS -> null;
            case CRACKED_DEEPSLATE_BRICKS -> null;
            case DEEPSLATE_TILES -> null;
            case CRACKED_DEEPSLATE_TILES -> null;
            case CHISELED_DEEPSLATE -> null;
            case REINFORCED_DEEPSLATE -> null;
            case MUSHROOM_STEM -> null;
            case IRON_BARS -> null;
            case CHAIN -> null;
            case GLASS_PANE -> null;
            case MELON -> null;
            case VINE -> null;
            case GLOW_LICHEN -> null;
            case BRICK_STAIRS -> null;
            case STONE_BRICK_STAIRS -> null;
            case MUD_BRICK_STAIRS -> null;
            case MYCELIUM -> null;
            case LILY_PAD -> null;
            case NETHER_BRICKS -> null;
            case CRACKED_NETHER_BRICKS -> null;
            case CHISELED_NETHER_BRICKS -> null;
            case NETHER_BRICK_FENCE -> null;
            case NETHER_BRICK_STAIRS -> null;
            case SCULK -> null;
            case SCULK_VEIN -> null;
            case SCULK_CATALYST -> null;
            case SCULK_SHRIEKER -> null;
            case ENCHANTING_TABLE -> null;
            case DRAGON_EGG -> null;
            case SANDSTONE_STAIRS -> null;
            case ENDER_CHEST -> null;
            case EMERALD_BLOCK -> null;
            case BEACON -> null;
            case COBBLESTONE_WALL -> null;
            case MOSSY_COBBLESTONE_WALL -> null;
            case BRICK_WALL -> null;
            case PRISMARINE_WALL -> null;
            case RED_SANDSTONE_WALL -> null;
            case MOSSY_STONE_BRICK_WALL -> null;
            case GRANITE_WALL -> null;
            case STONE_BRICK_WALL -> null;
            case MUD_BRICK_WALL -> null;
            case NETHER_BRICK_WALL -> null;
            case ANDESITE_WALL -> null;
            case RED_NETHER_BRICK_WALL -> null;
            case SANDSTONE_WALL -> null;
            case END_STONE_BRICK_WALL -> null;
            case DIORITE_WALL -> null;
            case BLACKSTONE_WALL -> null;
            case POLISHED_BLACKSTONE_WALL -> null;
            case POLISHED_BLACKSTONE_BRICK_WALL -> null;
            case COBBLED_DEEPSLATE_WALL -> null;
            case POLISHED_DEEPSLATE_WALL -> null;
            case DEEPSLATE_BRICK_WALL -> null;
            case DEEPSLATE_TILE_WALL -> null;
            case ANVIL -> null;
            case CHISELED_QUARTZ_BLOCK -> null;
            case QUARTZ_BLOCK -> null;
            case QUARTZ_BRICKS -> null;
            case QUARTZ_PILLAR -> null;
            case QUARTZ_STAIRS -> null;
            case HAY_BLOCK -> null;
            case TERRACOTTA -> null;
            case PACKED_ICE -> null;
            case DIRT_PATH -> null;
            case SUNFLOWER -> null;
            case LILAC -> null;
            case ROSE_BUSH -> null;
            case PEONY -> null;
            case TALL_GRASS -> null;
            case LARGE_FERN -> null;
            case PRISMARINE -> null;
            case PRISMARINE_BRICKS -> null;
            case DARK_PRISMARINE -> null;
            case PRISMARINE_STAIRS -> null;
            case PRISMARINE_BRICK_STAIRS -> null;
            case DARK_PRISMARINE_STAIRS -> null;
            case SEA_LANTERN -> null;
            case RED_SANDSTONE -> null;
            case CHISELED_RED_SANDSTONE -> null;
            case CUT_RED_SANDSTONE -> null;
            case RED_SANDSTONE_STAIRS -> null;
            case REPEATING_COMMAND_BLOCK -> null;
            case CHAIN_COMMAND_BLOCK -> null;
            case MAGMA_BLOCK -> null;
            case NETHER_WART_BLOCK -> null;
            case WARPED_WART_BLOCK -> null;
            case RED_NETHER_BRICKS -> null;
            case BONE_BLOCK -> null;
            case STRUCTURE_VOID -> null;
            case SHULKER_BOX -> null;
            case TURTLE_EGG -> null;
            case DEAD_TUBE_CORAL_BLOCK -> null;
            case DEAD_BRAIN_CORAL_BLOCK -> null;
            case DEAD_BUBBLE_CORAL_BLOCK -> null;
            case DEAD_FIRE_CORAL_BLOCK -> null;
            case DEAD_HORN_CORAL_BLOCK -> null;
            case TUBE_CORAL_BLOCK -> null;
            case BRAIN_CORAL_BLOCK -> null;
            case BUBBLE_CORAL_BLOCK -> null;
            case FIRE_CORAL_BLOCK -> null;
            case HORN_CORAL_BLOCK -> null;
            case TUBE_CORAL -> null;
            case BRAIN_CORAL -> null;
            case BUBBLE_CORAL -> null;
            case FIRE_CORAL -> null;
            case HORN_CORAL -> null;
            case DEAD_BRAIN_CORAL -> null;
            case DEAD_BUBBLE_CORAL -> null;
            case DEAD_FIRE_CORAL -> null;
            case DEAD_HORN_CORAL -> null;
            case DEAD_TUBE_CORAL -> null;
            case BLUE_ICE -> null;
            case CONDUIT -> null;
            case POLISHED_GRANITE_STAIRS -> null;
            case SMOOTH_RED_SANDSTONE_STAIRS -> null;
            case MOSSY_STONE_BRICK_STAIRS -> null;
            case POLISHED_DIORITE_STAIRS -> null;
            case MOSSY_COBBLESTONE_STAIRS -> null;
            case END_STONE_BRICK_STAIRS -> null;
            case STONE_STAIRS -> null;
            case SMOOTH_SANDSTONE_STAIRS -> null;
            case SMOOTH_QUARTZ_STAIRS -> null;
            case GRANITE_STAIRS -> null;
            case ANDESITE_STAIRS -> null;
            case RED_NETHER_BRICK_STAIRS -> null;
            case POLISHED_ANDESITE_STAIRS -> null;
            case DIORITE_STAIRS -> null;
            case COBBLED_DEEPSLATE_STAIRS -> null;
            case POLISHED_DEEPSLATE_STAIRS -> null;
            case DEEPSLATE_BRICK_STAIRS -> null;
            case DEEPSLATE_TILE_STAIRS -> null;
            case POLISHED_GRANITE_SLAB -> null;
            case SMOOTH_RED_SANDSTONE_SLAB -> null;
            case MOSSY_STONE_BRICK_SLAB -> null;
            case POLISHED_DIORITE_SLAB -> null;
            case MOSSY_COBBLESTONE_SLAB -> null;
            case END_STONE_BRICK_SLAB -> null;
            case SMOOTH_SANDSTONE_SLAB -> null;
            case SMOOTH_QUARTZ_SLAB -> null;
            case GRANITE_SLAB -> null;
            case ANDESITE_SLAB -> null;
            case RED_NETHER_BRICK_SLAB -> null;
            case POLISHED_ANDESITE_SLAB -> null;
            case DIORITE_SLAB -> null;
            case COBBLED_DEEPSLATE_SLAB -> null;
            case POLISHED_DEEPSLATE_SLAB -> null;
            case DEEPSLATE_BRICK_SLAB -> null;
            case DEEPSLATE_TILE_SLAB -> null;
            case SCAFFOLDING -> null;
            case REDSTONE -> null;
            case REDSTONE_TORCH -> null;
            case REDSTONE_BLOCK -> null;
            case REPEATER -> null;
            case COMPARATOR -> null;
            case PISTON -> null;
            case STICKY_PISTON -> null;
            case SLIME_BLOCK -> null;
            case OBSERVER -> null;
            case HOPPER -> null;
            case DISPENSER -> null;
            case DROPPER -> null;
            case LECTERN -> null;
            case TARGET -> null;
            case LEVER -> null;
            case LIGHTNING_ROD -> null;
            case DAYLIGHT_DETECTOR -> null;
            case SCULK_SENSOR -> null;
            case TRIPWIRE_HOOK -> null;
            case TRAPPED_CHEST -> null;
            case TNT -> null;
            case REDSTONE_LAMP -> null;
            case NOTE_BLOCK -> null;
            case STONE_BUTTON -> null;
            case POLISHED_BLACKSTONE_BUTTON -> null;
            case STONE_PRESSURE_PLATE -> null;
            case POLISHED_BLACKSTONE_PRESSURE_PLATE -> null;
            case LIGHT_WEIGHTED_PRESSURE_PLATE -> null;
            case HEAVY_WEIGHTED_PRESSURE_PLATE -> null;
            case IRON_DOOR -> null;
            case IRON_TRAPDOOR -> null;
            case POWERED_RAIL -> null;
            case DETECTOR_RAIL -> null;
            case RAIL -> null;
            case ACTIVATOR_RAIL -> null;
            case SADDLE -> null;
            case MINECART -> null;
            case CHEST_MINECART -> null;
            case FURNACE_MINECART -> null;
            case TNT_MINECART -> null;
            case HOPPER_MINECART -> null;
            case CARROT_ON_A_STICK -> null;
            case WARPED_FUNGUS_ON_A_STICK -> null;
            case ELYTRA -> null;
            case TURTLE_HELMET -> null;
            case SCUTE -> null;
            case FLINT_AND_STEEL -> null;
            case APPLE -> null;
            case BOW -> null;
            case ARROW -> null;
            case COAL -> null;
            case CHARCOAL -> null;
            case DIAMOND -> null;
            case EMERALD -> null;
            case LAPIS_LAZULI -> null;
            case QUARTZ -> null;
            case AMETHYST_SHARD -> null;
            case RAW_IRON -> null;
            case IRON_INGOT -> null;
            case RAW_COPPER -> null;
            case COPPER_INGOT -> null;
            case RAW_GOLD -> null;
            case GOLD_INGOT -> null;
            case NETHERITE_INGOT -> null;
            case NETHERITE_SCRAP -> null;
            case WOODEN_SWORD -> null;
            case WOODEN_SHOVEL -> null;
            case WOODEN_PICKAXE -> null;
            case WOODEN_AXE -> null;
            case WOODEN_HOE -> null;
            case STONE_SWORD -> null;
            case STONE_SHOVEL -> null;
            case STONE_PICKAXE -> null;
            case STONE_AXE -> null;
            case STONE_HOE -> null;
            case GOLDEN_SWORD -> null;
            case GOLDEN_SHOVEL -> null;
            case GOLDEN_PICKAXE -> null;
            case GOLDEN_AXE -> null;
            case GOLDEN_HOE -> null;
            case IRON_SWORD -> null;
            case IRON_SHOVEL -> null;
            case IRON_PICKAXE -> null;
            case IRON_AXE -> null;
            case IRON_HOE -> null;
            case DIAMOND_SWORD -> null;
            case DIAMOND_SHOVEL -> null;
            case DIAMOND_PICKAXE -> null;
            case DIAMOND_AXE -> null;
            case DIAMOND_HOE -> null;
            case NETHERITE_SWORD -> null;
            case NETHERITE_SHOVEL -> null;
            case NETHERITE_PICKAXE -> null;
            case NETHERITE_AXE -> null;
            case NETHERITE_HOE -> null;
            case STICK -> null;
            case BOWL -> null;
            case MUSHROOM_STEW -> null;
            case STRING -> null;
            case FEATHER -> null;
            case GUNPOWDER -> null;
            case WHEAT_SEEDS -> null;
            case WHEAT -> null;
            case BREAD -> null;
            case LEATHER_HELMET -> null;
            case LEATHER_CHESTPLATE -> null;
            case LEATHER_LEGGINGS -> null;
            case LEATHER_BOOTS -> null;
            case CHAINMAIL_HELMET -> null;
            case CHAINMAIL_CHESTPLATE -> null;
            case CHAINMAIL_LEGGINGS -> null;
            case CHAINMAIL_BOOTS -> null;
            case IRON_HELMET -> null;
            case IRON_CHESTPLATE -> null;
            case IRON_LEGGINGS -> null;
            case IRON_BOOTS -> null;
            case DIAMOND_HELMET -> null;
            case DIAMOND_CHESTPLATE -> null;
            case DIAMOND_LEGGINGS -> null;
            case DIAMOND_BOOTS -> null;
            case GOLDEN_HELMET -> null;
            case GOLDEN_CHESTPLATE -> null;
            case GOLDEN_LEGGINGS -> null;
            case GOLDEN_BOOTS -> null;
            case NETHERITE_HELMET -> null;
            case NETHERITE_CHESTPLATE -> null;
            case NETHERITE_LEGGINGS -> null;
            case NETHERITE_BOOTS -> null;
            case FLINT -> null;
            case PORKCHOP -> null;
            case COOKED_PORKCHOP -> null;
            case PAINTING -> null;
            case GOLDEN_APPLE -> null;
            case ENCHANTED_GOLDEN_APPLE -> null;
            case BUCKET -> null;
            case WATER_BUCKET -> null;
            case LAVA_BUCKET -> null;
            case POWDER_SNOW_BUCKET -> null;
            case SNOWBALL -> null;
            case LEATHER -> null;
            case MILK_BUCKET -> null;
            case PUFFERFISH_BUCKET -> null;
            case SALMON_BUCKET -> null;
            case COD_BUCKET -> null;
            case TROPICAL_FISH_BUCKET -> null;
            case AXOLOTL_BUCKET -> null;
            case TADPOLE_BUCKET -> null;
            case BRICK -> null;
            case CLAY_BALL -> null;
            case DRIED_KELP_BLOCK -> null;
            case PAPER -> null;
            case BOOK -> null;
            case SLIME_BALL -> null;
            case EGG -> null;
            case COMPASS -> null;
            case RECOVERY_COMPASS -> null;
            case FISHING_ROD -> null;
            case CLOCK -> null;
            case SPYGLASS -> null;
            case GLOWSTONE_DUST -> null;
            case COD -> null;
            case SALMON -> null;
            case TROPICAL_FISH -> null;
            case PUFFERFISH -> null;
            case COOKED_COD -> null;
            case COOKED_SALMON -> null;
            case INK_SAC -> null;
            case GLOW_INK_SAC -> null;
            case COCOA_BEANS -> null;
            case BONE_MEAL -> null;
            case BONE -> null;
            case SUGAR -> null;
            case CAKE -> null;
            case COOKIE -> null;
            case FILLED_MAP -> null;
            case SHEARS -> null;
            case MELON_SLICE -> null;
            case DRIED_KELP -> null;
            case PUMPKIN_SEEDS -> null;
            case MELON_SEEDS -> null;
            case BEEF -> null;
            case COOKED_BEEF -> null;
            case CHICKEN -> null;
            case COOKED_CHICKEN -> null;
            case ROTTEN_FLESH -> null;
            case ENDER_PEARL -> null;
            case BLAZE_ROD -> null;
            case GHAST_TEAR -> null;
            case GOLD_NUGGET -> null;
            case NETHER_WART -> null;
            case POTION -> null;
            case GLASS_BOTTLE -> null;
            case SPIDER_EYE -> null;
            case FERMENTED_SPIDER_EYE -> null;
            case BLAZE_POWDER -> null;
            case MAGMA_CREAM -> null;
            case BREWING_STAND -> null;
            case CAULDRON -> null;
            case ENDER_EYE -> null;
            case GLISTERING_MELON_SLICE -> null;
            case EXPERIENCE_BOTTLE -> null;
            case FIRE_CHARGE -> null;
            case WRITABLE_BOOK -> null;
            case WRITTEN_BOOK -> null;
            case ITEM_FRAME -> null;
            case GLOW_ITEM_FRAME -> null;
            case FLOWER_POT -> null;
            case CARROT -> null;
            case POTATO -> null;
            case BAKED_POTATO -> null;
            case POISONOUS_POTATO -> null;
            case MAP -> null;
            case GOLDEN_CARROT -> null;
            case SKELETON_SKULL -> null;
            case WITHER_SKELETON_SKULL -> null;
            case PLAYER_HEAD -> null;
            case ZOMBIE_HEAD -> null;
            case CREEPER_HEAD -> null;
            case DRAGON_HEAD -> null;
            case NETHER_STAR -> null;
            case PUMPKIN_PIE -> null;
            case FIREWORK_ROCKET -> null;
            case FIREWORK_STAR -> null;
            case ENCHANTED_BOOK -> null;
            case NETHER_BRICK -> null;
            case PRISMARINE_SHARD -> null;
            case PRISMARINE_CRYSTALS -> null;
            case RABBIT -> null;
            case COOKED_RABBIT -> null;
            case RABBIT_STEW -> null;
            case RABBIT_FOOT -> null;
            case RABBIT_HIDE -> null;
            case ARMOR_STAND -> null;
            case IRON_HORSE_ARMOR -> null;
            case GOLDEN_HORSE_ARMOR -> null;
            case DIAMOND_HORSE_ARMOR -> null;
            case LEATHER_HORSE_ARMOR -> null;
            case LEAD -> null;
            case NAME_TAG -> null;
            case COMMAND_BLOCK_MINECART -> null;
            case MUTTON -> null;
            case COOKED_MUTTON -> null;
            case END_CRYSTAL -> null;
            case CHORUS_FRUIT -> null;
            case POPPED_CHORUS_FRUIT -> null;
            case BEETROOT -> null;
            case BEETROOT_SEEDS -> null;
            case BEETROOT_SOUP -> null;
            case DRAGON_BREATH -> null;
            case SPLASH_POTION -> null;
            case SPECTRAL_ARROW -> null;
            case TIPPED_ARROW -> null;
            case LINGERING_POTION -> null;
            case SHIELD -> null;
            case TOTEM_OF_UNDYING -> null;
            case SHULKER_SHELL -> null;
            case IRON_NUGGET -> null;
            case KNOWLEDGE_BOOK -> null;
            case DISC_FRAGMENT_5 -> null;
            case TRIDENT -> null;
            case PHANTOM_MEMBRANE -> null;
            case NAUTILUS_SHELL -> null;
            case HEART_OF_THE_SEA -> null;
            case CROSSBOW -> null;
            case LOOM -> null;
            case FLOWER_BANNER_PATTERN -> null;
            case CREEPER_BANNER_PATTERN -> null;
            case SKULL_BANNER_PATTERN -> null;
            case MOJANG_BANNER_PATTERN -> null;
            case GLOBE_BANNER_PATTERN -> null;
            case PIGLIN_BANNER_PATTERN -> null;
            case GOAT_HORN -> null;
            case COMPOSTER -> null;
            case BARREL -> null;
            case SMOKER -> null;
            case BLAST_FURNACE -> null;
            case CARTOGRAPHY_TABLE -> null;
            case FLETCHING_TABLE -> null;
            case GRINDSTONE -> null;
            case SMITHING_TABLE -> null;
            case STONECUTTER -> null;
            case BELL -> null;
            case LANTERN -> null;
            case SOUL_LANTERN -> null;
            case SWEET_BERRIES -> null;
            case GLOW_BERRIES -> null;
            case CAMPFIRE -> null;
            case SOUL_CAMPFIRE -> null;
            case SHROOMLIGHT -> null;
            case BEE_NEST -> null;
            case BEEHIVE -> null;
            case LODESTONE -> null;
            case CRYING_OBSIDIAN -> null;
            case RESPAWN_ANCHOR -> null;
            case CANDLE -> null;
            case SMALL_AMETHYST_BUD -> null;
            case MEDIUM_AMETHYST_BUD -> null;
            case LARGE_AMETHYST_BUD -> null;
            case AMETHYST_CLUSTER -> null;
            case POINTED_DRIPSTONE -> null;
            case OCHRE_FROGLIGHT -> null;
            case VERDANT_FROGLIGHT -> null;
            case PEARLESCENT_FROGLIGHT -> null;
            case ECHO_SHARD -> null;

            case COCOA -> null;
            case TRIPWIRE -> null;
            case CARROTS -> null;
            case POTATOES -> null;
            case BEETROOTS -> null;
            case KELP_PLANT -> null;
            case BAMBOO_SAPLING -> null;
            case POTTED_BAMBOO -> null;
            case CAVE_VINES -> null;
            default -> null;
        };
    }*/

    private data class MaterialTagAndContainer(val tag: Tag<Material>, val container: GoalDifficultyAndTagContainer) {
        constructor(tag: Tag<Material>, difficulty: GoalDifficulty, vararg tags: GoalTag) : this(
            tag,
            GoalDifficultyAndTagContainer(difficulty, *tags).addTags(GoalTag[tag.key()])
        )

        constructor(tag: Tag<Material>, vararg tags: GoalTag) : this(
            tag,
            GoalDifficultyAndTagContainer(*tags).addTags(GoalTag[tag.key()])
        )
    }
}
