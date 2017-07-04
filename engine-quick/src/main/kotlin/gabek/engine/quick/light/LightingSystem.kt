package gabek.engine.quick.light

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import gabek.engine.core.graphics.Display
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.systems.graphics.RenderManager

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class LightingSystem: PassiveSystem(), RenderManager.DirectRenderSystem {
    lateinit var b2d: Box2dSystem
    lateinit var rayHandler: RayHandler

    private var buffW = 1
    private var buffH = 1
    private var scale = 0.25f

    private lateinit var testLight: PointLight

    override fun initialize() {
        super.initialize()

        rayHandler = RayHandler(b2d.rworld.world, buffW, buffH) // yeah i know

        testLight = PointLight(rayHandler, 200, Color(0f, 0f, 0f, 1f), 10f, 10f, 10f)
    }

    override fun prepare(display: Display, ortho: OrthographicCamera){
        val newBuffW = (display.pixWidth * scale).toInt()
        val newBuffH = (display.pixHeight * scale).toInt()

        if(newBuffW != buffW || newBuffH != buffH){
            buffW = newBuffW; buffH = newBuffH
            rayHandler.resizeFBO(buffW, buffH)
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f)
        //game.getLightingHandler().setShadows(true);
        //game.getLightingHandler().setBlur(true);
        rayHandler.setCombinedMatrix(ortho)
        rayHandler.update()
        rayHandler.prepareRender()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    override fun render(ortho: OrthographicCamera, context: RenderContext) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        rayHandler.renderOnly()
    }

    override fun dispose() {
        super.dispose()

        rayHandler.dispose()

    }
}