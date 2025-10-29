package com.example.cpacsummary.service;

import com.example.cpacsummary.model.StudentSummary;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelParserService {

    public List<StudentSummary> parse(InputStream in) throws Exception {
        Workbook wb = WorkbookFactory.create(in);
        Sheet sheet = wb.getSheetAt(0);

        // Row 0: CPAC headers, Row 1: CP identifiers (not used here, but can be)
        Row cpacRow = sheet.getRow(0);

        List<String> cpacHeaders = new ArrayList<>();
        for (int i = 3; i < cpacRow.getLastCellNum(); i++) { // First 3 columns are Surname, Year 12 Set, Year 13 Set
            cpacHeaders.add(cpacRow.getCell(i).getStringCellValue().trim());
        }

        List<StudentSummary> students = new ArrayList<>();
        for (int r = 2; r <= sheet.getLastRowNum(); r++) { // Skip header rows
            Row row = sheet.getRow(r);
            if (row == null) continue;

            String surname = getString(row.getCell(0));
            String y12 = getString(row.getCell(1));
            String y13 = getString(row.getCell(2));

            StudentSummary summary = new StudentSummary(surname, y12, y13);

            Map<String, List<String>> cpacResults = new LinkedHashMap<>();
            for (String c : cpacHeaders) cpacResults.put(c, new ArrayList<>());

            for (int i = 3; i < cpacHeaders.size() + 3; i++) {
                String cpac = cpacHeaders.get(i - 3);
                String val = getString(row.getCell(i)).toLowerCase();
                if (!cpacResults.containsKey(cpac)) cpacResults.put(cpac, new ArrayList<>());
                cpacResults.get(cpac).add(val);
            }
            summary.setCpacOutcomes(cpacResults);
            students.add(summary);
        }
        wb.close();
        return students;
    }

    private String getString(Cell cell) {
        if (cell == null) return "0";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.toString();
    }
}
