package gabek.magicarrow

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.prefab.CameraPrefab
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.common.*
import gabek.engine.core.systems.graphics.*
import gabek.engine.core.util.getSystem
import gabek.engine.core.world.EntityRenderManager
import gabek.engine.core.world.RenderManager
import gabek.magicarrow.prefab.BasicAttackerPrefab
import gabek.magicarrow.system.MagicArrowInputSystem

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
                EntityRenderManager(world.getSystem<SpriteRenderSystem>())
        )
    })

    wc.setSystem(PrefabManager.build(kodein, ::prefabs))
    wc.setSystem(ParentSystem())
    // </PASSIVE>

    // <INTERPOLATION PREP>
    wc.setSystem(TranslationSystem())
    wc.setSystem(BoundSystem())
    // </INTERPOLATION PREP>

    // <INPUT HANDLING>
    wc.setSystem(MagicArrowInputSystem()) //custom input for all projects
    // </INPUT HANDLING>

    // <MOVE STUFF>
    wc.setSystem(Box2dSystem())
    wc.setSystem(ParentTackingSystem()) // follow the parent entities movement
    wc.setSystem(CameraTrackingSystem()) // when the camera follows stuff
    wc.setSystem(CameraDirectControlSystem())
    // </MOVE STUFF>

    wc.setSystem(SpriteRenderSystem())
    wc.setSystem(AnimationSystem())

    return World(wc)
}

fun prefabs(builder: PrefabManager.Builder) = with(builder) {
    bind("camera", CameraPrefab())

    //mobs
    bind("attacker_basic", BasicAttackerPrefab())
}