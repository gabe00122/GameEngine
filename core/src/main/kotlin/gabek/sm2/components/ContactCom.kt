package gabek.sm2.components

import com.artemis.Component
import com.badlogic.gdx.utils.Array
import gabek.sm2.physics.RContact

/**
 * @author Gabriel Keith
 */
class ContactCom : Component(){
    val contacts = Array<RContact>(false, 10)
}