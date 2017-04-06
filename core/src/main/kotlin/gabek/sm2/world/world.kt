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
import gabek.sm2.factory.*
import gabek.sm2.factory.enviroment.SpinnerFactory
import gabek.sm2.kryo.kryoSetup
import gabek.sm2.physics.RCollisionAdapter
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon
import gabek.sm2.systems.*
import gabek.sm2.systems.brains.WanderingBrainSystem
import gabek.sm2.systems.character.*
import gabek.sm2.systems.gamemodes.GameModeManager
import gabek.sm2.systems.graphics.*
import gabek.sm2.systems.pellet.BleedingSystem
import gabek.sm2.systems.pellet.PelletCollisionSystem
import gabek.sm2.systems.pellet.PelletLifeSpanSystem
import gabek.sm2.systems.pellet.PelletMovmentSystem
import gabek.sm2.tilemap.TileDefinitions
import gabek.sm2.tilemap.TileReference
import gabek.sm2.tilemap.TileType

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
    config.setSystem(FactoryManager.build(kodein, ::factoryBindings))
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
    config.setSystem(DamageManager())
    //config.setSystem(BleedingSystem())
    config.setSystem(PelletLifeSpanSystem())
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

    config.setSystem(TileRenderSystem())
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
                        getSystem<TileRenderSystem>(),
                        getSystem<SpriteRenderSystem>()
                        //getSystem<HealthRenderSystem>()
                ),
                orthoSystems = listOf(
                        //getSystem<Box2dDebugSystem>()
                )
        )
    }
}

fun factoryBindings(builder: FactoryManager.Builder) = with(builder) {
    bind("camera", CameraFactory())
    bind("player", PlayerFactory())
    bind("acid_monk", AcidMonkFactory())
    bind("junk", JunkFactory())
    bind("babySnail", BabySnailFactory())

    bind("spinner", SpinnerFactory())

    bind("blood", BloodDroplet())
}

fun buildTileDefinitions(definitions: TileDefinitions, world: World, kodein: Kodein){
    val assets: Assets = kodein.instance()
    val tileMap: TileMapSystem = world.getSystem()
    val damageManager: DamageManager = world.getSystem()


    definitions.addType(TileType("none", null, false))
    definitions.addType(TileType("background", assets.findTexture("tiles:back"), false))
    definitions.addType(TileType("wall", assets.findTexture("tiles:wall"), true))

    definitions.addType(object: TileType("spike", texture = assets.findTexture("tiles:spike")) {
        override fun onTileInit(x: Int, y: Int, reference: TileReference) {
            val shape = RPolygon(tileMap.tileSize, tileMap.tileSize / 2, x * tileMap.tileSize + tileMap.tileSize / 2, y * tileMap.tileSize + tileMap.tileSize / 4)
            val fixture = RFixture(shape, isSensor = true)
            fixture.callbackList.add(object : RCollisionAdapter() {
                override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
                    val other = otherRFixture.ownerId
                    if (otherRFixture.body!!.linearVelocityY < -2 && damageManager.hasHealth(other)) {
                        damageManager.kill(other)
                    }
                }
            })

            tileMap.body.addFixture(fixture)
        }
    })
}
