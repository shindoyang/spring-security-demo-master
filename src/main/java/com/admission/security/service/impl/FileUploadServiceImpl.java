package com.admission.security.service.impl;

import com.admission.security.VO.FileRequestVO;
import com.admission.security.common.entity.JsonResult;
import com.admission.security.common.enums.ResultCode;
import com.admission.security.common.utils.ResultTool;
import com.admission.security.config.AdmissionConfig;
import com.admission.security.config.service.UserToolService;
import com.admission.security.entity.NmsSmsTmplExcelVo;
import com.admission.security.entity.SysUserFile;
import com.admission.security.service.FileUploadService;
import com.admission.security.utils.FileUtils;
import com.admission.security.utils.IdUtils;
import com.admission.security.utils.MobileUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mfexcel.sensitive.engine.SensitiveEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.max-rows}")
    int fileMaxRows;

    @Autowired
    UserToolService userToolService;

    @Resource
    SysUserFileServiceImpl sysUserFileService;

    @Override
    public JsonResult uploadFile(HttpServletRequest request, MultipartFile file, FileRequestVO param) {
        //1、文件保存
        File saveFile = null;
        if (null != file && StringUtils.isNotBlank(file.getOriginalFilename())) {
            // 校验文件后缀
            if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                return ResultTool.fail(ResultCode.FAIL_ERROR);
            }

            String originalFilename = file.getOriginalFilename();
            log.info("originalFilename = " + originalFilename);
            if (originalFilename.length() > 150) {
                return ResultTool.fail(ResultCode.FAIL_LENGTH_OVER_ERROR);
            }

            QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
            wrapper.eq("file_name", originalFilename);
            wrapper.eq("account", userToolService.getLoginUser(request));
            SysUserFile dbsysUserFile = sysUserFileService.getOne(wrapper);
            if (null != dbsysUserFile) {
                return ResultTool.fail(ResultCode.FAIL_FILE_REPEAT_ERROR);
            }

            // 临时文件名
            String fileName = IdUtils.randomUUID();
            saveFile = FileUtils.createNewFile(AdmissionConfig.getUploadPath() + "/", (fileName + "_temp.xlsx"));
            // 转存
            FileUtils.saveFile(file, saveFile);


            //2、校验文件
            if (null != saveFile) {
                // 读取文件
                List<NmsSmsTmplExcelVo> list = null;
                // 模板头部占了两行
                int lineIdx = 2;
                if (file != null) {
                    list = EasyExcel.read(saveFile).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
                }
                // 判断文件是否为空
                if (list == null || list.size() <= 0) {
                    return ResultTool.fail(ResultCode.FAIL_EMPTY);
                }
                // 判断文件条数是否超过限制
                if (list.size() > fileMaxRows) {
                    return ResultTool.fail(ResultCode.FAIL_OVER_MAX);
                }
                //敏感词校验
                for (NmsSmsTmplExcelVo vo : list) {
                    //校验文件模板：首列是否手机号
                    if (!MobileUtil.isMobileNO(vo.getMobile())) {
                        return ResultTool.fail(ResultCode.FAIL_TEMPLATE_ERROR);
                    }
                    String[] textArray = vo.getTextArray();
                    for (int i = 0; i < textArray.length; i++) {
                        if (textArray[i] != null) {
                            List<String> sensitiveList = SensitiveEngine.getInstance().findAllSensitive(textArray[i]);
                            if (sensitiveList != null && sensitiveList.size() > 0) {
                                return new JsonResult(false, ResultCode.FAIL_SENSITIVE_ERROR.getCode(), String.format(ResultCode.FAIL_SENSITIVE_ERROR.getMessage(), ""), sensitiveList.stream().distinct().limit(3).collect(Collectors.joining(",", "【", "】")));
                            }
                        }
                    }
                }

            }
            SysUserFile sysUserFile = new SysUserFile();
            sysUserFile.setAccount(userToolService.getLoginUser(request));
            sysUserFile.setFileName(originalFilename);
            sysUserFile.setFileUrl(fileName);
            sysUserFile.setCreateTime(new Date());
            sysUserFileService.save(sysUserFile);
        }
        return ResultTool.success();
    }

    @Override
    public JsonResult getDownloadData(HttpServletRequest request, HttpServletResponse response, FileRequestVO param) throws Exception {

        try {
            QueryWrapper<SysUserFile> wrapper = new QueryWrapper<>();
            wrapper.eq("id", param.getFileId());
            SysUserFile sysUserFile = sysUserFileService.getOne(wrapper);

            if (null == sysUserFile) {
                throw new Exception(ResultCode.FAIL_NOT_EXIST.getMessage());
            }

            //校验当前用户是否有该文件的操作权限
            String loginUser = userToolService.getLoginUser(request);
            if (null != sysUserFile && loginUser != null && !loginUser.equals(sysUserFile.getAccount())) {
                throw new Exception(ResultCode.FAIL_NO_FILE_AUTH.getMessage());
            }

            String filePath = AdmissionConfig.getUploadPath() + "/" + sysUserFile.getFileUrl();
            String finishFilePath = filePath + ".xlsx";
            String fileOrgName = sysUserFile.getFileName().substring(0, sysUserFile.getFileName().lastIndexOf(".xlsx"));

            File file = new File(finishFilePath);
            List<NmsSmsTmplExcelVo> nmsFileList = EasyExcel.read(file).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();

            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(fileOrgName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), NmsSmsTmplExcelVo.class).sheet("Sheet1").doWrite(nmsFileList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(ResultCode.FAIL_FILE_DOWNLOAD_ERROR.getMessage());
        }

        return null;
    }

}
