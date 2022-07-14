package cn.mesmile.admin.common.oss;

import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zb
 * @Description minio上传模板
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
        } catch (Throwable e) {
            
        }
    }

    public Bucket getBucket() {
        try {
            return getBucket(ossProperties.getBucketName());
        } catch (Throwable var2) {
            throw var2;
        }
    }

    public Bucket getBucket(String bucketName) {
        try {
            Optional<Bucket> bucketOptional = minioClient.listBuckets().stream().filter((bucket) -> {
                return bucket.name().equals(bucketName);
            }).findFirst();
            return bucketOptional.orElse(null);
        } catch (Throwable var3) {

        }
        return null;
    }

    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Throwable var2) {

        }
        return null;
    }

    @Override
    public void removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Throwable e) {

        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            BucketExistsArgs build = BucketExistsArgs.builder().bucket(bucketName).build();
            return minioClient.bucketExists(build);
        } catch (Throwable var3) {

        }
        return false;
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        try {
            this.copyFile(bucketName, fileName, destBucketName, fileName);
        } catch (Throwable var5) {
            throw var5;
        }
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        try {
            minioClient.copyObject(((CopyObjectArgs.builder().source(((CopySource.builder().bucket(bucketName)).object(fileName)).build()).bucket(destBucketName)).object(destFileName)).build());
        } catch (Throwable e) {

        }
    }

    @Override
    public OssFile statFile(String fileName) {
        try {
            return this.statFile(this.ossProperties.getBucketName(), fileName);
        } catch (Throwable var3) {
            throw var3;
        }
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        try {
            StatObjectResponse stat = minioClient.statObject(((StatObjectArgs.builder().bucket(bucketName)).object(fileName)).build());
            OssFile ossFile = new OssFile();
            ossFile.setName(StrUtil.isEmpty(stat.object()) ? fileName : stat.object());
            ossFile.setUrl(this.fileLink(ossFile.getName()));
            ossFile.setHash(String.valueOf(stat.hashCode()));
            ossFile.setLength(stat.size());
            LocalDateTime localDateTime = stat.lastModified().toLocalDateTime();
            ossFile.setPutTime(localDateTime);
            ossFile.setContentType(stat.contentType());
            return ossFile;
        } catch (Throwable e) {

        }
        return null;
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
        try {
            return ossProperties.getEndpoint().concat("/").concat(ossProperties.getBucketName()).concat("/").concat(fileName);
        } catch (Throwable e) {

        }
        return "";
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        try {
            return this.ossProperties.getEndpoint().concat("/").concat(ossProperties.getBucketName()).concat("/").concat(fileName);
        } catch (Throwable e) {

        }
        return "";
    }

    @Override
    public AdminFile putFile(MultipartFile file) {
        try {
            return this.putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
        } catch (Throwable e) {
            throw e;
        }
    }

    @Override
    public AdminFile putFile(String fileName, MultipartFile file) {
        try {
            return putFile(ossProperties.getBucketName(), fileName, file);
        } catch (Throwable e) {
            throw e;
        }
    }

    @Override
    public AdminFile putFile(String bucketName, String fileName, MultipartFile file) {
        try {
            return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
        } catch (Throwable e) {

        }
        return null;
    }

    @Override
    public AdminFile putFile(String fileName, InputStream stream) {
        try {
            return putFile(ossProperties.getBucketName(), fileName, stream);
        } catch (Throwable e) {
            throw e;
        }
    }

    @Override
    public AdminFile putFile(String bucketName, String fileName, InputStream stream) {
        try {
            return putFile(bucketName, fileName, stream, "application/octet-stream");
        } catch (Throwable e) {

        }
        return null;
    }

    public AdminFile putFile(String bucketName, String fileName, InputStream stream, String contentType) {
        try {
            this.makeBucket(bucketName);
            String originalName = fileName;
            fileName = this.getFileName(fileName);
            minioClient.putObject(((PutObjectArgs.builder().bucket(bucketName)).object(fileName)).stream(stream, (long)stream.available(), -1L).contentType(contentType).build());
            AdminFile file = new AdminFile();
            file.setOriginalName(originalName);
            file.setName(fileName);
            file.setDomain(this.getOssHost(bucketName));
            file.setUrl(this.fileLink(bucketName, fileName));
            return file;
        } catch (Throwable e) {

        }
        return null;
    }

    @Override
    public void removeFile(String fileName) {
        try {
            this.removeFile(this.ossProperties.getBucketName(), fileName);
        } catch (Throwable e) {

        }
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(((RemoveObjectArgs.builder().bucket(bucketName)).object(fileName)).build());
        } catch (Throwable e) {

        }
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        try {
            this.removeFiles(this.ossProperties.getBucketName(), fileNames);
        } catch (Throwable e) {

        }
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        try {
            Stream<DeleteObject> stream = fileNames.stream().map(DeleteObject::new);
            RemoveObjectsArgs.Builder builder = RemoveObjectsArgs.builder().bucket(bucketName);
            minioClient.removeObjects(builder.objects(stream::iterator).build());
        } catch (Throwable e) {

        }
    }

    private String getFileName(String originalFilename) {
        return ossRule.setName(originalFilename);
    }

    public String getPresignedObjectUrl(String bucketName, String fileName, Integer expires) {
        try {
            return minioClient.getPresignedObjectUrl(((GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName)).object(fileName)).expiry(expires).build());
        } catch (Throwable e) {

        }
        return null;
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
        return this.ossProperties.getEndpoint() + "/" + bucketName;
    }

    public String getOssHost() {
        return this.getOssHost(ossProperties.getBucketName());
    }


}
