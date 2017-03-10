package gabek.sm2.systems.graphics

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import gabek.sm2.systems.Box2dSystem
import gabek.sm2.world.RenderManager
import gabek.sm2.world.getSystem

/**
 * @author Gabriel Keith
 */
class Box2dDebugSystem : BaseSystem(), RenderManager.OrthoRenderSystem {
    private lateinit var render: Box2DDebugRenderer
    private lateinit var b2dWorld: World

    override fun initialize() {
        super.initialize()
        b2dWorld = world.getSystem<Box2dSystem>().box2dWorld
        render = Box2DDebugRenderer(true, true, false, true, false, true)
    }

    override fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float) {
        render.render(b2dWorld, ortho.combined)
    }

    override fun processSystem() {}

    override fun dispose() {
        super.dispose()
        render.dispose()
    }
}