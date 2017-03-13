package gabek.sm2.physics

import com.badlogic.gdx.physics.box2d.PolygonShape


class RPolygon : RShape {
    var vertices: FloatArray = FloatArray(0)

    constructor() : super()

    constructor(w: Float, h: Float, x: Float = 0f, y: Float = 0f) : super() {
        setAsBox(w, h, x, y)
    }

    override fun preInit() {
        val polyShape = PolygonShape()
        polyShape.set(vertices)
        shape = polyShape
    }

    fun setAsBox(w: Float, h: Float, x: Float, y: Float) {
        val hw = w / 2
        val hh = h / 2

        vertices = floatArrayOf(
                -hw + x, hh + y,
                -hw + x, -hh + y,
                hw + x, -hh + y,
                hw + x, hh + y
        )
    }

    fun withClippedCorners(w: Float, h: Float, x: Float, y: Float, clip: Float) {
        val hw = w / 2
        val hh = h / 2

        vertices = floatArrayOf(
                -hw + x, hh + y,
                -hw + x + clip, -hh + y,
                hw + x - clip, -hh + y,
                hw + x, hh + y
        )
    }
}