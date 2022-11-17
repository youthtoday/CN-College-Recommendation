package com.li;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ApiApplication.class)
class ApiApplicationTests {

//    @Autowired
//    CollegesRepository collegesRepository;
//    @Autowired
//    PermissionMapper permissionMapper;
//
//    @Autowired
//    UsersService usersService;
//
//    @Test
//    void contextLoads() {
//    }
//    @Test
//    public void encoder(){
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.matches("123", "$2a$10$3Vq5eQ.BBIE.L6kA3UxYZupjOkIUWKVqiIjWLJBrSyPu0q5AhMy1O"));
//
//    }

//    @Test
//    public void MapperTest(){
//        ResultVO resultVO = usersService.userSearchByPage(1, 10, "z");
//        System.out.println(resultVO);
//    }
//
//    @Test
//    public void ESTest(){
//        String id = "Op01pn8Bu8QhfKsG2FhK";
//        System.out.println("Op01pn8Bu8QhfKsG2FhK".equals(id));
//        Optional<Colleges> optional = collegesRepository.findById(id);
//        System.out.println(optional);
//    }
//测试遍历全表

//    @Resource
//    private HBaseService hbaseService;
//    @Test
//    public void testGetResultScanner() {
//        Map<String, Map<String, String>> result2 = hbaseService.getResultScanner("test_base");
//        System.out.println("-----遍历查询全表内容-----");
//        result2.forEach((k, value) -> {
//            System.out.println(k + "--->" + value);
//        });
//    }


}
