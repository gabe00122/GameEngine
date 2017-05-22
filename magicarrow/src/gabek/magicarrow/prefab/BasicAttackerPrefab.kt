package gabek.magicarrow.prefab

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.prefab.Prefab

/**
 * @another Gabriel Keith
 * @date 5/17/2017.
 */

class BasicAttackerPrefab: Prefab() {
    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val sprite = assets.findTexture("actors:attacker:0")

        add<TranslationCom>()
        add<SpriteCom> {
            textureRef = sprite

            width = 1f
            height = 1f
        }
    }
}