package com.example.communication.uselesss;

import com.example.quartz.job.sender.MessageForm;

public interface ComlinkInterface {
        boolean send(String id,MessageForm message);

        MessageForm getMessage();
        }
