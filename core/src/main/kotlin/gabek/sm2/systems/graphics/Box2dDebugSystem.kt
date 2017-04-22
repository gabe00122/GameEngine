package gabek.sm2.systems.graphics

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import gabek.sm2.systems.Box2dSystem
import gabek.sm2.systems.PassiveSystem
import gabek.sm2.graphics.RenderManager
import gabek.sm2.world.getSystem

/**
 * @author Gabriel Keith
 */
class Box2dDebugSystem : PassiveSystem(), RenderManager.OrthoRenderSystem {
    private var render: Box2DDebugRenderer? = null
    private lateinit var b2dSystem: Box2dSystem

    override fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float) {
        if(render == null){
            render = Box2DDebugRenderer(true, true, false, true, false, true)
        }

        render?.render(b2dSystem.box2dWorld, ortho.combined)
    }

    override fun processSystem() {}

    override fun dispose() {
        super.dispose()
        render?.dispose()
    }
}