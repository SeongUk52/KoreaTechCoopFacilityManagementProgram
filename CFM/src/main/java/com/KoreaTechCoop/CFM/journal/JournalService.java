package com.KoreaTechCoop.CFM.journal;

import com.KoreaTechCoop.CFM.DataNotFoundException;
import com.KoreaTechCoop.CFM.user.SiteUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JournalService {
    private final JournalRepository journalRepository;
    public Page<Journal> getList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findAll(pageable);
    }

    public Page<Journal> getListByYear(int page,Integer thisyear) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findByThisyear(pageable,thisyear);
    }
    public Page<Journal> getListByUser(int page,String id,Integer thisyear) {
        Pageable pageable = PageRequest.of(page, 20);
        return this.journalRepository.findBySiteUserUsernameAndThisyear(pageable,id,thisyear);
    }

    public List<Journal> getListByYear(Integer thisyear) {
        return this.journalRepository.findByThisyear(thisyear);
    }
    public Journal getJournal(Integer id){
        Optional<Journal> journal = this.journalRepository.findById(id);
        if (journal.isPresent()) {
            return journal.get();
        } else {
            throw new DataNotFoundException("journal not found");
        }
    }

    public void delete(Journal journal) {
        this.journalRepository.delete(journal);
    }
    public void change(Journal journal) {
        journal.setProcess(!journal.getProcess());
        this.journalRepository.save(journal);
    }
    public void createNewJournal(String campus, String category, String employee, Date time, String workInfo, Boolean process, String note, SiteUser author) {
        Journal journal = new Journal();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        journal.setCampus(campus);
        journal.setCategory(category);
        journal.setEmployee(employee);
        journal.setTime(time);
        journal.setWorkInfo(workInfo);
        journal.setProcess(process);
        journal.setNote(note);
        journal.setSiteUser(author);
        journal.setThisyear(calendar.get(Calendar.YEAR));
        this.journalRepository.save(journal);
    }
    public void modify(Journal journal, String campus, String category, String employee, Date time, String workInfo, Boolean process, String note){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        journal.setCampus(campus);
        journal.setCategory(category);
        journal.setEmployee(employee);
        journal.setTime(time);
        journal.setWorkInfo(workInfo);
        journal.setProcess(process);
        journal.setNote(note);
        journal.setThisyear(calendar.get(Calendar.YEAR));
        this.journalRepository.save(journal);
    }

    public void excelDownload(HttpServletResponse response, HttpServletRequest req, List<Journal> journalList) throws IOException {
        if(journalList.isEmpty()){return;}
        Journal firstJournal = journalList.get(0);
        //        Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(firstJournal.getThisyear()+"년 생협 시설지원팀 업무일지");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;
        CellStyle defaultStyle = wb.createCellStyle();
        CellStyle headerStyle = wb.createCellStyle();

        // 테두리 설정
        defaultStyle.setBorderTop(BorderStyle.THIN);
        defaultStyle.setBorderLeft(BorderStyle.THIN);
        defaultStyle.setBorderRight(BorderStyle.THIN);
        defaultStyle.setBorderBottom(BorderStyle.THIN);

        // 테두리 설정
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());

        // Header
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,10));
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        defaultStyle.setWrapText(true);
        cell.setCellStyle(defaultStyle);
        cell.setCellValue(firstJournal.getThisyear()+"년 생협 시설지원팀 업무일지");

        sheet.setColumnWidth(0,2500);
        for(int i =1; i<11;i++){
            row.createCell(i).setCellStyle(defaultStyle);
            sheet.setColumnWidth(i,2500);
        }
        sheet.setColumnWidth(7,5500);
        sheet.setColumnWidth(10,9500);
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,4));
        sheet.addMergedRegion(new CellRangeAddress(1,2,5,5));
        sheet.addMergedRegion(new CellRangeAddress(1,2,6,6));
        sheet.addMergedRegion(new CellRangeAddress(1,2,7,7));
        sheet.addMergedRegion(new CellRangeAddress(1,1,8,9));
        sheet.addMergedRegion(new CellRangeAddress(1,2,10,10));
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);cell.setCellStyle(headerStyle);cell.setCellValue("구분");
        cell = row.createCell(1);cell.setCellStyle(headerStyle);
        cell = row.createCell(2);cell.setCellStyle(headerStyle);
        cell = row.createCell(3);cell.setCellStyle(headerStyle);
        cell = row.createCell(4);cell.setCellStyle(headerStyle);
        cell = row.createCell(5);cell.setCellStyle(headerStyle);cell.setCellValue("담당자");
        cell = row.createCell(6);cell.setCellStyle(headerStyle);cell.setCellValue("일자");
        cell = row.createCell(7);cell.setCellStyle(headerStyle);cell.setCellValue("업무내용");
        cell = row.createCell(8);cell.setCellStyle(headerStyle);cell.setCellValue("처리결과");
        cell = row.createCell(9);cell.setCellStyle(headerStyle);
        cell = row.createCell(10);cell.setCellStyle(headerStyle);cell.setCellValue("비고");
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);cell.setCellStyle(headerStyle);cell.setCellValue("1캠");
        cell = row.createCell(1);cell.setCellStyle(headerStyle);cell.setCellValue("2캠");
        cell = row.createCell(2);cell.setCellStyle(headerStyle);cell.setCellValue("전기");
        cell = row.createCell(3);cell.setCellStyle(headerStyle);cell.setCellValue("영선");
        cell = row.createCell(4);cell.setCellStyle(headerStyle);cell.setCellValue("기계");
        cell = row.createCell(5);cell.setCellStyle(headerStyle);
        cell = row.createCell(6);cell.setCellStyle(headerStyle);
        cell = row.createCell(7);cell.setCellStyle(headerStyle);
        cell = row.createCell(8);cell.setCellStyle(headerStyle);cell.setCellValue("완료");
        cell = row.createCell(9);cell.setCellStyle(headerStyle);cell.setCellValue("처리중");
        cell = row.createCell(10);cell.setCellStyle(headerStyle);


        // Body
        for (int i=0; i<journalList.toArray().length; i++) {
            row = sheet.createRow(rowNum++);
            Journal thisJournal = journalList.get(i);
            cell = row.createCell(0);
            cell.setCellStyle(defaultStyle);
            if(Objects.equals(thisJournal.getCampus(), "1캠퍼스")){cell.setCellValue("○");}
            cell = row.createCell(1);
            cell.setCellStyle(defaultStyle);
            if(Objects.equals(thisJournal.getCampus(), "2캠퍼스")){cell.setCellValue("○");}
            cell = row.createCell(2);
            cell.setCellStyle(defaultStyle);
            if(Objects.equals(thisJournal.getCampus(), "전기")){cell.setCellValue("○");}
            cell = row.createCell(3);
            cell.setCellStyle(defaultStyle);
            if(Objects.equals(thisJournal.getCampus(), "영선")){cell.setCellValue("○");}
            cell = row.createCell(4);
            cell.setCellStyle(defaultStyle);
            if(Objects.equals(thisJournal.getCampus(), "기계")){cell.setCellValue("○");}
            cell = row.createCell(5);
            cell.setCellStyle(defaultStyle);
            cell.setCellValue(thisJournal.getEmployee());
            cell = row.createCell(6);
            cell.setCellStyle(defaultStyle);
            cell.setCellValue(thisJournal.getTime().getMonth()+thisJournal.getTime().getDay());
            cell = row.createCell(7);
            cell.setCellStyle(defaultStyle);
            cell.setCellValue(thisJournal.getWorkInfo());
            cell = row.createCell(8);
            cell.setCellStyle(defaultStyle);
            if(thisJournal.getProcess()){cell.setCellValue("○");}
            cell = row.createCell(9);
            cell.setCellStyle(defaultStyle);
            if(!thisJournal.getProcess()){cell.setCellValue("○");}
            cell = row.createCell(10);
            cell.setCellStyle(defaultStyle);
            cell.setCellValue(thisJournal.getNote());
        }

        // 컨텐츠 타입과 파일명 지정
        String fileNm = firstJournal.getThisyear()+"년 생협 시설지원팀 업무일지.xlsx";
        String browser = getBrowser(req);
        response.setContentType("ms-vnd/excel; charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Description", "file download");
        response.setHeader("Content-Disposition", "attachment; filename=\"".concat(getFileNm(browser, fileNm)).concat("\""));
        response.setHeader("Content-Transfer-Encoding", "binary");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }

    public String getFileNm(String browser, String fileNm){
        String reFileNm = null;
        try {
            if (browser.equals("MSIE") ||
                    browser.equals("Trident") ||
                    browser.equals("Edge")) {
                reFileNm = URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20");
            } else {
                if(browser.equals("Chrome")){
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < fileNm.length(); i++) {
                        char c = fileNm.charAt(i);
                        if (c > '~') {
                            sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
                        } else {
                            sb.append(c);
                        }
                    } reFileNm = sb.toString();
                } else{
                    reFileNm = new String(fileNm.getBytes("UTF-8"), "ISO-8859-1");
                }
                if(browser.equals("Safari") || browser.equals("Firefox"))
                    reFileNm = URLDecoder.decode(reFileNm);
            }
        } catch(Exception e){}
        return reFileNm;
    }

    public String getBrowser(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        if(userAgent.indexOf("MSIE") > -1
                || userAgent.indexOf("Trident") > -1 //IE11
                || userAgent.indexOf("Edge") > -1) {
            return "MSIE";
        } else if(userAgent.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if(userAgent.indexOf("Opera") > -1) {
            return "Opera";
        } else if(userAgent.indexOf("Safari") > -1) {
            return "Safari";
        } else if(userAgent.indexOf("Firefox") > -1){
            return "Firefox";
        } else{
            return null;
        }
    }

}
