package batch_tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(fixedRate = 30000) // Exécution toutes les 30 secondes
    public void runBatch() {
        try {
            jobLauncher.run(job, new JobParameters());
            System.out.println("Batch exécuté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
