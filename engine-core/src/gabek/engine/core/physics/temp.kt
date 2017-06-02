package gabek.engine.core.physics

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef

/**
 * @author Gabriel Keith
 */


internal val TEMP_BODY_DEF = BodyDef()
internal val TEMP_FIXTURE_DEF = FixtureDef()

// joints
internal val TEMP_REVOLUTE_DEF = RevoluteJointDef()
internal val TEMP_PRISMATIC_DEF = PrismaticJointDef()