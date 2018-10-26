package org.stathry.commons.model.config;

/**
 * TODO
 *
 * @author stathry
 * @date 2018/4/13
 */
public class ExcelRange {

    private Integer cellStart;
    private Integer cellEnd;
    private Integer rowStart;
    private Integer rowEnd;

    public ExcelRange() {

    }

    public ExcelRange(Integer cellStart, Integer cellEnd, Integer rowStart, Integer rowEnd) {
        this.cellStart = cellStart;
        this.cellEnd = cellEnd;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
    }

    @Override
    public String toString() {
        return "ExcelRange{" +
                "cellStart=" + cellStart +
                ", cellEnd=" + cellEnd +
                ", rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                '}';
    }

    public Integer getCellStart() {
        return cellStart;
    }

    public void setCellStart(Integer cellStart) {
        this.cellStart = cellStart;
    }

    public Integer getCellEnd() {
        return cellEnd;
    }

    public void setCellEnd(Integer cellEnd) {
        this.cellEnd = cellEnd;
    }

    public Integer getRowStart() {
        return rowStart;
    }

    public void setRowStart(Integer rowStart) {
        this.rowStart = rowStart;
    }

    public Integer getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(Integer rowEnd) {
        this.rowEnd = rowEnd;
    }
}
