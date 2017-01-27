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
import gabek.sm2.systems.pellet.PelletCollisionSystem
import gabek.sm2.systems.pellet.PelletLifeSpanSystem

/**
 * @author Gabriel Keith
 */
fun buildWorld(kodein: Kodein): World {
    val config = WorldConfiguration()
    //built in

    config.setSystem(EntityLinkManager())
    config.setSystem(TagManager())
    config.setSystem(GroupManager())
    //config.setSystem(PlayerManager())
    //config.setSystem(TeamManager())
    config.setSystem(FactoryManager(kodein))

    //needs to be first
    config.setSystem(TranslationSystem())
    //box2d
    config.setSystem(Box2dSystem())
    config.setSystem(ParentBodyTackingSystem())

    //config.setSystem(WorldBoundsSystem())
    config.setSystem(PelletLifeSpanSystem())
    config.setSystem(PelletCollisionSystem())

    //movement
    config.setSystem(PlayerInputSystem())
    config.setSystem(CharacterControllerSystem())
    config.setSystem(AbilityIndexSystem())

    //tiles
    config.setSystem(TileMapSystem(kodein))

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