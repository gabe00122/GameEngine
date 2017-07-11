package gabek.engine.core.graphics

/**
 * @author Gabriel Keith
 * @date 6/10/2017
 */
object Profiler {
    private const val RESET_AT = 1f

    var min: Float = 0f
    var max: Float = 0f

    private var sum: Float = 0f
    var mean: Float = 0f

    private var count: Int = 0
    var listener: Listener? = null

    fun reset(){
        min = 0f
        max = 0f
        mean = 0f

        count = 0
    }

    fun update(delta: Float){
        sum += delta
        if(sum > RESET_AT){
            sum = 0f
            listener?.onReset()
            reset()
        }

        if(count++ == 0){
            min = delta
            max = delta
        }

        if(delta < min){
            min = delta
        }
        if(delta > max){
            max = delta
        }

        mean = sum / count
    }

    interface Listener {
        fun onReset()
    }
}