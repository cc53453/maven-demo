package io.github.cc53453.file.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReaderWriter {
    private Workbook workbook;
    private Sheet sheet;
    private int currentRowIndex = 0;
    private Row currentRow;

    public ExcelReaderWriter(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        this.workbook = WorkbookFactory.create(fis);
        this.sheet = workbook.getSheetAt(0);
    }

    public boolean hasNext() {
        return currentRowIndex <= sheet.getLastRowNum();
    }

    public Row readNext() {
        currentRow = sheet.getRow(currentRowIndex++);
        return currentRow;
    }

    public String getCellString(int colIndex) {
        Cell cell = currentRow.getCell(colIndex);
        return cell == null ? null : cell.toString();
    }

    public void writeCell(int colIndex, String value) {
        Cell cell = currentRow.getCell(colIndex);
        if (cell == null) {
            cell = currentRow.createCell(colIndex);
        }
        cell.setCellValue(value);
    }

    public void save(String outputPath) throws Exception {
        FileOutputStream fos = new FileOutputStream(outputPath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
