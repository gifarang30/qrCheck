package com.qrCheck.tcpServer.config.dataSourceCfg

import com.qrCheck.tcpServer.config.props.PropsDatasource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.sf.log4jdbc.Log4jdbcProxyDataSource
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter
import net.sf.log4jdbc.tools.LoggingType
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource


/**
 * 금우 데이터소스 설정
 */
@Configuration
@MapperScan(
    basePackages = ["com.qrCheck.tcpServer"],
    sqlSessionFactoryRef = "sessionFactoryKumo"
)
class DataSourceCfgKumo(
    val propDataSource: PropsDatasource
) {

    /**
     * JNDI로 부터 데이터소스 생성
     * JNDI 사용 안하는 경우 @ConfigurationProperties 활성화 시키고 DataSourceBuilder를 이용하여 데이터소스 생성 할것
     */
    @Primary
    @Bean(name = ["dataSourceKumo"])
    //@ConfigurationProperties("datasource.kumo")
    fun dataSourceKumo(): DataSource {

        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = propDataSource.kumo["jdbc-url"]
        hikariConfig.driverClassName = propDataSource.kumo["driver-class-name"]
        hikariConfig.username = propDataSource.kumo["username"]
        hikariConfig.password = propDataSource.kumo["password"]
        hikariConfig.isAutoCommit = true

        // JNDI 없이 DB 직접 연결 코드
        val dataSource = HikariDataSource(hikariConfig)

        // JNDI dataSource
        /*
        val dataSourceLookup = JndiDataSourceLookup()
        val dataSource = dataSourceLookup.getDataSource(propDataSource.kumo["jndi-name"].toString())
         */

        // SQL 로그 포맷
        val formatter = Log4JdbcCustomFormatter().apply {
            loggingType = LoggingType.MULTI_LINE
            sqlPrefix = "--------------- SQL Statement --------------- \n"
        }

        return Log4jdbcProxyDataSource(dataSource).apply {
            logFormatter = formatter
        }
    }

    /**
     * 세션 팩토리
     * 매퍼 XML 위치 지정
     */
    @Primary
    @Bean(name = ["sessionFactoryKumo"])
    fun sessionFactoryKumo(

        @Qualifier("dataSourceKumo")
        dataSourceKumo: DataSource

    ): SqlSessionFactory {

        val configPath = "classpath:/spring/mapper/mapper-config.xml"
        val mapperPath01 = "classpath:/spring/mapper/${propDataSource.kumo["db-type"]}/kumo/*.xml"
        //val mapperPath02 = "classpath:/spring/mapper/${propDataSource.kumo["db-type"]}/assets/**/*.xml"

        var mapperResources = PathMatchingResourcePatternResolver().getResources(mapperPath01)
        //mapperResources = mapperResources.plus(PathMatchingResourcePatternResolver().getResources(mapperPath02))

        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSourceKumo)
        sqlSessionFactoryBean.setTypeAliasesPackage("com.qrCheck.tcpServer")
        sqlSessionFactoryBean.setConfigLocation(PathMatchingResourcePatternResolver().getResource(configPath))
        sqlSessionFactoryBean.setMapperLocations(*mapperResources)
        sqlSessionFactoryBean.vfs = SpringBootVFS::class.java

        return sqlSessionFactoryBean.`object`!!
    }

    /**
     * 트랜잭션 메니저
     */
    @Primary
    @Bean(name = ["txMgrKumo"])
    fun transactionManagerKumo(

        @Qualifier("dataSourceKumo")
        dataSourceKumo: DataSource

    ): DataSourceTransactionManager {

        return DataSourceTransactionManager(dataSourceKumo)
    }

}