package com.resume.backend.controller;

import com.resume.backend.service.ResumeRequest;
import com.resume.backend.service.ResumeService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("generate")
    public ResponseEntity<JSONObject> getResumeData(@RequestBody ResumeRequest resumeRequest) throws IOException {
        JSONObject response = resumeService.generateResumeResponse(resumeRequest.userDescription());
        return ResponseEntity.ok(response);
    }
}
