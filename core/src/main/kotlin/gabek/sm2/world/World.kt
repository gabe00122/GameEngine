package gabek.sm2.world

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.systems.*
import gabek.sm2.systems.character.AbilityIndexSystem
import gabek.sm2.systems.character.CharacterAnimatorSystem
import gabek.sm2.systems.character.CharacterControllerSystem
import gabek.sm2.systems.graphics.*
import net.namekdev.entity_tracker.EntityTracker
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow

/**
 * @author Gabriel Keith
 */
fun buildWorld(kodein: Kodein): World {
    val config = WorldConfiguration()
    //built in

    //config.setSystem(EntityTracker(EntityTrackerMainWindow()))

    config.setSystem(EntityLinkManager())
    config.setSystem(TagManager())
    config.setSystem(GroupManager())
    //config.setSystem(PlayerManager())
    //config.setSystem(TeamManager())
    config.setSystem(FactoryManager(kodein))

    //needs to be first
    config.setSystem(TranslationSystem())
    config.setSystem(WorldBoundsSystem())
    config.setSystem(PelletLifeSpanSystem())

    //movement
    config.setSystem(PlayerInputSystem())
    config.setSystem(CharacterControllerSystem())
    config.setSystem(AbilityIndexSystem())

    //tiles
    config.setSystem(TileMapSystem(kodein))

    //box2d
    config.setSystem(Box2dSystem())

    //graphics
    config.setSystem(CameraSystem())
    config.setSystem(CameraTrackingSystem())

    config.setSystem(RenderSystem())
    config.setSystem(CharacterAnimatorSystem())
    config.setSystem(AnimationSystem())
    config.setSystem(SpriteRenderSystem())

    config.setSystem(HealthRenderSystem(kodein))


    return World(config)
}