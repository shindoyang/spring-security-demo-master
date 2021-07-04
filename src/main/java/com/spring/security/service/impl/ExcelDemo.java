package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelDemo {

    @Test
    public void reloadExcel() throws Exception {
        StopWatch stopWatch = new StopWatch();
        String fileName = "D:/cellphoneSampleSMS.xlsx";
        stopWatch.start();
        List<NmsSmsTmplExcelVo> list = EasyExcel.read(fileName).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
        System.out.println(list.size());
        List<NmsSmsTmplExcelVo> newList = new ArrayList<>();
        for (NmsSmsTmplExcelVo nmsSmsTmplExcelVo : list) {
            nmsSmsTmplExcelVo.setText5("测试数据插入");
            newList.add(nmsSmsTmplExcelVo);
        }


//        OutputStream out = new FileOutputStream("D:/test2.xlsx");
        //ExcelWriter writer = EasyExcelFactory.getWriterWithTemp(inputStream, out, ExcelTypeEnum.XLSX, true);

        EasyExcel.write("D:/test1.xlsx", NmsSmsTmplExcelVo.class).sheet("Student").doWrite(newList);
        stopWatch.stop();
        System.out.println("总耗时："+stopWatch.getTotalTimeSeconds());
    }
}
