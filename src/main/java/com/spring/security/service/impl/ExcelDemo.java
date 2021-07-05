package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import org.junit.Test;

import java.util.List;

public class ExcelDemo {

    @Test
    public void reloadExcel() throws Exception {
        String fileName = "D:/cellphoneSampleSMS.xlsx";
        List<NmsSmsTmplExcelVo> list = EasyExcel.read(fileName).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
        System.out.println(list.size());


//        OutputStream out = new FileOutputStream("D:/test2.xlsx");
//        ExcelWriter writer = EasyExcelFactory.getWriterWithTemp(inputStream, out, ExcelTypeEnum.XLSX, true);

        EasyExcel.write("D:/test1.xlsx", NmsSmsTmplExcelVo.class).sheet("Student").doWrite(list);
    }
}
