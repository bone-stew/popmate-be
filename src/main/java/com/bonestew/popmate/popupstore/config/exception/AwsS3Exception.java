package com.bonestew.popmate.popupstore.config.exception;

import com.bonestew.popmate.exception.PopMateException;

public class AwsS3Exception extends PopMateException {

    public AwsS3Exception() {
        super("An error occurred during AWS S3 operation.");
    }
}
