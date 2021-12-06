package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {


        // This method is to get the list of test requests having status as 'LAB_TEST_COMPLETED'
        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);


    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        // This is to get the currently logged in user
        User doctor = userLoggedInService.getLoggedInUser();

        // This is to return the List of test requests available to see to the Doctor user
        return testRequestQueryService.findByDoctor(doctor);
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        // Implement this method

        // Implement this method to assign a particular test request to the current doctor(logged in user)
        //Create an object of TestRequest class and use the assignForConsultation() method of testRequestUpdateService
        // to assign the particular id to the current user
        // return the above created object
        // For reference check the method assignForLabTest() method from LabRequestController class
        try {

            // This is to get the currently logged in user
            User doctor = userLoggedInService.getLoggedInUser();

            // This is to assign the consultation id to the doctor user
            return testRequestUpdateService.assignForConsultation(id,doctor);

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        try {

            // This is to get the currently logged in user
            User doctor =userLoggedInService.getLoggedInUser();

            // This is to update the consulation with the cosultation id and the doctor object
            return testRequestUpdateService.updateConsultation(id,testResult,doctor);


        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}
