package org.stathry.commons;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Thumbnails图片压缩
 * Created by dongdaiming on 2018-09-21 11:27
 */
public class ThumbnailsTest {

    @Test
    public void testScale() throws IOException {
        File fp = new File("/temp/p1.jpg");
        File tp = new File("/temp/p2.jpg");
        // scale 缩放比例   outputQuality   输出文件质量系数 0.0 最低 1.0 最高
        Thumbnails.of(fp).scale(1f).outputQuality(0.5f).toFile(tp);
    }

    @Test
    public void testWaterMark() throws IOException {
        //根据文字自定义出一张水印图片
        BufferedImage waterMark = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = waterMark.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, 64, 64);
        char[] data = "github".toCharArray();
        g.drawChars(data, 0, data.length, 5, 32);

        Thumbnails.of(new File("/temp/p1.jpg"))
                .scale(0.5f)
                .watermark(Positions.BOTTOM_RIGHT, waterMark, 0.9f)
                .toFile(new File("/temp/p3.jpg"));
    }

}
