package com.project.hack.controller;

import com.project.hack.dto.response.PhotoDto;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final AwsService awsService;

    @PostMapping("/images")
    public String upload(@RequestParam("images") List<MultipartFile> multipartFile) throws IOException {
        awsService.uploadFile(multipartFile);

        return "test";
    }

    @PostMapping("/api/mypage")
    public List<PhotoDto> uploadProfilePic(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestPart(value = "file") List<MultipartFile> multipartFile) throws Exception {
        List<PhotoDto> photoDtos = awsService.uploadFile(multipartFile);
        return photoDtos;
    }

//    @PostMapping("/api/mypage")
//    public String uploadFile(
//            @RequestPart(value = "file") List<MultipartFile> multipartFile, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//            if(userDetails.getUser() == null) {
//                new NullPointerException("로그인 해주세요");
//            }
//        List<PhotoDto> profileImageList = awsService.uploadFile(multipartFile);
//        for(PhotoDto profileImage : profileImageList) {
//            photo
//        }
//
//            return fileName;
//    }





}
