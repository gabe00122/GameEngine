package gabek.engine.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape

/**
 * @author Gabriel Keith
 */
class RCircle : RShape {
    var x = 0f
    var y = 0f
    var radius: Float = 0f

    constructor() : super()

    constructor(radius: Float) {
        this.radius = radius
    }

    constructor(x: Float, y: Float, radius: Float) {
        this.x = x
        this.y = y
        this.radius = radius
    }

    override fun clone(): RShape {
        val out = RCircle()

        out.x = x
        out.y = y
        out.radius = radius

        return out
    }

    override fun preInit() {
        val circleShape = CircleShape()
        circleShape.radius = radius
        circleShape.position = Vector2(x, y)
        shape = circleShape
    }
}