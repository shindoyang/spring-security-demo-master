package com.spring.security.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 支持按 Excel 格式读取 详情参考 ：
 * https://alibaba-easyexcel.github.io/quickstart/read.html#simpleReadExcel 如下事例
 */
@Data
public class NmsSmsTmplExcelVo {

    @ExcelProperty(index = 0)
    private String mobile;

    @ExcelProperty(index = 1)
    private String text1;

    @ExcelProperty(index = 2)
    private String text2;

    @ExcelProperty(index = 3)
    private String text3;

    @ExcelProperty(index = 4)
    private String text4;

    @ExcelProperty(index = 5)
    private String text5;

    @ExcelProperty(index = 6)
    private String text6;

    @ExcelProperty(index = 7)
    private String text7;

    @ExcelProperty(index = 8)
    private String text8;

    @ExcelProperty(index = 9)
    private String text9;

    @ExcelProperty(index = 10)
    private String text10;


    public String[] getTextArray() {
        return new String[]{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10};
    }

}
