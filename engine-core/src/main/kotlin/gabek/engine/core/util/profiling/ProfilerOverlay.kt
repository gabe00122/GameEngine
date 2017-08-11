package gabek.engine.core.util.profiling

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel

/**
 * @author Gabriel Keith
 */

class ProfilerOverlay(val profiler: Profiler) : Container<VisLabel>(VisLabel("")), Profiler.Listener {

    init {
        setFillParent(true)
        align(Align.topRight)

        profiler.listener = this
    }

    override fun onReset() {
        with(actor.text) {
            setLength(0)

            append("max: ")
            append(formateMS(profiler.max))
            appendln()

            append("min: ")
            append(formateMS(profiler.min))
            appendln()

            append("mean: ")
            append(formateMS(profiler.mean))
            appendln()
        }

        actor.invalidateHierarchy()
    }

    private fun formateMS(s: Float): Float = s * 1000
}