package com.soprasteria.modelling.service.notification.app;

import com.soprasteria.modelling.framework.util.Tool;
import com.soprasteria.modelling.service.notification.NotificationSerevice;
public class AutoNotification {
    private static int intervals = 60 * 5;

    public static void autoSendMail() throws Exception {
        NotificationSerevice bean = new NotificationSerevice();
        bean.sendMailQueue();
    }

    public static void main(String[] args) {
        while (true) {
            try {
                autoSendMail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Tool.sleep(intervals);
        }
    }
}
