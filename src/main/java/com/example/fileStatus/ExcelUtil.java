package com.example.fileStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public void copyDataToDestFolder(String destinationPath) throws IOException {
        String fileName = "src/main/resources/input.csv";
        destinationPath = "src/main/resources/output.csv";

        File destFile = new File(destinationPath);
        File sourceFile = new File(fileName);

        if (destFile.exists() && destFile.isFile()) {
            Files.deleteIfExists(destFile.toPath());
            Files.copy(sourceFile.toPath(), destFile.toPath());
        } else {
            Files.copy(sourceFile.toPath(), destFile.toPath());
        }
    }

    public void copySheets(XSSFSheet newSheet, XSSFSheet oldSheet) {
        int maxColumn = 0;
        Map<Integer, XSSFCellStyle> styleMap = new HashMap<Integer, XSSFCellStyle>();

        for (int i = oldSheet.getFirstRowNum(); i <= oldSheet.getLastRowNum(); i++) {
            XSSFRow srcRow = oldSheet.getRow(i);
            XSSFRow destRow = newSheet.createRow(i);
            if (srcRow != null) {
                ExcelUtil.copyRow(oldSheet, newSheet, srcRow, destRow, styleMap);
                if (srcRow.getLastCellNum() > maxColumn) {
                    maxColumn = srcRow.getLastCellNum();
                }
            }
        }

        for (int i = 0; i <= maxColumn; i++) {
            newSheet.setColumnWidth(i, oldSheet.getColumnWidth(i));
        }

    }

    public static void copyRow(XSSFSheet oldSheet, XSSFSheet newSheet, XSSFRow srcRow, XSSFRow destRow,
            Map<Integer, XSSFCellStyle> styleMap) {
        destRow.setHeight(srcRow.getHeight());

        for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
            XSSFCell oldCell = srcRow.getCell(j);
            XSSFCell newCell = destRow.getCell(j);

            if (oldCell != null) {
                if (newCell == null) {
                    newCell = destRow.createCell(j);
                }
            }
            copyCell(oldCell, newCell, styleMap);
        }
    }

    public static void copyCell(XSSFCell oldCell, XSSFCell newCell, Map<Integer, XSSFCellStyle> styleMap) {
        if (styleMap != null) {
            if (oldCell.getSheet().getWorkbook() == newCell.getSheet().getWorkbook()) {
                newCell.setCellStyle(oldCell.getCellStyle());
            } else {
                int sthashCode = oldCell.getCellStyle().hashCode();
                XSSFCellStyle newCellStyle = styleMap.get(sthashCode);
                if (newCellStyle == null) {
                    newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    styleMap.put(sthashCode, newCellStyle);
                }
                newCell.setCellStyle(newCellStyle);
                newCell.setCellValue(oldCell.getRawValue());
            }
        }

    }

    public void filterSheet() throws IOException {
        String fileName = "src/main/resources/input.csv";
        String destinationPath = "src/main/resources/output.csv";

        List finallist = Files.lines(Paths.get(fileName))
                .filter(value -> value.startsWith("Hemanth"))
                .collect(Collectors.toList());

        Iterator it = finallist.iterator();
        int rownum = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Generated Data");
        FileOutputStream out = new FileOutputStream(new File(destinationPath));

        while (it.hasNext()) {
            List<String> templist = (List<String>) it.next();
            Iterator<String> tempIterator = templist.iterator();
            XSSFRow row = sheet.createRow(rownum++);
            int cellnum = 0;
            while (tempIterator.hasNext()) {
                String temp = (String) tempIterator.next();
                XSSFCell cell = row.createCell(cellnum++);
                cell.setCellValue(temp);

            }

        }
        workbook.write(out);
        out.close();
        workbook.close();
    }
}
