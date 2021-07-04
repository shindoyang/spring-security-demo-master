package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mfexcel.sensitive.engine.SensitiveEngine;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.dao.SysUserSchoolRelationDao;
import com.spring.security.entity.FileUploadEntity;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import com.spring.security.entity.SysUserSchoolRelation;
import com.spring.security.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.max-rows}")
    int fileMaxRows;

    @Resource
    SysUserSchoolRelationDao sysUserSchoolRelationDao;

    @Override
    public JsonResult uploadFile(File file, FileUploadEntity param, String username) {

        SysUserSchoolRelation relation = sysUserSchoolRelationDao.queryByUsername(username);
        System.out.println(relation.toString());

        if (file != null) {
            // 校验模板文件
            List<NmsSmsTmplExcelVo> list = null;
            int lineIdx = 2; // 模板头部占了两行
            if (file != null) {
                list = EasyExcel.read(file).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
            }

            if (list == null || list.size() <= 0) {
                return ResultTool.fail(ResultCode.FAIL_EMPTY);
            }
            if (list.size() > fileMaxRows) {
                return ResultTool.fail(ResultCode.FAIL_OVER_MAX);
            }

            for (NmsSmsTmplExcelVo vo : list) {
                lineIdx++;
                String[] textArray = vo.getTextArray();
                for (int i = 0; i < textArray.length; i++) {
                    if (textArray[i] != null) {
                        //todo 必须检查第一列是否手机号
                        List<String> sensitiveList = SensitiveEngine.getInstance().findAllSensitive(textArray[i]);
                        if (sensitiveList != null && sensitiveList.size() > 0) {
                            return new JsonResult(false, ResultCode.FAIL_SENSITIVE_ERROR.getCode(), String.format(ResultCode.FAIL_SENSITIVE_ERROR.getMessage(), ""), sensitiveList.stream().distinct().limit(3).collect(Collectors.joining(",", "【", "】")));
                        }
                    }
                }

                // 释放内存
                list = null;
                // 删除临时文件
                /*if (delFileFlag) {
                    file.delete();
                }*/
            }
        }
        return ResultTool.fail(ResultCode.SUCCESS);
    }

}
