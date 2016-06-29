package net.msonic.out2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}


	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {
		
		
		@Bean
	    @ConfigurationProperties(prefix = "spring.datasource_oauth")
	    public DataSource oauthDataSource() {
	        return DataSourceBuilder.create().build();
	    }
		
		@Override
	    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	        TokenStore tokenStore = new JdbcTokenStore(oauthDataSource());
	        
	        resources.resourceId("sparklr")
	                .tokenStore(tokenStore);
	    }

		
		
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

		
	}
}
