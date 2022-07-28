package cn.mesmile.admin.modules.social.util;

import java.util.Objects;

import cn.mesmile.admin.common.utils.SpringUtil;
import cn.mesmile.admin.modules.social.config.SocialProperties;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthCodingRequest;
import me.zhyd.oauth.request.AuthCsdnRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthDouyinRequest;
import me.zhyd.oauth.request.AuthElemeRequest;
import me.zhyd.oauth.request.AuthFacebookRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthGitlabRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthHuaweiRequest;
import me.zhyd.oauth.request.AuthKujialeRequest;
import me.zhyd.oauth.request.AuthLinkedinRequest;
import me.zhyd.oauth.request.AuthMeituanRequest;
import me.zhyd.oauth.request.AuthMiRequest;
import me.zhyd.oauth.request.AuthMicrosoftRequest;
import me.zhyd.oauth.request.AuthOschinaRequest;
import me.zhyd.oauth.request.AuthPinterestRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRenrenRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthStackOverflowRequest;
import me.zhyd.oauth.request.AuthTaobaoRequest;
import me.zhyd.oauth.request.AuthTeambitionRequest;
import me.zhyd.oauth.request.AuthToutiaoRequest;
import me.zhyd.oauth.request.AuthTwitterRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseWebRequest;
import me.zhyd.oauth.request.AuthWeChatMpRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.request.AuthWeiboRequest;


/**
 * @author zb
 * @Description
 */
public class SocialAuthUtil {
    
    private SocialAuthUtil() {}

    /**
     * 获取认证客户端
     * @param source 第三方认证源类型 {@link AuthDefaultSource}
     * @param cache 缓存配置
     * @return 认证客户端
     */
    public static AuthRequest getAuthRequest(String source, SocialProperties socialProperties, AuthStateCache cache) {
        AuthDefaultSource authSource = Objects.requireNonNull(AuthDefaultSource.valueOf(source.toUpperCase()));
        AuthConfig authConfig = socialProperties.getOauth().get(authSource);
        if (authConfig == null) {
            throw new AuthException("未获取到第三方有效的Auth配置");
        } else {
            AuthRequest authRequest = getAuthRequest(authSource, authConfig, cache);
            if (null == authRequest) {
                throw new AuthException("未获取到第三方有效的Auth配置");
            } else {
                return authRequest;
            }
        }
    }

    /**
     * 获取认证客户端
     * @param authSource 认证源
     * @param authConfig 配置
     * @param cache 缓存配置
     * @return 认证客户端
     */
    private static AuthRequest getAuthRequest(AuthDefaultSource authSource, AuthConfig authConfig,AuthStateCache cache) {
        AuthRequest authRequest = null;
        switch(authSource) {
        case GITHUB:
            authRequest = new AuthGithubRequest(authConfig, cache);
            break;
        case GITEE:
            authRequest = new AuthGiteeRequest(authConfig, cache);
            break;
        case OSCHINA:
            authRequest = new AuthOschinaRequest(authConfig, cache);
            break;
        case QQ:
            authRequest = new AuthQqRequest(authConfig, cache);
            break;
        case WECHAT_OPEN:
            authRequest = new AuthWeChatOpenRequest(authConfig, cache);
            break;
        case WECHAT_ENTERPRISE:
            authRequest = new AuthWeChatEnterpriseQrcodeRequest(authConfig, cache);
            break;
        case WECHAT_ENTERPRISE_WEB:
            authRequest = new AuthWeChatEnterpriseWebRequest(authConfig, cache);
            break;
        case WECHAT_MP:
            authRequest = new AuthWeChatMpRequest(authConfig, cache);
            break;
        case DINGTALK:
            authRequest = new AuthDingTalkRequest(authConfig, cache);
            break;
        case ALIPAY:
            authRequest = new AuthAlipayRequest(authConfig, cache);
            break;
        case BAIDU:
            authRequest = new AuthBaiduRequest(authConfig, cache);
            break;
        case WEIBO:
            authRequest = new AuthWeiboRequest(authConfig, cache);
            break;
        case CODING:
            authRequest = new AuthCodingRequest(authConfig, cache);
            break;
        case CSDN:
            authRequest = new AuthCsdnRequest(authConfig, cache);
            break;
        case TAOBAO:
            authRequest = new AuthTaobaoRequest(authConfig, cache);
            break;
        case GOOGLE:
            authRequest = new AuthGoogleRequest(authConfig, cache);
            break;
        case FACEBOOK:
            authRequest = new AuthFacebookRequest(authConfig, cache);
            break;
        case DOUYIN:
            authRequest = new AuthDouyinRequest(authConfig, cache);
            break;
        case LINKEDIN:
            authRequest = new AuthLinkedinRequest(authConfig, cache);
            break;
        case MICROSOFT:
            authRequest = new AuthMicrosoftRequest(authConfig, cache);
            break;
        case MI:
            authRequest = new AuthMiRequest(authConfig, cache);
            break;
        case TOUTIAO:
            authRequest = new AuthToutiaoRequest(authConfig, cache);
            break;
        case TEAMBITION:
            authRequest = new AuthTeambitionRequest(authConfig, cache);
            break;
        case PINTEREST:
            authRequest = new AuthPinterestRequest(authConfig, cache);
            break;
        case RENREN:
            authRequest = new AuthRenrenRequest(authConfig, cache);
            break;
        case STACK_OVERFLOW:
            authRequest = new AuthStackOverflowRequest(authConfig, cache);
            break;
        case HUAWEI:
            authRequest = new AuthHuaweiRequest(authConfig, cache);
            break;
        case KUJIALE:
            authRequest = new AuthKujialeRequest(authConfig, cache);
            break;
        case GITLAB:
            authRequest = new AuthGitlabRequest(authConfig, cache);
            break;
        case MEITUAN:
            authRequest = new AuthMeituanRequest(authConfig, cache);
            break;
        case ELEME:
            authRequest = new AuthElemeRequest(authConfig, cache);
            break;
        case TWITTER:
            authRequest = new AuthTwitterRequest(authConfig, cache);
        }
        return authRequest;
    }
}