package gabek.onebreath.def

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.InputSystem
import gabek.engine.core.systems.JointSystem
import gabek.engine.core.systems.TileMapSystem
import gabek.engine.core.systems.common.*
import gabek.engine.core.systems.graphics.*
import gabek.engine.core.util.getSystem
import gabek.engine.quick.light.LightingSystem
import gabek.engine.quick.water.BuoyancySystem
import gabek.onebreath.system.*

/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */


fun buildWorld(kodein: Kodein): World {
    val wc = WorldConfiguration()

    wc.setSystem(UpdateManager()) // tick frame time

    // <PASSIVE>
    wc.setSystem(EntityLinkManager())
    wc.setSystem(TagManager())
    wc.setSystem(GroupManager())

    //wc.setSystem(WorldUnitSystem(0.75f/16f))

    wc.setSystem(CameraSystem())
    wc.setSystem(Box2dDebugSystem())
    wc.setSystem(RenderManager(kodein, { world ->
        //define render order
        renderSystems = listOf(
                world.getSystem<TileMapSystem>().getLayer("background"),
                EntityRenderManager(
                        world.getSystem<SpriteRenderSystem>(),
                        world.getSystem<AnimationSystem>()
                ),
                world.getSystem<TileMapSystem>().getLayer("foreground")
        )
        directSystems = listOf(
                //world.getSystem(LightingSystem::class.java),
                world.getSystem(Box2dDebugSystem::class.java)
        )
    }))

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

    // <TILES>
    wc.setSystem(TileMapSystem(kodein, ::tileDef))
    // </TILES>

    // <MOVE STUFF>
    wc.setSystem(Box2dSystem())
    wc.setSystem(JointSystem())
    wc.setSystem(BuoyancySystem())
    wc.setSystem(MotorOscillatorSystem())

    wc.setSystem(ParentTackingSystem()) // follow the parent entities movement
    wc.setSystem(CameraTrackingSystem()) // when the camera follows stuff
    wc.setSystem(CameraDirectControlSystem())
    // </MOVE STUFF>

    wc.setSystem(CullingSystem())
    wc.setSystem(SpriteRenderSystem(kodein))
    wc.setSystem(AnimationSystem(kodein))

    wc.setSystem(LightingSystem())

    return World(wc)
}
