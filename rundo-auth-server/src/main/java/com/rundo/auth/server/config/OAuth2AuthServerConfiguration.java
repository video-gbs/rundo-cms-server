// package com.rundo.auth.server.config;
//
// import com.nimbusds.jose.jwk.JWKSet;
// import com.nimbusds.jose.jwk.RSAKey;
// import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
// import com.nimbusds.jose.jwk.source.JWKSource;
// import com.nimbusds.jose.proc.SecurityContext;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
// import org.springframework.security.oauth2.core.oidc.OidcScopes;
// import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
// import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//
// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.interfaces.RSAPublicKey;
// import java.util.UUID;
//
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName AuthorizationServerConfiguration
//  * @Description 认证服务器配置
//  * @date 2022-12-26 周一 22:02
//  */
// @Configuration
// public class OAuth2AuthServerConfiguration {
//
//     /**
//      * 协议端点 安全过滤器链
//      *
//      * @param http
//      * @return
//      * @throws Exception
//      */
//     @Bean
//     @Order(1)
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//         http
//                 // 未通过身份验证时重定向到登录页面
//                 // 授权端点
//                 .exceptionHandling(
//                         exceptions -> exceptions.authenticationEntryPoint(
//                                 new LoginUrlAuthenticationEntryPoint("/login")
//                         )
//                 );
//         return http.build();
//     }
//
//     /**
//      * 用于进行查询客户端加载器
//      *
//      * @return
//      */
//     @Bean
//     public RegisteredClientRepository registeredClientRepository() {
//         RegisteredClient registeredClient = RegisteredClient
//                 .withId(UUID.randomUUID().toString())
//                 // 唯一的客户端ID
//                 .clientId("messaging-client")
//                 // 客户端密码
//                 .clientSecret("{noop}123456")
//                 // 客户端认证方式
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                 // 授权方式
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                 // .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                 // 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名  不能使用 localhost
//                 .redirectUri("http://127.0.0.1:8801/login/oauth2/code/messaging-client-oidc")
//                 .redirectUri("http://127.0.0.1:8801/authorized")
//                 // 便于调试授权码流程
//                 .redirectUri("https://www.baidu.com")
//                 // OIDC支持
//                 .scope(OidcScopes.OPENID)
//                 // 其他scope
//                 .scope("messaging.read")
//                 .scope("messaging.write")
//                 // 配置客户端相关的配置项，包括验证密钥或者 是否需要授权页面
//                 .clientSettings(ClientSettings.builder()
//                         // 授权码模式需要用户手动授权！false表示默认通过
//                         .requireAuthorizationConsent(true)
//                         .build()
//                 )
//                 // // JWT的配置项 包括TTL  是否复用refreshToken等等
//                 // .tokenSettings(TokenSettings.builder()
//                 //         // 令牌有效期 设置为30分钟
//                 //         .accessTokenTimeToLive(Duration.ofMinutes(30))
//                 //         // 令牌刷新时间为60分钟
//                 //         .refreshTokenTimeToLive(Duration.ofMinutes(60))
//                 //         .reuseRefreshTokens(true)
//                 //         // 令牌格式
//                 //         // OAuth2TokenFormat.SELF_CONTAINED:自包含JWT
//                 //         // OAuth2TokenFormat.REFERENCE: 引用Opaque
//                 //         .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//                 //
//                 //
//                 //         // 访问限制时间
//                 //         .setting("accessTokenLimitTimeSeconds", 5 * 60)
//                 //         // 访问限制次数
//                 //         .setting("accessTokenLimitRate", 3)
//                 //         .build()
//                 // )
//
//                 .build();
//
//         return new InMemoryRegisteredClientRepository(registeredClient);
//
//     }
//
//     /**
//      * JWKSource 用于签名访问令牌
//      *
//      * @return JWKSource<SecurityContext>
//      * @see com.nimbusds.jose.jwk.source.JWKSource
//      */
//     @Bean
//     public JWKSource<SecurityContext> jwkSource() {
//         KeyPair keyPair = generateRSAKey();
//         RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//         RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//         RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                 .keyID(UUID.randomUUID().toString())
//                 .privateKey(privateKey)
//                 .build();
//         JWKSet jwkSet = new JWKSet(rsaKey);
//         return new ImmutableJWKSet<>(jwkSet);
//     }
//
//     /**
//      * 带有启动生成的密钥的密钥对，用于创建上面的 {@code com.nimbusds.jose.jwk.source.JWKSource }。
//      *
//      * @return KeyPair
//      * @see java.security.KeyPair
//      */
//     private static KeyPair generateRSAKey() {
//         try {
//             KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//             keyPairGenerator.initialize(2048);
//             return keyPairGenerator.generateKeyPair();
//         } catch (Exception e) {
//             throw new IllegalStateException(e);
//         }
//     }
//
//     /**
//      * 用于配置授权服务器的授权端点
//      *
//      * @return
//      */
//     @Bean
//     public ProviderSettings providerSettings() {
//         return ProviderSettings.builder().build();
//     }
//
// }
