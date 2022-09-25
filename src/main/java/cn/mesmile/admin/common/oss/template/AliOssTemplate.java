package cn.mesmile.admin.common.oss.template;

import cn.mesmile.admin.common.oss.domain.AdminFile;
import cn.mesmile.admin.common.oss.domain.OssFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author zb
 * @Description
 */
public class AliOssTemplate  implements OssTemplate {


    @Override
    public void makeBucket(String bucketName) {

    }

    @Override
    public void removeBucket(String bucketName) {

    }

    @Override
    public boolean bucketExists(String bucketName) {
        return false;
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {

    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {

    }

    @Override
    public OssFile statFile(String fileName) {
        return null;
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        return null;
    }

    @Override
    public String filePath(String fileName) {
        return null;
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return null;
    }

    @Override
    public String fileLink(String fileName) {
        return null;
    }

    @Override
    public String fileLink(String bucketName, String originalFilename) {
        return null;
    }

    @Override
    public AdminFile putFile(MultipartFile file) {
        return null;
    }

    @Override
    public AdminFile putFile(String originalFilename, MultipartFile file) {
        return null;
    }

    @Override
    public AdminFile putFile(String bucketName, String originalFilename, MultipartFile file) {
        return null;
    }

    @Override
    public AdminFile putFile(String originalFilename, InputStream stream) {
        return null;
    }

    @Override
    public AdminFile putFile(String bucketName, String originalFilename, InputStream stream) {
        return null;
    }

    @Override
    public String preview(String fileName) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> download(String fileName) {
        return null;
    }

    @Override
    public void removeFile(String originalFilename) {

    }

    @Override
    public void removeFile(String bucketName, String fileName) {

    }

    @Override
    public void removeFiles(List<String> fileNames) {

    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {

    }
}
