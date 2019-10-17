package com.hanmj.common;

import com.hanmj.bean.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    /**
     * 通过springSecurity获取当前正在访问的用户
     * @return
     */
    public static Hr getcurrentUser(){
        return (Hr)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
