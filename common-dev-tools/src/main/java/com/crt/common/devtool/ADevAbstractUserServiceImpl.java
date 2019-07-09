package com.crt.common.devtool;

import com.alibaba.fastjson.JSONObject;
import com.crt.common.service.AbstractUserService;
import com.crt.common.vo.AbstractUser;
import com.crt.common.vo.DefaultAbstractUser;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/6/10 14:47
 * @Description 这个类里包含两个职责，都只在开发模式下起作用，一个是开发默认用户，第二个是自动在所有url前面把applicationName加上
 * 为了方便单模块开发测试
 **/
@ConditionalOnProperty(
        value = {"crt.frame.user.session"},
        havingValue = "local",
        matchIfMissing = false
)
@Primary
@RestController
public class ADevAbstractUserServiceImpl implements AbstractUserService {
    static Logger logger = LoggerFactory.getLogger(ADevAbstractUserServiceImpl.class);

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    String springApplicationName;
    /**
     * 默认用户ID
     */
    @Value("${crt.frame.user.dev.userId:0}")
    Integer devUserId;
    /**
     * 所有在resources/devUsers.txt中配置的用户加载到这个map中
     */
    static Map<Integer, AbstractUser> devAbstractUserMap = null;
    Cache<Integer, AbstractUser> infoCache = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    /**
     * 自动注入 所有spring mvc controller注册的request mapping
     */
    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    private static String GET_USER_INFO_CACHE_BY_USER_ID_URL = "http://COMMON-DEV-USER-INFO-CACHE-CENTER/user/info/cache/getCacheUserInfoByUserId?userId={userId}";


    public ADevAbstractUserServiceImpl() {
        logger.warn("+++++++++++++++++++ADevAbstractUserServiceImpl init begin+++++++++++++++++++++++++");
        loadDevUserConfig();
        logger.warn("+++++++++++++++++++ADevAbstractUserServiceImpl init end+++++++++++++++++++++++++");
    }

    @Override
    public AbstractUser getLoginUser() {
        if (devUserId == null) {
            logger.error("【开发环境问题】没有配置开发默认userId,请在resources/devUsers.txt选择一个userId配置给e6yun.frame.user.dev.userId");
            return null;
        }
        return getAbstractUser();
    }

    /**
     * 刷新用户ID,方便开发和联调的时候切换默认用户而不重启
     *
     * @return
     */
    @GetMapping("/refreshUserId")
    @ApiOperation(value = "[开发模式]快速修改userId")
    public Object refreshUserId(Integer userId) {
        logger.info("[开发模式]把默认用户从{}修改为{}", devUserId, userId);
        Integer oldVal = devUserId;
        devUserId = userId;
        return "把默认用户从" + oldVal + "修改为" + userId;
    }

    @GetMapping("/getCurrentUser")
    @ApiOperation(value = "[开发模式]查看当前默认用户")
    public Object getCurrentUser() {
        if (devUserId == null) {
            return "【开发环境问题】没有配置开发默认userId,请在resources/devUsers.txt选择一个userId配置给e6yun.frame.user.dev.userId，或者通过refreshUserId动态设置一个";
        }
        AbstractUser abstractUser = getAbstractUser();
        if (abstractUser == null) {
            return "未获取到userId = " + devUserId + "的用户信息";
        }
        return abstractUser;
    }


    /**
     * 加载devUsers.txt文件到内存中
     */
    public static void loadDevUserConfig() {

        ClassPathResource resource = new ClassPathResource("devUsers.txt");
        InputStream fis = null;
        try {
            fis = resource.getInputStream();
        } catch (IOException e) {
            logger.error("未读取到resources/devUsers.txt 将尝试从远程获取用户信息");
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            devAbstractUserMap = new HashMap<>();
            String line = null;
            while ((line = br.readLine()) != null) {
                if (StringUtils.hasText(line)) {
                    AbstractUser abstractUser = JSONObject.parseObject(line, DefaultAbstractUser.class);
                    devAbstractUserMap.put(abstractUser.getUserId(), abstractUser);
                }
            }
            br.close();
        } catch (Exception ex) {
            logger.error("读取resources/devUsers.txt发生错误", ex);
        }
    }

    private AbstractUser getAbstractUser() {
        AbstractUser abstractUser;
        if (devAbstractUserMap == null) {
            logger.info("开发环境未配置resources/devUsers.txt 尝试从Caffeine获取用户 userId = {}", devUserId);
            abstractUser = infoCache.getIfPresent(devUserId);
            if (abstractUser != null) {
                return abstractUser;
            }
            logger.info("Caffeine缓存中未获取到用户信息，将尝试远程调用户信息 userId = {}", devUserId);
            logger.info("远程获取用户地址为 url = {}",GET_USER_INFO_CACHE_BY_USER_ID_URL);
            abstractUser = restTemplate.getForObject(GET_USER_INFO_CACHE_BY_USER_ID_URL, DefaultAbstractUser.class, devUserId);
            if (abstractUser == null) {
                logger.error("远程调用未找到用户id = {} 的用户信息，请检查配置是否正确", devUserId);
            } else {
                infoCache.put(devUserId, abstractUser);
            }
            return abstractUser;
        }
        abstractUser = devAbstractUserMap.get(devUserId);
        if (abstractUser == null) {
            logger.info("【开发环境问题】resources/devUsers.txt 内没有获取到 userId = {} 的用户信息 ，将尝试代用远程获取用户", devUserId);
            if (restTemplate != null) {
                logger.info("远程获取用户地址为 url = {}",GET_USER_INFO_CACHE_BY_USER_ID_URL);
                abstractUser = restTemplate.getForObject(GET_USER_INFO_CACHE_BY_USER_ID_URL, DefaultAbstractUser.class, devUserId);
                if (abstractUser == null) {
                    logger.error("【开发环境问题】配置e6yun.frame.user.dev.userId的值{}在resources/devUsers.txt中没有找到对应的数据", devUserId);
                } else {
                    devAbstractUserMap.put(devUserId, abstractUser);
                }
            }
        }
        return abstractUser;
    }

    /**
     * 所有url前面加上springApplicationName
     */
    @PostConstruct
    public void requestMappingAddApplicationName() {
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        //这里要和原来的keySet隔离，不然容易发生不修改错误
        Set<RequestMappingInfo> requestMappingInfoSet = new HashSet<>();
        requestMappingInfoSet.addAll(handlerMethodMap.keySet());
        //遍历所有的keyset
        requestMappingInfoSet.forEach(requestMappingInfo -> {
            HandlerMethod handlerMethod = handlerMethodMap.get(requestMappingInfo);
            //老的匹配规则
            Set<String> oldPatterns = requestMappingInfo.getPatternsCondition().getPatterns();
            //新的匹配规则
            Set<String> newPatterns = new HashSet<>();
            //老的key前面加上springApplicationName成为新的key
            oldPatterns.forEach(oldKey -> newPatterns.add("/" + springApplicationName.toUpperCase() + oldKey));
            //通过新的URL构造一个PatternsRequestCondition对象
            PatternsRequestCondition newPatternsRequestCondition = new PatternsRequestCondition(newPatterns.toArray(new String[oldPatterns.size()]));
            //构造新的RequestMappingInfo
            RequestMappingInfo newRequestMappingInfo = new RequestMappingInfo(requestMappingInfo.getName(), newPatternsRequestCondition, requestMappingInfo.getMethodsCondition(), requestMappingInfo.getParamsCondition(), requestMappingInfo.getHeadersCondition(), requestMappingInfo.getConsumesCondition(), requestMappingInfo.getProducesCondition(), requestMappingInfo.getCustomCondition());
            //注册到全局的requestMappping中去
            requestMappingHandlerMapping.registerMapping(newRequestMappingInfo, handlerMethod.getBean(), handlerMethod.getMethod());
        });
    }

}
