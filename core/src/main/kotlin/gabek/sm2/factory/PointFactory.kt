package gabek.sm2.factory

import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 * @date 4/8/2017
 */

class PointFactory: EntityFactory(){
    override fun define() {
        com<TranslationCom>()
    }
}