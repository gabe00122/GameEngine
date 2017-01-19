package gabek.sm2.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import gabek.sm2.graphics.DisplayBuffer

/**
 * @author Gabriel Keith
 */
class RenderSystem: BaseSystem(){
    private lateinit var cameraSystem: CameraSystem
    private lateinit var spriteRenderSystem: SpriteRenderSystem
    private lateinit var tileMapSystem: TileMapSystem

    private lateinit var healthRenderSystem: HealthRenderSystem

    private lateinit var box2dSystem: Box2dSystem
    private var box2dDebugRender: Box2DDebugRenderer? = null

    private val culling = Rectangle()

    override fun processSystem() {}

    override fun initialize() {
        super.initialize()
        //box2dDebugRender = Box2DDebugRenderer()
    }

    fun render(buffer: DisplayBuffer, batch: SpriteBatch, progress: Float){
        val ortho = cameraSystem.updateProjection(buffer.cameraHandle, progress, buffer.width, buffer.height)
        updateCulling(ortho)

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        buffer.beginPrimaryBuffer()
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        batch.begin()

        tileMapSystem.tileMap.render(batch, culling)
        spriteRenderSystem.render(batch, culling, progress)

        healthRenderSystem.render(batch, culling, progress)

        batch.end()
        box2dDebugRender?.render(box2dSystem.box2dWorld, ortho.combined)

        buffer.endPrimaryBuffer()
    }

    private fun updateCulling(ortho: OrthographicCamera){
        culling.set(ortho.position.x - ortho.viewportWidth / 2f,
                ortho.position.y - ortho.viewportHeight / 2f,
                ortho.viewportWidth, ortho.viewportHeight)
    }

    override fun dispose() {
        super.dispose()
        box2dDebugRender?.dispose()
    }
}