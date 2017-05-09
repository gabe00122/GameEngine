package gabek.engine.systems.graphics

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import gabek.engine.systems.Box2dSystem
import gabek.engine.systems.PassiveSystem
import gabek.engine.world.RenderManager

/**
 * @author Gabriel Keith
 */
class Box2dDebugSystem : PassiveSystem(), RenderManager.OrthoRenderSystem {
    private var render: Box2DDebugRenderer? = null
    private lateinit var b2dSystem: Box2dSystem

    var active: Boolean = false

    override fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float) {
        if(active) {
            if (render == null) {
                render = Box2DDebugRenderer(true, true, false, true, false, true)
            }

            render?.render(b2dSystem.box2dWorld, ortho.combined)
        }
    }

    override fun processSystem() {}

    override fun dispose() {
        super.dispose()
        render?.dispose()
    }
}