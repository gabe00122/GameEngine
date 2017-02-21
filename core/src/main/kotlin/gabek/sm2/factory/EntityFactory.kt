package gabek.sm2.factory

import com.artemis.*
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.systems.TranslationSystem

/**
 * @author Gabriel Keith
 */
class EntityFactory: Disposable{
    private val kodein: Kodein
    private val world: World

    private val compBuilders: List<CompBuilder<Component>>

    private val archetype: Archetype

    private lateinit var transSystem: TranslationSystem

    private constructor(builder: Builder,
                        kodein: Kodein,
                        world: World){
        this.kodein = kodein
        this.world = world
        world.inject(this)

        compBuilders = builder.compBuilders


        compBuilders.forEach { it.mapper = world.getMapper(it.clazz) }
        archetype = compBuilders.fold(ArchetypeBuilder()){ builder, comp ->
            builder.add(comp.clazz); builder
        }.build(world)
    }

    fun create(): Int{
        val entity = world.create(archetype)

        compBuilders.forEach { comp ->
            comp.body?.invoke(comp.mapper!!.get(entity), entity)
        }
        
        return entity
    }

    fun create(x: Float, y: Float, rotation: Float): Int{
        val id = create()
        transSystem.teleport(id, x, y, rotation)
        return id
    }

    fun create(x: Float, y: Float): Int{
        val id = create()
        transSystem.teleport(id, x, y, 0f)
        return id
    }

    override fun dispose() {}

    class Builder(val define: Builder.(kodein: Kodein, world: World) -> Unit){
        private var dirty = false
        internal var compBuilders = mutableListOf<CompBuilder<Component>>()

        @Suppress("UNCHECKED_CAST")
        fun <T: Component> com(clazz: Class<T>, body: (T.(entity: Int) -> Unit)? = null){
            if(dirty){
                dirty = false
                compBuilders = mutableListOf<CompBuilder<Component>>()
            }

            compBuilders.add(CompBuilder(clazz, body) as CompBuilder<Component>)
        }

        inline fun <reified T: Component> com(noinline body: (T.(entity: Int) -> Unit)? = null){
            com(T::class.java, body)
        }

        fun build(kodein: Kodein, world: World): EntityFactory{
            define(kodein, world)
            dirty = true
            return EntityFactory(this, kodein, world)
        }
    }

    internal data class CompBuilder<T: Component>(val clazz: Class<T>, val body: (T.(entity: Int) -> Unit)?, var mapper: ComponentMapper<T>? = null)
}

fun factory(define: EntityFactory.Builder.(kodein: Kodein, world: World) -> Unit): 
        EntityFactory.Builder{
    return EntityFactory.Builder(define)
}