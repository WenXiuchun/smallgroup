package com.cc.smallgroup.web;

import com.cc.smallgroup.entity.User;
import com.cc.smallgroup.model.SignIn;
import com.cc.smallgroup.repository.UserRespository;
import com.cc.smallgroup.util.ReturnUtil;

@RestController
@RequestMapping("userinfo")
@Api("用户相关的API")
public class UserInfoController {
    private static Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private UserRespository userRespository;

    public static final String ROOT = "upload-dir";

    /**
     * 添加用户相关的信息
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户相关信息", notes = "用户相关信息")
    @PostMapping("/userInfo")
    public Object updateUserInfo(@RequestBody User userInfo) {
        User user = userRespository.findById(userInfo.getId());
        if (user != null) {
            if (null != userInfo.getAge())user.setAge(userInfo.getAge());
            if (null != userInfo.getAlias())user.setAlias(userInfo.getAlias());
            if (null != userInfo.getCareer())user.setCareer(userInfo.getCareer());
            if (null != userInfo.getGender())user.setGender(userInfo.getGender());
            if (null != userInfo.getIntroduceOne())user.setIntroduceOne(userInfo.getIntroduceOne());
            if (null != userInfo.getIntroduceTwo())user.setIntroduceTwo(userInfo.getIntroduceTwo());
            userRespository.save(user);
            user.setPassword(null);
            return ReturnUtil.result(user);
        } else {
            return ReturnUtil.fail("该用户不存在");
        }
    }

    /**
     * 获取用户相关的信息
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户相关信息", notes = "用户相关信息")
    @GetMapping("/userInfo/{userId}")
    public Object getUserInfo(@PathVariable String userId) {
        User user = userRespository.findById(userId);
        if (user != null) {
            user.setPassword(null);
            return ReturnUtil.result(user);
        } else {
            return ReturnUtil.fail("该用户不存在");
        }
    }

    /**
     * 上传用户头像
     *
     * @param
     * @return
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像")
    @PostMapping("/upload/{userId}")
    public Object picUpload(@RequestParam("file") MultipartFile file,
                            @PathVariable String userId) {
        if (file.isEmpty()) {
            return ReturnUtil.fail("请选择一张图片");
        }
        User user = userRespository.findById(userId);
        if (null == user) {
            return ReturnUtil.fail("该用户不存在");
        }
        try {
            // Get the file and save it somewhere

            Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }
}
