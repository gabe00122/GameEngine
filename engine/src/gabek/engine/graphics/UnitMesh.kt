package gabek.sm2.graphics

import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 * @date 4/24/2017
 */
class UnitMesh : Disposable {
    private val mesh = createUnitMesh()

    fun render(shader: ShaderProgram) {
        mesh.render(shader, GL30.GL_TRIANGLE_FAN, 0, 4)
    }

    private fun createUnitMesh(): Mesh {
        val verts = FloatArray(16)
        verts[0] = -1f
        verts[1] = -1f
        verts[2] = 0f
        verts[3] = 0f

        verts[4] = 1f
        verts[5] = -1f
        verts[6] = 1f
        verts[7] = 0f

        verts[8] = 1f
        verts[9] = 1f
        verts[10] = 1f
        verts[11] = 1f

        verts[12] = -1f
        verts[13] = 1f
        verts[14] = 0f
        verts[15] = 1f

        val mesh = Mesh(true, 4, 0,
                VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"))
        mesh.setVertices(verts)
        return mesh
    }

    override fun dispose() {
        mesh.dispose()
    }
}