package cn.mesmile.admin.common.mongo.controller;

import cn.mesmile.admin.common.mongo.dao.StudentDao;
import cn.mesmile.admin.common.mongo.entity.Student;
import cn.mesmile.admin.common.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zb
 * @Description
 */
@RequestMapping("/api/v1/student")
@RestController
public class StudentController {

    @Resource
    private StudentDao studentDao;

    @GetMapping("/save")
    public R save(){
        ArrayList<Student> students = new ArrayList<>(10);
        for (int count = 0; count < 10; count++) {
            Student student = new Student()
                    //如果自己不去设置id则系统会分配给一个id
                    .setId("student_"+count)
                    .setName("test"+count)
                    .setAge(count)
                    .setScore(98.5-count)
                    .setBirthday(LocalDate.now());
            students.add(student);
        }
        List<Student> students1 = studentDao.saveAll(students);
        return R.data(students1);
    }

    @GetMapping("/get")
    public R get(){
        List<Student> test6 = studentDao.getAllByName("test6");
        return R.data(test6);
    }

}
