package io.github.cc53453.file.util;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExcelReaderWriterTest {
	@Test
	void test() throws EncryptedDocumentException, IOException {
		try(ExcelReaderWriter rw = new ExcelReaderWriter(
				"test/test.xls", "test-out/test.xls", "a");
				ExcelReaderWriter rw2 = new ExcelReaderWriter(
						"test/test.xlsx", "test-out/test.xlsx", "b");){
			rw.readNext();
			Assertions.assertEquals("id", rw.getCellString(0));
			rw.readNext();
			Assertions.assertEquals("张三", rw.getCellString(1));
			rw.readNext();
			Assertions.assertEquals("12/1/99", rw.getCellString(2));
			Assertions.assertFalse(rw.hasNext());
			rw.save();
			
			rw2.readNext();
			Assertions.assertEquals("id", rw2.getCellString(0));
			rw2.readNext();
			Assertions.assertEquals("张三", rw2.getCellString(1));
			rw2.writeCell(3, "27");
			rw2.readNext();
			Assertions.assertEquals("12/1/99", rw2.getCellString(2));
			Assertions.assertFalse(rw2.hasNext());
			
			rw2.save();
			rw2.saveAs("test-out/test2.xlsx");
		}
		
		try(ExcelReaderWriter rw = new ExcelReaderWriter(
				"test-out/test2.xlsx", "test-out/test3.xls", "b")){
			rw.readNext();
			rw.readNext();
			Assertions.assertEquals("27", rw.getCellString(3));
		}
	}
}
