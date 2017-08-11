package gabek.engine.core.physics

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import gabek.engine.core.physics.joint.RJoint
import gabek.engine.core.physics.shape.RShape
import gabek.engine.core.util.entity.Mirrorable

/**
 * @author Gabriel Keith
 */

class RBody: Mirrorable<RBody> {
    @Transient internal var body: Body? = null
    internal var world: RWorld? = null

    internal val joints = ArrayList<RJoint>()
    internal var listIndex = -1

    private val _fixutres = ArrayList<RFixture>()
    val fixutres: List<RFixture> get() = _fixutres

    val position = Vector2()

    fun setPosition(x: Float, y: Float) {
        position.set(x, y)
        body?.setTransform(position, rotationRad)
    }

    fun setPosition(pos: Vector2) {
        position.set(pos)
        body?.setTransform(position, rotationRad)
    }

    var rotationRad: Float = 0f
        set(value) {
            field = value
            body?.setTransform(position, value)
        }

    var rotation: Float
        get() = rotationRad * MathUtils.radDeg
        set(value) {
            rotationRad = value * MathUtils.degRad
        }

    var angularDamping: Float = 0f
        set(value) {
            field = value
            body?.angularDamping = value
        }

    var angularVelocity: Float = 0f
        set(value) {
            field = value
            body?.angularVelocity = value
        }

    var linearDamping: Float = 0f
        set(value) {
            field = value
            body?.linearDamping = value
        }

    val linearVelocity = Vector2()

    fun setLinearVelocity(vX: Float, vY: Float){
        linearVelocity.set(vX, vY)
        body?.linearVelocity = linearVelocity
    }

    fun setLinearVelocity(v: Vector2){
        linearVelocity.set(v)
        body?.linearVelocity = linearVelocity
    }

    var gravityScale: Float = 1f
        set(value) {
            field = value
            body?.gravityScale = value
        }

    var isActive: Boolean = true
        set(value) {
            field = value
            body?.isActive = value
        }

    var isAwake: Boolean = true
        set(value) {
            field = value
            body?.isAwake = value
        }

    var isSleepingAllowed: Boolean = true
        set(value) {
            field = value
            body?.isSleepingAllowed = value
        }

    var isBullet: Boolean = false
        set(value) {
            field = value
            body?.isBullet = value
        }

    var isFixedRotation: Boolean = false
        set(value) {
            field = value
            body?.isFixedRotation = value
        }

    var bodyType: BodyDef.BodyType = BodyDef.BodyType.StaticBody
        set(value) {
            field = value
            body?.type = value
        }

    var mass: Float = 0f
        private set

    private var massDirty = false

    fun applyForceToCenter(forceX: Float, forceY: Float, wakeBody: Boolean = true) {
        body?.applyForceToCenter(forceX, forceY, wakeBody)
    }

    fun applyLinearImpulse(impulseX: Float, impulseY: Float, pointX: Float, pointY: Float, wake: Boolean = true) {
        body?.applyLinearImpulse(impulseX, impulseY, pointX, pointY, wake)
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
            massDirty = true
        }
    }

    fun addFixture(shape: RShape, density: Float = 0f, friction: Float = 0f, restitution: Float = 0f, isSensor: Boolean = false,
                   categoryBits: Short = 1, maskBits: Short = -1, groupIndex: Short = 0) {
        addFixture(RFixture(shape, density, friction, restitution, isSensor, categoryBits, maskBits, groupIndex))
    }

    internal fun initialise(box2dWorld: RWorld) {
        world = box2dWorld
        val body = box2dWorld.createBody(bodyDef)
        this.body = body
        for (fixture in _fixutres) {
            fixture.initialise(body)
        }

        for (joint in joints) {
            if (joint.canInit) {
                joint.initialise()
            }
        }
    }

    fun store() {
        for (joint in joints) {
            if (joint.isInitialised) {
                joint.store()
                joint.world = null
            }
        }

        val body = body
        this.body = null
        if (body != null) {
            world?.destroyBody(body)
        }

        for (fixture in fixutres) {
            fixture.store()
        }

        world = null
    }

    private fun clearFixtures() {
        val body = body

        if (body != null) {
            for (fixture in _fixutres) {
                fixture.store()
            }
        }

        _fixutres.clear()
    }

    internal fun update() {
        val body = body
        if(body != null) {
            isAwake = body.isAwake

            position.set(body.position)
            rotationRad = body.angle

            if(isAwake) {
                linearVelocity.set(body.linearVelocity)
                angularVelocity = body.angularVelocity
            }

            if(massDirty) {
                massDirty = true
                mass = body.mass
            }
        }
    }

    override fun set(other: RBody) {
        clearFixtures()
        other._fixutres.forEach { addFixture(it.clone()) }

        setPosition(other.position)
        rotationRad = other.rotationRad

        angularDamping = other.angularDamping
        angularVelocity = other.angularVelocity

        linearDamping = other.linearDamping
        setLinearVelocity(other.linearVelocity)

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

        setPosition(0f, 0f)
        rotationRad = 0f

        angularDamping = 0f
        angularVelocity = 0f

        linearDamping = 0f
        setLinearVelocity(0f, 0f)

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

        def.position.set(position)
        def.angle = rotationRad

        def.angularDamping = angularDamping
        def.angularVelocity = angularVelocity

        def.linearDamping = linearDamping
        def.linearVelocity.set(linearVelocity)

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