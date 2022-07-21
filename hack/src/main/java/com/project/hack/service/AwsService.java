package com.project.hack.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.dto.response.PhotoDto;
import com.project.hack.model.Photo;
import com.project.hack.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service("awsS3Service")
@RequiredArgsConstructor
public class AwsService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;


    public PhotoDto uploadFile(List<MultipartFile> multipartFile) {
        System.out.println("s3에 사진 업로드 시도");
        List<PhotoDto> photoDtos = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            System.out.println("fileName : " + fileName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            System.out.println("옵젝메타데이터 성공");
            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            System.out.println("트라이 캐치 넘어감");
            PhotoDto photoDto = PhotoDto.builder()
                    .key(fileName)
                    .path(amazonS3.getUrl(bucket, fileName).toString())
                    .build();
            photoDtos.add(photoDto);

            Photo photo = new Photo(photoDto);
            photoRepository.save(photo);

        });
        System.out.println("s3에 업로드 성공");
        return photoDtos.get(0);
    }


    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    // s3에서 파일 삭제
    public String deleteFile(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
        return fileName + " removed ...";
    }

}
