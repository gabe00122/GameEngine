package gabek.onebreath.prefab

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.prefab.Prefab

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class BloodPrefab: Prefab(){
    override fun define() {
        super.define()
        val assets: Assets = kodein.instance()


    }
}