package cn.mesmile.admin.common.mongo.dao;

import cn.mesmile.admin.common.mongo.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author zb
 * @Description
 */
public interface StudentDao extends MongoRepository<Student, String> {

    /**
     * 可根据需求自己定义方法, 无需对方法进行实现
     * @param studentName
     * @return 查询结果
     */
    List<Student> getAllByName(String studentName);

}
