package gabek.engine.quick.water

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.gushikustudios.box2d.controllers.B2BuoyancyController
import gabek.engine.core.components.BodyCom
import gabek.engine.core.systems.Box2dSystem

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class BuoyancySystem: BaseEntitySystem(Aspect.all(BodyCom::class.java, BuoyantCom::class.java)){
    private val controller = B2BuoyancyController(
            Vector2(0f, 1f),
            Vector2.Zero,
            Vector2(0f, -9.807f),
            5.25f, 1f, 3f, 3f/2.5f)
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var buoyancyMapper: ComponentMapper<BuoyantCom>

    var waterLevel: Float
        get() = controller.mSurfaceHeight
        set(value) {
            controller.mSurfaceHeight = value
        }

    override fun initialize() {
        super.initialize()

        box2dSystem.onBodyInit = this::bodyInit
        box2dSystem.onBodyStore = this::bodyStore
    }

    fun bodyInit(entityId: Int, body: Body) {
        if(buoyancyMapper.has(entityId)) {
            controller.addBody(body)
        }
    }

    fun bodyStore(entityId: Int, body: Body) {
        if(buoyancyMapper.has(entityId)){
            controller.addBody(body)
        }
    }

    override fun processSystem() {
        controller.step(world.delta)
    }

}