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


    @Override
    protected List<String> findRow(MultipartFile source, List<Searcher> searchers) {
        List<String> result = new ArrayList<>();
        try {
            InputStream inputStream = source.getInputStream();

            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);

            result = getResultRow(sheet, searchers);
            inputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> getResultRow(Sheet sheet, List<Searcher> searchers) {
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (isNeededRow(searchers, row)) {
                return convertRowToStringsList(row);
            }
        }
        return new ArrayList<>();
    }

    //todo refactor -> move to superclass
    private boolean isNeededRow(List<Searcher> searchers, Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        int findCounter = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            List<Searcher> excludedSearchers = new ArrayList<>();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String stringCellValue = cell.getStringCellValue();
                for (Searcher searcher : searchers) {
                    if (!excludedSearchers.contains(searcher)
                            && searcher.isCellFind(stringCellValue)) {
                        findCounter++;
                        excludedSearchers.add(searcher);
                        break;
                    }
                }
                if (findCounter == searchers.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> convertRowToStringsList(Row row) {
        List<String> result = new ArrayList<>();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {

            Cell cell = cellIterator.next();

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    Double numericCellValue = cell.getNumericCellValue();
                    result.add(String.format(Locale.UK, "%.2f", numericCellValue));
                    break;
                case Cell.CELL_TYPE_STRING:
                    result.add(cell.getStringCellValue());
                    break;
            }
        }

        return result;
    }
}
