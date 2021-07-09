package com.admission.security.VO;

import lombok.Data;

@Data
public class FileRequestVO {

    private Integer page;

    private Integer size;

    private String fileId;

    private String fileName;

    private String startTime;

    private String endTime;

}
