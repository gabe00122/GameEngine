package gabek.sm2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.assets.Assets
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.ui.MenuControl
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class MainMenu(val kodein: Kodein) : Screen() {
    var timer = 0f

    val title: VisImage

    init {
        val window = VisWindow("Main Menu", "default")
        window.isMovable = false
        window.isResizable = false

        val titleImage = kodein.instance<Assets>().retrieveRegion("menus:title")
        title = VisImage(titleImage)
        title.setScale(2.5f)
        title.originX = title.width / 2f
        title.originY = title.height / 2f

        val startBut = VisTextButton("Start", "toggle")
        startBut.onChange { inputEvent, visTextButton ->
            manager.show("startGame")
        }

        val settingsBut = VisTextButton("Settings", "toggle")
        settingsBut.onChange { changeEvent, visTextButton ->
            manager.show("settings")
        }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange { inputEvent, visTextButton ->
            Gdx.app.exit()
        }
        val menu = MenuControl(startBut, settingsBut, quitBut)
        menu.playerInput = kodein.instance<PlayerInputManager>()
        window.add(menu).fill()

        val table = VisTable()
        table.setFillParent(true)

        table.add(title).pad(10f).row()
        table.add(window)

        root.addActor(table)
    }

    override fun update(delta: Float) {
        super.update(delta)

        timer += delta
        title.originY = title.height / 2 - MathUtils.sin(timer) * 10f - 10f
    }
}