package net.msonic.out2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	 * @RequestMapping("/") public String home() { return "Hello World"; }
	 */

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
					// Just for laughs, apply OAuth protection to only 2
					// resources
					.requestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/"),
							new AntPathRequestMatcher("/admin/beans")))
					.authorizeRequests().anyRequest().access("#oauth2.hasScope('read')");
			// @formatter:on
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId("sparklr");
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter  { //implements EnvironmentAware

		//private static final String ENV_OAUTH = "authentication.oauth.";

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
			 	.tokenStore(tokenStore())
				.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			clients.inMemory().withClient("my-trusted-client")
					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
					.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "trust")
					.resourceIds("sparklr").accessTokenValiditySeconds(60).and()
					.withClient("my-client-with-registered-redirect").authorizedGrantTypes("authorization_code")
					.authorities("ROLE_CLIENT").scopes("read", "trust").resourceIds("sparklr")
					.redirectUris("http://anywhere?key=value").and().withClient("my-client-with-secret")
					.authorizedGrantTypes("client_credentials", "password").authorities("ROLE_CLIENT").scopes("read")
					.resourceIds("sparklr").secret("secret");
             
			// @formatter:on
		}

		/*private RelaxedPropertyResolver propertyResolver;

		@Override
		public void setEnvironment(Environment environment) {
			// TODO Auto-generated method stub
			this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);

		}*/

		@Autowired
		private DataSource dataSource;

		@Bean
		public TokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}

	}
	
	/*
	 curl -H "Accept: application/json" my-client-with-secret:secret@127.0.0.1:8080/oauth/token -d grant_type=client_credentials
	 
	 GRANT ALL PRIVILEGES ON database_name TO user@host IDENTIFIED BY 'password';
		FLUSH PRIVILEGES;

		Fetching refresh_token
		curl -vu rajithapp:secret 'http://localhost:9191/api/oauth/token?username=admin&password=admin&grant_type=password'
		{"access_token":"91202244-431f-444a-b053-7f50716f2012","token_type":"bearer","refresh_token":"e6f8624f-213d-4343-a971-980e83f734be","expires_in":1738,"scope":"read write"}	
		
		Fetching acess_token by submitting refresh_token
		curl -vu rajithapp:secret 'http://localhost:9191/api/oauth/token?grant_type=refresh_token&refresh_token=<refresh_token>'
		{"access_token":"821c99d4-2c9f-4990-b68d-18eacaff54b2","token_type":"bearer","refresh_token":"e6f8624f-213d-4343-a971-980e83f734be","expires_in":1799,"scope":"read write"}

	 * */

}
