package com.li.service.impl;

import com.li.dao.*;
import com.li.entity.*;
import com.li.repository.CollegesRepository;
import com.li.repository.RecommCsRepository;
import com.li.service.UsersService;
import com.li.utils.JwtUtil;
import com.li.utils.MD5Utils;
import com.li.utils.RedisCache;
import com.li.vo.CategoryVO;
import com.li.vo.ResStatus;
import com.li.vo.ResultVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private HotSearchMapper hotSearchMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleUsersMapper roleUsersMapper;

    @Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    RecommCsRepository recommCsRepository;

    @Autowired
    CollegesRepository collegesRepository;

    @Transactional
    public ResultVO userRegist(String name, String pwd) {
        synchronized (this){
            //1.根据用户查询是否存在
            Users users = usersMapper.queryUserByName(name);

            //2.如果不存在插入数据库中
            if(users == null){
//                String md5Pwd = MD5Utils.md5(pwd);
                String pwdcode = passwordEncoder.encode(pwd);
                users = new Users();
                users.setUsername(name);
                users.setPassword(pwdcode);
                users.setCreateTime(new Date());
                users.setUpdateTime(new Date());
                Users save = usersMapper.save(users);
                if(save != null){
                    RoleUsers roleUsers = new RoleUsers();
                    roleUsers.setUid(save.getId());
                    roleUsers.setRid(2);
                    RoleUsers save1 = roleUsersMapper.save(roleUsers);
                    if (save1 != null){
                        return new ResultVO(ResStatus.OK,"注册成功",users);
                    }else {
                        return new ResultVO(ResStatus.NO,"注册失败",null);
                    }
                }else {
                    return new ResultVO(ResStatus.NO,"注册失败",null);
                }
            }else {
                return new ResultVO(ResStatus.EXIST,"用户名已经被注册",null);
            }
        }

    }
    @Override
    public ResultVO userRegist1(String username) {
        synchronized (this){
            //1.根据用户查询是否存在
            System.out.println("username:"+username);
            Users users = usersMapper.queryUserByName(username);
            System.out.println(users);
            //2.如果不存在
            if(users == null){
                return new ResultVO(ResStatus.OK,"用户名不存在，可注册",null);
            }else {
                return new ResultVO(ResStatus.EXIST,"用户名已经被注册",null);
            }
        }
    }

    @Override
    public ResultVO checkLogin(String name, String pwd) {
        Users users = usersMapper.queryUserByName(name);
        if(users == null){
            return new ResultVO(ResStatus.EXIST,"用户名不存在",null);
        }else {
            String md5Pwd = MD5Utils.md5(pwd);
            System.out.println(md5Pwd);
            if(md5Pwd.equals(users.getPassword())){
                //如果登录验证成功，则需要生成令牌token(token就是按照特定规则生成的字符串)
                //使用jwt规则生成token字符串
                JwtBuilder builder = Jwts.builder();
                HashMap<String,Object> map = new HashMap<>();
                map.put("k1","v1");
                map.put("k2","v2");
                String token = builder.setSubject(name)//主题，就是token中携带的数据
                        .setIssuedAt(new Date()) //设置生成时间
                        .setId(users.getId()+"")//设置用户is为token的id
                        .setClaims(map) //map中可以存放用户的角色权限信息
                        .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000)) //设置token的期限为1天
                        .signWith(SignatureAlgorithm.HS256,"ljm666")//设置加密方式和加密密码
                        .compact();//执行生成token
                return new ResultVO(ResStatus.OK,"登录成功",users);
            }else {
                return new ResultVO(ResStatus.NO,"密码错误",null);
            }
        }
    }

    @Override
    public ResultVO checkLogin2(String name, String pwd) {
        //AuthenticationManager authentic进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name,pwd);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        System.out.println("authentication:"+authentication);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authentication)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = String.valueOf(loginUser.getUsers().getId());
        String jwt = JwtUtil.createJWT(id);
        //将要响应的信息封装为map
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("user",loginUser.getUsers());
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (int i = 1;i < 8;i++){
            String  cname = categoryMapper.findCNameById(i);
            List<Major> majors = majorMapper.findMajorsByCid(i);
            categoryVOS.add(new CategoryVO(cname,majors));
        }
        //获取用户个性化推荐
        List<Syslog> syslogs = syslogMapper.findByUidOrderByCreateTimeDesc(loginUser.getUsers().getId());
        RecommCs recommCs = new RecommCs();
        Map<String,Object> recommdata = new HashMap<>();
        if(null != syslogs && syslogs.size() !=0 ){
            int sid = syslogs.get(0).getSid();
            Colleges colleges = collegesRepository.findBySid(sid);
            int scode = colleges.getScode();
            recommCs = recommCsRepository.findBySid(scode);
            if (recommCs == null) {
                recommCs = recommCsRepository.findBySid(10335);
                recommdata.put("school_id",114);
            }else {
                recommdata.put("school_id",sid);
            }
            recommdata.put("1",collegesRepository.findByScode(recommCs.getSim1()));
            recommdata.put("2",collegesRepository.findByScode(recommCs.getSim2()));
            recommdata.put("3",collegesRepository.findByScode(recommCs.getSim3()));
            recommdata.put("4",collegesRepository.findByScode(recommCs.getSim4()));
            recommdata.put("5",collegesRepository.findByScode(recommCs.getSim5()));
            recommdata.put("6",collegesRepository.findByScode(recommCs.getSim6()));
            recommdata.put("7",collegesRepository.findByScode(recommCs.getSim7()));
            recommdata.put("8",collegesRepository.findByScode(recommCs.getSim8()));
            recommdata.put("9",collegesRepository.findByScode(recommCs.getSim9()));
            recommdata.put("10",collegesRepository.findByScode(recommCs.getSim10()));
        }else{
            recommCs = recommCsRepository.findBySid(10335);
            recommdata.put("school_id",114);
            recommdata.put("1",collegesRepository.findByScode(recommCs.getSim1()));
            recommdata.put("2",collegesRepository.findByScode(recommCs.getSim2()));
            recommdata.put("3",collegesRepository.findByScode(recommCs.getSim3()));
            recommdata.put("4",collegesRepository.findByScode(recommCs.getSim4()));
            recommdata.put("5",collegesRepository.findByScode(recommCs.getSim5()));
            recommdata.put("6",collegesRepository.findByScode(recommCs.getSim6()));
            recommdata.put("7",collegesRepository.findByScode(recommCs.getSim7()));
            recommdata.put("8",collegesRepository.findByScode(recommCs.getSim8()));
            recommdata.put("9",collegesRepository.findByScode(recommCs.getSim9()));
            recommdata.put("10",collegesRepository.findByScode(recommCs.getSim10()));
        }

        map.put("allMajors",categoryVOS);
        map.put("hotSearch",hotSearchMapper.findAll());
        map.put("recommList",recommdata);
        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("login:"+id,loginUser);
        return new ResultVO(ResStatus.OK,"登录成功",map);
    }

    @Override
    public ResultVO logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUsers().getId();
        //删除redis中的值
        redisCache.deleteObject("login:"+id);
        return new ResultVO(ResStatus.OK,"退出成功",null);
    }


    @Override
    public ResultVO userSearchById(Integer id) {
        return new ResultVO(ResStatus.OK,"查询成功",usersMapper.findById(id).orElse(null));
    }


    //分页查询
    @Override
    public ResultVO userSearchByPage(Integer pageNum, Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        System.out.println(pageable);
        Page<Users> page = usersMapper.findNameLike(name,pageable);
        return new ResultVO(ResStatus.OK,"查询成功",page);
    }

    @Override
    public ResultVO userSearchByPage(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        Page<Users> all = usersMapper.findAll(pageable);
        return new ResultVO(ResStatus.OK,"查询成功",all);

    }

    //删除
    @Override
    public ResultVO userDeleteById(Integer id) {
        usersMapper.deleteById(id);
        return new ResultVO(ResStatus.OK,"删除成功",null);
    }



    //添加+修改
    @Override
    public ResultVO userSave(Users user) {
        String pwdcode = passwordEncoder.encode(user.getPassword());
        user.setPassword(pwdcode);
        usersMapper.save(user);
        return new ResultVO(ResStatus.OK,"修改成功",user);
    }



}
