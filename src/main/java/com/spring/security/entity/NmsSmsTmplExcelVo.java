package com.spring.security.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 支持按 Excel 格式读取 详情参考 ：
 * https://alibaba-easyexcel.github.io/quickstart/read.html#simpleReadExcel 如下事例
 */
@Data
public class NmsSmsTmplExcelVo {

    @ExcelProperty(value = "mobile", index = 0)
    private String mobile;

    @ExcelProperty(value = "text1", index = 1)
    private String text1;

    @ExcelProperty(value = "text2", index = 2)
    private String text2;

    @ExcelProperty(value = "text3", index = 3)
    private String text3;

    @ExcelProperty(value = "text4", index = 4)
    private String text4;

    @ExcelProperty(value = "text5", index = 5)
    private String text5;

    @ExcelProperty(value = "text6", index = 6)
    private String text6;

    @ExcelProperty(value = "text7", index = 7)
    private String text7;

    @ExcelProperty(value = "text8", index = 8)
    private String text8;

    @ExcelProperty(value = "text9", index = 9)
    private String text9;

    @ExcelProperty(value = "text10", index = 10)
    private String text10;


    public String[] getTextArray() {
        return new String[]{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10};
    }

}
