package restservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


@Component
public class AnnotateRequestInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AnnotateRequestInterceptor.class);
    public static String counterFilename = "counter.txt";

    public static Integer counter;

    public AnnotateRequestInterceptor(){
        super();
        counter = readCounter();
    }

    public Integer readCounter(){
        try{
            FileReader fileReader = new FileReader(counterFilename);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            if(line == null || line.equals(""))
                return 0;
            return Integer.parseInt(line);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        counter++;
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        logger.info("request number: " + counter + " request url: " + request.getRequestURL().toString() + " starttime: " + startTime);
        logger.info("updating the counter tracking number of times the service is called");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long)request.getAttribute("startTime");
        long responseTime = System.currentTimeMillis() - startTime;
        logger.info("Request url: " + request.getRequestURL().toString() + " response time: " + responseTime + " milliseconds");
    }

}
