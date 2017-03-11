package gabek.sm2.tilemap

import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class TileReference(val typeId: Int) {
    val fixtures = mutableListOf<RFixture>()
}