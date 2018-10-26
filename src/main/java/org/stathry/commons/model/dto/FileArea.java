package org.stathry.commons.model.dto;

import org.stathry.commons.excel.ExcelConstant;

public class FileArea {

    private int columnStart = 0;
    private int columnEnd = ExcelConstant.MAX_COLUMN;
    private int rowStart = 0;
    private int rowEnd = ExcelConstant.MAX_ROW;

    public FileArea() {
    }

    public FileArea(int columnStart, int columnEnd, int rowStart, int rowEnd) {
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
    }

    @Override
    public String toString() {
        return "{" +
                "columnStart=" + columnStart +
                ", columnEnd=" + columnEnd +
                ", rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                '}';
    }

    public int getColumnStart() {
        return columnStart;
    }

    public void setColumnStart(int columnStart) {
        this.columnStart = columnStart;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    public void setColumnEnd(int columnEnd) {
        this.columnEnd = columnEnd;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }
}
