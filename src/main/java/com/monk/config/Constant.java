package com.monk.config;

import okhttp3.MediaType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constant {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String FILE_UPLOAD_DIR = "upload-dir";
    public static final boolean IS_PRODUCTION = true;
}
