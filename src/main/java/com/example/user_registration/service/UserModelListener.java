package com.example.user_registration.service;

import com.example.user_registration.model.Admin;
import com.example.user_registration.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class UserModelListener extends AbstractMongoEventListener {
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public UserModelListener(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {
        Object user = event.getSource();

        if (user instanceof Admin) { // generating next Id for Admin
            if (((Admin) user).getId() < 1) {
                ((Admin) user).setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            }
        } else { // for User
            if (((User) user).getId() < 1) {
                ((User) user).setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            }
        }
    }
}
