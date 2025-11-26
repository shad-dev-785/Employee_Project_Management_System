package com.agile.utility;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Utility {
    public String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return userDetails.getUsername();
    }
    public static String getFileExtension(String fileName) {
        String extension = ".xlsx";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = "." + fileName.substring(index + 1);
        }
        return extension;
    }
}
