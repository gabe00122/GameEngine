package gabek.sm2.world

/**
 * @author Gabriel Keith
 */

val WALL: Int = 0x1
val PROP_SMALL: Int = 0x2
val PROP_LARGE: Int = 0x4
val CHARACTER: Int = 0x8
val PELLET: Int = 0x10

fun filter(vararg flags: Int): Short{
    var out: Int = 0
    for(flag in flags){
        out = out.or(flag)
    }
    return out.toShort()
}

fun category(flag: Int): Short = flag.toShort()