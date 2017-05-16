package gabek.engine.core.physics

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import gabek.engine.core.physics.joint.RJoint
import gabek.engine.core.physics.shape.RShape
import gabek.engine.core.util.Mirrorable

/**
 * @author Gabriel Keith
 */

class RBody: Mirrorable<RBody> {
    @Transient internal var body: Body? = null
    internal val joints = ArrayList<RJoint>()
    private val _fixutres = ArrayList<RFixture>()
    val fixutres: List<RFixture> get() = _fixutres

    var x: Float = 0f
        get() = body?.position?.x ?: field
        set(value) {
            val body = body
            if (body != null)
                body.setTransform(value, body.position.y, body.angle)
            else
                field = value
        }

    var y: Float = 0f
        get() = body?.position?.y ?: field
        set(value) {
            val body = body
            if (body != null)
                body.setTransform(body.position.x, value, body.angle)
            else
                field = value
        }

    var rotationRad: Float = 0f
        get() = body?.angle ?: field
        set(value) {
            val body = body
            if (body != null)
                body.setTransform(body.position, value)
            else
                field = value
        }

    var rotation: Float
        get() = rotationRad * MathUtils.radDeg
        set(value) {
            rotationRad = value * MathUtils.degRad
        }

    var angularDamping: Float = 0f
        get() = body?.angularDamping ?: field
        set(value) {
            val body = body
            if (body != null)
                body.angularDamping = value
            else
                field = value
        }

    var angularVelocity: Float = 0f
        get() = body?.angularVelocity ?: field
        set(value) {
            val body = body
            if (body != null)
                body.angularVelocity = value
            else
                field = value
        }

    var linearDamping: Float = 0f
        get() = body?.linearDamping ?: field
        set(value) {
            val body = body
            if (body != null)
                body.linearDamping = value
            else
                field = value
        }

    var linearVelocityX: Float = 0f
        get() = body?.linearVelocity?.x ?: field
        set(value) {
            val body = body
            if (body != null)
                body.setLinearVelocity(value, body.linearVelocity.y)
            else
                field = value
        }

    var linearVelocityY: Float = 0f
        get() = body?.linearVelocity?.y ?: field
        set(value) {
            val body = body
            if (body != null)
                body.setLinearVelocity(body.linearVelocity.x, value)
            else
                field = value
        }

    var gravityScale: Float = 1f
        get() = body?.gravityScale ?: field
        set(value) {
            val body = body
            if (body != null)
                body.gravityScale = value
            else
                field = value
        }

    var isActive: Boolean = true
        get() = body?.isActive ?: field
        set(value) {
            val body = body
            if (body != null)
                body.isActive = value
            else
                field = value
        }

    var isAwake: Boolean = true
        get() = body?.isAwake ?: field
        set(value) {
            val body = body
            if (body != null)
                body.isAwake = value
            else
                field = value
        }

    var isSleepingAllowed: Boolean = true
        get() = body?.isSleepingAllowed ?: field
        set(value) {
            val body = body
            if (body != null)
                body.isSleepingAllowed = value
            else
                field = value
        }

    var isBullet: Boolean = false
        get() = body?.isBullet ?: field
        set(value) {
            val body = body
            if (body != null)
                body.isBullet = value
            else
                field = value
        }

    var isFixedRotation: Boolean = false
        get() = body?.isFixedRotation ?: field
        set(value) {
            val body = body
            if (body != null)
                body.isFixedRotation = value
            else
                field = value
        }

    var bodyType: BodyDef.BodyType = BodyDef.BodyType.StaticBody
        get() = body?.type ?: field
        set(value) {
            val body = body
            if (body != null) {
                body.type = value
            } else {
                field = value
            }
        }

    val mass: Float get() = body?.mass ?: -1f

    fun applyForceToCenter(forceX: Float, forceY: Float, wakeBody: Boolean = true) {
        body?.applyForceToCenter(forceX, forceY, wakeBody)
    }

    fun applyLinearImpulse(impulseX: Float, impulseY: Float, pointX: Float, pointY: Float, wake: Boolean = true) {
        body?.applyLinearImpulse(impulseX, impulseY, pointX, pointY, wake)
    }

    fun setPosition(x: Float, y: Float) {
        val body = body
        if (body != null) {
            body.setTransform(x, y, body.angle)
        } else {
            this.x = x
            this.y = y
        }
    }

    val isInitialised: Boolean
        get() = body != null

    fun addCallbackToAll(callback: RCollisionCallback) {
        for (fixture in _fixutres) {
            fixture.callbackList.add(callback)
        }
    }

    fun getLocalVector(worldVector: Vector2): Vector2 {
        return body!!.getLocalVector(worldVector)
    }

    fun addFixture(fixture: RFixture) {
        _fixutres.add(fixture)
        fixture.body = this

        val body = body
        if (body != null) {
            fixture.initialise(body)
        }
    }

    fun addFixture(shape: RShape, density: Float = 0f, friction: Float = 0f, restitution: Float = 0f, isSensor: Boolean = false,
                   categoryBits: Short = 1, maskBits: Short = -1, groupIndex: Short = 0) {
        addFixture(RFixture(shape, density, friction, restitution, isSensor, categoryBits, maskBits, groupIndex))
    }

    fun initialise(box2dWorld: World) {
        val body = box2dWorld.createBody(bodyDef)
        for (fixture in _fixutres) {
            fixture.initialise(body)
        }
        this.body = body

        for (joint in joints) {
            if (joint.canInit) {
                joint.initialise(box2dWorld)
            }
        }
    }

    fun initialise(box2dWorld: World, ownerId: Int) {
        for (fixture in _fixutres) {
            fixture.ownerId = ownerId
        }

        initialise(box2dWorld)
    }

    fun store(box2dWorld: World) {
        for (joint in joints) {
            if (joint.isInitialised) {
                joint.store(box2dWorld)
            }
        }

        val body = body
        if (body != null) {
            box2dWorld.destroyBody(body)
            this.body = null
        }

        for (fixture in fixutres) {
            fixture.store(null)
        }
    }

    private fun clearFixtures() {
        val body = body

        if (body != null) {
            for (fixture in _fixutres) {
                fixture.store(body)
            }
        }

        _fixutres.clear()
    }

    override fun set(other: RBody) {
        clearFixtures()
        other._fixutres.forEach { addFixture(it.clone()) }

        x = other.x
        y = other.y
        rotationRad = other.rotationRad

        angularDamping = other.angularDamping
        angularVelocity = other.angularVelocity

        linearDamping = other.linearDamping
        linearVelocityX = other.linearVelocityX
        linearVelocityY = other.linearVelocityY

        gravityScale = other.gravityScale

        isActive = other.isActive
        isAwake = other.isAwake
        isSleepingAllowed = other.isSleepingAllowed
        isBullet = other.isBullet
        isFixedRotation = other.isFixedRotation
        bodyType = other.bodyType
    }

    fun reset() {
        clearFixtures()

        x = 0f
        y = 0f
        rotationRad = 0f

        angularDamping = 0f
        angularVelocity = 0f

        linearDamping = 0f
        linearVelocityX = 0f
        linearVelocityY = 0f

        gravityScale = 1f

        isActive = true
        isAwake = true
        isSleepingAllowed = true
        isBullet = false
        isFixedRotation = false
        bodyType = BodyDef.BodyType.StaticBody
    }

    private val bodyDef: BodyDef get() {
        val def = TEMP_BODY_DEF

        def.position.x = x
        def.position.y = y
        def.angle = rotationRad

        def.angularDamping = angularDamping
        def.angularVelocity = angularVelocity

        def.linearDamping = linearDamping
        def.linearVelocity.set(linearVelocityX, linearVelocityY)

        def.gravityScale = gravityScale

        def.active = isActive
        def.awake = isAwake
        def.allowSleep = isSleepingAllowed
        def.bullet = isBullet
        def.fixedRotation = isFixedRotation
        def.type = bodyType

        return def
    }
}