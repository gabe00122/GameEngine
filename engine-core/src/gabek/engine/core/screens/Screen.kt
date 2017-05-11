package gabek.engine.core.screens

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 */

abstract class Screen: Disposable {
    lateinit var manager: ScreenManager
        internal set
    val root: WidgetGroup = WidgetGroup()

    init {
        root.setFillParent(true)
    }

    open fun update(delta: Float) {}
    open fun render(batch: SpriteBatch) {}
    open fun resize(width: Int, height: Int) {}
    open fun show() {}
    open fun hide() {}
    override fun dispose() {}
}