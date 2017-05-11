package gabek.engine.core.input

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.PovDirection
import com.badlogic.gdx.math.Vector3

/**
 * @author Gabriel Keith
 */
class ControllerPlayerInput : PlayerInput(), ControllerListener {
    private val size = Actions.SIZE

    private val buttonBindings = mutableListOf<Int>()
    private val axisBindings = mutableListOf<AxisBinding?>()

    private val currentState = BooleanArray(size)
    private val currentAxisState = FloatArray(size)

    init {
        //bindButton(12, Actions.UP)
        //bindButton(14, Actions.DOWN)
        //bindButton(15, Actions.LEFT)
        //bindButton(13, Actions.RIGHT)

        bindAxis(1, true, 0.5f, Actions.UP)
        bindAxis(1, false, 0.5f, Actions.DOWN)
        bindAxis(0, true, 0.5f, Actions.LEFT)
        bindAxis(0, false, 0.5f, Actions.RIGHT)

        bindButton(1, Actions.SELECT)
        bindButton(9, Actions.ESCAPE)
    }

    override fun pollAction(actionId: Int): Boolean {
        return currentState[actionId]
    }

    fun bindButton(buttonCode: Int, actionId: Int) {
        while (buttonBindings.size - 1 <= buttonCode) {
            buttonBindings.add(-1)
        }

        buttonBindings[buttonCode] = actionId
    }

    fun bindAxis(axisCode: Int, invert: Boolean, threshold: Float, actionId: Int) {
        val newCode = axisCode * 2 + if (invert) 1 else 0

        while (axisBindings.size - 1 <= newCode) {
            axisBindings.add(null)
        }

        axisBindings[newCode] = AxisBinding(threshold, actionId)
    }

    override fun buttonUp(controller: Controller, buttonCode: Int): Boolean {
        val actionId = buttonBindings.getOrNull(buttonCode)

        if (actionId != null && actionId != -1) {
            currentState[actionId] = false
            return true
        }
        return false
    }

    override fun buttonDown(controller: Controller, buttonCode: Int): Boolean {
        println(buttonCode)
        val actionId = buttonBindings.getOrNull(buttonCode)

        if (actionId != null && actionId != -1) {
            currentState[actionId] = true
            return true
        }
        return false
    }

    private class AxisBinding(val threshold: Float, val actionId: Int)

    override fun accelerometerMoved(controller: Controller?, accelerometerCode: Int, value: Vector3?): Boolean = false
    override fun axisMoved(controller: Controller, axisCode: Int, value: Float): Boolean {
        val newCode = axisCode * 2 + if (value < 0) 1 else 0
        val newValue = Math.abs(value)

        val binding = axisBindings.getOrNull(newCode)

        if (binding != null) {
            currentAxisState[binding.actionId] = newValue
            currentState[binding.actionId] = newValue >= binding.threshold
            return true
        }

        return false
    }

    override fun povMoved(controller: Controller?, povCode: Int, value: PovDirection?): Boolean = false

    override fun xSliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = false
    override fun ySliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = false

    override fun connected(controller: Controller) {
    }

    override fun disconnected(controller: Controller) {
    }
}