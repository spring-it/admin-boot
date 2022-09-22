package cn.mesmile.admin.common.oss.template;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.exceptions.OssException;
import cn.mesmile.admin.common.oss.OssProperties;
import cn.mesmile.admin.common.oss.domain.AdminFile;
import cn.mesmile.admin.common.oss.domain.OssFile;
import cn.mesmile.admin.common.oss.enums.PolicyTypeEnum;
import cn.mesmile.admin.common.oss.rule.OssRule;
import cn.mesmile.admin.common.result.ResultCode;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import okhttp3.Headers;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zb
 * @Description mini操作模板
 */
public class MinioTemplate implements OssTemplate {

    private final OssRule ossRule;

    private final MinioClient minioClient;

    private final OssProperties ossProperties;

    public MinioTemplate(OssRule ossRule, MinioClient minioClient,OssProperties ossProperties) {
        this.ossRule = ossRule;
        this.minioClient = minioClient;
        this.ossProperties = ossProperties;
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            BucketExistsArgs build = BucketExistsArgs.builder()
                    .bucket(bucketName).build();
            if (!minioClient.bucketExists(build)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
                String policyTypeEnum = getPolicyTypeEnum(bucketName, PolicyTypeEnum.READ);
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyTypeEnum).build());
            }
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    public Bucket getBucket() {
        return getBucket(ossProperties.getBucketName());
    }

    public Bucket getBucket(String bucketName) {
        try {
            Optional<Bucket> bucketOptional = minioClient.listBuckets().stream().filter((bucket) -> {
                return bucket.name().equals(bucketName);
            }).findFirst();
            return bucketOptional.orElse(null);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public void removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            BucketExistsArgs build = BucketExistsArgs.builder().bucket(bucketName).build();
            return minioClient.bucketExists(build);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        try {
            this.copyFile(bucketName, fileName, destBucketName, fileName);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        try {
            CopySource copySource = CopySource.builder()
                    .bucket(bucketName).object(fileName).build();
            CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
                    .source(copySource)
                    .bucket(destBucketName)
                    .object(destFileName).build();
            minioClient.copyObject(copyObjectArgs);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public OssFile statFile(String fileName) {
        try {
            return statFile(ossProperties.getBucketName(), fileName);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        try {
            StatObjectResponse stat = minioClient.statObject(((StatObjectArgs.builder().bucket(bucketName)).object(fileName)).build());
            OssFile ossFile = new OssFile();
            ossFile.setName(StrUtil.isEmpty(stat.object()) ? fileName : stat.object());
            ossFile.setUrl(fileLink(ossFile.getName()));
            ossFile.setHash(String.valueOf(stat.hashCode()));
            ossFile.setLength(stat.size());
            LocalDateTime localDateTime = stat.lastModified().toLocalDateTime();
            ossFile.setPutTime(localDateTime);
            ossFile.setContentType(stat.contentType());
            return ossFile;
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public String filePath(String fileName) {
        return ossProperties.getBucketName().concat("/").concat(fileName);
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return bucketName.concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String fileName) {
            return ossProperties.getEndpoint().concat("/")
                    .concat(ossProperties.getBucketName()).concat("/")
                    .concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String originalFilename) {
            return ossProperties.getEndpoint().concat("/")
                    .concat(ossProperties.getBucketName()).concat("/")
                    .concat(originalFilename);
    }

    @Override
    public AdminFile putFile(MultipartFile file) {
        return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
    }

    @Override
    public AdminFile putFile(String fileName, MultipartFile file) {
        return putFile(ossProperties.getBucketName(), fileName, file);
    }

    @Override
    public AdminFile putFile(String bucketName, String originalFilename, MultipartFile file) {
        try {
            return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public AdminFile putFile(String originalFilename, InputStream stream) {
        return putFile(ossProperties.getBucketName(), originalFilename, stream);
    }

    @Override
    public AdminFile putFile(String bucketName, String originalFilename, InputStream stream) {
        return putFile(bucketName, originalFilename, stream, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public AdminFile putFile(String bucketName, String originalFilename, InputStream stream, String contentType) {
        try {
            makeBucket(bucketName);
            String fileName = getFileName(originalFilename);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName).object(fileName)
                    .stream(stream, stream.available(), -1L)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(putObjectArgs);
            return new AdminFile(fileLink(bucketName, fileName), getOssHost(bucketName)
                    , fileName, originalFilename);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    /**
     * 预览图片 fileName 为带有 /年月/年月日/文件名
     * @param fileName
     * @return
     */
    @Override
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .bucket(ossProperties.getBucketName())
                .object(fileName)
                .method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }


    /**
     * fileName 为带有 /年月/年月日/文件名
     * @param fileName
     * @return
     */
    @Deprecated
    @Override
    public ResponseEntity<byte[]> download(String fileName) {
        ResponseEntity<byte[]> responseEntity = null;
        GetObjectArgs build = GetObjectArgs.builder().bucket(ossProperties.getBucketName())
                .object(fileName)
                .build();
        try (
                GetObjectResponse in = minioClient.getObject(build);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ){
            IOUtils.copy(in, out);
            //封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, Constants.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Collections.singletonList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
        return responseEntity;
    }

    @Override
    public void removeFile(String fileName) {
        removeFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName).object(fileName).build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        removeFiles(ossProperties.getBucketName(), fileNames);
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        try {
            Stream<DeleteObject> stream = fileNames.stream().map(DeleteObject::new);
            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(stream::iterator).build();
            minioClient.removeObjects(removeObjectsArgs);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    private String getFileName(String originalFilename) {
        return ossRule.setName(originalFilename);
    }

    public String getPresignedObjectUrl(String bucketName, String fileName, Integer expires) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(expires).build();
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            throw new OssException(ResultCode.FAILURE, "minio异常", e);
        }
    }

    public String getPolicyTypeEnum(PolicyTypeEnum policyType) {
        return getPolicyTypeEnum(ossProperties.getBucketName(), policyType);
    }

    public static String getPolicyTypeEnum(String bucketName, PolicyTypeEnum policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"Statement\": [\n");
        builder.append("        {\n");
        builder.append("            \"Action\": [\n");
        switch(policyType) {
            case WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            case READ_WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucket\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            default:
                builder.append("                \"s3:GetBucketLocation\"\n");
        }

        builder.append("            ],\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n");
        builder.append("        },\n");
        if (PolicyTypeEnum.READ.equals(policyType)) {
            builder.append("        {\n");
            builder.append("            \"Action\": [\n");
            builder.append("                \"s3:ListBucket\"\n");
            builder.append("            ],\n");
            builder.append("            \"Effect\": \"Deny\",\n");
            builder.append("            \"Principal\": \"*\",\n");
            builder.append("            \"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n");
            builder.append("        },\n");
        }

        builder.append("        {\n");
        builder.append("            \"Action\": ");
        switch(policyType) {
            case WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            case READ_WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:GetObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            default:
                builder.append("\"s3:GetObject\",\n");
        }

        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n");
        builder.append("        }\n");
        builder.append("    ],\n");
        builder.append("    \"Version\": \"2012-10-17\"\n");
        builder.append("}\n");
        return builder.toString();
    }

    public String getOssHost(String bucketName) {
        return ossProperties.getEndpoint() + "/" + bucketName;
    }

    public String getOssHost() {
        return getOssHost(ossProperties.getBucketName());
    }

}
