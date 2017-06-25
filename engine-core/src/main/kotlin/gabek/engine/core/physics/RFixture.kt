package gabek.engine.core.physics

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import gabek.engine.core.physics.shape.RShape

/**
 * @author Gabriel Keith
 */
class RFixture {
    @Transient private var fixture: Fixture? = null
    @Transient var body: RBody? = null
    var shape: RShape? = null

    var ownerId: Int = -1
    var isTile: Boolean = false
    var tileX: Int = 0
    var tileY: Int = 0

    @Transient
    var callbackList = mutableListOf<RCollisionCallback>()

    constructor()

    constructor(shape: RShape, density: Float = 0f, friction: Float = 0f, restitution: Float = 0f, isSensor: Boolean = false,
                categoryBits: Short = 1, maskBits: Short = -1, groupIndex: Short = 0) {
        this.shape = shape
        this.density = density
        this.friction = friction
        this.restitution = restitution
        this.isSensor = isSensor

        filter.categoryBits = categoryBits
        filter.maskBits = maskBits
        filter.groupIndex = groupIndex
    }

    var density: Float = 0f
        set(value) {
            field = value
            fixture?.density = value
        }

    var friction: Float = 0f
        set(value) {
            field = value
            fixture?.friction = value
        }

    var restitution: Float = 0f
        set(value) {
            field = value
            fixture?.restitution = value
        }

    var isSensor: Boolean = false
        set(value) {
            field = value
            fixture?.isSensor = value
        }

    val filter = Filter()

    fun setFilter(f: Filter){
        filter.categoryBits = f.categoryBits
        filter.maskBits = f.maskBits
        filter.groupIndex = f.groupIndex

        fixture?.filterData = filter
    }

    fun setFilter(categoryBits: Short, maskBits: Short, groupIndex: Short){
        filter.categoryBits = categoryBits
        filter.maskBits = maskBits
        filter.groupIndex = groupIndex

        fixture?.filterData = filter
    }

    val isInitialised: Boolean
        get() = fixture != null

    internal fun initialise(parent: Body) {
        val shape = shape ?: throw IllegalStateException("Shape can't be null.")

        val shapeGen = shape.generate()
        val def = fixtureDef
        def.shape = shapeGen

        val fixture = parent.createFixture(def)
        fixture.userData = this
        this.fixture = fixture

        shapeGen.dispose()
    }

    fun store() {
        val fixture = fixture
        val body = body?.body

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
        out.setFilter(filter)

        return out
    }

    private val fixtureDef: FixtureDef get() {
        val fixtureDef = TEMP_FIXTURE_DEF

        fixtureDef.density = density
        fixtureDef.friction = friction
        fixtureDef.restitution = restitution
        fixtureDef.isSensor = isSensor
        fixtureDef.filter.categoryBits = filter.categoryBits
        fixtureDef.filter.maskBits = filter.maskBits
        fixtureDef.filter.groupIndex = filter.groupIndex

        return fixtureDef
    }
}