package gabek.engine.core.prefab

import gabek.engine.core.components.common.TranslationCom

/**
 * @author Gabriel Keith
 * @date 4/8/2017
 */

class PointPrefab : Prefab() {
    override fun define() {
        super.define()

        add<TranslationCom>()
    }
}