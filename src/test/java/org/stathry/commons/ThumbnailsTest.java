package org.stathry.commons;

import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Thumbnails图片压缩
 * Created by dongdaiming on 2018-09-21 11:27
 */
public class ThumbnailsTest {

    @Test
    public void test1() throws IOException {
        File fp = new File("/temp/p1.jpg");
        File tp = new File("/temp/p2.jpg");
        // scale 缩放比例   outputQuality   输出文件质量系数 0.0 最低 1.0 最高
        Thumbnails.of(fp).scale(1f).outputQuality(0.5f).toFile(tp);
    }



}
