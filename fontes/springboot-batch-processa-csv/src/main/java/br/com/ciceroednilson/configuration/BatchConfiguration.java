package br.com.ciceroednilson.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import br.com.ciceroednilson.entity.EmployeeEntity;
import br.com.ciceroednilson.process.EmployeeItemProcessor;
import br.com.ciceroednilson.process.JobCompletionNotificationListener;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	  
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	 
	/***
	 * PASSO 1
	 * REALIZANDO A LEITURA DO ARQUIVO
	 * @return
	 */	

	@Bean
	public FlatFileItemReader<EmployeeEntity> readerCsv(){


		FlatFileItemReaderBuilder<EmployeeEntity> flatFile = new FlatFileItemReaderBuilder<EmployeeEntity>();
		
		BeanWrapperFieldSetMapper<EmployeeEntity> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<EmployeeEntity>(); 
        beanWrapperFieldSetMapper.setTargetType(EmployeeEntity.class);
        
        flatFile.name("sheet-employee");
        flatFile.resource(new ClassPathResource("employee.csv"));
        
        /*PULA A PRIMERA LINHA*/
        flatFile.linesToSkip(1); 
        
        flatFile.delimited().names(new String[]{"name", "salary"});
        flatFile.fieldSetMapper(beanWrapperFieldSetMapper);
        		
        return flatFile.build();
		
	}
	
	/***
	 * 
	 * PASSO 2
	 * PROCESSANDO A REGRA DE NEGÓCIO, 
	 * NESSE CASO APENAS COLOCANDO O NOME EM MAIÚSCULO
	 * 
	 * @return
	 */
	@Bean
	public EmployeeItemProcessor processor(){	
		return new EmployeeItemProcessor();
	}
	
	
	/***
	 * PASSO 3
	 * ESCREVENDO OS DADOS NO BANCO DE DADOS
	 * @param dataSource
	 * @return
	 */
    @Bean
    public JdbcBatchItemWriter<EmployeeEntity> writeEmployee(DataSource dataSource) {
    	
    	JdbcBatchItemWriterBuilder<EmployeeEntity> jdbcBatchItemWriterBuilder = new JdbcBatchItemWriterBuilder<EmployeeEntity>();
    	
    	jdbcBatchItemWriterBuilder.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    	jdbcBatchItemWriterBuilder.sql("INSERT INTO CICERO.TB_FUNCIONARIOS (nome, salario) VALUES (:name, :salary)");
    	jdbcBatchItemWriterBuilder.dataSource(dataSource);
    	
    	return jdbcBatchItemWriterBuilder.build();
    	
    }
    
    
    /***
     * CONFIGURANDO AS ESTAPAS DO PROCESSO
     * READER    - REALIZA A EXECUÇÃO DO MÉTODO QUE VAI LER O EXCEL
     * PROCESSOR - PROCESSA ALGUMA REGRA DE NEGÓCIO
     * WRITER	 - PROCESSA A SAÍDA, NO NOSSO CASO INSERI O REGISTRO NO BANCO DE DADOS
     * @param write
     * @return
     */
    @Bean
    public Step stepOne(JdbcBatchItemWriter<EmployeeEntity> write){
    	    	
    	/*chunk DETERMINA O INTERVALO DOS COMMITS*/
    	
    	return stepBuilderFactory.get("stepOne")
    			.<EmployeeEntity,EmployeeEntity> chunk(2)
    			.reader(this.readerCsv())
    			.processor(this.processor())
    			.writer(write)
    			.build();
    			
    }
	
    /***
     * CONFIGURANDO O JOB
     * @param listener
     * @param stepOne
     * @return
     */
    @Bean
    public Job processEmployee(JobCompletionNotificationListener listener, Step stepOne) {
    	
        return jobBuilderFactory.get("processEmployee")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(stepOne)            
            .end()
            .build();
    }
	
	
	
}
