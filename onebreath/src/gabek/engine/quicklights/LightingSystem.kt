package gabek.engine.quicklights

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import gabek.engine.core.graphics.Display
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.world.RenderManager

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class LightingSystem: PassiveSystem(), RenderManager.DirectRenderSystem {
    lateinit var b2d: Box2dSystem
    lateinit var rayHandler: RayHandler

    private var buffW = 1
    private var buffH = 1
    private var scale = 0.5f

    private lateinit var testLight: PointLight

    override fun initialize() {
        super.initialize()

        rayHandler = RayHandler(b2d.box2dWorld, buffW, buffH) // yeah i know

        testLight = PointLight(rayHandler, 120, Color(0f, 0f, 0f, 1f), 10f, 7f, 7f)
    }

    override fun prepare(display: Display, ortho: OrthographicCamera){
        val newBuffW = (display.width * scale).toInt()
        val newBuffH = (display.height * scale).toInt()

        if(newBuffW != buffW || newBuffH != buffH){
            buffW = newBuffW; buffH = newBuffH
            rayHandler.resizeFBO(buffW, buffH)
        }

        //Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f)
        //game.getLightingHandler().setShadows(true);
        //game.getLightingHandler().setBlur(true);
        rayHandler.setCombinedMatrix(ortho)
        rayHandler.update()
        rayHandler.prepareRender()
        Gdx.gl20.glDisable(GL20.GL_BLEND)
    }

    override fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float) {
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        rayHandler.renderOnly()
    }

    override fun dispose() {
        super.dispose()

        rayHandler.dispose()

    }
}