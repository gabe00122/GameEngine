package gabek.sm2.components

import com.artemis.PooledComponent
import gabek.sm2.util.Mirrorable

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class RComponent<T : RComponent<T>> : PooledComponent(), Mirrorable<T>