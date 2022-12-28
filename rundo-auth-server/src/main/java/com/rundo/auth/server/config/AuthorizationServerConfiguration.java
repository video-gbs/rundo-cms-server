package com.rundo.auth.server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;


/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AuthorizationServerConfiguration
 * @Description 认证服务器配置
 * @date 2022-12-26 周一 22:02
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {


    /**
     * 初始化密码加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 配置 OAuth2.0 provider元信息
     *
     * @return
     */
    @Bean
    public ProviderSettings providerSettings(
            @Value("${server.port}") Integer port,
            @Value("${server.servlet.context-path}") String contextPath
    ) {
        return ProviderSettings.builder().issuer(String.format("http://127.0.0.1:%s%s", port, contextPath)).build();
    }

    /**
     * 协议端点 安全过滤器链
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        applyDefaultSecurity(httpSecurity);
        httpSecurity
                .exceptionHandling(
                        // 未通过身份验证时重定向到登录页面
                        exceptions -> exceptions.authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")
                        )
                );
        return httpSecurity.build();
    }

    /**
     * 用于身份验证安全过滤器链
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest().authenticated()
                )
                //表单登录处理从
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    /**
     * 用于进行查询用户，验证的用户的UserDetailsService实例
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User
                .withUsername("admin")
                .password("{noop}123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    /**
     * 用于进行查询客户端加载器
     *
     * @return
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                // 唯一的客户端ID
                .clientId("client")
                // 唯一的客户名称，可不定义
                .clientName("client")
                // 客户端密码
                .clientSecret("{noop}123456")
                // 授权方法
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权类型
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名  不能使用 localhost
                .redirectUri("http://127.0.0.1:8801/login/oauth2/code/client-oidc")
                // OIDC支持
                .scope(OidcScopes.OPENID)
                // 其他scope
                .scope("read")
                .scope("write")
                // 配置客户端相关的配置项，包括验证密钥或者 是否需要授权页面
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                // JWT的配置项 包括TTL  是否复用refreshToken等等
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .accessTokenTimeToLive(Duration.ofSeconds(30 * 60))
                        .refreshTokenTimeToLive(Duration.ofSeconds(60 * 60))
                        .reuseRefreshTokens(true)
                        .setting("accessTokenLimitTimeSeconds", 5 * 60)
                        .setting("accessTokenLimitRate", 3)
                        .build())

                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);

    }

    /**
     * JWKSource 用于签名访问令牌
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRSAKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID(UUID.randomUUID().toString())
                .privateKey(privateKey)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 有启动生成的密钥的密钥对
     *
     * @return
     */
    private static KeyPair generateRSAKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过 ProviderSettings配置 Spring Authorization Server实例
     *
     * @return
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().build();
    }

}
