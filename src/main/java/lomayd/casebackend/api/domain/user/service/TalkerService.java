package lomayd.casebackend.api.domain.user.service;

import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.dto.TalkerResponseDto;
import lomayd.casebackend.api.domain.user.repository.TalkerRepository;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TalkerService {

    private final TalkerRepository talkerRepository;
    private final TokenService tokenService;

    public TalkerResponseDto.UserPageInfo getUserPage(HttpServletRequest httpServletRequest) {

        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));
        List<Talker> talkerList = talkerRepository.findByUser(user);
        List<TalkerResponseDto.TalkerInfo> talkerInfoList = new ArrayList<>();

        for(Talker t : talkerList) {
            talkerInfoList.add(TalkerResponseDto.TalkerInfo.of(t));
        }

        return TalkerResponseDto.UserPageInfo.of(user, talkerInfoList);
    }

    public TalkerResponseDto.Opponent getOpponent(HttpServletRequest httpServletRequest) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        return TalkerResponseDto.Opponent.of(talkerRepository.findOpponents(user));
    }
}
