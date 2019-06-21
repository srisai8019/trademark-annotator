package restservices;


import annotator.Annotator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RestServices {
    Annotator annotator = new Annotator();

    @RequestMapping(value = "/", method = GET)
    public ResponseEntity<byte[]> helloWorld() {
        String content = "hello world, this is test";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        httpHeaders.setContentDispositionFormData("test.txt", "test.txt");

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(content.getBytes(), httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/annotate", method = POST)
    public ResponseEntity<byte[]> annotateFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "keywords",
                                                       required = false)
                                                       MultipartFile keywords) {



        String content = getMultiPartFileContent(file);
        String modifiedFilename = getAnnotatedFileName(file.getOriginalFilename());
        String annotatedContent;

        if(keywords == null){
            annotatedContent = annotator.annotate(content);
        } else{
            String keywordString = getMultiPartFileContent(keywords);
            annotatedContent = annotator.annotate(content, annotator.buildKeyWords(keywordString));
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        httpHeaders.setContentDispositionFormData(modifiedFilename, modifiedFilename);
        httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(annotatedContent.getBytes(),
                httpHeaders, HttpStatus.OK);
        return responseEntity;

    }

    public String getAnnotatedFileName(String filename) {
        return "modified_" + FilenameUtils.removeExtension(filename) + "."
                + FilenameUtils.getExtension(filename);
    }

    public String getMultiPartFileContent(MultipartFile file) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream in = file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
