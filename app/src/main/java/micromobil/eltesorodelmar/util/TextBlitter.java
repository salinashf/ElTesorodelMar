package micromobil.eltesorodelmar.util;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

/**
 * A class to blit text. Based on class written by CyberKilla.
 */
public class TextBlitter {


    /**
     * Blits some text.
     *
     * @param buffer the blitting target
     * @param line   the text
     * @param x      the x-position of the text
     * @param y      the y-position of the text
     */
    public void blitText(FrameBuffer buffer, String line, int x, int y) {
        blitText(buffer, line, x, y, 400, 250);
    }

    /**
     * Blits some text.
     *
     * @param buffer the blitting target
     * @param line   the text
     * @param x      the x-position of the text
     * @param y      the y-position of the text
     * @param maxX   the x-limit of the area to blit into
     * @param maxY   the y-limit of the area to blit into
     */
    public void blitText(FrameBuffer buffer, String line, int x, int y, int maxX, int maxY) {
        char end = 255;
        char start = 0;
        int rowLength = 32;
        int width = 9;
        int height = 16;
        Texture texture = TextureManager.getInstance().getTexture("vgafont");
        int xs = x;

        for (int i = 0; i < line.length(); i++) {
            char cNum = line.charAt(i);

            if (cNum == '\n') {
                y += height;
                x = xs;
            }

            int iNum = cNum - start;
            int yNum = 0;

            if (iNum > rowLength - 1) {
                int rows = iNum / rowLength;

                yNum += rows * height;
                iNum -= rows * rowLength;
            }

            if (x > maxX) {
                y += height;
                x = xs;
            }
            if (y > maxY) {
                return;
            }

            if (iNum > 0 && iNum <= end) {
                buffer.blit(texture, iNum * width, yNum, x, y, width, height, FrameBuffer.TRANSPARENT_BLITTING);
            }
            x += width;
        }
    }
}
