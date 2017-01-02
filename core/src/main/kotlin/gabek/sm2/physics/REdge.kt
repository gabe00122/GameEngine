package gabek.sm2.physics

import com.badlogic.gdx.physics.box2d.EdgeShape

/**
 * @author Gabriel Keith
 */
class REdge : RShape {
    val vertices = FloatArray(8)
    var hasVertex0: Boolean = false
    var hasVertex3: Boolean = false

    constructor() : super()

    fun set(v1X: Float, v1Y: Float, v2X: Float, v2Y: Float) {
        vertices[2] = v1X
        vertices[3] = v1Y
        vertices[4] = v2X
        vertices[5] = v2Y
    }

    fun setVertex0(x: Float, y: Float) {
        vertices[0] = x
        vertices[1] = y
        hasVertex0 = true
    }

    fun setVertex3(x: Float, y: Float) {
        vertices[6] = x
        vertices[7] = y
        hasVertex3 = true
    }

    override fun preInit() {
        val edgeShape = EdgeShape()
        edgeShape.setVertex0(vertices[0], vertices[1])
        edgeShape.set(vertices[2], vertices[3], vertices[4], vertices[5])
        edgeShape.setVertex3(vertices[6], vertices[7])
        edgeShape.setHasVertex0(hasVertex0)
        edgeShape.setHasVertex3(hasVertex3)
        shape = edgeShape
    }
}