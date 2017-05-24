package gabek.spacemonk

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.io.KryoArtemisSerializer
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.artemis.managers.WorldSerializationManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.prefab.*
import gabek.engine.core.systems.*
import gabek.spacemonk.system.WanderingBrainSystem
import gabek.engine.core.systems.character.BiDirectionSystem
import gabek.engine.core.systems.character.CharacterAnimatorSystem
import gabek.engine.core.systems.character.CharacterControllerSystem
import gabek.engine.core.systems.character.DamageSystem
import gabek.engine.core.systems.common.*
import gabek.engine.core.systems.gamemodes.GameModeManager
import gabek.engine.core.systems.graphics.*
import gabek.engine.core.systems.pellet.PelletCollisionSystem
import gabek.engine.core.tilemap.TileDefinitions
import gabek.spacemonk.prefab.SpinnerPropPrefab
import gabek.engine.core.systems.pellet.PelletMovmentSystem
import gabek.engine.core.tilemap.TileType
import gabek.spacemonk.tile.SpikeTile
import gabek.engine.core.util.getSystem
import gabek.engine.core.world.EntityRenderManager
import gabek.engine.core.world.RenderManager
import gabek.spacemonk.prefab.*

/**
 * @author Gabriel Keith
 */
fun buildWorld(kodein: Kodein): World {
    val config = WorldConfiguration()
    //built in

    config.setSystem(EntityLinkManager())
    config.setSystem(TagManager())
    config.setSystem(GroupManager())
    config.setSystem(WorldSerializationManager())
    //config.setSystem(GameModeManager())
    //config.setSystem(TeamManager())
    config.setSystem(PrefabManager.build(kodein, ::prefabBindings))
    config.setSystem(LevelTemplateLoader())
    config.setSystem(UpdateManager())

    //needs to be first
    config.setSystem(TranslationSystem())
    config.setSystem(BoundSystem())
    //box2d
    config.setSystem(Box2dSystem())
    config.setSystem(StaticJointSystem())
    config.setSystem(ParentSystem())
    config.setSystem(ParentTackingSystem())

    //config.setSystem(WorldBoundsSystem())
    config.setSystem(DamageSystem())
    //config.setSystem(BleedingSystem())
    config.setSystem(LifeSpanSystem())
    config.setSystem(PelletCollisionSystem())
    config.setSystem(PelletMovmentSystem())

    //brains
    config.setSystem(WanderingBrainSystem())

    //movement
    config.setSystem(PlayerInputSystem())
    config.setSystem(GameModeManager())
    config.setSystem(CharacterControllerSystem())
    //config.setSystem(AbilityIndexSystem())
    config.setSystem(BiDirectionSystem())

    //tiles
    config.setSystem(TileMapSystem(kodein, ::buildTileDefinitions))

    //graphics
    config.setSystem(CameraSystem())
    config.setSystem(CameraTrackingSystem())

    config.setSystem(CharacterAnimatorSystem())
    config.setSystem(AnimationSystem())

    config.setSystem(ParallaxRenderSystem(kodein))
    config.setSystem(SpriteRenderSystem())
    //config.setSystem(HealthRenderSystem(kodein))
    config.setSystem(RenderManager { world ->
        batchSystems = listOf(
                world.getSystem<ParallaxRenderSystem>(),
                world.getSystem<TileMapSystem>().getRendererForLayer(TileMapSystem.Layer.BACKGROUND),
                EntityRenderManager(world.getSystem<SpriteRenderSystem>()),
                world.getSystem<TileMapSystem>().getRendererForLayer(TileMapSystem.Layer.FOREGROUND)
                //getSystem<HealthRenderSystem>()
        )
        orthoSystems = listOf(
                world.getSystem<Box2dDebugSystem>()
        )
    })

    config.setSystem(Box2dDebugSystem())

    val world = World(config)

    val kryoSerializer = KryoArtemisSerializer(world)
    world.getSystem<WorldSerializationManager>().setSerializer(kryoSerializer)

    return world
}

fun prefabBindings(builder: PrefabManager.Builder) = with(builder) {
    bind("camera", CameraPrefab())
    bind("point", PointPrefab())

    bind("player", PlayerPrefab())
    bind("acid_monk", AcidMonkPrefab())
    bind("junk", JunkPrefab())
    bind("snail", SnailPrefab())

    bind("spinner", SpinnerPropPrefab())

    bind("blood", BloodPrefab())
}

fun buildTileDefinitions(definitions: TileDefinitions, world: World, kodein: Kodein) {
    val assets: Assets = kodein.instance()

    definitions.addType(TileType("none", null, false))
    definitions.addType(TileType("background", assets.findTexture("tiles:back"), false))
    definitions.addType(TileType("wall", assets.findTexture("tiles:wall"), true))

    definitions.addType(SpikeTile(world, kodein))
}
