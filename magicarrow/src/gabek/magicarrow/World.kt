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

/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */


fun buildWorld(kodein: Kodein): World {
    val wc = WorldConfiguration()
    wc.setSystem(EntityLinkManager())
    wc.setSystem(TagManager())
    wc.setSystem(GroupManager())

    wc.setSystem(UpdateManager())
    wc.setSystem(RenderManager { world ->
        batchSystems = listOf(
                EntityRenderManager(world.getSystem<SpriteRenderSystem>())
        )
    })

    wc.setSystem(PrefabManager.build(kodein, ::prefabs))

    wc.setSystem(ParentSystem())
    wc.setSystem(TranslationSystem())
    wc.setSystem(BoundSystem())

    wc.setSystem(Box2dSystem())

    wc.setSystem(CameraSystem())
    wc.setSystem(CameraTrackingSystem())

    wc.setSystem(SpriteRenderSystem())
    wc.setSystem(AnimationSystem())
    wc.setSystem(Box2dDebugSystem())

    return World(wc)
}

fun prefabs(builder: PrefabManager.Builder) = with(builder) {
    bind("camera", CameraPrefab())

    //mobs
    bind("attacker_basic", BasicAttackerPrefab())
}