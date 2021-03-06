package demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {

	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.pass}")
	private String pass;
	@Value("${jdbc.domain:infobip.local}")
	private String domain;
	@Value("${jdbc.useNTLMv2:true}")
	private boolean useNTLMv2;

	@Bean
	public HikariDataSource hikariDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl + ";domain=" + domain + ";useNTLMv2=" + useNTLMv2);
		config.setUsername(username);
		config.setPassword(pass);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setConnectionTestQuery("SELECT 1");

		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}

}
