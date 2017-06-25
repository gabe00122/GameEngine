package gabek.engine.core.systems.graphics

import com.artemis.ComponentMapper
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.systems.PassiveSystem

/**
 * @author Gabriel Keith
 * @date 6/1/2017
 */
class CullingSystem: PassiveSystem() {
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var sizeMapper: ComponentMapper<SizeCom>

    private val temp = Rectangle()

    fun cull(culling: Rectangle, entityId: Int, progress: Float): Boolean {
        val trans = transMapper[entityId]
        val size = sizeMapper[entityId]

        return culling.contains(rectToAABB(
                x = trans.lerpX(progress),
                y = trans.lerpY(progress),
                rotation = trans.lerpRotation(progress),
                width = size.lerpWidth(progress),
                height = size.lerpHeight(progress)
        ))
    }

    fun cull(culling: Rectangle, x: Float, y: Float, rotation: Float, width: Float, height: Float): Boolean {
        return culling.overlaps(rectToAABB(x, y, rotation, width, height))
    }

    fun rectToAABB(x: Float, y: Float, rotation: Float, width: Float, height: Float): Rectangle{
        val sin = MathUtils.sinDeg(rotation)

        val asin = if (sin > 0) sin else -sin
        val cos = MathUtils.cosDeg(rotation)
        val acos = if (cos > 0) cos else -cos

        temp.width = height * asin + width * acos
        temp.height = width * asin + height * acos
        temp.x = x - temp.width / 2f
        temp.y = y - temp.height / 2f

        return temp
    }
}