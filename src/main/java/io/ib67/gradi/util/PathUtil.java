package io.ib67.gradi.util;

import java.io.File;

public final class PathUtil {
    public static final String toUnixPath(String path){
        return path.replaceAll("\\"+File.separator,"/");
    }

}
