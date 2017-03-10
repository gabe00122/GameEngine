package gabek.sm2.factory

/**
 * @author Gabriel Keith
 */

/*
class PelletFactory(kodein: Kodein, val world: World){
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            SpriteCom::class.java,
            BodyCom::class.java,
            PelletLifeSpanCom::class.java,
            PelletCollisionCom::class.java
    ).build(world)
    
    private val assets: Assets = kodein.instance()
    private val textureRef: TextureRegion = assets.findTexture("actors", "rect")
    
    
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val lifeSpanMapper = world.getMapper(PelletLifeSpanCom::class.java)
    private val collisionEffectMapper = world.getMapper(PelletCollisionCom::class.java)

    fun create(x: Float, y: Float, direction: Float, speed: Float, lifeSpan: Float): Int{
        val id = world.create(arch)
        
        val trans = transMapper[id]
        val sprite = spriteMapper[id]
        val body = bodyMapper[id]
        val pelletLifeSpan = lifeSpanMapper[id]
        val effect = collisionEffectMapper[id]

        trans.initPos(x, y)
        
        sprite.textureRef = textureRef
        sprite.width = 0.2f
        sprite.height = 0.2f

        pelletLifeSpan.lifeSpan = lifeSpan
        effect.damage = 1f
        effect.diesOnCollision = true

        val fixture = RFixture()
        fixture.isSensor = true
        fixture.shape = RPolygon(0.2f, 0.2f)
        fixture.density = 10f
        fixture.restitution = 0.2f

        body.body.bodyType = BodyDef.BodyType.KinematicBody
        body.body.isBullet = true
        body.body.x = x
        body.body.y = y

        body.body.linearVelocityX = MathUtils.cosDeg(direction) * speed
        body.body.linearVelocityY = MathUtils.sinDeg(direction) * speed
        body.body.gravityScale = 0f
        body.body.addFixture(fixture)

        return id
    }
}
        */