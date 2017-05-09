package gabek.engine.console

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.*
import gabek.engine.screens.Screen
import ktx.actors.onKeyDown

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */
class ConsoleGuiOverlay(kodein: Kodein) : Screen(), Console.ConsoleListener {
    val console: Console = kodein.instance()

    val outputGroup = VerticalGroup()
    val outputScroll = VisScrollPane(outputGroup)
    val inputField = VisTextField()
    val splitPane: VisSplitPane

    private val temp = Vector2()

    init {
        console.outputProcessor = this
        console.initializeCommands()

        outputGroup.align(Align.bottomLeft)
        outputGroup.grow()

        outputScroll.setScrollingDisabled(true, false)
        outputScroll.setScrollbarsOnTop(true)
        outputScroll.setFadeScrollBars(false)

        outputScroll.style.background = VisUI.getSkin().getDrawable("window-bg")
        //outputGroup.isDisabled = true

        inputField.onKeyDown { event, actor, keyCode ->
            if (keyCode == Input.Keys.ENTER) {
                if (!inputField.isEmpty) {
                    console.processInput(inputField.text)
                    inputField.text = ""
                }
            }
        }

        val topPane = VisTable()
        topPane.add(outputScroll).grow()
        topPane.row()
        topPane.add(inputField).bottom().growX()

        splitPane = VisSplitPane(topPane, null, true)
        splitPane.setFillParent(true)

        root.addActor(splitPane)
        root.touchable = Touchable.childrenOnly
        root.setColor(1f, 1f, 1f, 0.9f)
        root.isVisible = false
    }

    override fun write(message: String) {
        val label = VisLabel(message)
        label.setAlignment(Align.left)
        outputGroup.addActor(label)

        if (outputScroll.scrollPercentY == 1f || outputScroll.scrollPercentY == Float.NaN) {
            outputScroll.layout()
            outputScroll.scrollPercentY = 1f
        }
    }

    override fun update(delta: Float) {
        if (Gdx.input.isKeyJustPressed(68)) {
            root.isVisible = !root.isVisible
            if (root.stage.keyboardFocus == inputField) {
                root.stage.keyboardFocus = null
                inputField.text = ""
            }
        }

        if (root.isVisible) {
            temp.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            temp.set(splitPane.stage.screenToStageCoordinates(temp))
            if (temp.y < splitPane.height - splitPane.split * splitPane.height) {
                splitPane.touchable = Touchable.disabled
            } else {
                splitPane.touchable = Touchable.enabled
            }
        }
    }
}