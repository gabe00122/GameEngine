package gabek.engine.core.components

import com.artemis.annotations.EntityId
import gabek.engine.core.physics.joint.RJoint

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */
class JointCom : RComponent<JointCom>() {
    var joint: RJoint? = null

    var sourceType = SourceType.SELF
    @JvmField @EntityId var srcId: Int = -1

    var destinationType = DestinationType.STATIC
    @JvmField @EntityId var destId: Int = -1

    override fun reset() {
        joint = null

        sourceType = SourceType.SELF
        srcId = -1

        destinationType = DestinationType.STATIC
        destId = -1
    }

    override fun set(other: JointCom) {
        joint = other.joint?.clone()

        sourceType = other.sourceType
        srcId = other.srcId

        destinationType = other.destinationType
        destId = other.destId
    }

    override fun toString() = "JointCom"

    enum class SourceType {
        SELF, OTHER
    }

    enum class DestinationType {
        STATIC, OTHER
    }
}