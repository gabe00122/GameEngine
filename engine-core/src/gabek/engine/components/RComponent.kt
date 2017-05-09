package gabek.engine.components

import com.artemis.PooledComponent
import gabek.engine.util.Mirrorable

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class RComponent<T : RComponent<T>> : PooledComponent(), Mirrorable<T>