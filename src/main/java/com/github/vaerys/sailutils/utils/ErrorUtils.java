package com.github.vaerys.sailutils.utils;

import com.github.vaerys.sailutils.handlers.StringHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorUtils {

    final static Logger logger = LoggerFactory.getLogger(ErrorUtils.class);

    public static void sendStack(Exception e) {
        StringHandler s = new StringHandler(ExceptionUtils.getStackTrace(e));
        s.setContent(s.substring(0, s.length() - 2));
        if (!s.toString().endsWith(")")) {
            s.append(")");
        }
        logger.error(s.toString());
    }
}
