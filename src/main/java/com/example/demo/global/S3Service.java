//package com.example.demo.global;
//
//import com.amazonaws.SdkClientException;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Component
//@Service
//public class S3Service {
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//    public String upload(MultipartFile multipartFile, String dirName, String userName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("Failed to convert MultipartFile to file"));
//        return uploadToS3(uploadFile, dirName, userName);
//    }
//
//    //AWS S3에 이미지 업로드 후 업로드된 이미지의 url 반환
//    private String uploadToS3(File uploaFile, String dirName, String userName){
//        String fileName = dirName + "/" + userName;
//        amazonS3Client.putObject(
//                new PutObjectRequest(bucket, fileName, uploaFile)
//                        .withCannedAcl(CannedAccessControlList.PublicRead)
//        );
//        removeDummyFile(uploaFile);
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    // AWS S3 버켓 내의 기존의 파일 삭제 메소드
//    public void deleteFile(String fileName) throws IOException{
//        try{
//            amazonS3Client.deleteObject(bucket, fileName);
//        } catch (SdkClientException e){
//            throw new IOException("An error has been occurred during deleting file from s3");
//        }
//    }
//
//    //MultipartFile -> File 로 변환 시 로컬에 생성되는 더미 파일 삭제
//    private void removeDummyFile(File targetFile) {
//        if(targetFile.delete()){
//            System.out.println("파일 삭제 성공");
//        }
//        else{
//            System.out.println("파일 삭제 실패");
//        }
//    }
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File converFile = new File(file.getOriginalFilename());
//
//        if(converFile.createNewFile()){
//            try(FileOutputStream fos = new FileOutputStream(converFile)){
//                fos.write(file.getBytes());
//            }
//            return Optional.of(converFile);
//        }
//        return Optional.empty();
//    }
//
//}
