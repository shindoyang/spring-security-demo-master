package com.admission.security.exception.file;


import com.admission.security.exception.BaseException;

public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }
}
