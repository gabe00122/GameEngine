package gabek.sm2.world

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.TagManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.systems.*
import gabek.sm2.systems.character.BiDirectionSystem
import gabek.sm2.systems.character.CharacterAnimatorSystem
import gabek.sm2.systems.character.CharacterControllerSystem
import gabek.sm2.systems.character.DamageSystem
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
    config.setSystem(TimeManager())

    //needs to be first
    config.setSystem(TranslationSystem())
    //box2d
    config.setSystem(Box2dSystem())
    config.setSystem(ParentSystem())
    config.setSystem(ParentBodyTackingSystem())

    //config.setSystem(WorldBoundsSystem())
    config.setSystem(DamageSystem())
    config.setSystem(PelletLifeSpanSystem())
    config.setSystem(PelletCollisionSystem())

    //movement
    config.setSystem(PlayerInputSystem())
    config.setSystem(CharacterControllerSystem())
    //config.setSystem(AbilityIndexSystem())
    config.setSystem(BiDirectionSystem())

    //tiles
    config.setSystem(TileMapSystem(kodein))

    //graphics
    config.setSystem(CameraSystem())
    config.setSystem(CameraTrackingSystem())

    config.setSystem(CharacterAnimatorSystem())
    config.setSystem(AnimationSystem())

    config.setSystem(TileRenderSystem())
    config.setSystem(SpriteRenderSystem(kodein))
    config.setSystem(HealthRenderSystem(kodein))


    return World(config)
}


fun buildRenderManager(kodein: Kodein): RenderManager{
    with(kodein.instance<World>()){
        return RenderManager(kodein,
                cameraSystem = getSystem(),
                batchSystems = listOf(
                        getSystem<TileRenderSystem>(),
                        getSystem<SpriteRenderSystem>(),
                        getSystem<HealthRenderSystem>()
                )
                )
    }
}