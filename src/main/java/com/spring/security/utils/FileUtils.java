package com.spring.security.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.spring.security.exception.file.FileSizeLimitExceededException;
import com.spring.security.exception.file.InvalidExtensionException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    public static final String TXT_PATH = ClassLoaderUtil.getClassPath()
            + File.separator + "TXT" + File.separator;

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public static void writeBytes(String filePath, byte[] b) throws IOException {

        FileOutputStream os = null;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            os = new FileOutputStream(file);
            os.write(b, 0, b.length);
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension) throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = FileUtils.getExtension(file);
        if (allowedExtension != null && !FileUtils.isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension, fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        return FileUtils.class.getResource("/").getPath();
    }

    public static File createNewFile(String preDic, String pathName) {
        if (preDic == null) {
            preDic = getPath();
        }
        File file = new File(preDic + pathName);
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return file;
    }

    public static File createNewDic(String preDic, String pathName) {
        if (preDic == null) {
            preDic = getPath();
        }
        File file = new File(preDic + pathName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File readFileByClassPath(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readFile(String pathName) {
        return new File(pathName);
    }

    /**
     * 将MultipartFile转换为File
     *
     * @param inFile
     */
    public static void saveFile(MultipartFile inFile, File dirFile) {
        InputStream fileStream = null;
        try {
            fileStream = inFile.getInputStream();
            org.apache.commons.io.FileUtils.copyInputStreamToFile(fileStream, dirFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                fileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 将File转换为MultipartFile
     *
     * @param filePath
     */
    public static MultipartFile transFile(String filePath) {
        InputStream fileStream = null;
        MultipartFile multipartFile = null;
        try {
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileStream != null) {
                try {
                    //关闭流
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return multipartFile;

    }

    /**
     * 文件下载
     */
    public static void fileDownloadByPath(String filePath, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(filePath);
            String fileName = resource.getFilename();
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            response.addHeader("Content-Length", "" + resource.getFile().length());
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取txt文档的内容,忽略
     *
     * @param file  文件
     * @param split 分隔符
     * @param limit 分隔符
     * @return 词组
     */
    public static Set<String> readWordFromFile(File file, String split, int limit) {
        InputStream is = null;
        BufferedReader br = null;
        Set<String> wordList = new HashSet<>();
        Set<String> lowerWords = new HashSet<>();
        try {
            is = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while (null != (line = br.readLine())) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                String[] words = line.split(split);
                for (String word : words) {
                    if (StringUtils.isEmpty(word)) {
                        continue;
                    }
                    word = word.replace("\uFEFF", "");
                    String lowerWord = word.trim().toLowerCase();
                    if (lowerWords.contains(lowerWord) || word.length() > limit) {
                        continue;
                    }
                    lowerWords.add(lowerWord);
                    wordList.add(word);
                }
            }
        } catch (Exception e) {
            System.out.println(file.getName() + "读取文件时异常");
        } finally {
            IOUtils.closeQuietly(br, is);
        }
        return wordList;
    }

    /**
     * 导出文本文件
     *
     * @param response
     * @param jsonString
     * @param fileName
     */
    public static void writeToTxt(String jsonString, String fileName, HttpServletResponse response) {

        //设置响应的字符集
        response.setCharacterEncoding("utf-8");
        //设置响应内容的类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader(
                "Content-Disposition",
                "attachment; filename=" + fileName + ".txt");//通过后缀可以下载不同的文件格式
        BufferedOutputStream buff = null;
        OutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(delNull(jsonString).getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 将一个字符串转化为输入流
     *
     * @param sInputString
     * @return
     */
    public static InputStream getStrToStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 如果字符串对象为 null，则返回空字符串，否则返回去掉字符串前后空格的字符串
     *
     * @param str
     * @return
     */
    public static String delNull(String str) {
        String returnStr = "";
        if (StringUtils.isNotBlank(str)) {
            returnStr = str.trim();
        }
        return returnStr;
    }

    public static File exportWebExcel(HttpServletResponse response, String excelName, String sheetName, Class clazz, List data, String path) throws Exception {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(file, clazz)
                .sheet(sheetName)
                .doWrite(data);
        return file;

    }

    /**
     * @param exportPath
     * @param list
     * @description: 导出zip文件
     */
    public static void exportZipFile(String exportPath, LinkedList<File> list) {

        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(exportPath);
            boolean b = zipFile.exists();
            if (!b) {
                zipFile.createNewFile();
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (File i : list) {
                ZipEntry zipEntry = new ZipEntry(i.getName());
                zos.putNextEntry(zipEntry);
                int len;
                byte[] buffer = new byte[1024 * 4];
                FileInputStream fis = new FileInputStream(i);
                while (((len = fis.read(buffer)) > 0)) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
