package com.example.user_registration.service;

import com.example.user_registration.model.Admin;
import com.example.user_registration.model.Company;
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
        Object objectToConvert = event.getSource();

        if (objectToConvert instanceof Admin) { // generating next Id for Admin
            if (((Admin) objectToConvert).getId() < 1) {
                ((Admin) objectToConvert).setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            }
        } else if (objectToConvert instanceof User) { // for User
            if (((User) objectToConvert).getId() < 1) {
                ((User) objectToConvert).setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            }
        } else { // for Company
            if (((Company) objectToConvert).getId() < 1) {
                ((Company) objectToConvert).setId(sequenceGeneratorService.generateSequence(Company.SEQUENCE_NAME));
            }
        }

    }
}
