package gabek.onebreath

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.prefab.CameraPrefab
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.InputSystem
import gabek.onebreath.system.OneBreathLevelLoader
import gabek.engine.core.systems.TileMapSystem
import gabek.engine.core.systems.character.BiDirectionSystem
import gabek.engine.core.systems.character.CharacterAnimatorSystem
import gabek.engine.core.systems.character.CharacterControllerSystem
import gabek.engine.core.systems.character.DamageSystem
import gabek.engine.core.systems.common.*
import gabek.engine.core.systems.graphics.*
import gabek.engine.core.tilemap.TileDefinitions
import gabek.engine.core.tilemap.TileType
import gabek.engine.core.util.getSystem
import gabek.engine.core.world.EntityRenderManager
import gabek.engine.core.world.RenderManager
import gabek.engine.quickbuoyancy.BuoyancySystem
import gabek.engine.quicklights.LightingSystem
import gabek.onebreath.prefab.BloodPrefab
import gabek.onebreath.prefab.Junk1Prefab
import gabek.onebreath.prefab.PlayerPrefab
import gabek.onebreath.prefab.RatPrefab
import gabek.onebreath.system.OneBreathInputSystem

/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */


fun buildWorld(kodein: Kodein): World {
    val wc = WorldConfiguration()

    wc.setSystem(UpdateManager()) // tick frame count

    // <PASSIVE>
    wc.setSystem(EntityLinkManager())
    wc.setSystem(TagManager())
    wc.setSystem(GroupManager())

    wc.setSystem(CameraSystem())
    wc.setSystem(Box2dDebugSystem())
    wc.setSystem(RenderManager { world -> //define render order
        batchSystems = listOf(
                world.getSystem(TileMapSystem::class.java).getRendererForLayer(TileMapSystem.Layer.BACKGROUND),
                EntityRenderManager(world.getSystem<SpriteRenderSystem>()),
                world.getSystem(TileMapSystem::class.java).getRendererForLayer(TileMapSystem.Layer.FOREGROUND)
        )
        directSystems = listOf(
                world.getSystem(LightingSystem::class.java),
                world.getSystem(Box2dDebugSystem::class.java)
        )
    })

    wc.setSystem(PrefabManager.build(kodein, ::prefabs))
    wc.setSystem(ParentSystem())

    wc.setSystem(OneBreathLevelLoader())

    // </PASSIVE>

    wc.setSystem(LifeSpanSystem())

    // <INTERPOLATION PREP>
    wc.setSystem(TranslationSystem())
    wc.setSystem(BoundSystem())
    // </INTERPOLATION PREP>

    // <INPUT HANDLING>
    wc.setSystem(InputSystem())
    wc.setSystem(OneBreathInputSystem()) //custom input for all projects
    // </INPUT HANDLING>

    // <OBJECT ACTIONS>
    wc.setSystem(DamageSystem())
    wc.setSystem(CharacterControllerSystem())
    wc.setSystem(CharacterAnimatorSystem())
    wc.setSystem(BiDirectionSystem())
    // </OBJECT ACTIONS>

    // <MOVE STUFF>
    wc.setSystem(Box2dSystem())
    wc.setSystem(BuoyancySystem())

    wc.setSystem(ParentTackingSystem()) // follow the parent entities movement
    wc.setSystem(CameraTrackingSystem()) // when the camera follows stuff
    wc.setSystem(CameraDirectControlSystem())
    // </MOVE STUFF>

    // <TILES>
    wc.setSystem(TileMapSystem(kodein, ::tileDef))
    // </TILES>

    wc.setSystem(SpriteRenderSystem())
    wc.setSystem(AnimationSystem())

    wc.setSystem(LightingSystem())

    return World(wc)
}

fun prefabs(builder: PrefabManager.Builder) = with(builder) {
    bind("camera", CameraPrefab())

    bind("player", PlayerPrefab())

    bind("rat", RatPrefab())


    bind("junk_1", Junk1Prefab())
    bind("blood", BloodPrefab())
}

fun tileDef(tileDefinitions: TileDefinitions, world: World, kodein: Kodein) {
    val assets: Assets = kodein.instance()

    tileDefinitions.addType(TileType("empty"))

    tileDefinitions.addType(TileType("stone", assets.findTexture("actors:tile:5"), true))
    tileDefinitions.addType(TileType("water", assets.findTexture("actors:tile:0")))
}