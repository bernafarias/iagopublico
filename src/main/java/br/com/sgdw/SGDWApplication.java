package br.com.sgdw;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;


@SpringBootApplication(exclude={EmbeddedMongoAutoConfiguration.class})
@EnableScheduling
@EnableAsync
@EnableSwagger2 
@ComponentScan(basePackages = {"br.com.sgdw"}) 
public class SGDWApplication extends SpringBootServletInitializer{
	
	@Value("${api.version}")
	private String apiVersion;

	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder sgdwApplication) {
        return sgdwApplication.sources(SGDWApplication.class);
    }
       
    @Bean
    public Docket productApi(){
    	
    	return new Docket(DocumentationType.SWAGGER_2)
    			.select().apis(RequestHandlerSelectors.basePackage("br.com.sgdw.api"))
    			.paths(regex("/admin.*|/open.*"))
    			.build()
    			.apiInfo(metaData());
    }
     
    // Substituir
    private ApiInfo metaData(){
    	return new ApiInfo("IAGO", 
    								  "Documentação gerada com uso do Swagger. Dúvidas? Entrar em contato: aladin@cin.ufpe.br", 
    								  apiVersion,
    								  "Uso aberto, sob Licença da GPL v2", 
    								  "aladin@cin.ufpe.br", 
    								  "GPL v2", 
    								  "https://github.com/dass-cin/SGDW/blob/master/LICENSE" );
    }
        
	public static void main(String[] args){
		SpringApplication.run(SGDWApplication.class, args);
		BasicConfigurator.configure();
	}
}
