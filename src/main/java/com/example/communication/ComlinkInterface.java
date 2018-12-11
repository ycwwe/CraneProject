package com.example.communication;

import com.example.quartz.job.sender.MessageForm;

public interface ComlinkInterface {
    boolean send(Message message);
    MessageForm getMessage();
}
