package gabek.engine.core.tilemap.types

import com.artemis.World
import com.badlogic.gdx.physics.box2d.Contact
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.physics.RCollisionAdapter
import gabek.engine.core.physics.RFixture
import gabek.engine.core.physics.RPolygon
import gabek.engine.core.systems.TileMapSystem
import gabek.engine.core.systems.character.DamageSystem
import gabek.engine.core.tilemap.TileReference
import gabek.engine.core.tilemap.TileType

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