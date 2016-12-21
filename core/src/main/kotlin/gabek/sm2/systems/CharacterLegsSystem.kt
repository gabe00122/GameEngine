package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.CharacterLegsCom

/**
 * @author Gabriel Keith
 */
class CharacterLegsSystem : BaseEntitySystem(Aspect.all(CharacterLegsCom::class.java)){
    private lateinit var box2d: Box2dSystem
    private lateinit var legMapper: ComponentMapper<CharacterLegsCom>

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        legMapper[entityId].body.initialise(box2d.box2dWorld)
    }

    override fun removed(entityId: Int) {
        legMapper[entityId].body.store(box2d.box2dWorld)
    }
}