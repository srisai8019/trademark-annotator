package restservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PreDestroy;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class RestMain {
    public static void main(String[] args) {
        SpringApplication.run(RestMain.class, args);
    }

    @PreDestroy
    public void onExit(){
        try{
            FileWriter fileWriter = new FileWriter(AnnotateRequestInterceptor.counterFilename, false);
            fileWriter.write(Integer.toString(AnnotateRequestInterceptor.counter));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
