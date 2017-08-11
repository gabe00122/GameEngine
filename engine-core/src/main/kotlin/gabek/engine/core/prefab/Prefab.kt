package gabek.engine.core.prefab

import com.artemis.*
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.components.RComponent
import gabek.engine.core.components.meta.PrefabCom
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.util.getSystem
import kotlin.reflect.KClass

/**
 * @author Gabriel Keith
 */
open class Prefab: Disposable {
    lateinit var kodein: Kodein
    lateinit var world: World

    private lateinit var archetype: Archetype
    private lateinit var transSystem: TranslationSystem

    private val parts = HashMap<KClass<*>, Part<*>>()
    private val quickList = ArrayList<Part<*>>()

    var name: String = ""

    fun initialise(kodein: Kodein, world: World) {
        this.kodein = kodein
        this.world = world
        transSystem = world.getSystem()
    }

    open fun define() {
        add<PrefabCom> {
            name = this@Prefab.name
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun assemble() {
        val archetypeBuilder = ArchetypeBuilder()
        for ((clazz, part) in parts) {
            val jclazz = clazz.java as Class<Component>
            archetypeBuilder.add(jclazz)

            if (!part.default) {
                part.initialise(world)
                quickList.add(part)
            }
        }
        archetype = archetypeBuilder.build(world)
    }

    fun create(): Int {
        val id = world.create(archetype)

        for (i in 0 until quickList.size) {
            quickList[i].use(id)
        }

        return id
    }

    fun create(x: Float, y: Float): Int {
        val id = create()
        transSystem.setPosition(id, x, y)

        return id
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: RComponent<T>> add(clazz: KClass<T>, builder: (T.() -> Unit)? = null) {
        val part = parts.computeIfAbsent(clazz) { Part(clazz, instanceOf(clazz)) } as Part<T>
        if (builder != null) {
            part.component.builder()
            part.default = false
        }
    }

    inline fun <reified T: RComponent<T>> add(noinline builder: (T.() -> Unit)? = null) {
        add(T::class, builder)
    }

    private fun <T: Component> instanceOf(clazz: KClass<T>): T {
        return clazz.java.newInstance()
    }

    override fun dispose() {}

    private class Part<T: RComponent<T>>(val clazz: KClass<T>, val component: T) {
        private lateinit var mapper: ComponentMapper<T>
        var default: Boolean = true

        fun initialise(world: World) {
            mapper = world.getMapper(clazz.java)
        }

        fun use(entityId: Int) {
            mapper[entityId].set(component)
        }
    }
}