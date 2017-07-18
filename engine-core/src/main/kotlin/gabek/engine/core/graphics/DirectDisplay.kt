package gabek.engine.core.graphics

import com.badlogic.gdx.Gdx

/**
 * @author Gabriel Keith
 * @date 7/16/2017
 */

class DirectDisplay: Display(){
    override fun layout() {
        super.layout()

        pixWidth = Gdx.graphics.width
        pixHeight = Gdx.graphics.height
    }


    override fun begin() {}
    override fun end() {}
    override fun dispose() {}
}