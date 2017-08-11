package gabek.engine.core.graphics

import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 * @date 7/16/2017
 */

abstract class Display: Widget(), Disposable{
    var cameraTag: String = "camera"

    var pixWidth: Int = 0
        protected set

    var pixHeight: Int = 0
        protected set

    abstract fun begin()
    abstract fun end()

    inline fun render(block: () -> Unit){
        begin()
        block()
        end()
    }
}
