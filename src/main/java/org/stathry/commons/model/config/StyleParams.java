/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.model.config;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author dongdaiming@free.com
 * <p>
 * 2016年8月22日
 */
public class StyleParams {

    /**
     * 字体加粗
     */
    private boolean fontBold = false;
    /**
     * 有边框
     */
    private boolean hasBorder = true;
    /**
     * 字体
     */
    private String fontFamily = "宋体";
    /**
     * 字体颜色
     */
    private short fontColor = IndexedColors.BLACK.getIndex();
    /**
     * 字体大小
     */
    private short fontHeight = 16;
    /**
     * 水平对齐方式
     *
     */
    private short align;// = CellStyle.ALIGN_CENTER;
    /**
     * 垂直对齐方式
     *
     */
    private short verticalAlign;// = CellStyle.VERTICAL_CENTER;
    /**
     * 背景色
     *
     * @see org.apache.poi.ss.usermodel.IndexedColors#BLUE
     */
    private short backgroundColor = IndexedColors.WHITE.getIndex();
    /**
     * 开始行索引
     */
    private int startRow = 0;
    /**
     * 开始列索引
     */
    private int startColumn = 0;

    public boolean isFontBold() {
        return fontBold;
    }

    public void setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public short getFontColor() {
        return fontColor;
    }

    public void setFontColor(short fontColor) {
        this.fontColor = fontColor;
    }

    public short getFontHeight() {
        return fontHeight;
    }

    public void setFontHeight(short fontHeight) {
        this.fontHeight = fontHeight;
    }

    public short getAlign() {
        return align;
    }

    public void setAlign(short align) {
        this.align = align;
    }

    public short getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(short verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public short getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(short backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

}
