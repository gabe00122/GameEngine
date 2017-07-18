package gabek.engine.core.systems.graphics

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import gabek.engine.core.graphics.Display
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.PassiveSystem

/**
 * @author Gabriel Keith
 */
class Box2dDebugSystem : PassiveSystem(), RenderManager.DirectRenderSystem {
    private var render: Box2DDebugRenderer? = null
    private lateinit var b2dSystem: Box2dSystem

    var active: Boolean = false

    override fun render(ortho: OrthographicCamera, context: RenderContext) {
        if(active) {
            if (render == null) {
                render = Box2DDebugRenderer(true, true, false, true, false, true)
            }

            render?.render(b2dSystem.rworld.world, ortho.combined)
        }
    }

    override fun prepare(display: Display, ortho: OrthographicCamera) {}
    override fun processSystem() {}

    override fun dispose() {
        super.dispose()
        render?.dispose()
    }
}