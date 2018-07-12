package com.service.service.constant;

import com.service.service.Constants;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des:
 */
public class FileStore {
    public static final int PROTOCOL_VERSION = 1;

    public static final String GIT_LFS_META_MIME = "application/vnd.git-lfs+json";

    public static final String REGEX_PATH = "^(.*?)/(r)/(.*?)/info/lfs/objects/(batch|" + Constants.REGEX_SHA256 + ")";
    public static final int REGEX_GROUP_BASE_URI = 1;
    public static final int REGEX_GROUP_PREFIX = 2;
    public static final int REGEX_GROUP_REPOSITORY = 3;
    public static final int REGEX_GROUP_ENDPOINT = 4;

}
