package gabek.sm2.scopes

import com.artemis.BaseSystem
import com.artemis.Component
import com.artemis.ComponentMapper
import gabek.sm2.components.*
import gabek.sm2.components.character.CharacterAnimatorCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.components.character.CharacterMovementCom
import gabek.sm2.components.character.CharacterStateCom
import gabek.sm2.components.graphics.*

/**
 * @author Gabriel Keith
 */
class GeneralMapper : BaseSystem() {
    lateinit var transMapper: ComponentMapper<TranslationCom>
    lateinit var bodyMapper: ComponentMapper<BodyCom>
    lateinit var parentOfMapper: ComponentMapper<ParentOfCom>
    lateinit var parentOffsetMapper: ComponentMapper<ParentOffsetCom>

    lateinit var healthMapper: ComponentMapper<HealthCom>

    lateinit var playerInputMapper: ComponentMapper<PlayerInputCom>
    lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    lateinit var characterControllerMapper: ComponentMapper<CharacterControllerCom>
    lateinit var characterMovementMapper: ComponentMapper<CharacterMovementCom>
    lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    lateinit var spriteMapper: ComponentMapper<SpriteCom>
    lateinit var animationMapper: ComponentMapper<AnimationCom>
    lateinit var cameraMapper: ComponentMapper<CameraCom>
    lateinit var cameraTargetsMapper: ComponentMapper<CameraTargetsCom>

    lateinit var healthDisplayMapper: ComponentMapper<HealthDisplayCom>


    override fun processSystem() {}


    fun <A: Component> ComponentMapper<A>.recursiveGet(entity: Int): A? {
        if(has(entity)){
            return get(entity)
        } else if(parentOfMapper.has(entity)){
            return recursiveGet(parentOfMapper[entity].parent)
        } else {
            return null
        }
    }
}
