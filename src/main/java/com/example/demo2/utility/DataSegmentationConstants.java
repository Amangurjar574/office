package com.example.demo2.utility;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
public class DataSegmentationConstants {
    public static final  String messagetemplateFirst = "Dear \r\n"
            + "\r\n"
            + "We've received a request to set your password for your account.\r\n"
            + "\r\n"
            + "To verify your identity, please enter the strong password:\r\n"
            + "\r\n"
            + "[";
    public static final String getMessagetemplateLast="]\r\n"
            + "\r\n"
            + "This url is valid for 10 minutes.\r\n"
            + "\r\n"
            + "If you did not request this password set, please ignore this email.\r\n"
            + "\r\n"
            + "Thank you,";
}
