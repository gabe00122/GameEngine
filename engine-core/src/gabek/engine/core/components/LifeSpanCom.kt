package gabek.engine.core.components

/**
 * @author Gabriel Keith
 */
class LifeSpanCom: RComponent<LifeSpanCom>() {
    var lifeSpan: Float = 0f

    override fun set(other: LifeSpanCom) {
        lifeSpan = other.lifeSpan
    }

    override fun reset() {
        lifeSpan = 0f
    }

    override fun toString() = "LifeSpanCom: lifeSpan = $lifeSpan"
}