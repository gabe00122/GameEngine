package gabek.engine.physics

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef

/**
 * @author Gabriel Keith
 */
class RFixture {
    @Transient private var fixture: Fixture? = null
    @Transient internal var body: RBody? = null
    var shape: RShape? = null

    var ownerId: Int = -1
    var isTile: Boolean = false
    var tileX: Int = 0
    var tileY: Int = 0

    var callbackList = mutableListOf<RCollisionCallback>()

    constructor()

    constructor(shape: RShape, density: Float = 0f, friction: Float = 0f, restitution: Float = 0f, isSensor: Boolean = false,
                categoryBits: Short = 1, maskBits: Short = -1, groupIndex: Short = 0) {
        this.shape = shape
        this.density = density
        this.friction = friction
        this.restitution = restitution
        this.isSensor = isSensor

        this.categoryBits = categoryBits
        this.maskBits = maskBits
        this.groupIndex = groupIndex
    }

    var density: Float = 0f
        get() = fixture?.density ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.density = value
            else
                field = value
        }

    var friction: Float = 0f
        get() = fixture?.friction ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.friction = value
            else
                field = value
        }

    var restitution: Float = 0f
        get() = fixture?.restitution ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.restitution = value
            else
                field = value
        }

    var isSensor: Boolean = false
        get() = fixture?.isSensor ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.isSensor = value
            else
                field = value
        }

    var categoryBits: Short = 1
        get() = fixture?.filterData?.categoryBits ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.filterData.categoryBits = value
            else
                field = value
        }

    var maskBits: Short = -1
        get() = fixture?.filterData?.maskBits ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.filterData.maskBits = value
            else
                field = value
        }

    var groupIndex: Short = 0
        get() = fixture?.filterData?.groupIndex ?: field
        set(value) {
            val fixture = fixture
            if (fixture != null)
                fixture.filterData.groupIndex = value
            else
                field = value
        }

    val isInitialised: Boolean
        get() = fixture != null

    internal fun initialise(parent: Body) {
        val shape = shape ?: throw IllegalStateException("Shape can't be null.")

        shape.preInit()
        val def = fixtureDef
        def.shape = shape.shape!!

        fixture = parent.createFixture(def)
        fixture!!.userData = this
        shape.postInit()
    }

    fun store(body: Body?) {
        val fixture = this.fixture
        this.fixture = null
        if (fixture != null && body != null) {
            body.destroyFixture(fixture)
        }
    }

    fun clone(): RFixture {
        val out = RFixture()

        out.shape = shape?.clone()
        out.density = density
        out.friction = friction
        out.restitution = restitution
        out.isSensor = isSensor
        out.categoryBits = categoryBits
        out.maskBits = maskBits
        out.groupIndex = groupIndex

        return out
    }

    private val fixtureDef: FixtureDef get() {
        val fixtureDef = TEMP_FIXTURE_DEF

        fixtureDef.density = density
        fixtureDef.friction = friction
        fixtureDef.restitution = restitution
        fixtureDef.isSensor = isSensor
        fixtureDef.filter.categoryBits = categoryBits
        fixtureDef.filter.maskBits = maskBits
        fixtureDef.filter.groupIndex = groupIndex

        return fixtureDef
    }
}