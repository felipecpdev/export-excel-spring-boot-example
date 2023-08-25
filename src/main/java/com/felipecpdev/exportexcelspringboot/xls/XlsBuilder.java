package com.felipecpdev.exportexcelspringboot.xls;

import com.felipecpdev.exportexcelspringboot.entity.Post;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class XlsBuilder {

    public Workbook buildExcelPost(List<String> headers, List<Post> list) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("posts");

        CellStyle styleHeader = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeight(13);
        styleHeader.setFont(font);

        buildHeader(sheet, styleHeader, headers);

        CellStyle styleContent = workbook.createCellStyle();
        XSSFFont fontContent = (XSSFFont) workbook.createFont();
        fontContent.setFontHeight(11);
        styleContent.setFont(fontContent);

        int rowIdx = 1;
        for (Post post : list) {
            Row sectionsRow = sheet.getRow(rowIdx) == null ? sheet.createRow(rowIdx) : sheet.getRow(rowIdx);
            createCell(sectionsRow, 0, post.getId(), styleContent);
            createCell(sectionsRow, 1, post.getTitle(), styleContent);
            createCell(sectionsRow, 2, post.getAuthor(), styleContent);
            createCell(sectionsRow, 3, post.getContent(), styleContent);
            createCell(sectionsRow, 4, post.getDateCreated(), styleContent);
            rowIdx++;
        }

        int colIdx = headers.size();
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, colIdx - 1));
        sheet.createFreezePane(0, 1);

        for (int i = 0; i < colIdx; i++)
            sheet.autoSizeColumn(i, true);
        return workbook;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(formatLocalDateTime((LocalDateTime) value));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void buildHeader(Sheet sheet, CellStyle headerCellStyle, List<String> headersList) {
        Row sectionsRow = sheet.createRow(0);

        headersList.forEach(value -> {
            Cell cell = sectionsRow.createCell(headersList.indexOf(value));
            cell.setCellValue(value);
            cell.setCellStyle(headerCellStyle);
        });

        setColumnHeaders(headerCellStyle, headersList, sectionsRow);
    }

    private void setColumnHeaders(CellStyle headerCellStyle, List<String> columnsHeaders, Row rowHeader) {

        columnsHeaders.forEach(value -> {
            Cell cell = rowHeader.createCell(columnsHeaders.indexOf(value));
            cell.setCellValue(value);
            cell.setCellStyle(headerCellStyle);
        });
    }

    public String buildFilename(String name) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss");
        String strDateTime = dateFormat.format(LocalDateTime.now());
        return name + "_" + strDateTime + ".xlsx";
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(localDateTime);
    }

    public HttpHeaders getHttpHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Filename", fileName);
        return headers;
    }

    public String folderPath() throws IOException {
        Resource resourceTmp = new ClassPathResource("templates");
        // ruta absoluta de la carpeta
        File folder = resourceTmp.getFile();
        return folder.getAbsolutePath() + "\\";
    }
}
