package gabek.engine.core.components

import com.artemis.PooledComponent
import gabek.engine.core.util.entity.Mirrorable


/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class RComponent<T: RComponent<T>>: PooledComponent(), Mirrorable<T>