package gabek.sm2.physics

import com.badlogic.gdx.physics.box2d.PolygonShape


class RPolygon : RShape {
    var vertices: FloatArray = FloatArray(0)

    constructor() : super()

    constructor(w: Float, h: Float) : super() {
        setAsBox(0f, 0f, w, h)
    }

    constructor(x: Float, y: Float, w: Float, h: Float) : super() {
        setAsBox(x, y, w, h)
    }

    override fun preInit() {
        val polyShape = PolygonShape()
        polyShape.set(vertices)
        shape = polyShape
    }

    fun setAsBox(x: Float, y: Float, w: Float, h: Float) {
        val hw = w / 2
        val hh = h / 2

        vertices = floatArrayOf(
                -hw + x, hh + y,
                -hw + x, -hh + y,
                hw + x, -hh + y,
                hw + x, hh + y
        )
    }
}