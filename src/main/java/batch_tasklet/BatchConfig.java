package batch_tasklet;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    // ItemReader : Lecture des utilisateurs depuis la base de données
    @Bean
    public JdbcCursorItemReader<Member> reader(DataSource dataSource) {
        JdbcCursorItemReader<Member> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, name, age FROM members");
        reader.setRowMapper((rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("name"), rs.getInt("age")));
        return reader;
    }

    // ItemProcessor : Transformation des données (nom en majuscule)
    @Bean
    public ItemProcessor<Member, Member> processor() {
        return member -> {
        	member.setName(member.getName().toUpperCase());
            return member;
        };
    }

    // ItemWriter : Écriture des utilisateurs transformés dans un fichier CSV
    @Bean
    public FlatFileItemWriter<Member> writer() {
        return new FlatFileItemWriterBuilder<Member>()
                .name("memberWriter")
                .resource(new FileSystemResource("src/main/resources/members_output.csv"))
                .delimited()
                .names("id", "name", "age")
                .build();
    }

    // Définition du Step
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     JdbcCursorItemReader<Member> reader, ItemProcessor<Member, Member> processor, FlatFileItemWriter<Member> writer) {
        return new StepBuilder("step", jobRepository)
                .<Member, Member>chunk(5, transactionManager) // Utilisation de JobRepository
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // Définition du Job
    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .build();
    }
}


