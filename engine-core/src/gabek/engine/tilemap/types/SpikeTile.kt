package gabek.engine.tilemap.types

import com.artemis.World
import com.badlogic.gdx.physics.box2d.Contact
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.physics.RCollisionAdapter
import gabek.engine.physics.RFixture
import gabek.engine.physics.RPolygon
import gabek.engine.systems.TileMapSystem
import gabek.engine.systems.character.DamageSystem
import gabek.engine.tilemap.TileReference
import gabek.engine.tilemap.TileType

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */
class SpikeTile(world: World, kodein: Kodein) : TileType("spike", texture = kodein.instance<Assets>().findTexture("tiles:spike")) {
    val tileMap = world.getSystem(TileMapSystem::class.java)!!
    val damageSystem = world.getSystem(DamageSystem::class.java)!!

    override fun onTileInit(x: Int, y: Int, reference: TileReference) {
        val shape = RPolygon(tileMap.tileSize, tileMap.tileSize / 2, x * tileMap.tileSize + tileMap.tileSize / 2, y * tileMap.tileSize + tileMap.tileSize / 4)
        val fixture = RFixture(shape, isSensor = true)
        fixture.callbackList.add(object : RCollisionAdapter() {
            override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
                val other = otherRFixture.ownerId
                if (otherRFixture.body!!.linearVelocityY < -2 && damageSystem.hasHealth(other)) {
                    damageSystem.kill(other)
                }
            }
        })

        tileMap.body.addFixture(fixture)
    }
}