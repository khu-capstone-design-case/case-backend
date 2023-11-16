package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

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

        String path = "room/" + room++ + getFileExtension(record);

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
}
