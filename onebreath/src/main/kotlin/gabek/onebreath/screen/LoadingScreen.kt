package gabek.onebreath.screen

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import gabek.engine.core.assets.Assets
import gabek.engine.core.console.ConsoleGuiOverlay
import gabek.engine.core.screen.Screen
import ktx.log.logger

/**
 * @author Gabriel Keith
 * @date 6/23/2017
 */
class LoadingScreen(private val kodein: Kodein): Screen() {
    companion object {
        private val log = logger<LoadingScreen>()
    }

    val assets: Assets = kodein.instance()

    private val loadingText = "Loading"

    private val dotRate = 0.5f
    private val maxDot = 3

    private val label = VisLabel("")

    var time: Float = 0f
    var dot: Int = 0

    init {
        val table = VisTable()
        label.setFontScale(4f)
        table.add(label)

        updateText()

        table.setFillParent(true)
        root.addActor(table)
    }

    override fun update(delta: Float) {
        super.update(delta)

        if(assets.update()){
            log.info { "Finished Loading Assets" }
            manager.show("GameViewScreen")
            manager.overlay = ConsoleGuiOverlay(kodein)
        }

        time += delta
        while(time > dotRate) {
            time -= dotRate
            if(dot >= maxDot) {
                dot = 0
            } else {
                dot++
            }
            updateText()
        }
    }

    private fun updateText(){
        val text = StringBuilder(loadingText.length + maxDot)
        text.append(loadingText)
        for(i in 0 until maxDot){
            if(i < dot){
                text.append('.')
            } else {
                text.append(' ')
            }
        }

        label.setText(text)
    }
}