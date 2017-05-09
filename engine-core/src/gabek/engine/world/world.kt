package gabek.engine.world

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.io.KryoArtemisSerializer
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.artemis.managers.WorldSerializationManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.prefab.*
import gabek.engine.prefab.enviroment.SpinnerPropPrefab
import gabek.engine.serialisation.kryoSetup
import gabek.engine.systems.*
import gabek.engine.systems.brains.WanderingBrainSystem
import gabek.engine.systems.character.BiDirectionSystem
import gabek.engine.systems.character.CharacterAnimatorSystem
import gabek.engine.systems.character.CharacterControllerSystem
import gabek.engine.systems.character.DamageSystem
import gabek.engine.systems.common.*
import gabek.engine.systems.gamemodes.GameModeManager
import gabek.engine.systems.graphics.*
import gabek.engine.systems.pellet.PelletCollisionSystem
import gabek.engine.systems.pellet.PelletMovmentSystem
import gabek.engine.tilemap.TileDefinitions
import gabek.engine.tilemap.TileType
import gabek.engine.tilemap.types.SpikeTile
import gabek.engine.util.getSystem

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
    config.setSystem(TimeManager())

    //needs to be first
    config.setSystem(TranslationSystem())
    config.setSystem(BoundSystem())
    //box2d
    config.setSystem(Box2dSystem())
    config.setSystem(StaticJointSystem())
    config.setSystem(ParentSystem())
    config.setSystem(ParentBodyTackingSystem())

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
    config.setSystem(SpriteRenderSystem(kodein))
    //config.setSystem(HealthRenderSystem(kodein))

    config.setSystem(Box2dDebugSystem())

    val world = World(config)

    val kryoSerializer = KryoArtemisSerializer(world)
    kryoSetup(kodein, kryoSerializer.kryo)
    world.getSystem<WorldSerializationManager>().setSerializer(kryoSerializer)

    return world
}


fun buildRenderManager(kodein: Kodein): RenderManager {
    with(kodein.instance<World>()) {
        return RenderManager(kodein,
                cameraSystem = getSystem(),
                batchSystems = listOf(
                        getSystem<ParallaxRenderSystem>(),
                        getSystem<TileMapSystem>().getRendererForLayer(TileMapSystem.Layer.BACKGROUND),
                        EntityRendererManager(listOf(getSystem<SpriteRenderSystem>())),
                        getSystem<TileMapSystem>().getRendererForLayer(TileMapSystem.Layer.FOREGROUND)
                        //getSystem<HealthRenderSystem>()
                ),
                orthoSystems = listOf(
                        getSystem<Box2dDebugSystem>()
                )
        )
    }
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
