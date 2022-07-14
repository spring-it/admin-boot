package cn.mesmile.admin.common.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * Oss操作模板
 * @author zb
 */
public interface OssTemplate {

    /**
     * 创建桶
     * @param bucketName 桶名称
     */
    void makeBucket(String bucketName);

    /**
     * 根据桶名称 删除桶
     * @param bucketName 桶名称
     */
    void removeBucket(String bucketName);

    /**
     * 根据桶名称，判断桶是否存在
     * @param bucketName 桶名称
     * @return 是否存在
     */
    boolean bucketExists(String bucketName);

    /**
     * 把一个桶里面的文件，拷贝到另外一个桶
     * @param bucketName 源桶名称
     * @param fileName 文件名
     * @param destBucketName 目标桶名称
     */
    void copyFile(String bucketName, String fileName, String destBucketName);

    /**
     * 把一个桶里面的文件，拷贝到另外一个桶
     * @param bucketName 源桶名称
     * @param fileName 文件名
     * @param destBucketName 目标桶名称
     * @param destFileName 目标文件名
     */
    void copyFile(String bucketName, String fileName, String destBucketName, String destFileName);

    /**
     * 统计文件
     * @param fileName 文件名
     * @return 文件相关信息
     */
    OssFile statFile(String fileName);

    /**
     * 统计文件
     * @param bucketName 桶名称
     * @param fileName 文件名
     * @return 文件相关信息
     */
    OssFile statFile(String bucketName, String fileName);

    /**
     * 获取文件路径
     * @param fileName 文件名
     * @return 文件路径
     */
    String filePath(String fileName);

    /**
     * 获取文件路径
     * @param bucketName 桶名称
     * @param fileName 文件名
     * @return 文件路径
     */
    String filePath(String bucketName, String fileName);

    /**
     * 获取文件下载链接
     * @param fileName 文件名
     * @return 文件下载链接
     */
    String fileLink(String fileName);

    /**
     * 获取文件下载链接
     * @param bucketName 桶名称
     * @param fileName 文件名
     * @return 文件下载链接
     */
    String fileLink(String bucketName, String fileName);

    /**
     * 上传文件
     * @param file 文件
     * @return 上传结果
     */
    AdminFile putFile(MultipartFile file);

    /**
     * 上传文件
     * @param fileName 文件名称
     * @param file 文件
     * @return 上传结果
     */
    AdminFile putFile(String fileName, MultipartFile file);

    /**
     * 上传文件
     * @param bucketName 桶名称
     * @param fileName 文件名称
     * @param file 文件
     * @return 上传结果
     */
    AdminFile putFile(String bucketName, String fileName, MultipartFile file);

    /**
     * 上传文件
     * @param fileName 文件名称
     * @param stream 输入流
     * @return 上传结果
     */
    AdminFile putFile(String fileName, InputStream stream);

    /**
     * 上传文件
     * @param bucketName 桶名称
     * @param fileName 文件名称
     * @param stream 输入流
     * @return 上传结果
     */
    AdminFile putFile(String bucketName, String fileName, InputStream stream);

    /**
     * 删除文件
     * @param fileName 文件名称
     */
    void removeFile(String fileName);

    /**
     * 删除文件
     * @param bucketName 桶名称
     * @param fileName 文件名称
     */
    void removeFile(String bucketName, String fileName);

    /**
     * 删除一个或多个文件
     * @param fileNames 一个或多个文件名称
     */
    void removeFiles(List<String> fileNames);

    /**
     * 删除文件
     * @param bucketName 桶名称
     * @param fileNames 一个或多个文件名称
     */
    void removeFiles(String bucketName, List<String> fileNames);
}