package io.github.cc53453.file.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.CellType;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * excel读写工具
 */
@Slf4j
public class ExcelReaderWriter implements AutoCloseable {
    private Workbook workbook;
    private Sheet sheet;
    private int currentRowIndex;
    private Row currentRow;
    private FileInputStream fis;
    private String fileOutputPath; // 保存输出路径
    private final DataFormatter dataFormatter;
    private final FormulaEvaluator formulaEvaluator;
    
    /**
     * 构造函数，打开指定路径的Excel文件并定位到指定sheet
     * @param fileInputPath 输入文件
     * @param fileOutputPath 输出文件
     * @param sheetName sheet名字
     * @throws EncryptedDocumentException 如果文件被加密且无法打开
     * @throws IOException 如果文件无法访问或读取错误
     */
    public ExcelReaderWriter(
            @NonNull String fileInputPath, 
            @NonNull String fileOutputPath, 
            @NonNull String sheetName) throws EncryptedDocumentException, IOException {
        this.fis = new FileInputStream(fileInputPath);
        this.fileOutputPath = fileOutputPath;
        try {
            this.workbook = WorkbookFactory.create(fis);
            this.sheet = workbook.getSheet(sheetName);
            if (this.sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }
            this.currentRowIndex = 0;
            this.dataFormatter = new DataFormatter();
            this.formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        } catch (Exception e) {
            // 如果创建失败，关闭已打开的资源（包括 workbook）
        	closeQuietly(this.workbook);
        	closeQuietly(this.fis);
            throw e;
        }
    }

    /**
     * 是否有下一行
     * @return true如果有下一行，false如果没有
     */
    public boolean hasNext() {
        int last = sheet.getLastRowNum();
        while (currentRowIndex <= last) {
            Row r = sheet.getRow(currentRowIndex);
            // 跳过空行
            if (r != null) {
                return true;
            }
            currentRowIndex++;
        }
        return false;
    }

    /**
	 * 读取下一行
	 * @return 下一行的Row对象，如果没有更多行则返回null
	 */
    public Row readNext() {
        if (!hasNext()) {
            return null;
        }
        currentRow = sheet.getRow(currentRowIndex++);
        return currentRow;
    }

    /**
     * 在当前行下新建一行
     */
    public Row createNewRow() {
        sheet.createRow(currentRowIndex);
        currentRow = sheet.getRow(currentRowIndex++);
        return currentRow;
    }

    /**
	 * 获取当前行指定列的字符串值
	 * @param colIndex 列索引（0-based）
	 * @return 单元格的字符串值，如果单元格为空则返回null
	 * @throws IllegalStateException 如果没有当前行（即未调用readNext()或已到末尾）
	 */
    public String getCellString(int colIndex) {
        if (currentRow == null) {
            throw new IllegalStateException("No row selected. Call readNext() first.");
        }
        Cell cell = currentRow.getCell(colIndex);
        return cell == null ? null : getCellValueAsString(cell);
    }

    private String getCellValueAsString(Cell cell) {
        // Use DataFormatter + FormulaEvaluator to get a human-readable string similar to Excel
        if (cell == null) {
            return null;
        }
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            // Evaluate formula and format the result
            try {
                return dataFormatter.formatCellValue(cell, formulaEvaluator);
            } catch (Exception e) {
                // fallback to formula string
                return cell.getCellFormula();
            }
        }
        try {
            return dataFormatter.formatCellValue(cell);
        } catch (Exception e) {
            // last resort
            switch (type) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    }
                    return String.valueOf(numValue);
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                default:
                    return cell.toString();
            }
        }
    }

    /**
     * 写入当前行指定列的值，如果单元格不存在则创建
     * @param colIndex 列索引（0-based）
     * @param value 要写入的字符串值，如果为null则设置单元格为空
     */
    public void writeCell(int colIndex, String value) {
        if (currentRow == null) {
            throw new IllegalStateException("No row selected. Call readNext() first.");
        }
        Cell cell = currentRow.getCell(colIndex);
        if (cell == null) {
            cell = currentRow.createCell(colIndex);
        }
        if (value == null) {
            cell.setBlank();
        } else {
            cell.setCellValue(value);
        }
    }

    /**
	 * 保存当前工作簿到原始输出路径
	 * @throws IOException 如果写入文件失败
	 */
    public void save() throws IOException {
        try (FileOutputStream out = new FileOutputStream(fileOutputPath)) {
            workbook.write(out);
            out.flush();
        }
    }
    
    /**
     * 保存当前工作簿到指定路径
     * @param newPath 新的文件路径
     * @throws IOException 如果写入文件失败
     */
    public void saveAs(@NonNull String newPath) throws IOException {
        try (FileOutputStream newFos = new FileOutputStream(newPath)) {
            workbook.write(newFos);
            newFos.flush();
        }
    }

    private void closeQuietly(AutoCloseable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				log.error("close stream failed: {}", e.getMessage(), e);
			}
		}
	}
    
    @Override
    public void close(){
    	closeQuietly(this.workbook);
    	closeQuietly(this.fis);
    }
}