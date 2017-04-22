package gabek.sm2.prefab

import gabek.sm2.components.common.TranslationCom

/**
 * @author Gabriel Keith
 * @date 4/8/2017
 */

class PointPrefab : Prefab(){
    override fun define() {
        add<TranslationCom>()
    }
}