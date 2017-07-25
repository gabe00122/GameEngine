package gabek.onebreath.prefab.test

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.AnimationCom
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.prefab.Prefab

/**
 * @author Gabriel Keith
 * @date 7/25/2017
 */
class AnimTestPrefab: Prefab(){

    override fun define() {
        super.define()

        val pixelToMeter = kodein.instance<PixelRatio>().pixelToMeters
        val assets: Assets = kodein.instance()

        val anim = assets.getAnimation("test:test")

        add<TranslationCom>()
        add<AnimationCom>{
            animation = anim
        }
        add<SizeCom>{
            width = 32 * pixelToMeter
            height = 32 * pixelToMeter
        }
    }

}