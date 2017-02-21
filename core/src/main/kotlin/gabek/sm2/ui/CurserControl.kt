package gabek.sm2.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.input.Actions
import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class CurserControl(val kodein: Kodein) : Actor() {
    private val curserTextures = mutableListOf<TextureRegion>()
    private val curserList = mutableListOf<Curser>()

    init {
        val assets: Assets = kodein.instance()

        (0..5).mapTo(curserTextures) { assets.retrieveRegion("menus:curser:$it") }
    }

    fun join(playerInput: PlayerInput, index: Int) {
        curserList.add(Curser(playerInput, curserTextures[index]))
    }

    override fun act(delta: Float) {
        super.act(delta)
        for (curser in curserList) {
            curser.update(delta)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        for (curser in curserList) {
            curser.draw(batch)
        }
    }

    private inner class Curser(val playerInput: PlayerInput, val textureRegion: TextureRegion) {
        val speed = 200f
        val size = 24f

        var x = 0f
        var y = 0f

        fun update(delta: Float) {
            if (playerInput.pollAction(Actions.UP)) {
                y += speed * delta
            }
            if (playerInput.pollAction(Actions.DOWN)) {
                y -= speed * delta
            }
            if (playerInput.pollAction(Actions.LEFT)) {
                x -= speed * delta
            }
            if (playerInput.pollAction(Actions.RIGHT)) {
                x += speed * delta
            }


            if (playerInput.pollAction(Actions.SELECT)) {
                select(size / 2, size / 2)
            }
        }

        private fun select(offsetX: Float, offsetY: Float) {
            stage.hit(x + offsetX, y + offsetY, true)?.fire(ChangeListener.ChangeEvent())
        }

        fun draw(batch: Batch) {
            batch.draw(textureRegion, x, y, size, size)
        }
    }
}