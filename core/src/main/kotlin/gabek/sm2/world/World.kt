package gabek.sm2.world

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.link.EntityLinkManager
import com.artemis.managers.GroupManager
import com.artemis.managers.PlayerManager
import com.artemis.managers.TagManager
import com.artemis.managers.TeamManager
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.factory.CameraFactory
import gabek.sm2.systems.*

/**
 * @author Gabriel Keith
 */
fun buildWorld(kodein: Kodein): World{
    val config = WorldConfiguration()
    //built in
    config.setSystem(EntityLinkManager())
    config.setSystem(TagManager())
    config.setSystem(GroupManager())
    //config.setSystem(PlayerManager())
    //config.setSystem(TeamManager())

    //needs to be first
    config.setSystem(TranslationSystem())

    //movement
    config.setSystem(PlayerInputSystem())
    config.setSystem(CharacterControllerSystem())

    //tiles
    config.setSystem(TileMapSystem(kodein))

    //box2d
    config.setSystem(Box2dSystem())
    config.setSystem(CharacterLegsSystem())

    //graphics
    config.setSystem(CameraSystem())
    config.setSystem(RenderSystem())


    return World(config)
}