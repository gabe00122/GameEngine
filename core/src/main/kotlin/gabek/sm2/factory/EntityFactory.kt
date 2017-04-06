package gabek.sm2.factory

import com.artemis.*
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import gabek.sm2.systems.TranslationSystem

/**
 * @author Gabriel Keith
 */
open class EntityFactory(
        //private val populateFunction: (EntityFactory.() -> Unit)? = null
) : Disposable {
    lateinit var kodein: Kodein
    lateinit var world: World

    private val compBuilders = mutableListOf<CompBuilder<Component>>()

    private lateinit var archetype: Archetype
    private lateinit var transSystem: TranslationSystem


    fun initialize(kodein: Kodein, world: World){
        this.kodein = kodein
        this.world = world
        world.inject(this)

        define()

        compBuilders.forEach { it.mapper = world.getMapper(it.clazz) }
        archetype = compBuilders.fold(ArchetypeBuilder()) { builder, comp ->
            builder.add(comp.clazz); builder
        }.build(world)
    }

    open fun define(){
        //populateFunction?.invoke(this)
    }

    open fun create(): Int {
        val entity = world.create(archetype)

        compBuilders.forEach { comp ->
            comp.body?.invoke(comp.mapper!!.get(entity), entity)
        }

        return entity
    }

    open fun create(x: Float, y: Float, rotation: Float = 0f): Int {
        val id = create()
        transSystem.teleport(id, x, y, rotation)
        return id
    }

    override fun dispose() {}

    @Suppress("UNCHECKED_CAST")
    fun <T : Component> com(clazz: Class<T>, body: BodyLambda<T>? = null) {
        compBuilders.add(CompBuilder(clazz, body) as CompBuilder<Component>)
    }

    inline fun <reified T : Component> com(noinline body: BodyLambda<T>? = null) {
        com(T::class.java, body)
    }

    internal data class CompBuilder<T : Component>(val clazz: Class<T>, val body: BodyLambda<T>?, var mapper: ComponentMapper<T>? = null)
}

typealias BodyLambda<T> = (T.(entity: Int) -> Unit)