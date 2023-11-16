package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final TokenService tokenService;

    private final String absolutePath = new File("").getAbsolutePath() + "/record/";


    private int room = 1;

    public void uploadRecord(HttpServletRequest httpServletRequest, MultipartFile record) throws IOException {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        if (record.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "음성 파일이 비어있습니다.");
        }

        String path = "room-" + room++ + getFileExtension(record);

        File file = new File(absolutePath + path);
        file.mkdirs();
        record.transferTo(file);
    }

    public String getFileExtension(MultipartFile data) {
        String contentType = data.getContentType();

        if (ObjectUtils.isEmpty(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "음성 파일 확장자를 알 수 없습니다.");
        }

        if (contentType.contains("audio/mp4")) {
            return ".m4a";
        }

        if (contentType.contains("audio/mp3")) {
            return ".mp3";
        }

        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "음성 파일 확장자는 .m4a, .mp3만 가능합니다");
    }

    public Resource getRecord(HttpServletRequest httpServletRequest, String fileName) throws MalformedURLException {

        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        String absolutePath = new File("").getAbsolutePath();
        return new UrlResource("file:" + absolutePath + "/record/" + fileName);
    }

    public void getRecordScript(String fileName, String user, String opponent) throws IOException {

        File file = new File("/record/" + fileName);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        MultipartBodyBuilder requestBody = new MultipartBodyBuilder();
        requestBody.part("user",user);
        requestBody.part("opponent",opponent);
        requestBody.part("record",new ByteArrayResource(fileContent));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity entity = new HttpEntity(requestBody.toString(), headers);

        String url = "<https://example.com/api>";
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
    }
}
