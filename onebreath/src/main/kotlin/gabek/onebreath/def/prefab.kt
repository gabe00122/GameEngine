package gabek.onebreath.def

import gabek.engine.core.prefab.CameraPrefab
import gabek.engine.core.systems.common.PrefabManager
import gabek.onebreath.prefab.BloodPrefab
import gabek.onebreath.prefab.Junk1Prefab
import gabek.onebreath.prefab.PlayerPrefab
import gabek.onebreath.prefab.RatPrefab
import gabek.onebreath.prefab.test.AnimTestPrefab
import gabek.onebreath.prefab.test.PrisJointTestPrefab
import gabek.onebreath.prefab.test.RotJointTestPefab

/**
 * @author Gabriel Keith
 * @date 7/27/2017
 */
fun prefabs(builder: PrefabManager.Builder) = with(builder) {
    bind("camera", CameraPrefab())

    bind("player", PlayerPrefab())

    bind("rat", RatPrefab())


    bind("junk_1", Junk1Prefab())
    bind("blood", BloodPrefab())

    // <TESTS>
    bind("rot", RotJointTestPefab())
    bind("pris", PrisJointTestPrefab())
    bind("anim", AnimTestPrefab())
    // </TESTS>
}