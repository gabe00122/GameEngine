package gabek.sm2.world

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.io.KryoArtemisSerializer
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.artemis.managers.WorldSerializationManager
import com.badlogic.gdx.physics.box2d.Contact
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.graphics.EntityRendererManager
import gabek.sm2.graphics.RenderManager
import gabek.sm2.prefab.*
import gabek.sm2.prefab.enviroment.SpinnerPropPrefab
import gabek.sm2.serialisation.kryoSetup
import gabek.sm2.physics.RCollisionAdapter
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon
import gabek.sm2.systems.*
import gabek.sm2.systems.brains.WanderingBrainSystem
import gabek.sm2.systems.character.*
import gabek.sm2.systems.gamemodes.GameModeManager
import gabek.sm2.systems.graphics.*
import gabek.sm2.systems.BleedingSystem
import gabek.sm2.systems.common.*
import gabek.sm2.systems.pellet.PelletCollisionSystem
import gabek.sm2.systems.pellet.PelletMovmentSystem
import gabek.sm2.tilemap.TileDefinitions
import gabek.sm2.tilemap.TileReference
import gabek.sm2.tilemap.TileType
import gabek.sm2.tilemap.types.SpikeTile

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
                        //getSystem<Box2dDebugSystem>()
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

fun buildTileDefinitions(definitions: TileDefinitions, world: World, kodein: Kodein){
    val assets: Assets = kodein.instance()

    definitions.addType(TileType("none", null, false))
    definitions.addType(TileType("background", assets.findTexture("tiles:back"), false))
    definitions.addType(TileType("wall", assets.findTexture("tiles:wall"), true))

    definitions.addType(SpikeTile(world, kodein))
}
