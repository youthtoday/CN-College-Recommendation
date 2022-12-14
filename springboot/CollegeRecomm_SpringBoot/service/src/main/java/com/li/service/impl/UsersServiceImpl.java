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
            //1.??????????????????????????????
            Users users = usersMapper.queryUserByName(name);

            //2.?????????????????????????????????
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
                        return new ResultVO(ResStatus.OK,"????????????",users);
                    }else {
                        return new ResultVO(ResStatus.NO,"????????????",null);
                    }
                }else {
                    return new ResultVO(ResStatus.NO,"????????????",null);
                }
            }else {
                return new ResultVO(ResStatus.EXIST,"????????????????????????",null);
            }
        }

    }
    @Override
    public ResultVO userRegist1(String username) {
        synchronized (this){
            //1.??????????????????????????????
            System.out.println("username:"+username);
            Users users = usersMapper.queryUserByName(username);
            System.out.println(users);
            //2.???????????????
            if(users == null){
                return new ResultVO(ResStatus.OK,"??????????????????????????????",null);
            }else {
                return new ResultVO(ResStatus.EXIST,"????????????????????????",null);
            }
        }
    }

    @Override
    public ResultVO checkLogin(String name, String pwd) {
        Users users = usersMapper.queryUserByName(name);
        if(users == null){
            return new ResultVO(ResStatus.EXIST,"??????????????????",null);
        }else {
            String md5Pwd = MD5Utils.md5(pwd);
            System.out.println(md5Pwd);
            if(md5Pwd.equals(users.getPassword())){
                //????????????????????????????????????????????????token(token??????????????????????????????????????????)
                //??????jwt????????????token?????????
                JwtBuilder builder = Jwts.builder();
                HashMap<String,Object> map = new HashMap<>();
                map.put("k1","v1");
                map.put("k2","v2");
                String token = builder.setSubject(name)//???????????????token??????????????????
                        .setIssuedAt(new Date()) //??????????????????
                        .setId(users.getId()+"")//????????????is???token???id
                        .setClaims(map) //map??????????????????????????????????????????
                        .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000)) //??????token????????????1???
                        .signWith(SignatureAlgorithm.HS256,"ljm666")//?????????????????????????????????
                        .compact();//????????????token
                return new ResultVO(ResStatus.OK,"????????????",users);
            }else {
                return new ResultVO(ResStatus.NO,"????????????",null);
            }
        }
    }

    @Override
    public ResultVO checkLogin2(String name, String pwd) {
        //AuthenticationManager authentic??????????????????
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name,pwd);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        System.out.println("authentication:"+authentication);
        //?????????????????????????????????????????????
        if(Objects.isNull(authentication)){
            throw new RuntimeException("????????????");
        }
        //??????????????????????????????userid????????????jwt
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = String.valueOf(loginUser.getUsers().getId());
        String jwt = JwtUtil.createJWT(id);
        //??????????????????????????????map
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("user",loginUser.getUsers());
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (int i = 1;i < 8;i++){
            String  cname = categoryMapper.findCNameById(i);
            List<Major> majors = majorMapper.findMajorsByCid(i);
            categoryVOS.add(new CategoryVO(cname,majors));
        }
        //???????????????????????????
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
        //??????????????????????????????redis userid??????key
        redisCache.setCacheObject("login:"+id,loginUser);
        return new ResultVO(ResStatus.OK,"????????????",map);
    }

    @Override
    public ResultVO logout() {
        //??????SecurityContextHolder????????????id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUsers().getId();
        //??????redis?????????
        redisCache.deleteObject("login:"+id);
        return new ResultVO(ResStatus.OK,"????????????",null);
    }


    @Override
    public ResultVO userSearchById(Integer id) {
        return new ResultVO(ResStatus.OK,"????????????",usersMapper.findById(id).orElse(null));
    }


    //????????????
    @Override
    public ResultVO userSearchByPage(Integer pageNum, Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        System.out.println(pageable);
        Page<Users> page = usersMapper.findNameLike(name,pageable);
        return new ResultVO(ResStatus.OK,"????????????",page);
    }

    @Override
    public ResultVO userSearchByPage(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        Page<Users> all = usersMapper.findAll(pageable);
        return new ResultVO(ResStatus.OK,"????????????",all);

    }

    //??????
    @Override
    public ResultVO userDeleteById(Integer id) {
        usersMapper.deleteById(id);
        return new ResultVO(ResStatus.OK,"????????????",null);
    }



    //??????+??????
    @Override
    public ResultVO userSave(Users user) {
        String pwdcode = passwordEncoder.encode(user.getPassword());
        user.setPassword(pwdcode);
        usersMapper.save(user);
        return new ResultVO(ResStatus.OK,"????????????",user);
    }



}
