package com.pricegsm.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * User: o.logunov
 * Date: 14.02.15
 * Time: 2:52
 */
public class PriceListExcelParser extends PriceListParser<MultipartFile> {

    private InputStream inputStream;
    private Iterator<Row> rowIterator;
    private Sheet sheet;

    @Override
    protected void openSource(MultipartFile source) throws IOException, InvalidFormatException {
        inputStream = source.getInputStream();
        Workbook wb = WorkbookFactory.create(inputStream);
        sheet = wb.getSheetAt(0);
        resetSource();
    }

    @Override
    protected void resetSource() {
        rowIterator = sheet.iterator();
    }

    @Override
    protected void closeSource(MultipartFile source) throws IOException {
        inputStream.close();
    }

    @Override
    protected boolean hasNextRow() {
        return rowIterator.hasNext();
    }

    @Override
    protected List<String> getNextRow() {
        Row row = rowIterator.next();
        return convertRowToStringsList(row);
    }

    private List<String> convertRowToStringsList(Row row) {
        List<String> result = new ArrayList<>();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {

            Cell cell = cellIterator.next();

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    Double numericCellValue = cell.getNumericCellValue();
                    result.add(String.format(Locale.UK, "%.0f", numericCellValue));
                    break;
                case Cell.CELL_TYPE_STRING:
                    result.add(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    result.add("");
                    break;
            }
        }

        return result;
    }
}
